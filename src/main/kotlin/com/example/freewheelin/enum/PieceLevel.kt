package com.example.freewheelin.enum

enum class PieceLevel(val lowRate:Double, val middleRate:Double, highRate:Double) {
    LOW(0.5, 0.3, 0.2),       // 하 문제 50%, 중 문제 30%, 상 문제 20%
    MIDDLE(0.25, 0.5, 0.25),  // 하 문제 25%, 중 문제 50%, 상 문제 25%
    HIGH(0.2, 0.3, 0.5),      // 하 문제 20%, 중 문제 30%, 상 문제 50%
}