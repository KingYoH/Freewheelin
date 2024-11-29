package com.example.freewheelin.repository

import com.example.freewheelin.domain.PieceStudent
import org.springframework.data.jpa.repository.JpaRepository

interface PieceStudentRepository: JpaRepository<PieceStudent, Long> {
    fun findPieceStudentByPieceIdAndStudentId(pieceId: Long, studentId: Long): PieceStudent?
}