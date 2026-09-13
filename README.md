# Social Media Backend Clone

A RESTful social media backend developed using **Java, Spring Boot, Spring Security, JWT, JPA/Hibernate, and MySQL** as part of Phase 2 of my Java Programming Internship at Sqrock IT Solutions.

The project implements the core backend functionality of a social media platform, including authentication, user profiles, posts, likes, comments, following users, and a personalized news feed.

## Features

### Authentication & Authorization
- User registration
- Secure login
- JWT-based authentication
- BCrypt password hashing
- Role-based authorization (`USER` / `ADMIN`)
- Stateless Spring Security configuration
- Protected REST APIs

### User Management
- View own profile
- Update profile
- View other users
- Follow users
- Unfollow users
- View followers
- View following

### Post Management
- Create posts
- View posts
- Update own posts
- Delete own posts
- Track likes and comments

### Likes & Comments
- Like posts
- Unlike posts
- Add comments
- Delete own comments
- Associate interactions with authenticated users

### News Feed
- View posts from followed users
- Retrieve latest posts
- Newest-first ordering

### Admin
- Role-based admin endpoints
- View users
- Administrative post management

## Tech Stack

- Java 26
- Spring Boot 4.1.1
- Spring Web MVC
- Spring Security
- JWT (JJWT)
- Spring Data JPA
- Hibernate
- MySQL
- Maven
- Jakarta Validation
- Postman

## Project Architecture

The application follows a layered backend architecture:

```text
HTTP Request
     |
     v
Spring Security / JWT Filter
     |
     v
Controller
     |
     v
Service
     |
     v
Repository
     |
     v
MySQL Database
```

### Package Structure

```text
src/main/java/com/sqrock/socialmediabackend
|
+-- config
+-- controller
+-- dto
+-- exception
+-- model
+-- repository
+-- security
+-- service
+-- SqrockSocialMediaBackendApplication.java
```

## Security Flow

1. A user registers with an email and password.
2. The password is hashed using BCrypt before being stored.
3. The user logs in with their credentials.
4. Spring Security authenticates the credentials.
5. The backend generates a signed JWT.
6. The client sends the JWT with protected requests:

```text
Authorization: Bearer <JWT>
```

7. The JWT authentication filter validates the token.
8. The authenticated user is stored in Spring Security's security context.
9. Spring Security and the service layer enforce authorization rules.

## Database Relationships

The main entities are:

- User
- Post
- Comment
- PostLike
- Follow

Examples of relationships:

```text
User 1 ---- * Post
User 1 ---- * Comment
User * ---- * Post (through PostLike)

User ---- Follow ---- User
          |       |
       follower following
```

Unique constraints prevent a user from liking the same post or following the same user more than once.

## Configuration

The application uses environment variables for sensitive configuration.

Example `application.properties`:

```properties
spring.application.name=sqrock-social-media-backend

spring.datasource.url=jdbc:mysql://localhost:3306/social_media_db?createDatabaseIfNotExist=true
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD:}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.open-in-view=false

jwt.secret=${JWT_SECRET}
jwt.expiration-ms=${JWT_EXPIRATION_MS:86400000}
```

Set a sufficiently strong `JWT_SECRET` before running the application.

## Running the Project

### Requirements

- Java 26
- Maven
- MySQL

### 1. Clone the repository

```bash
git clone https://github.com/YOUR_USERNAME/sqrock-social-media-backend.git
cd sqrock-social-media-backend
```

### 2. Configure MySQL

Start MySQL and provide the required database credentials through environment variables or your local configuration.

The database can be created automatically using:

```text
createDatabaseIfNotExist=true
```

### 3. Run the application

Windows:

```bash
mvnw.cmd spring-boot:run
```

Linux/macOS:

```bash
./mvnw spring-boot:run
```

The API will run at:

```text
http://localhost:8080
```

## REST API Concepts Demonstrated

This project demonstrates:

- REST API design
- Request/response DTOs
- Dependency injection
- Layered architecture
- Service-layer business logic
- Repository pattern
- JPA entity relationships
- Database constraints
- Authentication
- Authorization
- JWT security
- Password hashing
- Role-based access control
- Resource ownership authorization
- Validation
- Global exception handling
- HTTP status codes
- Transaction management

## Learning Outcomes

This project strengthened my understanding of designing a structured Spring Boot backend rather than only implementing individual CRUD operations.

In particular, I gained practical experience with:

- Designing controller-service-repository architecture
- Securing REST APIs using Spring Security
- Implementing stateless JWT authentication
- Separating authentication from authorization
- Modeling relational data using JPA/Hibernate
- Applying business rules inside the service layer
- Protecting user-owned resources
- Building APIs around authenticated users

## Internship

Developed as part of the **Java Programming Internship — Project Phase 2** at **Sqrock IT Solutions**.