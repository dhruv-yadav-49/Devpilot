# DevPilot Backend 🚀

Backend REST API service for **DevPilot** built with **Java 21**, **Spring Boot**, **Spring AI**, **Spring Security (OAuth2)**, and **PostgreSQL (pgvector)**.

---

## 🛠 Tech Stack

- **Java**: 21
- **Framework**: Spring Boot 4.x
- **GenAI & RAG**: Spring AI (OpenAI GPT-4o-mini & text-embedding-3-small)
- **Database**: PostgreSQL 16 with `pgvector`, `uuid-ossp`, and `hstore` extensions
- **ORM & Migrations**: Spring Data JPA, Hibernate, Flyway
- **Security**: Spring Security, GitHub OAuth2 Client, Session-based Cookies
- **Build Tool**: Apache Maven (Maven Wrapper included)

---

## 📁 Project Architecture

```
backend/
├── src/
│   ├── main/
│   │   ├── java/devPilot/backend/
│   │   │   ├── config/          # Security, CORS, and Crypto configurations
│   │   │   ├── controller/      # REST API Controllers (e.g., AuthController)
│   │   │   ├── dto/             # Data Transfer Objects & Records
│   │   │   ├── entity/          # JPA Entities (e.g., User)
│   │   │   ├── exceptions/      # Custom exceptions & GlobalExceptionHandler
│   │   │   ├── repository/      # Spring Data JPA Repositories
│   │   │   ├── security/        # OAuth2 User Service & Principal management
│   │   │   ├── services/        # Business logic services
│   │   │   └── BackendApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── pom.xml
├── mvnw / mvnw.cmd
└── README.md
```

---

## 📋 Prerequisites

Before running the backend, ensure you have installed:

- **JDK 21** (e.g., Oracle JDK or Eclipse Temurin)
- **Docker & Docker Desktop** (for PostgreSQL with vector support)
- **Git**
- A **GitHub OAuth Application** (for GitHub login)
- *(Optional)* An **OpenAI API Key** (for RAG & GenAI features)

---

## ⚙️ Configuration & Environment Variables

The backend uses [application.properties](file:///d:/abhim/Projects/DEVPILOT/backend/src/main/resources/application.properties) with support for environment variable overrides:

| Property | Environment Variable | Default Value | Description |
| :--- | :--- | :--- | :--- |
| `server.port` | `SERVER_PORT` | `8082` | HTTP Server port |
| `spring.datasource.url` | `DB_URL` | `jdbc:postgresql://localhost:5434/devpilot` | PostgreSQL connection URL |
| `spring.datasource.username` | `DB_USERNAME` | `postgres` | Database username |
| `spring.datasource.password` | `DB_PASSWORD` | `postgres` | Database password |
| `spring.ai.openai.api-key` | `OPENAI_API_KEY` | `your_openai_api_key_here` | OpenAI API Key for embeddings & chat |
| `spring.security.oauth2.client.registration.github.client-id` | `GITHUB_CLIENT_ID` | `your_github_client_id_here` | GitHub OAuth2 Client ID |
| `spring.security.oauth2.client.registration.github.client-secret` | `GITHUB_CLIENT_SECRET` | `your_github_client_secret_here` | GitHub OAuth2 Client Secret |
| `app.frontend-url` | `FRONTEND_URL` | `http://localhost:3000` | Frontend web client URL |
| `app.cors.allowed-origins` | `CORS_ALLOWED_ORIGINS` | `http://localhost:3000` | Allowed CORS origins |
| `app.token-encryptor-password` | `TOKEN_ENCRYPTOR_PASSWORD` | `devpilot-local-encrypt-key-change-me` | Secret key for token encryption |
| `app.token-encryptor-salt` | `TOKEN_ENCRYPTOR_SALT` | `deadbeefcafebabe` | Salt for token encryption |

---

## 🚀 Getting Started

### 1. Start the PostgreSQL Container

From the project root directory (`DEVPILOT`), launch the database container:

```bash
docker compose up -d
```

> **Note:** The database runs on host port **`5434`** as defined in `docker-compose.yml`.

### 2. Configure GitHub OAuth App

1. Go to **GitHub Settings > Developer Settings > OAuth Apps > New OAuth App**.
2. Set **Homepage URL**: `http://localhost:3000`
3. Set **Authorization callback URL**: `http://localhost:8082/login/oauth2/code/github`
4. Copy your **Client ID** and generate a **Client Secret**.
5. Set them in your environment or in `application.properties`:
   ```bash
   export GITHUB_CLIENT_ID="your_client_id"
   export GITHUB_CLIENT_SECRET="your_client_secret"
   ```

### 3. Build & Run the Backend

Navigate to the `backend` directory:

```bash
cd backend
```

#### On Windows (PowerShell):
```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-21.0.11"
.\mvnw.cmd spring-boot:run
```

#### On Windows (Command Prompt - CMD):
```cmd
set JAVA_HOME=C:\Program Files\Java\jdk-21.0.11
mvnw.cmd spring-boot:run
```

#### On Linux / macOS:
```bash
export JAVA_HOME=/path/to/jdk-21
./mvnw spring-boot:run
```

---

## 🔌 API Endpoints Overview

### Authentication (`/api/auth`)

| Method | Endpoint | Auth Required | Description |
| :--- | :--- | :---: | :--- |
| `GET` | `/api/auth/login-url` | No | Returns the OAuth2 login initiation route |
| `GET` | `/oauth2/authorization/github` | No | Redirects the user to GitHub OAuth login |
| `GET` | `/api/auth/me` | Yes | Returns current authenticated user profile |
| `POST` | `/api/auth/logout` | Yes | Logs out the user and clears `DEVPILOT_SESSION` |

#### Sample `/api/auth/login-url` Response:
```json
{
  "url": "/oauth2/authorization/github"
}
```

#### Sample `/api/auth/me` Response:
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "githubId": 12345678,
  "githubUsername": "octocat",
  "displayName": "The Octocat",
  "avatarUrl": "https://avatars.githubusercontent.com/u/583231"
}
```

---

## 🧪 Testing & Verification

To verify the code and run the build:

```bash
# Compile and verify dependencies
.\mvnw.cmd clean test-compile

# Run unit and integration tests
.\mvnw.cmd test
```

---

## 🛡 Security & Best Practices

- **Never commit real secrets** to version control. Always use environment variables or a local `.env` setup.
- Session cookies (`DEVPILOT_SESSION`) are configured with `HttpOnly` and `SameSite=Lax`.
