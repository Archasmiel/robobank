package net.archasmiel.robobank.repository

import net.archasmiel.robobank.model.Student
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface StudentRepository : JpaRepository<Student, Long> {
    fun findByEmail(email: String): Optional<Student>
    fun existsByEmail(email: String): Boolean

    // for leaderboard
    fun findTop10ByOrderByBalanceDesc(): List<Student>
}