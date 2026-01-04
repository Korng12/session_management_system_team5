
-- 1. Insert roles
INSERT INTO roles (name) VALUES
('ROLE_ADMIN'),
('ROLE_CHAIR'),
('ROLE_SPEAKER'),
('ROLE_ATTENDEE');

-- 2. Insert users
INSERT INTO users (name, email, password) VALUES
('Meng Kong', 'mengkong@gmail.com', '$2a$10$hashedpassword1'),
('Mesa', 'mesa@gmail.com', '$2a$10$hashedpassword2'),
('Seng kea', 'sengkea@gmail.com', '$2a$10$hashedpassword3'),
('Vathanak', 'vathanak@gmail.com', '$2a$10$hashedpassword4'),
('Kakda', 'kakda@gmail.com', '$2a$10$hashedpassword5');

-- 3. Assign roles to users
INSERT INTO users_roles (user_id, role_id) VALUES
(1, 1), -- Meng Kong is ADMIN
(2, 2), -- Mesa is CHAIR
(2, 4), -- Mesa is also ATTENDEE
(3, 3), -- Seng kea is SPEAKER
(3, 4), -- Seng kea is also ATTENDEE
(4, 4), -- Vathanak is ATTENDEE
(5, 4); -- Kakda is ATTENDEE

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
('Opening Keynote', 2, 1, 1, '2024-06-15 09:00:00', '2024-06-15 10:30:00'),
('AI in Modern Web', 2, 2, 1, '2024-06-15 11:00:00', '2024-06-15 12:30:00'),
('Database Optimization', NULL, 3, 1, '2024-06-16 10:00:00', '2024-06-16 11:30:00'),
('Machine Learning Basics', 2, 4, 2, '2024-07-20 09:00:00', '2024-07-20 11:00:00'),
('React Workshop', NULL, 2, 3, '2024-08-10 13:00:00', '2024-08-10 15:00:00');

-- 7. Insert conference registrations
INSERT INTO registrations (participant_id, conference_id, status) VALUES
(2, 1, 'CONFIRMED'), 
(3, 1, 'CONFIRMED'), 
(4, 1, 'CONFIRMED'), 
(5, 1, 'CONFIRMED'), 
(2, 2, 'CONFIRMED'), 
(3, 2, 'PENDING'),   
(4, 3, 'CONFIRMED'); 

-- 8. Insert session attendance (FIXED)
INSERT INTO session_attendance (participant_id, session_id) VALUES
(3, 1), -- Seng kea attends Opening Keynote
(4, 1), -- Vathanak attends Opening Keynote
(5, 1), -- Kakda attends Opening Keynote
(3, 2), -- Seng kea attends AI in Modern Web
(4, 2), -- Vathanak attends AI in Modern Web
(3, 4), -- Seng kea attends Machine Learning Basics
(4, 5); -- Vathanak attends React Workshop ✅
