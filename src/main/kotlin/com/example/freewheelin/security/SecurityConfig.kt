package com.example.freewheelin.security

import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@EnableMethodSecurity
@Configuration
class SecurityConfig(
    val jwtTokenProvider: JwtTokenProvider,
) {
    private val allowedUrls = arrayOf("/", "/swagger-ui/**", "/v3/**", "/auth/**")
    @Bean
    fun filterChain(http: HttpSecurity) = http
        .csrf { it.disable() }
        .headers { it.frameOptions { frameOptions -> frameOptions.sameOrigin() } }
        .authorizeHttpRequests {
            it.requestMatchers(*allowedUrls).permitAll()
                .requestMatchers(PathRequest.toH2Console()).permitAll()
                .anyRequest().authenticated()
        }
        .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
        .addFilterBefore(JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter::class.java)
        .build()!!

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()
}