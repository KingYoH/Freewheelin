package com.example.freewheelin.dto.common

data class BaseResponse<T>(
    val resultCode: String = "SUCCESS",
    val data: T? = null,
    val message: String = "ok",
)