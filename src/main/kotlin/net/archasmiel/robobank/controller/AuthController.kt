package net.archasmiel.robobank.controller

import jakarta.validation.Valid
import net.archasmiel.robobank.dto.AuthResponse
import net.archasmiel.robobank.dto.LoginRequest
import net.archasmiel.robobank.dto.RefreshTokenRequest
import net.archasmiel.robobank.dto.RefreshTokenResponse
import net.archasmiel.robobank.dto.RegisterRequest
import net.archasmiel.robobank.dto.UserInfoResponse
import net.archasmiel.robobank.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/register")
    fun register(@Valid @RequestBody request: RegisterRequest): ResponseEntity<RefreshTokenResponse> {
        val response = authService.register(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<RefreshTokenResponse> {
        val response = authService.login(request)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/refresh")
    fun refreshToken(@Valid @RequestBody request: RefreshTokenRequest): ResponseEntity<RefreshTokenResponse> {
        val response = authService.refreshToken(request)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/logout")
    fun logout(@AuthenticationPrincipal userDetails: UserDetails): ResponseEntity<Map<String, String>> {
        authService.logout(userDetails.username)
        return ResponseEntity.ok(mapOf("message" to "Logged out successfully"))
    }

    @GetMapping("/me")
    fun getCurrentUser(@AuthenticationPrincipal userDetails: UserDetails): ResponseEntity<UserInfoResponse> {
        val userInfo = authService.getCurrentUserInfo(userDetails.username)
        return ResponseEntity.ok(userInfo)
    }

    @GetMapping("/test")
    fun test(): ResponseEntity<String> {
        return ResponseEntity.ok("If you see this, endpoint works")
    }
}