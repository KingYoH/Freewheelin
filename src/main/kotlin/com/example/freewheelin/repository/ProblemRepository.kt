package com.example.freewheelin.repository

import com.example.freewheelin.domain.Problem
import org.springframework.data.jpa.repository.JpaRepository

interface ProblemRepository: JpaRepository<Problem, Long> {
    fun findProblemsByUnitCodeIn(unitCodeList: List<String>): List<Problem>
    fun findProblemsByUnitCodeInAndType(unitCodeList: List<String>,type: String): List<Problem>
    fun findProblemsByIdIn(ids: List<Long>): List<Problem>
}