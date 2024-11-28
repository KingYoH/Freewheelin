package com.example.freewheelin.domain

import com.example.freewheelin.enum.ProblemLevel
import com.example.freewheelin.enum.ProblemType
import com.example.freewheelin.validation.EnumValid
import jakarta.persistence.*
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull

@Entity
@Table(
    name = "Problem",
    indexes = [
        Index(name = "idx_problem_level", columnList = "`level`"),
        Index(name = "idx_problem_type", columnList = "`type`"),
    ]
)
class Problem(
    unitCode: String,
    level: Int,
    type: ProblemType,
    answer: String,
):PrimaryKey(){
    @Column(name = "`unit_code`", nullable = false)
    var unitCode: String = unitCode
        protected set

    @Column(name = "`level`", nullable = false)
    @Min(value = 1, message = ProblemLevel.errorMessage)
    @Max(value = 5, message = ProblemLevel.errorMessage)
    var level: Int = level
        protected set

    @Column(name = "`type`", nullable = false)
    @EnumValid(enumClass = ProblemType::class, message = ProblemType.errorMessage)
    var type: String = type.name
        protected set

    @Column(name = "`answer`", nullable = false)
    var answer: String = answer
        protected set
}
