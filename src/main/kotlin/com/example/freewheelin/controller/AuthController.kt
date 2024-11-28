package com.example.freewheelin.controller

import com.example.freewheelin.dto.auth.SignInDto
import com.example.freewheelin.dto.auth.SignUpDto
import com.example.freewheelin.dto.common.BaseResponse
import com.example.freewheelin.security.CustomUser
import com.example.freewheelin.service.AuthService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService,
) {
    @PostMapping("/sign-up")
    fun singUp(@RequestBody request: SignUpDto.Request): BaseResponse<String> =
            BaseResponse(authService.signUp(request))

    @PostMapping("/sign-in")
    fun login(@RequestBody request: SignInDto.Request): BaseResponse<SignInDto.Response> =
            BaseResponse(data = authService.signIn(request))

    @GetMapping("/my-info")
    fun myInfo():BaseResponse<String>{
        val userId = (SecurityContextHolder.getContext().authentication.principal as CustomUser).id
        return BaseResponse(data = authService.myInfo(userId))
    }
}