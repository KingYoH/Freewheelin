package com.example.freewheelin.repository

import com.example.freewheelin.domain.Problem
import com.example.freewheelin.domain.UnitCode
import org.springframework.data.jpa.repository.JpaRepository

interface ProblemRepository: JpaRepository<Problem, Long> {
    fun findProblemsByUnitCodeIn(unitCodeList: List<UnitCode>): List<Problem>
    fun findProblemsByUnitCodeInAndType(unitCodeList: List<UnitCode>,type: String): List<Problem>
    fun findProblemsByIdIn(ids: List<Long>): List<Problem>
}