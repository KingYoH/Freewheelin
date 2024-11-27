package com.example.freewheelin.repository

import com.example.freewheelin.domain.Member
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface MemberRepository:JpaRepository<Member,UUID>{
    fun findMemberByName(name: String?): Member?
}