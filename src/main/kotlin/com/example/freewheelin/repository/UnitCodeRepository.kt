package com.example.freewheelin.repository

import com.example.freewheelin.domain.UnitCode
import org.springframework.data.jpa.repository.JpaRepository

interface UnitCodeRepository: JpaRepository<UnitCode, Long> {
    fun findUnitCodesByUnitCodeIn(unitCodeList: List<String>): List<UnitCode>
}