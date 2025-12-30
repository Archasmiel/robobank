package net.archasmiel.robobank.controller

import jakarta.validation.Valid
import net.archasmiel.robobank.dto.AdjustPointsRequest
import net.archasmiel.robobank.dto.TransactionResponse
import net.archasmiel.robobank.service.TransactionService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/transactions")
class TransactionController(
    private val transactionService: TransactionService
) {

    @PostMapping("/award/{studentId}")
    fun awardPoints(
        @PathVariable studentId: Long,
        @Valid @RequestBody request: AdjustPointsRequest
    ): ResponseEntity<TransactionResponse> {
        val transaction = transactionService.awardPoints(studentId, request)
        return ResponseEntity.status(HttpStatus.CREATED).body(transaction)
    }

    @PostMapping("/spend/{studentId}")
    fun spendPoints(
        @PathVariable studentId: Long,
        @Valid @RequestBody request: AdjustPointsRequest
    ): ResponseEntity<TransactionResponse> {
        val transaction = transactionService.spendPoints(studentId, request)
        return ResponseEntity.status(HttpStatus.CREATED).body(transaction)
    }

    @PostMapping("/adjust/{studentId}")
    fun adjustPoints(
        @PathVariable studentId: Long,
        @Valid @RequestBody request: AdjustPointsRequest
    ): ResponseEntity<TransactionResponse> {
        val transaction = transactionService.adjustPoints(studentId, request)
        return ResponseEntity.status(HttpStatus.CREATED).body(transaction)
    }

    @GetMapping("/student/{studentId}")
    fun getStudentTransactions(
        @PathVariable studentId: Long
    ): ResponseEntity<List<TransactionResponse>> {
        val transactions = transactionService.getStudentTransactions(studentId)
        return ResponseEntity.ok(transactions)
    }

    @GetMapping("/leaderboard")
    fun getTop20Transactions(): ResponseEntity<List<TransactionResponse>> {
        val transactions = transactionService.getTop20Transactions()
        return ResponseEntity.ok(transactions)
    }
}