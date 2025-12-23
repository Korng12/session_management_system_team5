### Team5
#### Week 2 project Report
#### Member Focus Week 2 Accomplishment
#### 1. Security Authentication Defined Role hierarchy (Admin, Chair, User).
#### 2. Main Entity Conference CRUD Designed Admin forms for event creation.
#### 3. Secondary Participant Module Separated Account Sign-up from Event Enrollment.
#### 4. Frontend Thymeleaf/UI Implemented Master Layout and Fragment system.
#### 5. Database Schema & JPA Finished SQL Schema and started JPA Entity mappin
#### But all of these are not working well for now , we need to improve and config more
#### to ensure that between 3 core layers of the project conected well.


### Chantha Mengkong
#### Design Database schema
#### Structure folders of the project
#### config database
#### write sql script
#### merge members branche with main
### Chheang Kakda
#### 1. Create login page
#### 2. Homepage (user)
#### 3. Schedule(user)
#### 4. Register( user register)
#### 5. Profile (user)
#### 6. Admin dashboard
#### 7. manage room (Admin can manage room)
#### 8. View user ( admin can view user register )
#### 9. Schedule ( Admin manage schedule depent on user registered) (schedule.html)


**Name**: Hak Sengkea

![alt text](image.png)
![alt text](image-1.png)
---

## Project Overview

This project implements a secure session management system using Spring Boot 3.3.5 with Spring Security and JWT (JSON Web Token) authentication. The system manages user roles (Admin, Chair, Attendee) with role-based access control.

---

## Implementation Progress


**Implementation Details:**
- Configured JWT-based stateless authentication
- Implemented role-based authorization for three user types:
  - `ADMIN` - Access to `/admin/**` endpoints
  - `CHAIR` - Access to `/chair/**` endpoints
  - `ATTENDEE` - Access to `/attendee/**` endpoints
- Disabled session management (STATELESS mode)
- Configured CORS to allow all origins
- Set up custom authentication entry point for unauthorized access
- Integrated JWT filter before UsernamePasswordAuthenticationFilter

---

## Security Features Implemented

### Authentication
- JWT-based authentication
- Stateless session management
- BCrypt password hashing
- Email-based user identification

### Authorization
- Role-based access control (RBAC)
- Method-level security annotations
- Endpoint protection by role
- Custom authentication entry point

### API Security
- CORS configuration
- CSRF protection (disabled for REST API)
- Custom error responses
- Token expiration handling

---

## API Endpoints

### Public Endpoints
- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration

### Protected Endpoints
- `/admin/**` - Requires ADMIN role
- `/chair/**` - Requires CHAIR role
- `/attendee/**` - Requires ATTENDEE role

---


## Testing Progress

**Completed:**
- ✅ Manual API testing using REST client
- ✅ Login endpoint tested successfully
- ✅ JWT token generation verified
- ✅ Token format: Bearer token in Authorization header

**Tested Scenarios:**
- User login with valid credentials
- JWT token generation and return
- Response format validation

**Pending:**
- Role-based endpoint access testing
- Registration endpoint testing
- Token expiration testing
- Invalid credentials handling
- Automated unit tests
- Integration tests

---
## Learning Outcomes

Through this project, the following concepts were learned and applied:

### Spring Security Concepts
1. **Authentication vs Authorization**
   - Understanding the difference between verifying identity and granting permissions

2. **JWT Token-Based Authentication**
   - Stateless authentication mechanism
   - Token generation, validation, and expiration
   - Bearer token format and transmission

3. **Password Security**
   - BCrypt hashing algorithm
   - Salt generation with SecureRandom
   - Password strength configuration

4. **Role-Based Access Control (RBAC)**
   - Defining user roles and authorities
   - Protecting endpoints based on roles
   - Method-level security annotations

---

## Next Steps



### Future Enhancements
1. ⏳ Implement refresh token mechanism
2. ⏳ Add password reset functionality
3. ⏳ Implement email verification
4. ⏳ Add account activation/deactivation
5. ⏳ Integrate Thymeleaf for UI
6. ⏳ Write comprehensive unit tests
7. ⏳ Add API documentation (Swagger/OpenAPI)
8. ⏳ Implement rate limiting
9. ⏳ Add logging and monitoring

---

my dependency
```    // Spring Boot starters (keep these)
    implementation 'org.springframework.boot:spring-boot-starter-security'
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    // ... your other starters

    // Explicit Spring Security modules (required for advanced features)
    implementation 'org.springframework.security:spring-security-config'
    implementation 'org.springframework.security:spring-security-web'

    // JWT (JJWT) - required for JwtUtil and JwtFilter
    implementation 'io.jsonwebtoken:jjwt-api:0.12.6'  // Latest stable
    runtimeOnly 'io.jsonwebtoken:jjwt-impl:0.12.6'
    runtimeOnly 'io.jsonwebtoken:jjwt-jackson:0.12.6'  // JSON parser (or use jjwt-gson)

    // Lombok (ensure annotation processing)
    compileOnly 'org.projectlombok:lombok:1.18.34'  // Latest
    annotationProcessor 'org.projectlombok:lombok:1.18.34'
    testCompileOnly 'org.projectlombok:lombok:1.18.34'
    testAnnotationProcessor 'org.projectlombok:lombok:1.18.34'
   ```

### Davin Vatanak

# Session Management System - Task Progress

## 1. Session Management

### 1.1 Session CRUD Operations
- [x] **Create Session**
  - [x] Design session creation form
  - [x] Implement backend endpoint
  - [x] Add form validation
- [x] **Read Sessions**
  - [x] List all sessions
  - [x] View session details
  - [x] Pagination support
  - [x] Sorting options
- [x] **Update Session**
  - [x] Edit session details
  - [x] Update session status
  - [x] Handle concurrent edits
- [x] **Delete Session**
  - [x] Soft delete implementation
  - [x] Handle dependencies

### 1.2 Session Validation
- [x] Required fields
- [x] Title (non-empty, max length)
- [x] Date/time validation
- [x] Room availability check
- [x] Time conflict validation

### 1.3 Session Chair Assignment
- [x] Assign chair to session
- [x] Remove chair from session
- [x] Chair permissions
- [x] Chair access control

## 2. Registration System

### 2.1 Entity Relationships
- [x] Updated Registration entity with proper JPA relationships
- [x] Added Many-to-One relationship with User (as participant)
- [x] Added Many-to-One relationship with Session (as conference)
- [x] Implemented proper cascading and fetching strategies

### 2.2 Registration Service
- [x] Updated registration logic to use entity relationships
- [x] Added proper error handling
- [x] Implemented status management
- [x] Added registration time tracking

### 2.3 Repository Updates
- [x] Enhanced RegistrationRepository with relationship-based queries
- [x] Added custom query methods
- [x] Maintained backward compatibility

## 3. Room Management
- [x] Room CRUD operations
- [x] Capacity management
- [x] Room scheduling
- [x] Availability checking
- [x] Room assignment validation

## 4. User Management
- [x] User registration
- [x] Authentication
- [x] Role-based access control
  - [x] Admin role
  - [x] Chair role
  - [x] Author role
  - [x] Participant role
- [x] Profile management

## 5. Search & Filter

### 5.1 Session Filters
- [x] By speaker/chair
- [x] By date/time
- [x] By status
- [x] By room
- [x] By title
- [x] By participant

## 6. User Interface

### 6.1 Session Views
- [ ] List view
- [ ] Detail view
- [ ] Calendar view
- [ ] Print view

### 6.2 Dashboard
- [ ] Upcoming sessions
- [ ] Session statistics
- [ ] User activities

## 7. Testing

### 7.1 Unit Tests
- [ ] Session service
- [ ] Registration service
- [ ] Validation logic
- [ ] Business rules

### 7.2 Integration Tests
- [ ] API endpoints
- [ ] Database operations
- [ ] User workflows
- [ ] Registration flows

## 8. Documentation

### 8.1 API Documentation
- [ ] Endpoint specs
- [ ] Request/response examples
- [ ] Error codes

### 8.2 User Guides
- [ ] Session management
- [ ] User management
- [ ] Registration process

### 8.3 Technical Documentation
- [x] Entity relationship diagrams
- [ ] API documentation
- [ ] Database schema


#### Team5
### Name: Hong Dyhengmesa
## ID: e20220997

<h1><center>Individual Report</center><h1/>
<h2><center>Secondary Module Lead – Logistics & Registration</center></h2>

## 1. Introduction
```
   *The Logistics & Registration module is responsible for managing conference attendance logistics and scheduling.
    + This module ensures that:
        -> Users can register for a conference only once
        -> Physical rooms and time slots are managed correctly
        -> Double-booking of rooms is prevented
        -> Double-booking of rooms is prevented
``` 
## 2. Scope and Responsibilities
```
   + As the Secondary Module Lead, my responsibilities include:
        -> Designing entities related to logistics and registration
        -> Implementing CRUD operations for rooms and time slots
        -> Handling conference registration logic
        -> Applying validation rules to prevent conflicts
        -> Generating dashboard data for user schedules
```
## 3. System Architecture Overview
```
    + This module follows a layered Spring Boot architecture:
            -> Controller Layer – Handles HTTP requests
            -> Service Layer – Contains business logic and validation
            -> Repository Layer – Manages database operations using JPA
            -> Entity Layer – Defines database tables
```
## 4. Entity Design
```
4.1 ConferenceRegistration Entity
        + Purpose: Stores user registration information for a conference.
            - Key Fields:
                -> id – Primary key
                -> user – Registered user
                -> conference – Conference being registered
                -> registrationDate – Date of registration
4.2 Room Entity
        + Purpose: Represents physical rooms used for conference sessions.
            - Key Fields:
                -> id – Primary key
                -> roomName – Name or number of the room
                -> capacity – Maximum number of attendees 
4.3 TimeSlot Entity
        + Purpose: Defines available time slots for sessions.
            - Key Fields:
                -> id – Primary key
                -> startTime – Session start time
                -> endTime – Session end time
                -> room – Associated room
```
## 5. CRUD Operations
```
5.1 Room Management (CRUD)
        -> Create: Admins can add new rooms with name and capacity.
        -> Read: Admins can view all available rooms.
        -> Update: Admins can edit room details.
        -> Delete: Rooms can only be deleted if they are not assigned to any time slot.
5.2 Time Slot Management (CRUD)
        -> Create: Admins create time slots and assign them to rooms.
        -> Validation:
                Before saving, the system checks:
                   . If the selected room is already booked during the same time range.
        -> Read / Update / Delete: Admins can manage time slots with conflict checks applied.
```
## 6. Regirstration Flow Logic
``` 
   - Registration proccess:
      1. User logs into the system
      2. User selects a conference
      3. User clicks Register
      4. System checks:
           -> If the user is already registered
      5. If not registered:
           -> A ConferenceRegistration record is created
      6. Confirmation message is shown
```
## 7. Dashboard Logic
```
   Purpose: The My Schedule dashboard allows logged-in users to see:
       -> Conferences they are registered for
       -> Sessions, rooms, and time slots related to those conferences
```
## 8. Conclusion
```
    + The Logistics & Registration module successfully ensures:
            -> Accurate conference registration
            -> Conflict-free room scheduling
            -> Strong validation for business rules
            -> Personalized scheduling for users
        This module enhances system reliability, improves user experience, and supports smooth conference operations.
```