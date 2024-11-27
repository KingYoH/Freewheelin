package com.example.freewheelin.dto.auth

import com.example.freewheelin.enum.MemberType

class SignUpDto {
    data class Request(
        val name: String,
        val password: String,
        val memberType: MemberType,
    )
}