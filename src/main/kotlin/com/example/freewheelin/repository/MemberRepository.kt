package com.example.freewheelin.repository

import com.example.freewheelin.domain.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository:JpaRepository<Member,Long>{
    fun findMemberByName(name: String?): Member?
    fun findMemberById(id: Long): Member?
}