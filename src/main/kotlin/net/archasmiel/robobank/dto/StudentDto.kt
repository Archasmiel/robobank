package net.archasmiel.robobank.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime

data class CreateStudentRequest(
    @field:NotBlank(message = "Name is required")
    val name: String,

    @field:Email(message = "Invalid email format")
    @field:NotBlank(message = "Email is required")
    val email: String
)

data class StudentResponse(
    val id: Long,
    val name: String,
    val email: String,
    val balance: Int,
    val createdAt: LocalDateTime
)
