package com.example.freewheelin

import com.example.freewheelin.controller.*
import com.example.freewheelin.dto.auth.SignInDto
import com.example.freewheelin.dto.auth.SignUpDto
import com.example.freewheelin.dto.common.BaseResponse
import com.example.freewheelin.enum.MemberType
import com.example.freewheelin.service.*
import com.example.freewheelin.repository.*
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.*
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FreewheelinApplicationTests(
    @Autowired val controller: Controller,
    @Autowired val authController: AuthController,
    @Autowired val authService: AuthService,
    @Autowired val memberRepository: MemberRepository,
    @Autowired private val restTemplate: TestRestTemplate,
    @LocalServerPort private val port: Int
) {
    private var rootUrl = "http://localhost:$port"
    private fun Any.toJson(mapper: ObjectMapper = jacksonObjectMapper()): String = mapper.writeValueAsString(this)
    private val objectMapper = jacksonObjectMapper()
    private val headers = HttpHeaders().apply { contentType = MediaType.APPLICATION_JSON }
    @BeforeTest
    fun preProcess(){
        // add Members
    }
    @AfterTest
    fun postProcess(){
        // Delete All
        memberRepository.deleteAll()
    }

    @Test
    @DisplayName("0. Auth Test")
    fun loginTest() {
        // 0-1) sign up test
        testAPI(
            "/auth/sign-up",
            HttpMethod.POST,
            this.headers,
            SignUpDto.Request(
                "teacher1",
                "1234",
                MemberType.TEACHER,
            ).toJson(),
            "0-1) sign up test",
        )
        // 0-2) sign in test
        val accessToken = testAPI(
            "/auth/sign-in",
            HttpMethod.POST,
            this.headers,
            SignInDto.Request(
                "teacher1",
                "1234",
            ).toJson(),
            "0-2) sign in test",
        ){data->
            assert(data != null)
            val dataValue:SignInDto.Response = objectMapper.readValue(objectMapper.writeValueAsString(data))
        }
        this.headers.set("Access-Token", accessToken)
        // 0-3) login check
        testAPI(
            "/auth/my-info",
            HttpMethod.GET,
            this.headers,
            "",
            "0-3) login check test",
        ){data->
            assert(data != null)
            val dataValue = objectMapper.writeValueAsString(data)
            println("\t user info : $dataValue")
        }
    }




    private fun testAPI(
        endpoint: String,
        httpMethod: HttpMethod,
        headers: HttpHeaders,
        requsetBody: String,
        displayMessage: String,
        assertBlock: (data: Any?) -> Unit = {}
    ):String {
        var out = ""
        val responseBody = restTemplate.exchange(
            "$rootUrl$endpoint",
            httpMethod,
            HttpEntity(requsetBody, headers),
            String::class.java
        ).body
        println(displayMessage)
        println("\t\tendpoint : $endpoint")
        println("\t\tbody :$requsetBody")
        println("\t\tresponse: $responseBody")
        val body = objectMapper.readValue(responseBody, BaseResponse::class.java)
        try {
            assert(body?.resultCode?.lowercase() == "success")
            assertBlock(body.data)
            println("\t PASSED")
        } catch (e: Exception) {
            println("\t FAILED")
            println(e.message)
        }
        if(endpoint =="/auth/sign-in"){
            out += (body.data as LinkedHashMap<*, *>)["accessToken"]?.toString()?:""
        }
        return out
    }
}
