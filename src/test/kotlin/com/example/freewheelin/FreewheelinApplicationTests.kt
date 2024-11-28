package com.example.freewheelin

import com.example.freewheelin.controller.*
import com.example.freewheelin.domain.Member
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
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.util.UriComponentsBuilder
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FreewheelinApplicationTests(
    @Autowired val controller: Controller,
    @Autowired val authController: AuthController,
    @Autowired val authService: AuthService,
    @Autowired val memberRepository: MemberRepository,
    @Autowired val passwordEncoder: PasswordEncoder,
    @Autowired private val restTemplate: TestRestTemplate,
    @LocalServerPort private val port: Int
) {
    private var rootUrl = "http://localhost:$port"
    private fun Any.toJson(mapper: ObjectMapper = jacksonObjectMapper()): String = mapper.writeValueAsString(this)
    private val objectMapper = jacksonObjectMapper()
    @BeforeTest
    fun preProcess(){
        // add Members
        memberRepository.deleteAll()
        val teacher1 = Member("teacher1", passwordEncoder.encode("1234"),MemberType.TEACHER)
        val student1 = Member("student1", passwordEncoder.encode("1234"),MemberType.STUDENT)
        memberRepository.save(teacher1)
        memberRepository.save(student1)
    }
    @AfterTest
    fun postProcess(){
        // Delete All
        memberRepository.deleteAll()
    }

    @Test
    @DisplayName("0. Auth Test")
    fun loginTest() {
        val headers = HttpHeaders().apply { contentType = MediaType.APPLICATION_JSON }
        // 0-1) sign up test
        testAPI(
            "${rootUrl}/auth/sign-up",
            HttpMethod.POST,
            headers,
            SignUpDto.Request(
                "teacher2",
                "1234",
                MemberType.TEACHER,
            ).toJson(),
            "0-1) sign up test",
        )
        // 0-2) sign in test
        val accessToken = testAPI(
            "${rootUrl}/auth/sign-in",
            HttpMethod.POST,
            headers,
            SignInDto.Request(
                "teacher2",
                "1234",
            ).toJson(),
            "0-2) sign in test",
        ){data->
            assert(data != null)
            val dataValue:SignInDto.Response = objectMapper.readValue(objectMapper.writeValueAsString(data))
        }
        headers.set("Access-Token", accessToken)
        // 0-3) login check
        testAPI(
            "${rootUrl}/auth/my-info",
            HttpMethod.GET,
            headers,
            "",
            "0-3) login check test",
        ){data->
            assert(data != null)
            val dataValue = objectMapper.writeValueAsString(data)
            println("\t user info : $dataValue")
        }
    }

    @Test
    @DisplayName("1. 문제 조회")
    fun getProblemsTest() {
        val headers = HttpHeaders().apply { contentType = MediaType.APPLICATION_JSON }
        //login
        val accessToken = testAPI(
            "${rootUrl}/auth/sign-in",
            HttpMethod.POST,
            headers,
            SignInDto.Request(
                "teacher1",
                "1234",
            ).toJson(),
            "",
        )
        headers.set("Access-Token", accessToken)
        //getProblem
        val url = UriComponentsBuilder.fromUriString("${rootUrl}/problems")
            .queryParam("totalCount", 10)     // 총문제수
            .queryParam("unitCodeList", "uc1503,uc1506,uc1510,uc1513,uc1519,uc1520,uc1521,uc1523,uc1524,uc1526,uc1529")   //유형코드 리스트
            .queryParam("level", "LOW")          // LOW, MIDDLE, HIGH
            .queryParam("problemType", "ALL")    // ALL, SUBJECTIVE, SELECTION
            .build()
            .toUri()
        testAPI(
            url.toString(),
            HttpMethod.GET,
            headers,
            "",
            "1) 문제 조회 test",
        ){
            //TODO
        }
    }



    private fun testAPI(
        url: String,
        httpMethod: HttpMethod,
        headers: HttpHeaders,
        requsetBody: String,
        displayMessage: String,
        assertBlock: (data: Any?) -> Unit = {}
    ):String {
        var out = ""
        val responseBody = restTemplate.exchange(
            url,
            httpMethod,
            HttpEntity(requsetBody, headers),
            String::class.java
        ).body
        if(displayMessage.isNotBlank()){
            println(displayMessage)
            println("\t\turl : $url")
            println("\t\tbody :$requsetBody")
            println("\t\tresponse: $responseBody")
        }
        val body = objectMapper.readValue(responseBody, BaseResponse::class.java)
        try {
            println(body)
            assert(body?.resultCode?.lowercase() == "success")
            assertBlock(body.data)
            if(displayMessage.isNotBlank()) println("\t PASSED")
        } catch (e: Exception) {
            if(displayMessage.isNotBlank()) println("\t FAILED")
            println(e.message)
        }
        if(url =="${rootUrl}/auth/sign-in"){
            out += (body.data as LinkedHashMap<*, *>)["accessToken"]?.toString()?:""
        }
        return out
    }
}
