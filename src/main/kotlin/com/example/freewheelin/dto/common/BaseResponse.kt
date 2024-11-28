package com.example.freewheelin.dto.common

data class BaseResponse<T>(
    val resultCode: String = "Success",
    val data: T? = null,
    val message: String = "ok",
)