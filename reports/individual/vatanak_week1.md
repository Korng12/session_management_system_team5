# VATANAK Branch - Week 1 Progress Report

## Overview

## 1. Session Management

### 1.1 Session CRUD Operations

#### Create Session
- Design session creation form
- Implement backend endpoint
- Add form validation

![alt text](image-4.png)

![alt text](image-5.png)

#### Read Sessions
- List all sessions
- View session details
- Pagination support
- orting options

#### Update Session
- Edit session details
- pdate session status
- Handle concurrent edits

![alt text](image-7.png)

![alt text](image-8.png)

#### Delete Session
- Soft delete implementation
- Handle dependencies

![alt text](image-9.png)

### 1.2 Session Validation
- Required fields
  - itle (non-empty, max length)
  - Date/time validation
- Room availability check
- ime conflict validation

### 1.3 Session Chair Assignment
- Assign chair to session
- Remove chair from session
- Chair permissions
- Chair access control

---

## Technical Implementation

### Backend Endpoints
- `POST /admin/sessions` - Create session
- `GET /admin/sessions` - List sessions
- `GET /admin/sessions/{id}` - Get session by ID
- `PUT /admin/sessions/{id}` - Update session
- `DELETE /admin/sessions/{id}` - Delete session
- `GET /admin/api/conferences` - Get conferences dropdown
- `GET /admin/api/users` - Get users dropdown
- `GET /admin/api/rooms` - Get rooms dropdown

### Login via api

![alt text](image-10.png)

### Get conference ID

![alt text](image-11.png)

### Get Room ID

![alt text](image-12.png)

### Get User ID

![alt text](image-13.png)

### Create Session

![alt text](image-14.png)