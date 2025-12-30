package net.archasmiel.robobank.dto

import jakarta.validation.constraints.NotBlank
import net.archasmiel.robobank.model.TransactionType
import java.time.LocalDateTime

data class AdjustPointsRequest(
    val amount: Int,

    @field:NotBlank(message = "Description is required")
    val description: String
)

data class TransactionResponse(
    val id: Long,
    val studentId: Long,
    val studentName: String,
    val amount: Int,
    val type: TransactionType,
    val description: String,
    val createdAt: LocalDateTime
)