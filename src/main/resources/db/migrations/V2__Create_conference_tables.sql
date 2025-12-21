USE conference_db;

-- Create conference table
CREATE TABLE IF NOT EXISTS conferences (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    INDEX idx_dates (start_date, end_date),
    CONSTRAINT chk_dates CHECK (end_date >= start_date)
);

-- Create rooms table
CREATE TABLE IF NOT EXISTS rooms (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(30) NOT NULL UNIQUE,
    capacity INT NOT NULL CHECK (capacity > 0),
    INDEX idx_room_name (name)
);

-- Create sessions table
CREATE TABLE IF NOT EXISTS sessions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(50) NOT NULL,
    location VARCHAR(50),
    chair_id BIGINT,
    room_id BIGINT,
    conference_id BIGINT NOT NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (chair_id) REFERENCES users(id) ON DELETE SET NULL,
    FOREIGN KEY (room_id) REFERENCES rooms(id) ON DELETE SET NULL,
    FOREIGN KEY (conference_id) REFERENCES conferences(id) ON DELETE CASCADE,
    
    INDEX idx_conference_id (conference_id),
    INDEX idx_time_range (start_time, end_time),
    INDEX idx_room_time (room_id, start_time),
    
    CONSTRAINT chk_session_time CHECK (end_time > start_time),
    CONSTRAINT uq_room_time UNIQUE (room_id, start_time) -- Prevent double booking at same start time
);