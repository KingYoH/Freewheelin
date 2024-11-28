package com.example.freewheelin.service


import com.example.freewheelin.domain.Problem
import com.example.freewheelin.dto.GetProblemDto
import com.example.freewheelin.enum.PieceLevel
import com.example.freewheelin.enum.ProblemLevel
import com.example.freewheelin.enum.ProblemType
import com.example.freewheelin.repository.MemberRepository
import com.example.freewheelin.repository.ProblemRepository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestParam

@Service
class Service(
    private val memberRepository: MemberRepository,
    private val problemRepository: ProblemRepository,
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
}