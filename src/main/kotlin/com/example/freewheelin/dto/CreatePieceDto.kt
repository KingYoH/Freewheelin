package com.example.freewheelin.dto

class CreatePieceDto {
    data class Request(
        val pieceName: String,
        val problems: List<Long>,
    )
    data class Response(
        val pieceId: Long,
        val pieceName: String,
        val problemCount: Int,
    )
}