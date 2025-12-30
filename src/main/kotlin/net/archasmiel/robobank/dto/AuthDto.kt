package net.archasmiel.robobank.dto

import jakarta.validation.constraints.*
import net.archasmiel.robobank.model.UserRole
import java.time.LocalDateTime

data class RegisterRequest(
    @field:NotBlank(message = "Username is required")
    @field:Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    val username: String,

    @field:NotBlank(message = "Password is required")
    @field:Size(min = 6, message = "Password must be at least 6 characters")
    val password: String,

    @field:Email(message = "Invalid email format")
    @field:NotBlank(message = "Email is required")
    val email: String,

    val role: UserRole = UserRole.STUDENT
)

data class LoginRequest(
    @field:NotBlank(message = "Username is required")
    val username: String,

    @field:NotBlank(message = "Password is required")
    val password: String
)

data class RefreshTokenRequest(
    @field:NotBlank(message = "Refresh token is required")
    val refreshToken: String
)

data class AuthResponse(
    val token: String,
    val type: String = "Bearer",
    val username: String,
    val email: String,
    val role: UserRole
)

data class UserInfoResponse(
    val id: Long,
    val username: String,
    val email: String,
    val role: UserRole,
    val createdAt: LocalDateTime
)

data class RefreshTokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val type: String = "Bearer"
)