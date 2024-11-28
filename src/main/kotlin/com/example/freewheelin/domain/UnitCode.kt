package com.example.freewheelin.domain

import jakarta.persistence.*

@Entity
@Table(
    name = "UNIT_CODE",
)
class UnitCode(
    unitName: String,
    description: String,
):PrimaryKey(){
    @Column(name = "`unit_name`", nullable = false, unique = true)
    var unitName: String = unitName
        protected set

    @Column(name = "`description`", nullable = false)
    var description: String = description
        protected set
}