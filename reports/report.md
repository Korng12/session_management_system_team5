<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<!-----



Conversion time: 0.971 seconds.


Using this Markdown file:

1. Paste this output into your source file.
2. See the notes and action items below regarding this conversion run.
3. Check the rendered output (headings, lists, code blocks, tables) for proper
   formatting and use a linkchecker before you publish this page.

Conversion notes:

* Docs™ to Markdown version 2.0β1
* Mon Dec 15 2025 18:49:57 GMT-0800 (PST)
* Source doc: SE_report
----->


ព្រះរាជាណាចក្រកម្ពុជា

ជាតិ សាសនា ព្រះមហាក្សត្រ

Software Engineering

Project: Research Conference and Session Management System

Group: I4-GIC-B5

**Team Member’s Name					ID**

1. DAVIN VATANAK							e20220234	

2. CHEANG KAKDA							e20221438

3. CHANTHA MENGKONG					e20221526	

4. HONG DYHENGMESA					e20220997

5. HAK SENGKEA							e20220175	

Lecturer: Mr. Roeun Pacharoth

Academic year: 2025-2026


### **Project Overview**



* **System Name:** ConfManage
* **Goal:** A centralized platform where researchers can register, submit papers, and sign up for sessions, while admins manage the schedule and session chairs oversee presentation slots.
* **Core Tech Stack:** Java 21/17, Spring Boot 3.x, Spring Security, Thymeleaf, MySQL/PostgreSQL, Flyway, Git.


### **Team Roles & Responsibilities**


#### **1. Security & Authentication Lead**



* **Focus:** User Identity and Access Control.
* **Entities:** `User`, `Role`, `Privilege`.
* **Key Tasks:**
    * **Spring Security Setup:** Configure `SecurityFilterChain`, Password Encoding (BCrypt), and Custom Login/Logout success handlers.
    * **Registration Logic:** Create the sign-up service for new researchers/attendees.
    * **Method Security:** Implement `@PreAuthorize` annotations on Service methods (e.g., only 'ADMIN' can delete users).
    * **Profile Management:** Allow users to update their passwords and bio.


#### **2. Main Entity Lead (Sessions & Papers)**



* **Focus:** The core content of the conference.
* **Entities:** `Session` (e.g., "AI in Healthcare"), `Paper` (Abstracts/Submissions).
* **Key Tasks:**
    * **CRUD Operations:** Create, Read, Update, Delete for Sessions and Papers.
    * **Validation:** Ensure Papers have titles, authors, and non-empty abstracts.
    * **Business Logic:**
        * Logic to assign a **Session Chair** to a specific Session.
        * Logic to link accepted **Papers** to a **Session**.
    * **Search/Filter:** Allow filtering sessions by topic or speaker.


#### **3. Secondary Module Lead (Logistics & Registration)**



* **Focus:** The logistics of attending and scheduling.
* **Entities:** `ConferenceRegistration` (Ticket purchase), `Room` (Venue), `TimeSlot`.
* **Key Tasks:**
    * **CRUD Operations:** Manage physical Rooms and available Time Slots to prevent double-booking.
    * **Registration Flow:** Logic for a User to "Register" for the conference (creates a `ConferenceRegistration` record).
    * **Validation:** Ensure a user cannot register for the same conference twice; ensure a Room isn't booked for two sessions at the same time.
    * **Dashboard Logic:** Generate a "My Schedule" view data for the logged-in user.


#### **4. Frontend & UI/UX Lead (Thymeleaf)**



* **Focus:** User Interface, Accessibility, and Dynamic Content.nn,
* **Key Tasks:**
    * **Layout System:** Create the `layout.html` (header, footer, sidebar) using Thymeleaf Fragments (`th:replace`).
    * **Security Integration in UI:** Use `sec:authorize` attributes to hide "Admin" buttons from regular users.
    * **Form Handling:** Design reusable forms for Login, Registration, and CRUD inputs with visible error messages (`th:errors`).
    * **Styling:** Integrate Bootstrap or Tailwind CSS to make the dashboard responsive.
    * **Controller Advice:** Create global exception handling pages (e.g., custom 403 Access Denied or 404 Not Found pages).


#### **5. Database & DevOps Lead**



* **Focus:** Data Integrity, Migrations, and Repository Layer.
* **Key Tasks:**
    * **ER Diagram:** Design the database schema and relationships (One-to-Many, Many-to-Many).
    * **Flyway Migrations:** Write SQL scripts (`V1__init.sql`, `V2__seed_data.sql`) to version control the database structure.
    * **Data Seeding:** Create a script to populate the DB with a default Admin, dummy Sessions, and Rooms on startup.
    * **Repository Optimization:** Write complex JPQL or Native Queries (e.g., "Find all sessions where the room capacity is > 50").
    * **Git Management:** Manage the repository `main` branch and resolve merge conflicts.


### **Entity Relationship Strategy (Database Design)**

To meet the "Entity Relationships" requirement, the group should implement the following:



1. **User 1:N Registration** (One user can have one registration record per conference).
2. **Session 1:N Paper** (One session includes multiple papers).
3. **Session N:1 Room** (Many sessions can happen in one room, but at different times).
4. **User N:M Role** (A user can be both a SPEAKER and an ATTENDEE).
    * *Note for Member 5:* This requires a Join Table (`user_roles`).


### **Security Roles Breakdown**



* **ADMIN:** Can create Rooms, schedule Sessions, manage Users.
* **CHAIR:** Can view papers in their assigned Session, mark papers as "Presented."
* **SPEAKER:** Can submit Papers, view their own submission status.
* **ATTENDEE:** Can view the schedule, register for the conference.


### **Proposed Development Workflow (Git)**

Since you are required to show commit logs from all members, follow this workflow:



1. **Initialize:** Member 5 sets up the Spring Boot Project and pushes to GitHub.
2. **Branching:** Each member creates a branch for their feature (e.g., `feature/security-setup`, `feature/session-crud`).
3. **Pull Requests:** Do **not** merge directly to main. Open Pull Requests (PRs) so other members can review the code.
4. **Flyway:** If a member changes the DB, they must add a new V# SQL file. Do not modify existing SQL files after they have been run.
=======
### Team5

>>>>>>> b902c0a (design database schema,write sql script,structure project folders)
=======
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
>>>>>>> 55a6064 (Implement Secondary module lead-Logistics and Registration)
=======
### Team5

>>>>>>> 0b0d10bcd0bb2bccf4da22007dbf612f34bda259
