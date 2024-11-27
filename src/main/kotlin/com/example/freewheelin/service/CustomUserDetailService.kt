package com.example.freewheelin.service

import com.example.freewheelin.domain.Member
import com.example.freewheelin.repository.MemberRepository
import com.example.freewheelin.security.CustomUser
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val memberRepository: MemberRepository
): UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails =
        memberRepository.findMemberByName(username)
            ?.let { createUserDetails(it) }
            ?: throw UsernameNotFoundException("No User Info")

    private fun createUserDetails(member: Member): UserDetails =
        CustomUser(
            member.id,
            member.name,
            member.password,
            listOf(SimpleGrantedAuthority("ROLE_${member.type}")),
        )
}