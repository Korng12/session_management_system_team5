
-- 1. ROLES (Create this first)
CREATE TABLE roles (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE -- Changed from 'username' to 'name' (e.g., ROLE_ADMIN)
);

-- 2. USERS
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 3. USERS_ROLES (The Bridge Table for Spring Security)
CREATE TABLE users_roles (
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- 4. CONFERENCES
CREATE TABLE conferences (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    start_date DATE,
    end_date DATE
);

-- 5. ROOMS
CREATE TABLE rooms (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(30) NOT NULL UNIQUE,
    capacity INT NOT NULL CHECK (capacity > 0 AND capacity <= 100)
);

-- 6. SESSIONS (Fixed typos in Foreign Keys)
CREATE TABLE sessions (    
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    chair_id INT,
    room_id INT,
    conference_id INT NOT NULL,
    start_time DATETIME,
    end_time DATETIME,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (chair_id) REFERENCES users(id) ON DELETE SET NULL,
    FOREIGN KEY (room_id) REFERENCES rooms(id) ON DELETE SET NULL,
    FOREIGN KEY (conference_id) REFERENCES conferences(id) ON DELETE CASCADE
);

-- 7. REGISTRATIONS (Conference level)
CREATE TABLE registrations (
    id INT PRIMARY KEY AUTO_INCREMENT,
    participant_id INT NOT NULL,
    conference_id INT NOT NULL,
    registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'CONFIRMED',
    FOREIGN KEY (participant_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (conference_id) REFERENCES conferences(id) ON DELETE CASCADE
);

-- 8. SESSION_ATTENDANCE (Session level Bridge Table)
CREATE TABLE session_attendance (
    participant_id INT NOT NULL,
    session_id INT NOT NULL,
    PRIMARY KEY (participant_id, session_id),
    FOREIGN KEY (participant_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (session_id) REFERENCES sessions(id) ON DELETE CASCADE
);