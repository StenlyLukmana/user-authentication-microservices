# Spring Boot Microservices – User Management and Authentication
Microservices-based architecture project built using Spring Boot. Implementing JWT authentication and RESTful API inter-service communication between user management and authentication services.


---


## 🧩 Main Microservices:
1. Auth Service – Handles user registration, login, token generation, and token validation.
2. User Service – Handles CRUD operations for user data. Communicates with Auth Service through REST APIs.


---


## 🔑 Key Features:
1. User Registration: Endpoint for new users to register.
2. Login: Endpoint for users to log in with their credentials and receive a token (e.g., JWT).
3. Token Validation: Ensure protected routes require a valid token.
4. User Information Retrieval: Endpoint for fetching user details.
5. User Profile Management: Allow users to update their profile information.


---


## ⚙️ Tech Stack
- Java 17
- Spring Boot 3.4.11
- Gradle - Groovy
- Spring Web
- Spring Security
- H2 Database
- Postman


---


## 🚀 How To Run The Application
This project uses an an in-memory database (H2), so it can be run immediately without any external database setup.
### 1. Clone the repository
```text
git clone https://github.com/StenlyLukmana/user-authentication-microservices.git
cd user-authentication-microservices
```
### 2. Configure Application Settings
Both auth-service and user-service already includes an H2 configuration inside.
auth-service/src/main/resources/application.yml
```text
server:
  port: 8080

services:
  user-service:
    url: http://localhost:8082

jwt:
  secret: this-is-a-secure-encoded-secret-key
  expiration: 3600000
```
user-service/src/main/resources/application.yml
```text
server:
  address: 0.0.0.0
  port: 8082

spring:
  datasource:
    url: "jdbc:h2:mem:user"
    username: "test"
    password: "test"
    driverClassName: org.h2.Driver
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
    hibernate:
      ddl-auto: update
```
### 3. Run Each Service
auth-service
```text
cd auth-service
./gradlew bootRun
```
user-service
```text
cd user-service
./gradlew bootRun
```


---


## 🧪 How To Test The APIs (Postman)

### User Registration
**Endpoint:**
```text
POST http://127.0.0.1:8080/auth/register
```
**Body(JSON):**
```text
{
    "username": "Batman",
    "password": "TheCapedCrusader",
    "email": "wayne@example.com",
    "name": "Bruce Wayne",
    "age": 35
}
```
**Expected Response:**
```text
{
    "username": "Batman",
    "email": "wayne@example.com",
    "name": "Bruce Wayne",
    "age": 35
}
```

### Login
**Endpoint:**
```text
POST http://127.0.0.1:8080/auth/login
```
**Body(JSON):**
```text
{
    "username": "Batman",
    "password": "TheCapedCrusader"
}
```
**Expected Response:**
```text
{
    "result": true,
    "token": "[Token]"
}
```
#### 📓Note
Copy the token given through the response in order to be able to access the User Information Retrieval and User Profile Management features.

### User Information Retrieval
**Endpoint:**
```text
GET http://127.0.0.1:8080/auth/profile
```
**Headers:**
```text
Key              | Value
Authorization    | [Paste Token Here]
```
**Expected Response:**
```text
{
    "username": "Batman",
    "email": "wayne@example.com",
    "name": "Bruce Wayne",
    "age": 35,
    "createdAt": 1762452617880988,
    "updatedAt": 1762452617880988
}
```

### Profile Management
**Endpoint:**
```text
POST http://127.0.0.1:8080/auth/update
```
**Headers:**
```text
Key              | Value
Authorization    | [Paste Token Here]
Content-Type     | application/json
```
**Body(JSON):**
```text
{
    "username": "The Dark Knight"
}
```
**Expected Response:**
```text
{
    "username": "The Dark Knight",
    "email": "wayne@example.com",
    "name": "Bruce Wayne",
    "age": 35,
    "updatedAt": 1762453140092056
}
```

### View User Profile Directly Through User Service (Optional)
**Endpoint:**
```text
http://127.0.0.1:8082/users/1
```
**Expected Response:**
```text
{
    "result": true,
    "user": {
        "id": 1,
        "username": "The Dark Knight",
        "password": "$2a$10$1mNFAKLZdWsgAqZkit/ozedpOHAFmpg7VHGc7fvYV1K8NgqcDG5zK",
        "email": "wayne@example.com",
        "name": "Bruce Wayne",
        "age": 35,
        "createdAt": 1762452617880988,
        "updatedAt": 1762453140092056
    }
}
```
#### 📓Note
Other endpoints in user-services can also be directly tested, but are completely optional.


---


## 🐉 Author
**Stenly Lukmana**<br>
Computer Science Student @ BINUS University<br>
Interested in Cybersecurity and Backend Development

---


## 📜 License
This project is for educational and assessment purposes only.
