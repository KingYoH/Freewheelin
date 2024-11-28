package com.example.freewheelin.enum

import com.fasterxml.jackson.annotation.JsonCreator

enum class MemberType {
    TEACHER,
    STUDENT,
    ;

    companion object {
        @JsonCreator
        @JvmStatic
        fun fromString(str: String): MemberType {
            return entries.find { it.name.equals(str, ignoreCase = true) }
                ?: throw IllegalArgumentException("Unknown status: $str")
        }
    }
}