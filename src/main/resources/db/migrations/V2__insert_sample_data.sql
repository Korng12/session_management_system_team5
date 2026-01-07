-- 1. Insert roles
INSERT INTO roles (name) VALUES
('ADMIN'),
('CHAIR'),
('ATTENDEE');

-- 2. Insert users
INSERT INTO users (name, email, password) VALUES
('admin', 'admin@gmail.com', '$2a$10$uzwe.DvzO39HY4oNlxVKKefEgjd/psD18AYliojFJGDIfCRpkMSja'),
('user', 'user@gmail.com', '$2a$10$uzwe.DvzO39HY4oNlxVKKefEgjd/psD18AYliojFJGDIfCRpkMSja'),
('kea', 'kea@gmail.com', '$2a$10$uzwe.DvzO39HY4oNlxVKKefEgjd/psD18AYliojFJGDIfCRpkMSja'),
('mesa', 'mesa@gmail.com', '$2a$10$uzwe.DvzO39HY4oNlxVKKefEgjd/psD18AYliojFJGDIfCRpkMSja');

-- 3. Assign roles
INSERT INTO users_roles (user_id, role_id) VALUES
(1, 1),
(4, 2),
(2, 3),
(3, 3);

-- 4. Insert conferences
INSERT INTO conferences (title, description, start_date, end_date) VALUES
('Tech Conference 2024', 'Annual technology conference for developers', '2024-06-15', '2024-06-17'),
('Data Science Summit', 'Conference about AI and data science', '2024-07-20', '2024-07-22'),
('Web Development Workshop', 'Hands-on web development workshop', '2024-08-10', '2024-08-11');

-- 5. Insert rooms
INSERT INTO rooms (name, capacity) VALUES
('Main Hall', 100),
('Room A', 50),
('Room B', 30),
('Room C', 20),
('Auditorium', 80);

-- 6. Insert sessions
INSERT INTO sessions (title, chair_id, room_id, conference_id, start_time, end_time) VALUES
('Opening Keynote', 4, 1, 1, '2024-06-15 09:00:00', '2024-06-15 10:30:00'),
('AI in Modern Web', 4, 2, 1, '2024-06-15 11:00:00', '2024-06-15 12:30:00'),
('Database Optimization', NULL, 3, 1, '2024-06-16 10:00:00', '2024-06-16 11:30:00'),
('Machine Learning Basics', 4, 4, 2, '2024-07-20 09:00:00', '2024-07-20 11:00:00'),
('React Workshop', NULL, 2, 3, '2024-08-10 13:00:00', '2024-08-10 15:00:00');

-- 7. Insert conference registrations
INSERT INTO registrations (participant_id, conference_id, status) VALUES
(2, 1, 'CONFIRMED'),
(3, 1, 'CONFIRMED');

-- 8. Insert session attendance
INSERT INTO session_attendance (participant_id, session_id) VALUES
(2, 1),
(3, 1),
(3, 2),
(3, 4);
