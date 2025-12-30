package net.archasmiel.robobank.model

import jakarta.persistence.*
import net.archasmiel.robobank.dto.StudentResponse
import net.archasmiel.robobank.dto.TransactionResponse
import java.time.LocalDateTime

@Entity
@Table(name = "transactions")
data class Transaction(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    val student: Student,

    @Column(nullable = false)
    val amount: Int,   // + = add, - = subtract

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val type: TransactionType,

    @Column(nullable = false)
    val description: String,

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)

enum class TransactionType {
    AWARDED,
    SPENT,
    ADJUSTED
}