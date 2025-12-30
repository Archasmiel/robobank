package net.archasmiel.robobank.service

import jakarta.transaction.Transactional
import net.archasmiel.robobank.model.RefreshToken
import net.archasmiel.robobank.model.User
import net.archasmiel.robobank.repository.RefreshTokenRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
class RefreshTokenService(
    private val refreshTokenRepository: RefreshTokenRepository
) {

    @Value("\${jwt.refresh-expiration:604800000}") // 7 days in milliseconds
    private var refreshTokenDurationMs: Long = 604800000

    @Transactional
    fun createRefreshToken(user: User): RefreshToken {
        // Delete existing refresh tokens for this user
        refreshTokenRepository.deleteByUser(user)

        val refreshToken = RefreshToken(
            token = UUID.randomUUID().toString(),
            user = user,
            expiryDate = LocalDateTime.now().plusSeconds(refreshTokenDurationMs / 1000)
        )

        return refreshTokenRepository.save(refreshToken)
    }

    fun findByToken(token: String): RefreshToken {
        return refreshTokenRepository.findByToken(token)
            .orElseThrow { IllegalArgumentException("Refresh token not found") }
    }

    fun verifyExpiration(token: RefreshToken): RefreshToken {
        if (token.expiryDate.isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(token)
            throw IllegalArgumentException("Refresh token expired. Please login again")
        }
        return token
    }

    @Transactional
    fun deleteByUser(user: User) {
        refreshTokenRepository.deleteByUser(user)
    }

    @Transactional
    fun deleteExpiredTokens(): Int {
        return refreshTokenRepository.deleteByExpiryDateBefore(LocalDateTime.now())
    }
}