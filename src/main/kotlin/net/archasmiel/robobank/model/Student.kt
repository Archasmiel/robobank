package net.archasmiel.robobank.model

import jakarta.persistence.*
import net.archasmiel.robobank.dto.StudentResponse
import java.time.LocalDateTime

@Entity
@Table(name = "students")
data class Student(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true)
    val email: String,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val balance: Int = 0,

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)