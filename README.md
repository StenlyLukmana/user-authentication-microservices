# Spring Boot Microservices – User Management and Authentication
Microservices-based architecture project built using Spring Boot. Implementing JWT authentication, refresh token rotation, rate limiting, and secure RESTful inter-service communication.


---


## Main Microservices:
1. **Auth Service**  
   Handles:
   - User registration
   - Login
   - Access token (JWT) generation
   - Refresh token management
   - Token blacklisting (logout)
   - Rate limiting
   - Token validation for protected endpoints
2. **User Service**  
   Handles:
   - User CRUD
   - Profile management
   - User data persistence (H2 in-memory database)
   - Responds to Auth Service via REST APIs


---


## Security Architecture Overview
- **JWT Access Tokens** — short-lived tokens for accessing protected resources.  
- **Refresh Tokens** — long-lived UUID tokens stored in the database to issue new access tokens.  
- **Refresh Token Rotation** — backend issues new access tokens as long as the refresh token is valid.  
- **Token Blacklisting** — ensures logged-out users cannot reuse old tokens.  
- **IP-Based Rate Limiting** — token bucket algorithm with 1 token per minute to mitigate brute force attacks.  
- **BCrypt Password Hashing** — all passwords stored securely using BCrypt.  
- **Stateless Authentication** — protected endpoints require signature + expiry validation.


---


## Key Features:
1. User Registration
2. Login
3. Token Validation
4. User Profile Retrieval
5. User Profile Update
6. Access Token Renewal
7. Logout
8. Rate limiting

---


## Tech Stack
- Java 17
- Spring Boot 3.4.11
- Gradle (Groovy)
- Spring Web
- Spring Security
- H2 Database
- Postman


---


## How To Run The Application
This project uses an an in-memory database (H2), so it can be run immediately without any external database setup.
### 1. Clone the repository
```bash
git clone https://github.com/StenlyLukmana/user-authentication-microservices.git
cd user-authentication-microservices
```
### 2. Configure Application Settings
Both auth-service and user-service already includes an H2 configuration inside.<br>
auth-service/src/main/resources/application.yml
```yaml
server:
  port: 8080

services:
  user-service:
    url: http://localhost:8082

jwt:
  secret: this-is-a-secure-encoded-secret-key
  expiration: 120000

app:
  jwtRefreshExpirationMs: 3600000
```
user-service/src/main/resources/application.yml
```yaml
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
#### Note: In order to run the project in Visual Studio Code and test the services in Postman, make sure the proper extensions and softwares are installed
- Visual Code Extensions: <a href="https://marketplace.visualstudio.com/items?itemName=vmware.vscode-boot-dev-pack">Spring Boot Extension Pack</a>
- API Testing (Postman): <a href="https://learning.postman.com/docs/getting-started/installation/installation-and-updates/">Installation Guide<a/>
#### Once the Spring Boot Extension Pack has been installed, services can be run via command terminal (ctrl+`) or via Spring Boot Dashboard (icon available in VSC sidebar).
#### Command terminal
Run the following commands in different command terminals.
auth-service
```bash
cd auth-service
./gradlew bootRun
```
user-service
```bash
cd user-service
./gradlew bootRun
```
#### Spring Boot Dashboard
Click on the run button in the Apps section.<br>
<img width="300" height="400" alt="image" src="https://github.com/user-attachments/assets/aaf68736-80ad-4c58-92bd-1f4397c957b0" />


---


## How To Test The APIs (Postman)

### User Registration
**Endpoint:**
```nginx
POST http://127.0.0.1:8080/auth/register
```
**Body(JSON):**
```json
{
    "username": "Batman",
    "password": "Th3C@p3dCrus@d3r",
    "email": "wayne@example.com",
    "name": "Bruce Wayne",
    "age": 35
}
```

### Login
**Endpoint:**
```nginx
POST http://127.0.0.1:8080/auth/login
```
**Body(JSON):**
```json
{
    "username": "Batman",
    "password": "Th3C@p3dCrus@d3r"
}
```
#### Note
Copy both tokens (jwtToken and refresh token) given through the response in order to be able to access the User Information Retrieval, User Profile Management, and Access Token Renewal features.

### User Information Retrieval
**Endpoint:**
```nginx
GET http://127.0.0.1:8080/auth/profile
```
**Headers:**
```text
Key              | Value
Authorization    | [Paste Token Here]
```

### Profile Management
**Endpoint:**
```nginx
POST http://127.0.0.1:8080/auth/update
```
**Headers:**
```text
Key              | Value
Authorization    | [Paste Token Here]
Content-Type     | application/json
```
**Body(JSON):**
```json
{
    "username": "The Dark Knight"
}
```

### Token Renewal
**Endpoint:**
```nginx
POST http://127.0.0.1:8080/auth/refresh
```
**Headers:**
```text
Key              | Value
Content-Type     | application/json
```
**Body(JSON):**
```json
{
    "accessToken": "[Access Token]",
    "refreshToken": "[Refresh Token]"
}
```

### View User Profile Directly Through User Service (Optional)
**Endpoint:**
```nginx
GET http://127.0.0.1:8082/users/1
```
#### Note
Other endpoints in user-services can also be directly tested, but are completely optional.


---


## Author
**Stenly Lukmana**<br>
Computer Science Student @ BINUS University<br>
