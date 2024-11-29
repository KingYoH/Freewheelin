package com.example.freewheelin.dto

import com.example.freewheelin.enum.ProblemType

class GetPieceProblemsDto {
    data class Response(
        val problems: List<ProblemContent>,
    )
    data class ProblemContent(
        val id: Long,
        val unitCode: String,
        val unitCodeName: String,
        val level: Int,
        val problemType: String,
    )
}