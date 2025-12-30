# 🏦 RoboBank - Student Points Management System

A Spring Boot backend application for managing a virtual currency system for students. Teachers can award points, students can spend them, and admins have full control.

![Kotlin](https://img.shields.io/badge/Kotlin-1.9+-purple?logo=kotlin)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2+-green?logo=springboot)
![License](https://img.shields.io/badge/License-MIT-blue)

## 📋 Table of Contents

- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Getting Started](#-getting-started)
- [API Endpoints](#-api-endpoints)
- [Authentication](#-authentication)
- [Testing with cURL](#-testing-with-curl)
- [Database Console](#-database-console)
- [Project Structure](#-project-structure)

## ✨ Features

- 🔐 **JWT Authentication** - Secure token-based authentication with refresh tokens
- 👥 **Role-Based Access Control** - Three roles: STUDENT, TEACHER, ADMIN
- 💰 **Points System** - Award, spend, and adjust student points
- 📊 **Leaderboard** - Track top students by points balance
- 📝 **Transaction History** - Complete audit trail of all point movements
- 🔄 **Refresh Tokens** - Long-lived tokens for seamless re-authentication
- 🗄️ **H2 Database** - In-memory database with web console for testing

## 🛠️ Tech Stack

- **Language**: Kotlin 1.9+
- **Framework**: Spring Boot 3.2+
- **Security**: Spring Security + JWT (jjwt 0.12.3)
- **Database**: H2 (in-memory)
- **ORM**: Spring Data JPA / Hibernate
- **Build Tool**: Gradle (Kotlin DSL)

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Gradle 8+ (or use the included wrapper)
- Git

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/robobank.git
   cd robobank
   ```

2. **Run the application**
   ```bash
   # Using Gradle Wrapper (recommended)
   ./gradlew bootRun

   # Or on Windows
   gradlew.bat bootRun
   ```

3. **Verify it's running**
   ```bash
   curl http://localhost:8080/api/auth/test
   ```

   You should see: `"If you see this, endpoint works"`

### Default Test Accounts

The application comes with pre-configured test accounts:

| Username | Password    | Role    | Description                    |
|----------|-------------|---------|--------------------------------|
| admin    | admin123    | ADMIN   | Full system access             |
| teacher  | teacher123  | TEACHER | Can award/spend points         |
| student  | student123  | STUDENT | Can view data and spend points |

## 📡 API Endpoints

### Authentication Endpoints

All authentication endpoints are **public** (no token required).

#### Register New User
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "john_doe",
  "password": "password123",
  "email": "john@school.com",
  "role": "TEACHER"
}
```

**Response:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "refreshToken": "550e8400-e29b-41d4-a716-446655440000",
  "type": "Bearer"
}
```

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "teacher",
  "password": "teacher123"
}
```

#### Refresh Access Token
```http
POST /api/auth/refresh
Content-Type: application/json

{
  "refreshToken": "550e8400-e29b-41d4-a716-446655440000"
}
```

#### Get Current User Info
```http
GET /api/auth/me
Authorization: Bearer <your_access_token>
```

#### Logout
```http
POST /api/auth/logout
Authorization: Bearer <your_access_token>
```

---

### Student Endpoints

All student endpoints require authentication.

#### Create Student
```http
POST /api/students
Authorization: Bearer <your_access_token>
Content-Type: application/json

{
  "name": "Alice Johnson",
  "email": "alice@school.com"
}
```

#### Get All Students
```http
GET /api/students
Authorization: Bearer <your_access_token>
```

#### Get Student by ID
```http
GET /api/students/{id}
Authorization: Bearer <your_access_token>
```

#### Get Student by Email
```http
GET /api/students/email/{email}
Authorization: Bearer <your_access_token>
```

#### Get Leaderboard (Top 10)
```http
GET /api/students/leaderboard
Authorization: Bearer <your_access_token>
```

#### Delete Student
```http
DELETE /api/students/{id}
Authorization: Bearer <your_access_token>
```

---

### Transaction Endpoints

#### Award Points (TEACHER/ADMIN only)
```http
POST /api/transactions/award/{studentId}
Authorization: Bearer <your_access_token>
Content-Type: application/json

{
  "amount": 100,
  "description": "Excellent homework submission"
}
```

#### Spend Points
```http
POST /api/transactions/spend/{studentId}
Authorization: Bearer <your_access_token>
Content-Type: application/json

{
  "amount": 50,
  "description": "Bought extra recess time"
}
```

#### Adjust Points (ADMIN only)
```http
POST /api/transactions/adjust/{studentId}
Authorization: Bearer <your_access_token>
Content-Type: application/json

{
  "amount": -25,
  "description": "Penalty for misbehavior"
}
```

#### Get Student's Transaction History
```http
GET /api/transactions/student/{studentId}
Authorization: Bearer <your_access_token>
```

#### Get Recent Transactions (Top 20)
```http
GET /api/transactions/leaderboard
Authorization: Bearer <your_access_token>
```

---

## 🔐 Authentication

This API uses **JWT (JSON Web Tokens)** for authentication.

### How to Use

1. **Login or Register** to get tokens:
   ```bash
   curl -X POST http://localhost:8080/api/auth/login \
     -H "Content-Type: application/json" \
     -d '{"username": "teacher", "password": "teacher123"}'
   ```

2. **Save the tokens** from the response:
    - `accessToken` - Short-lived (24 hours), use for API requests
    - `refreshToken` - Long-lived (7 days), use to get new access tokens

3. **Use the access token** in all protected requests:
   ```bash
   curl http://localhost:8080/api/students \
     -H "Authorization: Bearer YOUR_ACCESS_TOKEN_HERE"
   ```

4. **Refresh expired tokens**:
   ```bash
   curl -X POST http://localhost:8080/api/auth/refresh \
     -H "Content-Type: application/json" \
     -d '{"refreshToken": "YOUR_REFRESH_TOKEN_HERE"}'
   ```

### Role-Based Permissions

| Endpoint                      | STUDENT | TEACHER | ADMIN |
|-------------------------------|---------|---------|-------|
| View students & leaderboard   | ✅      | ✅      | ✅    |
| View transactions             | ✅      | ✅      | ✅    |
| Spend points                  | ✅      | ✅      | ✅    |
| Award points                  | ❌      | ✅      | ✅    |
| Adjust points (manual)        | ❌      | ❌      | ✅    |

---

## 🧪 Testing with cURL

### Complete Test Flow

```bash
# 1. Login as teacher
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "teacher", "password": "teacher123"}'

# Save the accessToken from response, then use it below

# 2. Get all students
curl http://localhost:8080/api/students \
  -H "Authorization: Bearer YOUR_TOKEN"

# 3. Award points to student ID 1
curl -X POST http://localhost:8080/api/transactions/award/1 \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"amount": 100, "description": "Great participation!"}'

# 4. Check student balance
curl http://localhost:8080/api/students/1 \
  -H "Authorization: Bearer YOUR_TOKEN"

# 5. View transaction history
curl http://localhost:8080/api/transactions/student/1 \
  -H "Authorization: Bearer YOUR_TOKEN"

# 6. Check leaderboard
curl http://localhost:8080/api/students/leaderboard \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Testing Error Handling

```bash
# Test insufficient balance
curl -X POST http://localhost:8080/api/transactions/spend/1 \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"amount": 999999, "description": "Try to spend too much"}'

# Test invalid email
curl -X POST http://localhost:8080/api/students \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"name": "Test", "email": "not-an-email"}'
```

---

## 🗄️ Database Console

Access the H2 database web console to inspect data:

1. Navigate to: http://localhost:8080/h2-console
2. Use these credentials:
    - **JDBC URL**: `jdbc:h2:mem:robobank`
    - **Username**: `sa`
    - **Password**: *(leave blank)*
3. Click **Connect**

You can now run SQL queries like:
```sql
SELECT * FROM students;
SELECT * FROM transactions ORDER BY created_at DESC;
SELECT * FROM users;
```

---

## 📁 Project Structure

```
src/main/kotlin/net/archasmiel/robobank/
├── config/
│   └── DataInitializer.kt          # Sample data loader
├── controller/
│   ├── AuthController.kt           # Authentication endpoints
│   ├── StudentController.kt        # Student CRUD endpoints
│   ├── TransactionController.kt    # Transaction endpoints
│   └── GlobalExceptionHandler.kt   # Error handling
├── dto/
│   ├── AuthDto.kt                  # Auth request/response objects
│   ├── StudentDto.kt               # Student DTOs
│   └── TransactionDto.kt           # Transaction DTOs
├── model/
│   ├── User.kt                     # User entity
│   ├── Student.kt                  # Student entity
│   ├── Transaction.kt              # Transaction entity
│   └── RefreshToken.kt             # Refresh token entity
├── repository/
│   ├── UserRepository.kt           # User data access
│   ├── StudentRepository.kt        # Student data access
│   ├── TransactionRepository.kt    # Transaction data access
│   └── RefreshTokenRepository.kt   # Refresh token data access
├── security/
│   ├── SecurityConfig.kt           # Spring Security configuration
│   ├── JwtUtility.kt               # JWT token generation/validation
│   ├── JwtAuthFilter.kt            # JWT authentication filter
│   └── CustomUserDetailsService.kt # User loading service
├── service/
│   ├── AuthService.kt              # Authentication logic
│   ├── StudentService.kt           # Student business logic
│   ├── TransactionService.kt       # Transaction business logic
│   └── RefreshTokenService.kt      # Refresh token logic
└── RobobankApplication.kt          # Main application entry point
```

---

## 🐛 Troubleshooting

### Application won't start
- Ensure port 8080 is not in use
- Check Java version: `java -version` (must be 17+)
- Clean and rebuild: `./gradlew clean build`

### Authentication not working
- Make sure you're using `Authorization: Bearer <token>` header
- Check token hasn't expired (24 hours for access tokens)
- Use refresh token to get a new access token

### Database errors
- H2 is in-memory, data resets on restart
- Check H2 console at http://localhost:8080/h2-console

---

## 📝 License

This project is licensed under the MIT License.

---

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

---

## 📧 Contact

For questions or support, please open an issue on GitHub.

---

**Built with ❤️ using Kotlin and Spring Boot**