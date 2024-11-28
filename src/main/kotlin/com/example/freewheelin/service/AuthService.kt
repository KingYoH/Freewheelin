package com.example.freewheelin.service

import com.example.freewheelin.domain.Member
import com.example.freewheelin.dto.auth.SignInDto
import com.example.freewheelin.dto.auth.SignUpDto
import com.example.freewheelin.repository.MemberRepository
import com.example.freewheelin.security.JwtTokenProvider
import org.springframework.dao.DuplicateKeyException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val jwtTokenProvider: JwtTokenProvider,
) {
    fun signUp(request: SignUpDto.Request): String {
        memberRepository.findMemberByName(request.name)?.let {
            throw DuplicateKeyException("{request.name} is already Exists")
        }
        memberRepository.save(
            Member(
                name = request.name,
                password = passwordEncoder.encode(request.password),
                memberType = request.memberType,
            )
        )
        return "Success"
    }

    fun signIn(request: SignInDto.Request): SignInDto.Response {
        val authenticationToken = UsernamePasswordAuthenticationToken(request.name, request.password)
        val authentication = authenticationManagerBuilder.`object`.authenticate(authenticationToken)

        val accessToken = jwtTokenProvider.generateToken(authentication)

        return SignInDto.Response(accessToken)
    }

    fun myInfo(id: Long): String {
        val member = memberRepository.findMemberById(id)
            ?: throw InvalidPropertiesFormatException("There is no user information for id $id")
        return "${member.name} ( type : ${member.type} )"
    }
}