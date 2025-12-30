package net.archasmiel.robobank.controller

import jakarta.validation.Valid
import net.archasmiel.robobank.dto.CreateStudentRequest
import net.archasmiel.robobank.dto.StudentResponse
import net.archasmiel.robobank.service.StudentService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/students")
class StudentController(
    private val studentService: StudentService
) {

    @PostMapping
    fun createStudent(@Valid @RequestBody request: CreateStudentRequest): ResponseEntity<StudentResponse> {
        val student = studentService.createStudent(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(student)
    }

    @GetMapping("/{id}")
    fun getStudent(@PathVariable id: Long): ResponseEntity<StudentResponse> {
        val student = studentService.getStudentById(id)
        return ResponseEntity.ok(student)
    }

    @GetMapping("/email/{email}")
    fun getStudentByEmail(@PathVariable email: String): ResponseEntity<StudentResponse> {
        val student = studentService.getStudentByEmail(email)
        return ResponseEntity.ok(student)
    }

    @GetMapping
    fun getAllStudents(): ResponseEntity<List<StudentResponse>> {
        val students = studentService.getAllStudents()
        return ResponseEntity.ok(students)
    }

    @GetMapping("/leaderboard")
    fun getLeaderboard(): ResponseEntity<List<StudentResponse>> {
        val students = studentService.getLeaderboard()
        return ResponseEntity.ok(students)
    }

    @DeleteMapping("/{id}")
    fun deleteStudent(@PathVariable id: Long): ResponseEntity<Long> {
        studentService.deleteStudent(id)
        return ResponseEntity.noContent().build()
    }
}