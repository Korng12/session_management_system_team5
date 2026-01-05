# VATANAK Branch - Week 2 Progress Report

## 2. Room Management

### 2.1 Entity Relationships
- Updated Registration entity with proper JPA relationships
- Added Many-to-One relationship with User (as participant)
- Added Many-to-One relationship with Session (as conference)
- Implemented proper cascading and fetching strategies

### 2.2 Registration Service

## 3. User Management

### User registration
### Authentication
### Role-based access control
- Admin role
- Chair role
- Author role
- Participant role
### Profile management

## 4. Search & Filter

### 4.1 Session Filters
- By speaker/chair
- By date/time
- By status
- By room
- By title

2. GET /api/sessions/search?chairId=3
   → Filter by session chair

3. GET /api/sessions/search?conferenceId=1
   → Filter by conference

4. GET /api/sessions/search?startDate=2026-01-15&endDate=2026-01-31

