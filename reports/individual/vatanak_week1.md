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
- Edit session details (title, chair, room, conference, start/end time, status)
- Update session status (Scheduled, Ongoing, Completed, Cancelled)
- Modal form for inline editing
- Form validation before submission
- Error handling and user feedback

**Implementation:**
- Modal dialog for editing (editSessionModal)
- PUT endpoint: `PUT /admin/sessions/{id}` - Update session
- JavaScript fetch API for async updates
- Real-time form validation
- Success/error messages with auto-hide

**Features:**
- Pre-populate form with current session details
- Dropdown selection for chair, room, conference
- Date/time picker for start and end times
- Submit validation
- Network error handling

![alt text](image-7.png)

![alt text](image-8.png)

#### Delete Session
- Soft delete implementation (mark as deleted, not permanent)
- Sessions can be restored/undone
- Modal confirmation before deletion
- Deleted status indicator in table
- Restore button for deleted sessions

**Implementation:**
- DELETE endpoint: `DELETE /admin/sessions/{id}` - Soft delete
- POST endpoint: `POST /admin/sessions/{id}/restore` - Restore deleted session
- Deleted column shows Active/Deleted badges
- Conditional button rendering (Edit/Delete for active, Restore for deleted)
- Confirmation dialogs for both delete and restore actions

**Features:**
- No permanent data loss
- Easy undo/recover functionality
- Clear visual indication of deleted sessions
- User-friendly restore process

![alt text](image-9.png)

### 1.2 Session Validation
- Required fields
  - Title (non-empty, max length)
  - Date/time validation
- Room availability check (prevents double-booking)
  - Detects time conflicts with other sessions
  - Returns conflicting session details
  - Supports exclusion of current session during updates
  - Real-time validation via API
- Time conflict validation
  - End time must be after start time
  - Checks room booking overlaps
  - Validates against existing sessions

**Implementation:**
- `checkRoomAvailability()` method in SessionService
- `GET /admin/rooms/{roomId}/availability` endpoint
- Calls repository method `findConflictingSessions()`
- Returns RoomAvailabilityResponse with conflict details
- Integrated into create and update operations

**Conflict Detection:**
- Finds sessions with overlapping time slots in the same room
- Returns details of conflicting sessions (ID, title, start/end times)
- Allows room selection only if no conflicts exist
- Provides user-friendly error messages

### 1.3 Session Search & Filter
- Real-time search by title, chair name, or room name
- Filter by session status (All, Scheduled, Ongoing, Completed, Cancelled)
- Reset button to clear all filters
- Client-side filtering for instant results
- Search is case-insensitive

**Implementation:**
- Search input field with keyup event listener
- Status dropdown with change event listener
- filterSessions() function filters allSessions array
- renderSessions() function renders filtered results
- Reset button clears both search and filter inputs

### 1.4 Session Chair Assignment
- Assign chair to session via dropdown selection
  - Select from list of available users with CHAIR role
  - Real-time chair availability checking
  - Prevents scheduling conflicts (chair cannot be in two places at once)
- Remove chair from session
  - One-click chair removal
  - Session remains but chair is unassigned
- Chair permissions and access control
  - Only admins can assign/remove chairs
  - Chairs can view their own sessions
  - Chair-specific filtering available
- Chair-specific session queries
  - Get all sessions for a specific chair
  - Paginated results for better performance

**Implementation:**
- Dropdown selection integrated into create/update forms
- `assignChairToSession()` method in SessionService with conflict checking
- `removeChairFromSession()` method for unassigning chairs
- `POST /admin/sessions/{sessionId}/assign-chair/{chairId}` endpoint
- `DELETE /admin/sessions/{sessionId}/remove-chair` endpoint
- Chair availability checked via `checkChairAvailability()` method

**Features:**
- Validates chair role/permissions before assignment
- Detects overlapping sessions in chair's schedule
- Returns detailed conflict information if chair is unavailable
- Automatic timestamp updates when chair is assigned/removed
- Success/failure messages returned to client
- Returns 409 Conflict status when chair has scheduling conflicts

**Chair Availability Logic:**
- Finds all sessions assigned to chair in the specified time period
- Excludes current session from conflict check during updates
- Returns list of conflicting sessions with details (ID, title, room, times)
- Prevents assignment if any conflicts exist

---

## Technical Implementation

### Backend Endpoints
- `POST /admin/sessions` - Create session
- `GET /admin/sessions` - List all sessions (active only)
- `GET /admin/sessions/{id}` - Get session by ID
- `PUT /admin/sessions/{id}` - Update session details
- `DELETE /admin/sessions/{id}` - Soft delete session (mark as deleted)
- `POST /admin/sessions/{id}/restore` - Restore deleted session
- `DELETE /admin/sessions/{id}/permanent` - Permanently delete session
- `POST /admin/sessions/{sessionId}/assign-chair/{chairId}` - Assign chair to session
- `DELETE /admin/sessions/{sessionId}/remove-chair` - Remove chair from session
- `GET /admin/rooms/{roomId}/availability` - Check room availability for time period
- `GET /admin/users/{chairId}/availability` - Check chair availability for time period
- `GET /admin/api/conferences` - Get conferences dropdown
- `GET /admin/api/users` - Get users dropdown
- `GET /admin/api/rooms` - Get rooms dropdown

### Frontend Features
- Modal-based CRUD interface for session management
- Real-time search and filtering
- Soft delete with restore capability
- Deleted status indicator column
- Form validation and error handling
- Responsive Bootstrap layout

### Database Schema
- Session entity with `deleted` boolean flag (soft delete)
- Chair/room/conference foreign key relationships
- Status enumeration (Scheduled, Ongoing, Completed, Cancelled)

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

### List all sessions

![alt text](image-15.png)