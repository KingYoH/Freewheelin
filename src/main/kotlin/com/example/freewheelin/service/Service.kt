package com.example.freewheelin.service


import com.example.freewheelin.domain.Member
import com.example.freewheelin.domain.Piece
import com.example.freewheelin.domain.PieceStudent
import com.example.freewheelin.dto.CreatePieceDto
import com.example.freewheelin.dto.GetProblemDto
import com.example.freewheelin.dto.SubmitPieceDto
import com.example.freewheelin.enum.PieceLevel
import com.example.freewheelin.enum.ProblemLevel
import com.example.freewheelin.enum.ProblemType
import com.example.freewheelin.repository.MemberRepository
import com.example.freewheelin.repository.PieceRepository
import com.example.freewheelin.repository.PieceStudentRepository
import com.example.freewheelin.repository.ProblemRepository
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
) {
    fun getProblems(
        totalCount: Int,
        unitCodeList: List<String>,
        level: PieceLevel,
        problemType: ProblemType,
    ): GetProblemDto.Response{
        // unitCode와 problemType으로 필터
        val problems = if(problemType!=ProblemType.ALL){
            problemRepository.findProblemsByUnitCodeInAndType(unitCodeList, problemType.name)
        } else {
            problemRepository.findProblemsByUnitCodeIn(unitCodeList)
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
        return GetProblemDto.Response(low+mid+high+remain)
    }

    fun createPiece(
        request: CreatePieceDto.Request
    ): CreatePieceDto.Response{
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
        notYet.forEach{
            val newPieceStudent = pieceStudentRepository.save(PieceStudent(piece, it))
            piece.addStudent(newPieceStudent)
            it.addPiece(newPieceStudent)
            memberRepository.save(it)
        }
        pieceRepository.save(piece)
        return SubmitPieceDto.Response(
            notYet.map{it.id},
            already.map{it.id},
        )
    }

    private fun getUser(): Member{
        val userId = (SecurityContextHolder.getContext().authentication.principal as CustomUser).id
        return memberRepository.findMemberById(userId)
            ?: throw InvalidPropertiesFormatException("There is no user information for id $userId")
    }
}