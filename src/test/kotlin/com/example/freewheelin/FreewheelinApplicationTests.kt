package com.example.freewheelin

import com.example.freewheelin.controller.*
import com.example.freewheelin.service.*
import com.example.freewheelin.repository.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

@SpringBootTest
@Transactional
class FreewheelinApplicationTests {

    @Autowired
    private lateinit var controller: Controller
    @Autowired
    private lateinit var authController: AuthController
    @Autowired
    private lateinit var authService: AuthService
    @Autowired
    private lateinit var memberRepository: MemberRepository


    @BeforeTest
    fun preProcess(){

    }
    @AfterTest
    fun postProcess(){

    }

    @Test
    fun contextLoads() {

    }

}
