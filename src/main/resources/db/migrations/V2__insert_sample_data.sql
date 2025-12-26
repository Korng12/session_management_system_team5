    INSERT IGNORE INTO roles (role_name) VALUES 
    ('ADMIN'),
    ('ORGANIZER'),
    ('SPEAKER'),
    ('ATTENDEE');

    -- Insert sample rooms for testing
    INSERT IGNORE INTO rooms (name, capacity) VALUES 
    ('Grand Ballroom', 500),
    ('Conference Room A', 100),
    ('Conference Room B', 80),
    ('Workshop Room 1', 50),
    ('Workshop Room 2', 50);

    -- Insert a sample conference for testing
    INSERT IGNORE INTO conferences (title, description, start_date, end_date) VALUES 
        ('Tech Summit 2024', 'Annual technology conference for developers and innovators', '2025-12-26', '2025-12-27'),
        ('Business Leadership Forum', 'Leadership and management conference', '2025-12-30', '2025-12-31');

    -- Insert a test user 
    INSERT IGNORE INTO users (name, email, password) VALUES 
        ('Test Admin', 'admin@test.com', '11112222'),
        ('Test Speaker', 'speaker@test.com', '11112222'),
        ('Test Attendee', 'attendee@test.com', '11112222');

    -- Assign roles to test users
    INSERT IGNORE INTO user_roles (user_id, role_id) 
    SELECT u.id, r.id FROM users u, roles r 
    WHERE u.email = 'admin@test.com' AND r.role_name = 'ADMIN'
    ON DUPLICATE KEY UPDATE user_id = user_id;
    -- Assign roles to test speaker
    INSERT IGNORE INTO user_roles (user_id, role_id) 
    SELECT u.id, r.id FROM users u, roles r 
    WHERE u.email = 'speaker@test.com' AND r.role_name = 'SPEAKER'
    ON DUPLINATE KEY UPDATE user_id = user_id;
    -- Assign roles to test attendee
    INSERT IGNORE INTO user_roles (user_id, role_id) 
    SELECT u.id, r.id FROM users u, roles r 
    WHERE u.email = 'attendee@test.com' AND r.role_name = 'ATTENDEE'
    ON DUPLINATE KEY UPDATE user_id = user_id;