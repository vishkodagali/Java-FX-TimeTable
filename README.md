# MyTimetable Application

MyTimetable is a GUI-based Java application that allows students to manage their courses efficiently. This application implements object-oriented design principles and incorporates JavaFX for the graphical user interface, along with Java SE 8+.

## Table of Contents
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Getting Started](#getting-started)
- [Application Structure](#application-structure)
- [Usage](#usage)
- [Bonus Features](#bonus-features)
- [Future Improvements](#future-improvements)
- [Academic Integrity](#academic-integrity)

## Features
### Basic Functionalities
- **User Management:** 
  - Create user profiles with unique usernames, passwords (hashed for security), first name, last name, and student number.
  - Login functionality for existing users.
- **Dashboard:** 
  - Displays student information (student number, first name, and last name) post-login.
  - Allows users to edit their profile (except for the student number).
- **Course Management:**
  - View all courses with indicators for available and full courses.
  - Search for courses using course names.
  - Enroll in courses with checks for duplicate enrollments and timetable clashes.
  - Withdraw from courses.
  - Export a list of enrolled courses to a file.
- **Timetable View:** 
  - Display enrolled courses in a list view and a timetable view.
- **Data Persistence:** 
  - User profile data persists between sessions using JDBC for database storage.

### Bonus Functionalities
- Customize the application's font size, type (e.g., Calibri), color, and style (bold, italic).
- User passwords are stored securely using hashing.

## Tech Stack
- **Language:** Java SE 8 or later
- **GUI Framework:** JavaFX
- **Design Patterns:** MVC (Model-View-Controller), Singleton
- **Data Storage:** JDBC (Java Database Connectivity)
- **Java Collections:** Utilized for data management
- **Other:** Serialization and deserialization for data persistence

## Getting Started
### Prerequisites
- Java Development Kit (JDK) 8 or later
- JavaFX library
- JDBC-compatible database (e.g., SQLite, MySQL)

## Application Structure
- **Model:** Manages data and logic (user profiles, courses).
- **View:** JavaFX components for UI (login window, dashboard, course views).
- **Controller:** Links the model and view, handling user interactions.
- **Data Storage:** Implements JDBC for saving and loading user data.

## Usage
1. **Login or Create a New User Profile.**
2. **Dashboard:**
   - View and edit user profile.
   - View all available courses.
   - Search for courses and enroll in desired courses, ensuring there are no time clashes or duplicate enrollments.
   - Withdraw from courses if necessary.
   - View the list and timetable of enrolled courses.
   - Export the list of enrolled courses.
3. **Log Out:** Safely log out of the application, with data persisted for future logins.

## Bonus Features
- Change font size, type, color, and style through the application settings.
- Passwords are stored securely using hashing mechanisms.

## Future Improvements
- Add support for different course schedules (e.g., semesters, different start and end dates).
- Implement real-time notifications for enrollment updates.
