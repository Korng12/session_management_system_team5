USE conference_db;

-- Create registrations table
CREATE TABLE IF NOT EXISTS registrations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    participant_id BIGINT NOT NULL,
    conference_id BIGINT NOT NULL,
    registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'PENDING',
    
    FOREIGN KEY (participant_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (conference_id) REFERENCES conferences(id) ON DELETE CASCADE,
    
    INDEX idx_participant (participant_id),
    INDEX idx_conference (conference_id),
    INDEX idx_status (status),
    
    -- Prevent duplicate registrations
    CONSTRAINT uq_participant_conference UNIQUE (participant_id, conference_id)
);

-- Create session_attendance junction table
CREATE TABLE IF NOT EXISTS session_attendance (
    participant_id BIGINT NOT NULL,
    session_id BIGINT NOT NULL,
    registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    PRIMARY KEY (participant_id, session_id),
    FOREIGN KEY (participant_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (session_id) REFERENCES sessions(id) ON DELETE CASCADE,
    
    INDEX idx_session_id (session_id),
    INDEX idx_participant_id (participant_id)
);