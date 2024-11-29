package com.example.freewheelin.service


import com.example.freewheelin.domain.Member
import com.example.freewheelin.domain.Piece
import com.example.freewheelin.domain.PieceStudent
import com.example.freewheelin.dto.*
import com.example.freewheelin.dto.GetProblemDto.ProblemInfo
import com.example.freewheelin.enum.PieceLevel
import com.example.freewheelin.enum.ProblemLevel
import com.example.freewheelin.enum.ProblemType
import com.example.freewheelin.repository.*
import com.example.freewheelin.security.CustomUser
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.util.*

@Service
class Service(
    private val memberRepository: MemberRepository,
    private val problemRepository: ProblemRepository,
    private val pieceRepository: PieceRepository,
    private val pieceStudentRepository: PieceStudentRepository,
    private val unitCodeRepository: UnitCodeRepository,
    private val studentProblemRepository: StudentProblemRepository,
    private val pieceProblemRepository: PieceProblemRepository,
) {
    fun getProblems(
        totalCount: Int,
        unitCodeList: List<String>,
        level: PieceLevel,
        problemType: ProblemType,
    ): GetProblemDto.Response{
        // unitCode와 problemType으로 필터
        val unitCodes = unitCodeRepository.findUnitCodesByUnitCodeIn(unitCodeList)
        val problems = if(problemType!=ProblemType.ALL){
            problemRepository.findProblemsByUnitCodeInAndType(unitCodes, problemType.name)
        } else {
            problemRepository.findProblemsByUnitCodeIn(unitCodes)
        }
        // level으로 필터 ( 총 개수 보다 적어도 비율을 맞추게 )
        val lowProblems = problems.filter { it.level in ProblemLevel.LOW.values }.shuffled()
        val midProblems = problems.filter { it.level in ProblemLevel.MIDDLE.values }.shuffled()
        val highProblems = problems.filter { it.level in ProblemLevel.HIGH.values }.shuffled()
        val low = lowProblems.take((totalCount * level.lowRate).toInt())
        val mid = midProblems.take((totalCount * level.middleRate).toInt())
        val high = highProblems.take((totalCount * level.highRate).toInt())
        // 나머지
        val remainProblems = (lowProblems.drop(low.size) + midProblems.drop(mid.size) + highProblems.drop(high.size)).shuffled()
        val remain = remainProblems.take(totalCount - low.size - mid.size - high.size)
        return GetProblemDto.Response(
            (low+mid+high+remain).map{
                GetProblemDto.ProblemInfo(
                    it.id,
                    it.answer,
                    it.unitCode.unitCode,
                    it.level,
                    it.type
                )
            }
        )
    }

    fun createPiece(
        request: CreatePieceDto.Request
    ): CreatePieceDto.Response{
        if(request.problems.size>50){
            throw Exception("문제 개수가 50개가 넘을 수 없습니다.")
        }
        val user = getUser()
        val problems = problemRepository.findProblemsByIdIn(request.problems)
        val newPiece = Piece(request.pieceName, user)
        problems.forEach{ newPiece.addProblem(it) }
        val saved = pieceRepository.save(newPiece)
        return CreatePieceDto.Response(
            saved.id,
            saved.name,
            problems.size,
        )
    }

    fun submitPiece(
        pieceId: Long,
        studentIds: List<Long>,
    ): SubmitPieceDto.Response{
        val user = getUser()
        val piece = pieceRepository.findById(pieceId).orElseThrow { Exception("Piece not found") }
        val students = memberRepository.findMembersByIdIn(studentIds)
        val (already, notYet) = students.partition{stu->piece.id in stu.pieces.map{it.id}}
        notYet.forEach{student->
            val newPieceStudent = pieceStudentRepository.save(PieceStudent(piece, student))
            piece.pieceProblems.forEach{ newPieceStudent.addPieceProblem(it) }
            piece.addStudent(newPieceStudent)
            student.addPiece(newPieceStudent)
            memberRepository.save(student)
        }
        pieceRepository.save(piece)


        return SubmitPieceDto.Response(
            notYet.map{it.id},
            already.map{it.id},
        )
    }

    fun getPieceProblems(pieceId: Long): GetPieceProblemsDto.Response{
        val user = getUser()
        val piece = user.pieces.find { it.id == pieceId }
            ?:throw Exception("Piece($pieceId) is not a Piece given to the user(${user.name})")

        return GetPieceProblemsDto.Response(
            piece.problems.map {problem->
                GetPieceProblemsDto.ProblemContent(
                    problem.id,
                    problem.unitCode.unitCode,
                    problem.unitCode.name,
                    problem.level,
                    problem.type,
                )
            }
        )
    }

    fun gradePiece(
        pieceId: Long,
        answerSheets: List<GradePieceDto.AnswerSheet>,
    ): GradePieceDto.Response {
        val user = getUser()
        val pieceStudent = pieceStudentRepository.findPieceStudentByPieceIdAndStudentId(pieceId, user.id)
            ?: throw Exception("Piece($pieceId) not found for User(${user.name})")
        val studentProblems = pieceStudent.studentProblems
            .filter{sp->sp.pieceProblem.problem.id in answerSheets.map{it.problemId}}
            .sortedBy { it.pieceProblem.problem.id }
        if(studentProblems.size != answerSheets.size){
            throw Exception("There are Problems that are not included in the Piece(${pieceId})")
        }
        val results = mutableListOf<GradePieceDto.Result>()
        studentProblems.zip(answerSheets.sortedBy{it.problemId}){sp, ans->
            sp.submitAnswer = ans.answer
            sp.isCorrect = (sp.pieceProblem.problem.answer==ans.answer)
            sp.pieceProblem.attemptCount+=1
            pieceStudent.attemptCount +=1
            if(sp.isCorrect==true){
                sp.pieceProblem.correctCount +=1
                pieceStudent.correctCount +=1
            }
            pieceProblemRepository.save(sp.pieceProblem)
            studentProblemRepository.save(sp)
            results.add(
                GradePieceDto.Result(
                    ans.problemId,
                    sp.isCorrect!!,
                )
            )
        }
        pieceStudentRepository.save(pieceStudent)
        return GradePieceDto.Response(results.toList())
    }

    fun analyzePiece(pieceId: Long):AnalyzePieceDto.Response {
        val user = getUser()
        val piece = pieceRepository.findById(pieceId).orElseThrow { Exception("Piece not found") }
        if(user.id != piece.teacher.id){
            throw Exception("User(${user.name}) does not have permission to the piece(${pieceId}")
        }

        val studentInfos: List<AnalyzePieceDto.StudentInfo> = piece.pieceStudents.map {ps->
            AnalyzePieceDto.StudentInfo(
                ps.student.id,
                ps.student.name,
                (ps.correctCount.toDouble()/ps.attemptCount)*100
            )
        }

        val problemInfos: List<AnalyzePieceDto.ProblemInfo> = piece.pieceProblems.map{pp->
            AnalyzePieceDto.ProblemInfo(
                pp.problem.id,
                pp.problem.unitCode.unitCode,
                pp.problem.unitCode.name,
                pp.problem.level,
                pp.problem.type,
                (pp.correctCount.toDouble()/pp.attemptCount)*100
            )
        }

        return AnalyzePieceDto.Response(
            pieceId = pieceId,
            pieceName = piece.name,
            students = studentInfos,
            problems = problemInfos
        )
    }

    private fun getUser(): Member{
        val userId = (SecurityContextHolder.getContext().authentication.principal as CustomUser).id
        return memberRepository.findMemberById(userId)
            ?: throw InvalidPropertiesFormatException("There is no user information for id $userId")
    }
}