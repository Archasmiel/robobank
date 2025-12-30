package net.archasmiel.robobank.repository

import net.archasmiel.robobank.model.RefreshToken
import net.archasmiel.robobank.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*

@Repository
interface RefreshTokenRepository : JpaRepository<RefreshToken, Long> {
    fun findByToken(token: String): Optional<RefreshToken>
    fun deleteByUser(user: User)
    fun deleteByExpiryDateBefore(now: LocalDateTime): Int
}