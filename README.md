# University Management System 🎓

A modern RESTful backend service for managing university users (Administrators, Teachers, and Students) built with **Spring Boot 4 (Java 21)**, **Spring Security**, and **Spring Data JPA**. The project is containerized with Docker & Docker Compose and automated via GitHub Actions CI.

[![CI - Build & Test](https://github.com/ArifulHridoy/University_Management_System/actions/workflows/ci.yml/badge.svg)](https://github.com/ArifulHridoy/University_Management_System/actions/workflows/ci.yml)

---

## 🚀 Tech Stack

- **Language:** Java 21
- **Framework:** Spring Boot 4.1.1
- **Security:** Spring Security (Session-based auth, BCrypt password hashing, method-level authorization)
- **Database:**
  - **Development/CI:** H2 In-Memory Database
  - **Production/Docker:** MySQL 8.4
- **Persistence:** Spring Data JPA / Hibernate
- **Containerization:** Docker & Docker Compose
- **Testing:** JUnit 5, MockMvc, Spring Boot Test
- **CI/CD:** GitHub Actions

---

## 📁 Project Structure

```
university-management/
├── .github/workflows/
│   └── ci.yml                     # GitHub Actions CI workflow
├── src/
│   ├── main/
│   │   ├── java/com/university/management/
│   │   │   ├── api/               # REST Controllers, DTOs & Exception Handlers
│   │   │   │   ├── ApiExceptionHandler.java
│   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   ├── UserController.java
│   │   │   │   ├── UserRequest.java
│   │   │   │   └── UserResponse.java
│   │   │   ├── security/          # Spring Security configs & Authentication
│   │   │   │   ├── AuthController.java
│   │   │   │   └── SecurityConfig.java
│   │   │   └── user/              # User entity, Enums, Repo & DB Seeder
│   │   │       ├── DataInitializer.java
│   │   │       ├── User.java
│   │   │       ├── UserRepository.java
│   │   │       └── UserRole.java
│   │   └── resources/
│   │       └── application.yaml   # Application configuration
│   └── test/
│       └── java/com/university/management/
│           ├── UniversityManagementApplicationTests.java
│           ├── unit/              # 6 Unit Tests
│           │   └── UserUnitTests.java
│           └── integration/       # 3 Integration Tests
│               └── UserControllerIntegrationTest.java
├── Dockerfile                     # Multi-stage Docker build
├── compose.yaml                   # Docker Compose setup (App + MySQL)
├── pom.xml                        # Maven dependencies & build setup
└── README.md
```

---

## 🔑 Default Credentials

On application startup, a default administrator account is seeded automatically:

- **Username:** `admin`
- **Password:** `admin123`
- **Role:** `ADMIN`

*(Configurable via environment variables `APP_ADMIN_USERNAME` and `APP_ADMIN_PASSWORD`)*

---

## 🧪 Testing

The project includes unit tests, integration tests, and context verification:

- **6 Unit Tests (`UserUnitTests.java`)**:
  - `testUserConstructorAndGetters`: Entity constructor and getter validations.
  - `testUserSetters`: Property setter behavior.
  - `testUserRoleValues`: Enum values verification (`ADMIN`, `TEACHER`, `STUDENT`).
  - `testUserResponseFromUser`: DTO mapping logic from `User` entity.
  - `testResourceNotFoundExceptionMessage`: Custom exception message checking.
  - `testUserRequestRecord`: DTO record validation.

- **3 Integration Tests (`UserControllerIntegrationTest.java`)**:
  - `testLoginWithBadCredentials`: Verifies `POST /api/auth/login` rejects invalid credentials (401 Unauthorized).
  - `testGetStudentsAsAdminRole`: Verifies `GET /api/students` with `@WithMockUser(roles = "ADMIN")` returns 200 OK.
  - `testCreateStudentAsAdmin`: Verifies `POST /api/students` creates a new student (201 Created).

Run the tests locally:
```bash
# Windows
.\mvnw.cmd test

# Linux/macOS
./mvnw test
```

---

## ⚙️ Running Locally

### Prerequisites
- JDK 21+ installed
- Maven 3.9+ (or use the included `./mvnw` wrapper)

### Step 1: Clone the repository
```bash
git clone https://github.com/ArifulHridoy/University_Management_System.git
cd University_Management_System
```

### Step 2: Run the application
By default, the application runs using the in-memory **H2 database**, requiring no external setup:
```bash
# Windows
.\mvnw.cmd spring-boot:run

# Linux / macOS
./mvnw spring-boot:run
```
The server will start at `http://localhost:8080`.

---

## 🐳 Running with Docker & Docker Compose

To run the complete production setup with **MySQL 8.4**:

```bash
docker compose up --build
```

This starts:
- **MySQL Database:** on port `3306` (service with health check)
- **Spring Boot Application:** on port `8080`

To stop:
```bash
docker compose down
```

---

## 📡 API Endpoints

### 🔐 Authentication
| Method | Endpoint | Description | Access |
|---|---|---|---|
| `POST` | `/api/auth/login` | Authenticate and create session | Public |
| `POST` | `/api/auth/logout` | Invalidate session | Authenticated |

### 👥 User Management
| Method | Endpoint | Description | Allowed Roles |
|---|---|---|---|
| `GET` | `/api/admins` | List all admins | `ADMIN` |
| `POST` | `/api/admins` | Create a new admin | `ADMIN` |
| `GET` | `/api/teachers` | List all teachers | `ADMIN`, `TEACHER` |
| `POST` | `/api/teachers` | Create a new teacher | `ADMIN`, `TEACHER` |
| `GET` | `/api/students` | List all students | `ADMIN`, `STUDENT` |
| `POST` | `/api/students` | Create a new student | `ADMIN`, `STUDENT` |
| `GET` | `/api/{role}/{id}` | Get user by role & ID | Role-specific / `ADMIN` |
| `PUT` | `/api/{role}/{id}` | Update user details | Role-specific / `ADMIN` |
| `DELETE` | `/api/{role}/{id}` | Delete user | Role-specific / `ADMIN` |

---

## 🔄 CI/CD Pipeline

The project uses **GitHub Actions** (`.github/workflows/ci.yml`) to automatically build and test on every `push` and `pull_request`:
1. Checks out the code (`actions/checkout@v4`).
2. Configures Java 21 Temurin with Maven caching (`actions/setup-java@v5`).
3. Executes `./mvnw -q test` with H2 in-memory DB.
