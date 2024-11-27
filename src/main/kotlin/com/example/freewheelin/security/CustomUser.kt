package com.example.freewheelin.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

class CustomUser (
    val id: Long,
    userName: String,
    password: String,
    authorities: Collection<GrantedAuthority>
): User(userName, password, authorities)