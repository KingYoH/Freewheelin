package com.example.freewheelin.dto

import com.example.freewheelin.dto.common.BaseResponse

class AnalyzePieceDto {
    data class Response(
        val pieceId: Long,
        val pieceName: String,
        val students: List<StudentInfo>,
        val problems: List<ProblemInfo>,
    )
    data class StudentInfo(
        val id: Long,
        val name: String,
        val correctRate: Double,
    )
    data class ProblemInfo(
        val id: Long,
        val unitCode: String,
        val unitCodeName: String,
        val level: Int,
        val problemType: String,
        val correctRate: Double,
    )
}