package com.example.freewheelin.dto

class SubmitPieceDto {
    data class Response(
        val sucess: List<Long>,
        val alreadySubmitted: List<Long>,
    )
}