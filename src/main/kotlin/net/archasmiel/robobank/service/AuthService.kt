package net.archasmiel.robobank.service

import jakarta.transaction.Transactional
import net.archasmiel.robobank.dto.AuthResponse
import net.archasmiel.robobank.dto.LoginRequest
import net.archasmiel.robobank.dto.RefreshTokenRequest
import net.archasmiel.robobank.dto.RefreshTokenResponse
import net.archasmiel.robobank.dto.RegisterRequest
import net.archasmiel.robobank.dto.UserInfoResponse
import net.archasmiel.robobank.model.User
import net.archasmiel.robobank.repository.UserRepository
import net.archasmiel.robobank.security.JwtUtility
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtility: JwtUtility,
    private val authenticationManager: AuthenticationManager,
    private val refreshTokenService: RefreshTokenService
) {

    @Transactional
    fun register(request: RegisterRequest): RefreshTokenResponse {
        if (userRepository.existsByUsername(request.username)) {
            throw IllegalArgumentException("Username already exists")
        }

        if (userRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("Email already exists")
        }

        val user = User(
            username = request.username,
            password = passwordEncoder.encode(request.password),
            email = request.email,
            role = request.role
        )

        val savedUser = userRepository.save(user)
        val accessToken = jwtUtility.generateToken(savedUser)
        val refreshToken = refreshTokenService.createRefreshToken(savedUser)

        return RefreshTokenResponse(
            accessToken = accessToken,
            refreshToken = refreshToken.token
        )
    }

    fun login(request: LoginRequest): RefreshTokenResponse {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                request.username,
                request.password
            )
        )

        val user = userRepository.findByUsername(request.username)
            .orElseThrow { IllegalArgumentException("Invalid username or password") }

        val accessToken = jwtUtility.generateToken(user)
        val refreshToken = refreshTokenService.createRefreshToken(user)

        return RefreshTokenResponse(
            accessToken = accessToken,
            refreshToken = refreshToken.token
        )
    }

    @Transactional
    fun refreshToken(request: RefreshTokenRequest): RefreshTokenResponse {
        val refreshToken = refreshTokenService.findByToken(request.refreshToken)

        refreshTokenService.verifyExpiration(refreshToken)

        val user = refreshToken.user
        val newAccessToken = jwtUtility.generateToken(user)

        val newRefreshToken = refreshTokenService.createRefreshToken(user)

        return RefreshTokenResponse(
            accessToken = newAccessToken,
            refreshToken = newRefreshToken.token
        )
    }

    @Transactional
    fun logout(username: String) {
        val user = userRepository.findByUsername(username)
            .orElseThrow { NoSuchElementException("User not found") }

        refreshTokenService.deleteByUser(user)
    }

    fun getCurrentUserInfo(username: String): UserInfoResponse {
        val user = userRepository.findByUsername(username)
            .orElseThrow { NoSuchElementException("User not found") }

        return UserInfoResponse(
            id = user.id!!,
            username = user.username,
            email = user.email,
            role = user.role,
            createdAt = user.createdAt
        )
    }
}