package com.example.freewheelin.repository

import com.example.freewheelin.domain.StudentProblem
import org.springframework.data.jpa.repository.JpaRepository

interface StudentProblemRepository: JpaRepository<StudentProblem, Long> {

}