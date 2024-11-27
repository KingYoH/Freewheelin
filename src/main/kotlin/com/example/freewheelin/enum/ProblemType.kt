package com.example.freewheelin.enum

enum class ProblemType {
    ALL,
    SUBJECTIVE,
    SELECTION,
    ;
    companion object {
        const val errorMessage = "잘못된 문제유형 입니다"
    }
}