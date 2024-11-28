package com.example.freewheelin.dto.auth

import com.example.freewheelin.enum.MemberType
import com.fasterxml.jackson.annotation.JsonProperty

class SignInDto {
    data class Request(
        val name: String,
        val password: String,
    )
    data class Response(
        @JsonProperty("accessToken")
        val accessToken: String,
    )
}