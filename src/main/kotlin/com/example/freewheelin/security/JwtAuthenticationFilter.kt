package com.example.freewheelin.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.WebAuthenticationDetails
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Order(0)
@Component
class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider,
) : OncePerRequestFilter() {
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val token = parseBearerToken(request)
        val authentication = token?.let { jwtTokenProvider.getAuthentication(it) }
        try {
            SecurityContextHolder.getContext().authentication = authentication
            response.setHeader("Access-Token", token)
        } catch (e: Exception) {
            request.setAttribute("exception", e)
        }
        filterChain.doFilter(request, response)
    }

    private fun parseBearerToken(request: HttpServletRequest) = request.getHeader("Access-Token")
        .takeIf { it?.startsWith("Bearer ", true) ?: false }?.substring(7)
}