package net.archasmiel.robobank.service

import jakarta.transaction.Transactional
import net.archasmiel.robobank.dto.AdjustPointsRequest
import net.archasmiel.robobank.dto.TransactionResponse
import net.archasmiel.robobank.model.Transaction
import net.archasmiel.robobank.model.TransactionType
import net.archasmiel.robobank.repository.StudentRepository
import net.archasmiel.robobank.repository.TransactionRepository
import org.springframework.stereotype.Service

@Service
class TransactionService(
    private val transactionRepository: TransactionRepository,
    private val studentRepository: StudentRepository
) {

    @Transactional
    fun awardPoints(studentId: Long, request: AdjustPointsRequest): TransactionResponse {
        val student = studentRepository.findById(studentId)
            .orElseThrow { NoSuchElementException("Student with id $studentId not found") }

        if (request.amount <= 0) {
            throw IllegalArgumentException("Request amount must be positive for award")
        }

        val transaction = Transaction(
            student = student,
            amount = request.amount,
            type = TransactionType.AWARDED,
            description = request.description
        )

        val updatedStudent = student.copy(balance = student.balance + request.amount)
        studentRepository.save(updatedStudent)

        val savedTransaction = transactionRepository.save(transaction)
        return savedTransaction.toResponse()
    }

    @Transactional
    fun spendPoints(studentId: Long, request: AdjustPointsRequest): TransactionResponse {
        val student = studentRepository.findById(studentId)
            .orElseThrow { NoSuchElementException("Student with id $studentId not found") }

        if (request.amount <= 0) {
            throw IllegalArgumentException("Request amount must be positive for spend")
        }

        if (student.balance < request.amount) {
            throw IllegalArgumentException("Insufficient balance. Current: ${student.balance}. Needed: ${request.amount}")
        }

        val transaction = Transaction(
            student = student,
            amount = request.amount,
            type = TransactionType.SPENT,
            description = request.description
        )

        val updatedStudent = student.copy(balance = student.balance - request.amount)
        studentRepository.save(updatedStudent)

        val savedTransaction = transactionRepository.save(transaction)
        return savedTransaction.toResponse()
    }

    @Transactional
    fun adjustPoints(studentId: Long, request: AdjustPointsRequest): TransactionResponse {
        val student = studentRepository.findById(studentId)
            .orElseThrow { NoSuchElementException("Student with id $studentId not found") }

        if (request.amount == 0) {
            throw IllegalArgumentException("Expensive call for zero adjust")
        }

        val transaction = Transaction(
            student = student,
            amount = request.amount,
            type = TransactionType.ADJUSTED,
            description = request.description
        )

        val updatedStudent = student.copy(balance = student.balance + request.amount)
        studentRepository.save(updatedStudent)

        val savedTransaction = transactionRepository.save(transaction)
        return savedTransaction.toResponse()
    }

    fun getStudentTransactions(studentId: Long): List<TransactionResponse> {
        val student = studentRepository.findById(studentId)
            .orElseThrow { NoSuchElementException("Student with id $studentId not found") }

        return transactionRepository.findByStudentOrderByCreatedAtDesc(student)
            .map { it.toResponse() }
    }

    fun getTop20Transactions(): List<TransactionResponse> {
        return transactionRepository.findTop20ByOrderByCreatedAtDesc()
            .map { it.toResponse() }
    }

    fun Transaction.toResponse() = TransactionResponse(
        id = id!!,
        studentId = student.id!!,
        studentName = student.name,
        amount = amount,
        type = type,
        description = description,
        createdAt = createdAt
    )
}