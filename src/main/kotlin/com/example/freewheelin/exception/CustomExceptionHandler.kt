package com.example.freewheelin.exception

import com.example.freewheelin.dto.common.BaseResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class CustomExceptionHandler {
    @ExceptionHandler(Exception::class)
    protected fun defaultException(ex: Exception): ResponseEntity<BaseResponse<String>> {
        return ResponseEntity(BaseResponse("ERROR", "", ex.message ?: "Error" ), HttpStatus.BAD_REQUEST)
    }
}