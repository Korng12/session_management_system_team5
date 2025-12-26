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