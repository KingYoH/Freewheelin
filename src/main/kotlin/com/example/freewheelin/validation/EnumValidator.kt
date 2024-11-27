package com.example.freewheelin.validation

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class EnumValidator : ConstraintValidator<EnumValid, String> {
    private lateinit var enumClass: Array<out Enum<*>>

    override fun initialize(constraintAnnotation: EnumValid) {
        enumClass = constraintAnnotation.enumClass.java.enumConstants
    }

    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
        if (value == null) return false
        return enumClass.any { it.name == value }
    }
}