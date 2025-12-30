package net.archasmiel.robobank.service

import jakarta.transaction.Transactional
import net.archasmiel.robobank.dto.CreateStudentRequest
import net.archasmiel.robobank.dto.StudentResponse
import net.archasmiel.robobank.model.Student
import net.archasmiel.robobank.repository.StudentRepository
import org.springframework.stereotype.Service

@Service
class StudentService(
    private val studentRepository: StudentRepository
) {

    @Transactional
    fun createStudent(request: CreateStudentRequest): StudentResponse {
        if (studentRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("Student with email ${request.email} already exists")
        }

        val student = Student(
            name = request.name,
            email = request.email,
            balance = 0
        )

        val saved = studentRepository.save(student)
        return saved.toResponse()
    }

    fun getStudentById(id: Long): StudentResponse {
        val student = studentRepository.findById(id)
            .orElseThrow {
                NoSuchElementException("Student with id $id not found")
            }
        return student.toResponse()
    }

    fun getStudentByEmail(email: String): StudentResponse {
        val student = studentRepository.findByEmail(email)
            .orElseThrow {
                NoSuchElementException("Student with email $email not found")
            }
        return student.toResponse()
    }

    fun getAllStudents(): List<StudentResponse> {
        return studentRepository.findAll().map { it.toResponse() }
    }

    fun getLeaderboard(): List<StudentResponse> {
        return studentRepository.findTop10ByOrderByBalanceDesc()
            .map { it.toResponse() }
    }

    @Transactional
    fun deleteStudent(id: Long) {
        if (!studentRepository.existsById(id)) {
            throw NoSuchElementException("Student with $id not found")
        }
        studentRepository.deleteById(id)
    }

    fun Student.toResponse() = StudentResponse(
        id = id!!,
        name = name,
        email = email,
        balance = balance,
        createdAt = createdAt
    )
}