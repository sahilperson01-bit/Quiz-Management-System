# Java Online Quiz App

A comprehensive GUI-based online quiz management system built using Java Swing and MySQL. This application provides a platform for administrators to manage quizzes and for students to test their knowledge.

## Features

### 👨‍💼 Admin Panel
- **Quiz Management:** Create, update, and delete quizzes.
- **Question Bank:** Add, edit, and remove questions for each quiz.
- **Result Monitoring:** View scores and performance of students.

### 👨‍🎓 Student Panel
- **User Account:** Secure registration and login system.
- **Quiz Interface:** Browse available quizzes and take them in an interactive interface.
- **Instant Feedback:** View scores immediately after completing a quiz.
- **History:** Track past quiz results.

## Technologies Used
- **Programming Language:** Java (Swing, AWT)
- **Database:** MySQL
- **Database Connectivity:** JDBC (Java Database Connectivity)
- **IDE:** Compatible with VS Code, IntelliJ IDEA, Eclipse, or NetBeans.

## Prerequisites
Before running the application, ensure you have the following installed:
- **Java Development Kit (JDK):** Version 8 or higher.
- **MySQL Server:** For database management.

## Setup & Installation

1.  **Clone/Download the Repository**
    Extract the project files to your local machine.

2.  **Database Configuration**
    - Open your MySQL client (e.g., MySQL Workbench, Command Line).
    - Execute the `database_setup.sql` script located in the root directory.
    - This will create the `quiz_db` database, necessary tables, and a default admin account.

3.  **Update Connection Details**
    - Open `src/com/quiz/util/DBConnection.java`.
    - Modify the `USERNAME` and `PASSWORD` fields to match your local MySQL credentials:
      ```java
      private static final String USERNAME = "root"; 
      private static final String PASSWORD = "YOUR_MYSQL_PASSWORD";
      ```

4.  **Add Dependencies**
    - Ensure the `mysql-connector-j-9.5.0.jar` file (included in the root directory) is added to your project's **Classpath** or **Build Path**.

## How to Run
1.  Navigate to `src/com/quiz/ui/MainFrame.java`.
2.  Compile and run the file.
3.  The application window should appear.

## Usage

### Default Admin Credentials
- **Username:** `admin`
- **Password:** `admin123`

### For Students
- Launch the app and click on "Register" to create a new account.
- Login with your credentials to start taking quizzes.

## Project Structure
```
src/com/quiz/
├── dao/          # Data Access Objects (Database interactions)
├── model/        # Data Models (User, Quiz, Question, etc.)
├── service/      # Business Logic
├── ui/           # User Interface (Swing Components)
├── util/         # Utilities (Database Connection)
└── MainFrame.java # Entry point of the application
```
