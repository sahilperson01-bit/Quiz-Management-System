-- MySQL Database Setup Script for Quiz Management System

DROP DATABASE IF EXISTS quiz_db;
CREATE DATABASE quiz_db;
USE quiz_db;

-- Create Users Table
CREATE TABLE Users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL,
    role ENUM('ADMIN', 'STUDENT') NOT NULL
);

-- Create Quizzes Table
CREATE TABLE Quizzes (
    quiz_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    description TEXT
);

-- Create Questions Table
CREATE TABLE Questions (
    question_id INT PRIMARY KEY AUTO_INCREMENT,
    quiz_id INT,
    question_text VARCHAR(500) NOT NULL,
    option1 VARCHAR(200) NOT NULL,
    option2 VARCHAR(200) NOT NULL,
    option3 VARCHAR(200) NOT NULL,
    option4 VARCHAR(200) NOT NULL,
    correct_option INT CHECK (correct_option BETWEEN 1 AND 4) NOT NULL,
    FOREIGN KEY (quiz_id) REFERENCES Quizzes(quiz_id) ON DELETE CASCADE
);

-- Create Results Table
CREATE TABLE Results (
    result_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    quiz_id INT,
    score INT NOT NULL,
    quiz_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (quiz_id) REFERENCES Quizzes(quiz_id) ON DELETE CASCADE
);

-- Seed Admin User (password: admin123)
INSERT INTO Users (username, password, role) VALUES ('admin', 'admin123', 'ADMIN');
