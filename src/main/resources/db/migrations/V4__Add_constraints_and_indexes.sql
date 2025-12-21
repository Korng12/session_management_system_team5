USE conference_db;

-- Add additional indexes for performance
CREATE INDEX IF NOT EXISTS idx_sessions_conference_time 
ON sessions(conference_id, start_time);

CREATE INDEX IF NOT EXISTS idx_registrations_user_conference 
ON registrations(participant_id, conference_id, status);

CREATE INDEX IF NOT EXISTS idx_users_created 
ON users(created_at);

-- Add trigger for updated_at (if not using JPA @UpdateTimestamp)
DELIMITER //
CREATE TRIGGER IF NOT EXISTS update_users_timestamp
BEFORE UPDATE ON users
FOR EACH ROW
BEGIN
    SET NEW.updated_at = CURRENT_TIMESTAMP;
END//
DELIMITER ;

DELIMITER //
CREATE TRIGGER IF NOT EXISTS update_sessions_timestamp
BEFORE UPDATE ON sessions
FOR EACH ROW
BEGIN
    SET NEW.updated_at = CURRENT_TIMESTAMP;
END//
DELIMITER ;

-- Create a view for available rooms
CREATE OR REPLACE VIEW available_rooms AS
SELECT 
    r.id,
    r.name,
    r.capacity,
    CASE 
        WHEN s.id IS NOT NULL THEN 'OCCUPIED'
        ELSE 'AVAILABLE'
    END as status,
    s.start_time as next_available_from
FROM rooms r
LEFT JOIN sessions s ON r.id = s.room_id 
    AND s.end_time > NOW()
    AND s.start_time <= DATE_ADD(NOW(), INTERVAL 1 HOUR)
WHERE s.id IS NULL OR s.end_time < NOW();

-- Create a view for user schedules
CREATE OR REPLACE VIEW user_schedules AS
SELECT 
    u.id as user_id,
    u.name as user_name,
    s.id as session_id,
    s.title as session_title,
    s.start_time,
    s.end_time,
    r.name as room_name,
    c.title as conference_title
FROM users u
JOIN session_attendance sa ON u.id = sa.participant_id
JOIN sessions s ON sa.session_id = s.id
LEFT JOIN rooms r ON s.room_id = r.id
JOIN conferences c ON s.conference_id = c.id
ORDER BY u.id, s.start_time;