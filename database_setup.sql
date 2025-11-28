-- Oracle Database Setup for Quiz Management System

-- Create Users table
CREATE TABLE users (
  user_id NUMBER PRIMARY KEY,
  username VARCHAR2(50) UNIQUE NOT NULL,
  password VARCHAR2(100) NOT NULL,
  email VARCHAR2(100),
  role VARCHAR2(20) NOT NULL,
  created_at TIMESTAMP DEFAULT SYSTIMESTAMP
);

-- Create Questions table
CREATE TABLE questions (
  question_id NUMBER PRIMARY KEY,
  question_text VARCHAR2(1000) NOT NULL,
  option_a VARCHAR2(500),
  option_b VARCHAR2(500),
  option_c VARCHAR2(500),
  option_d VARCHAR2(500),
  correct_answer VARCHAR2(10),
  difficulty_level VARCHAR2(20),
  created_at TIMESTAMP DEFAULT SYSTIMESTAMP
);

-- Create Scores table
CREATE TABLE scores (
  score_id NUMBER PRIMARY KEY,
  user_id NUMBER,
  quiz_id NUMBER,
  score NUMBER,
  total_questions NUMBER,
  percentage NUMBER,
  quiz_date TIMESTAMP DEFAULT SYSTIMESTAMP,
  CONSTRAINT fk_user FOREIGN KEY(user_id) REFERENCES users(user_id)
);

-- Create sequences for auto-increment
CREATE SEQUENCE user_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE question_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE score_seq START WITH 1 INCREMENT BY 1;

-- Insert sample data
INSERT INTO users VALUES (1, 'admin', 'admin123', 'admin@quiz.com', 'ADMIN', SYSTIMESTAMP);
INSERT INTO users VALUES (2, 'user1', 'pass123', 'user1@quiz.com', 'USER', SYSTIMESTAMP);

INSERT INTO questions VALUES (1, 'What is Java?', 'Programming Language', 'Database', 'Network', 'Framework', 'A', 'EASY', SYSTIMESTAMP);
INSERT INTO questions VALUES (2, 'Which is faster?', 'Java', 'Python', 'JavaScript', 'Rust', 'D', 'MEDIUM', SYSTIMESTAMP);
INSERT INTO questions VALUES (3, 'What does OOP stand for?', 'Object Oriented Programming', 'Only Oriented Protocol', 'Object Operation Process', 'Operation Oriented Programming', 'A', 'EASY', SYSTIMESTAMP);

COMMIT;
