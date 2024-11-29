package com.example.freewheelin.domain

import jakarta.persistence.*

@Entity
@Table(
    name = "UNIT_CODE", uniqueConstraints = [UniqueConstraint(columnNames = ["`unit_code`"])]
)
class UnitCode(
    unitCode: String,
    name: String,
):PrimaryKey(){
    @Column(name = "`unit_code`", nullable = false)
    var unitCode: String = unitCode
        protected set

    @Column(name = "`name`", nullable = false)
    var name: String = name
        protected set
}