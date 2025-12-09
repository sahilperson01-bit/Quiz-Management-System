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

- ## Screenshots

### Login Screen
The login screen provides a secure entry point for both administrators and students.
<img width="1920" height="1004" alt="Login Page" src="https://github.com/user-attachments/assets/0851d70e-8765-41d3-90aa-618086ac4677" />


### Admin Panel
#### Dashboard
The admin dashboard gives administrators quick access to all management functions.
<img width="1920" height="997" alt="Screenshot (47)" src="https://github.com/user-attachments/assets/86bc9d43-694a-451d-9e9e-a6d1107772af" />


#### Add Questions
Administrators can easily add questions to quizzes with the intuitive question creation interface.
<img width="1920" height="1011" alt="Screenshot (48)" src="https://github.com/user-attachments/assets/8b3ddb44-4631-4b73-98bf-c1b31f679b24" />


#### Bulk Import
Import multiple questions at once using the bulk import feature.
<img width="1920" height="993" alt="Screenshot (49)" src="https://github.com/user-attachments/assets/04cdfe03-038b-435a-88f5-27b419ed8f84" />


### Student Panel
#### Dashboard
The student dashboard shows available quizzes and past performance.
<img width="1920" height="1000" alt="Screenshot (50)" src="https://github.com/user-attachments/assets/f0c637f4-a759-4f97-b72f-d1da46fa7ca6" />


#### Quiz Results
Instant feedback with detailed score reports after completing a quiz.
<img width="1920" height="1000" alt="Screenshot (50)" src="https://github.com/user-attachments/assets/34e29981-0126-44b3-b755-d604d53cf23e" />


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


