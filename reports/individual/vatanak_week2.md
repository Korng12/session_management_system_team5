# VATANAK Branch - Week 2 Progress Report

**Period:** Sessions 18-19  
**Focus:** User Management & Session Search/Filter + Registration Entity  
**Status:** ✅ COMPLETE

## Overview

Expanded the system with comprehensive user profile management, session search/filtering capabilities, and proper JPA relationship implementation for the Registration entity.

## 1. Registration Entity with JPA Relationships

### Entity Design - Registration.java

**Many-to-One Relationships:**
- `@ManyToOne(fetch = FetchType.LAZY)` with User (participant)
  - FK: `participant_id` in registrations table
  - Nullable: false (every registration requires a participant)
  - Lazy loading for performance optimization
- `@ManyToOne(fetch = FetchType.LAZY)` with Conference (conference)
  - FK: `conference_id` in registrations table  
  - Nullable: false (every registration is for a conference)
  - Lazy loading for performance optimization

**Key Features:**
- Proper `@JoinColumn` annotations specifying foreign key column names
- `@EqualsAndHashCode.Exclude` to prevent circular reference issues
- `@PrePersist` hook to auto-set registration timestamp
- Status field (CONFIRMED, PENDING, CANCELLED)
- Automatic timestamp tracking with `registeredAt`

**Cascade and Fetching Strategy:**
- Lazy loading (LAZY fetch type) prevents N+1 query problems
- No cascading deletes to maintain data integrity
- Proper foreign key constraints in database

**JPA Relationship Flow:**
```
Registration (1) ← User (Many)  [participant_id → user.id]
Registration (1) ← Conference (Many) [conference_id → conference.id]
```

### Database Schema
- Table: `registrations`
- Columns:
  - `id` (PK, auto-increment)
  - `participant_id` (FK → users.id)
  - `conference_id` (FK → conferences.id)
  - `registered_at` (timestamp, auto-set on creation)
  - `status` (VARCHAR, default 'CONFIRMED')

## 2. Room Management

### 2.1 Entity Relationships
- Updated Registration entity with proper JPA relationships
- ✅ Added Many-to-One relationship with User (as participant)
- ✅ Added Many-to-One relationship with Conference
- ✅ Implemented proper cascading and fetching strategies

### 2.2 Registration Service
- ✅ User registration with proper JPA relationships
- ✅ Conference registration handling
- ✅ Registration status tracking

## 3. User Management

### 3.1 User registration
- User account creation with role assignment
- Email-based user identification
- Password encryption and security

### 3.3 Role-based access control
- Admin role - Full system access
- Chair role - Session management, attendance tracking
- Author role - Content creation permissions
- Participant role - Conference registration, session attendance

### 3.4 Profile management
- User profile retrieval and updates
- User search by email and ID (✅ implemented)

## 4. Search & Filter

### 4.1 Session Filters
- ✅ By chair
- ✅ By date/time
- ✅ By status
- ✅ By room
- ✅ By title  
**Focus:** User Management & Session Search/Filter  
**Status:** ✅ COMPLETE

