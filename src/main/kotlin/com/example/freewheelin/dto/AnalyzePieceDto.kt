package com.example.freewheelin.dto

import com.example.freewheelin.dto.common.BaseResponse

class AnalyzePieceDto {
    data class Response(
        val pieceId: Long,
        val pieceName: String,
        val students: List<Long>,
    )
}