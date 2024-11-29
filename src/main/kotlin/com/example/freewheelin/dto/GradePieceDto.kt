package com.example.freewheelin.dto

class GradePieceDto {
    data class Request(
        val answers: List<AnswerSheet>,
    )
    data class Response(
        val results: List<Result>
    )

    data class AnswerSheet(
        val problemId: Long,
        val answer: String,
    )
    data class Result(
        val problemId: Long,
        val correct: Boolean,
    )
}