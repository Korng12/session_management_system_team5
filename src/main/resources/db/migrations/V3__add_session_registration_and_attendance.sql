-- 1. Add session registrations
CREATE TABLE session_registrations (
    id INT PRIMARY KEY AUTO_INCREMENT,
    participant_id INT NOT NULL,
    session_id INT NOT NULL,
    registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (participant_id, session_id),
    FOREIGN KEY (participant_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (session_id) REFERENCES sessions(id) ON DELETE CASCADE
);

-- 2. Enhance session attendance
ALTER TABLE session_attendance
ADD COLUMN status VARCHAR(20) NOT NULL DEFAULT 'ABSENT',
ADD COLUMN marked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN marked_by INT;

ALTER TABLE session_attendance
ADD CONSTRAINT fk_attendance_marked_by
FOREIGN KEY (marked_by) REFERENCES users(id);

-- 3. Add safety checks
ALTER TABLE sessions
ADD CONSTRAINT chk_session_status
CHECK (status IN ('SCHEDULED', 'ONGOING', 'COMPLETED', 'CANCELED'));

ALTER TABLE session_attendance
ADD CONSTRAINT chk_attendance_status
CHECK (status IN ('PRESENT', 'ABSENT'));
