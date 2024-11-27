package com.example.freewheelin.dto.auth

import com.example.freewheelin.enum.MemberType

class SignInDto {
    data class Request(
        val name: String,
        val password: String,
    )
    data class Response(
        val accessToken: String,
    )
}