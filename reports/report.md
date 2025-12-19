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
