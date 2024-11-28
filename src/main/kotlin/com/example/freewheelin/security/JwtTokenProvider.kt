package com.example.freewheelin.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Date
import javax.crypto.SecretKey

@Component
class JwtTokenProvider(
    @Value("\${jwt.secret-key}")
    private val secret: String,
    @Value("\${jwt.access-token-expire-minutes}")
    private val accessTokenExpireTime: Long,
) {
    private val secretKey: SecretKey = Keys.hmacShaKeyFor(this.secret.toByteArray())

    fun generateToken(authentication: Authentication):String {
        val auth = authentication.authorities.joinToString(",", transform = GrantedAuthority::getAuthority)
        val userId = (authentication.principal as CustomUser).id
        return "Bearer " + Jwts.builder()
            .setSubject(authentication.name)
            .claim("auth", auth)
            .claim("userId", userId)
            .setExpiration(Date.from(Instant.now().plus(this.accessTokenExpireTime, ChronoUnit.MINUTES)))
            .signWith(this.secretKey, SignatureAlgorithm.HS256)
            .compact()
    }

    fun getAuthentication(token: String): Authentication {
        val claims = Jwts.parserBuilder().setSigningKey(this.secretKey).build().parseClaimsJws(token).body
        return getAuthentication(claims)
    }

    private fun getAuthentication(claims: Claims): Authentication {
        val auth = claims["auth"] ?: throw RuntimeException("there is no auth claim")
        val userId = claims["userId"] ?: throw RuntimeException("there is no user claim")
        val authorities: Collection<GrantedAuthority> = (auth as String).split(",").map { SimpleGrantedAuthority(it) }
        val principal: UserDetails = CustomUser(userId.toString().toLong(), claims.subject, "", authorities)
        return UsernamePasswordAuthenticationToken(principal, "", authorities)
    }
}