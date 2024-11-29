package com.example.freewheelin.dto

import com.example.freewheelin.domain.Problem
import com.example.freewheelin.enum.ProblemType

class GetProblemDto {
    data class Response(
        val problemList: List<ProblemInfo>,
    )
    data class ProblemInfo(
        val id: Long,
        val answer: String,
        val unitCode: String,
        val level: Int,
        val problemType: String,
    )
}