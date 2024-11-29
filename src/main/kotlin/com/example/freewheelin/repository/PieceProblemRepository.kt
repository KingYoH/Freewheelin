package com.example.freewheelin.repository

import com.example.freewheelin.domain.PieceProblem
import org.springframework.data.jpa.repository.JpaRepository

interface PieceProblemRepository: JpaRepository<PieceProblem, Long> {
}