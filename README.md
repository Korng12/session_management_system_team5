# Session Management System - Team 5

A Spring Boot web application for managing user sessions and registrations for a research conference.

## Features

- User registration and login
- User dashboard
- Schedule management
- Admin panel for user management
- Responsive UI with Bootstrap

## Technologies Used

- Java 21
- Spring Boot 3.5.8
- Thymeleaf
- Bootstrap 5
- H2 Database (for future use)

## Prerequisites

- Java 21 or higher
- Gradle

## Getting Started

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd session_management_system_team5
   ```

2. Build the project:
   ```bash
   ./gradlew build
   ```

3. Run the application:
   ```bash
   ./gradlew bootRun
   ```

4. Open your browser and go to `http://localhost:8081`

## Project Structure

- `src/main/java/com/team5/demo/` - Java source files
  - `DemoApplication.java` - Main application class
  - `UserController.java` - Handles user-related routes
  - `AdminController.java` - Handles admin routes
- `src/main/resources/templates/` - Thymeleaf templates
  - `user/` - User pages
  - `admin/` - Admin pages
  - `fragments/` - Reusable template fragments
- `src/main/resources/application.properties` - Application configuration

## Available Routes

- `/` - Home page
- `/register` - User registration
- `/login` - User login
- `/user/dashboard` - User dashboard
- `/admin` - Admin panel
- `/schedule` - Event schedule
- `/homepage` - Additional homepage

## Development

To add new features:
1. Create controllers in `src/main/java/com/team5/demo/`
2. Add templates in `src/main/resources/templates/`
3. Update routes as needed

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is for educational purposes.