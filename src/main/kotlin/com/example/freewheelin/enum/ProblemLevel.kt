package com.example.freewheelin.enum

enum class ProblemLevel(val values: List<Int>) {
    LOW(listOf(1)),
    MIDDLE(listOf(2,3,4)),
    HIGH(listOf(5)),
    ;
    companion object {
        const val errorMessage = "문제 level은 1 ~ 5 값을 가져야 합니다."
    }
}