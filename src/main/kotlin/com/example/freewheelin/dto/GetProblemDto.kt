package com.example.freewheelin.dto

import com.example.freewheelin.domain.Problem

class GetProblemDto {
    data class Response(
        val problemList: List<Problem>,
    )
}