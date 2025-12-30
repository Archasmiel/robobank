package net.archasmiel.robobank.config

import net.archasmiel.robobank.model.Student
import net.archasmiel.robobank.model.User
import net.archasmiel.robobank.model.UserRole
import net.archasmiel.robobank.repository.StudentRepository
import net.archasmiel.robobank.repository.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class DataInitializer {

    @Bean
    fun initData(
        studentRepository: StudentRepository,
        userRepository: UserRepository,
        passwordEncoder: PasswordEncoder
    ) = CommandLineRunner {
        // Initialize users
        if (userRepository.count() == 0L) {
            val users = listOf(
                User(
                    username = "admin",
                    password = passwordEncoder.encode("admin123"),
                    email = "admin@school.com",
                    role = UserRole.ADMIN
                ),
                User(
                    username = "teacher",
                    password = passwordEncoder.encode("teacher123"),
                    email = "teacher@school.com",
                    role = UserRole.TEACHER
                ),
                User(
                    username = "student",
                    password = passwordEncoder.encode("student123"),
                    email = "student@school.com",
                    role = UserRole.STUDENT
                )
            )

            userRepository.saveAll(users)
            println("✅ Sample users created:")
            println("   - admin/admin123 (ADMIN)")
            println("   - teacher/teacher123 (TEACHER)")
            println("   - student/student123 (STUDENT)")
        }

        // Initialize students
        if (studentRepository.count() == 0L) {
            val students = listOf(
                Student(name = "Alice Johnson", email = "alice@school.com", balance = 150),
                Student(name = "Bob Smith", email = "bob@school.com", balance = 200),
                Student(name = "Charlie Brown", email = "charlie@school.com", balance = 75),
                Student(name = "Diana Prince", email = "diana@school.com", balance = 300),
                Student(name = "Ethan Hunt", email = "ethan@school.com", balance = 50)
            )

            studentRepository.saveAll(students)
            println("✅ Sample students created: ${students.size} students")
        }
    }
}