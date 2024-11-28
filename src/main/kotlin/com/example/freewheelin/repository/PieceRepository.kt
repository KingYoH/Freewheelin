package com.example.freewheelin.repository

import com.example.freewheelin.domain.Piece
import org.springframework.data.jpa.repository.JpaRepository

interface PieceRepository: JpaRepository<Piece, Long> {

}