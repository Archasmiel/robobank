package net.archasmiel.robobank.repository

import net.archasmiel.robobank.model.Student
import net.archasmiel.robobank.model.Transaction
import org.springframework.data.jpa.repository.JpaRepository

interface TransactionRepository : JpaRepository<Transaction, Long> {
    // get all transactions for specific student
    fun findByStudentOrderByCreatedAtDesc(student: Student): List<Transaction>

    // top-20 for activity feed
    fun findTop20ByOrderByCreatedAtDesc(): List<Transaction>
}