-- STEP 2: Oracle Database Setup for Quiz Management System
-- Execute this script in Oracle 11G
-- User: system / Password: admin

-- ============================================================================
-- TABLE 1: USERS
-- ============================================================================
CREATE TABLE users (
    id NUMBER PRIMARY KEY,
    username VARCHAR2(50) NOT NULL UNIQUE,
    password VARCHAR2(50) NOT NULL,
    role VARCHAR2(20) DEFAULT 'USER'
);

CREATE SEQUENCE users_seq START WITH 1 INCREMENT BY 1;

CREATE OR REPLACE TRIGGER users_trigger
BEFORE INSERT ON users
FOR EACH ROW
BEGIN
    SELECT users_seq.NEXTVAL INTO :NEW.id FROM dual;
END;
/

-- ============================================================================
-- TABLE 2: QUESTIONS
-- ============================================================================
CREATE TABLE questions (
    id NUMBER PRIMARY KEY,
    question VARCHAR2(500) NOT NULL,
    optionA VARCHAR2(100) NOT NULL,
    optionB VARCHAR2(100) NOT NULL,
    optionC VARCHAR2(100) NOT NULL,
    optionD VARCHAR2(100) NOT NULL,
    answer VARCHAR2(1) NOT NULL CHECK (answer IN ('A', 'B', 'C', 'D')),
    created_date TIMESTAMP DEFAULT SYSTIMESTAMP
);

CREATE SEQUENCE questions_seq START WITH 1 INCREMENT BY 1;

CREATE OR REPLACE TRIGGER questions_trigger
BEFORE INSERT ON questions
FOR EACH ROW
BEGIN
    SELECT questions_seq.NEXTVAL INTO :NEW.id FROM dual;
END;
/

-- ============================================================================
-- TABLE 3: USER_SCORES
-- ============================================================================
CREATE TABLE user_scores (
    id NUMBER PRIMARY KEY,
    user_id NUMBER NOT NULL,
    score NUMBER(3) NOT NULL,
    total_questions NUMBER(3) NOT NULL,
    quiz_date TIMESTAMP DEFAULT SYSTIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE SEQUENCE user_scores_seq START WITH 1 INCREMENT BY 1;

CREATE OR REPLACE TRIGGER user_scores_trigger
BEFORE INSERT ON user_scores
FOR EACH ROW
BEGIN
    SELECT user_scores_seq.NEXTVAL INTO :NEW.id FROM dual;
END;
/

-- ============================================================================
-- SAMPLE DATA
-- ============================================================================

INSERT INTO users (username, password, role) VALUES ('admin', 'admin123', 'ADMIN');
INSERT INTO users (username, password, role) VALUES ('student1', 'pass123', 'USER');
INSERT INTO users (username, password, role) VALUES ('student2', 'pass456', 'USER');
COMMIT;

INSERT INTO questions (question, optionA, optionB, optionC, optionD, answer) VALUES 
('What is the capital of India?', 'Mumbai', 'Delhi', 'Bangalore', 'Chennai', 'B');

INSERT INTO questions (question, optionA, optionB, optionC, optionD, answer) VALUES 
('Java is a...', 'Language', 'Framework', 'Database', 'Server', 'A');

INSERT INTO questions (question, optionA, optionB, optionC, optionD, answer) VALUES 
('SQL stands for?', 'Standard QL', 'Structured QL', 'Simple QL', 'System QL', 'B');

INSERT INTO questions (question, optionA, optionB, optionC, optionD, answer) VALUES 
('Which is not OOP?', 'Inheritance', 'Polymorphism', 'Recursion', 'Encapsulation', 'C');

INSERT INTO questions (question, optionA, optionB, optionC, optionD, answer) VALUES 
('Oracle is a...', 'Language', 'Database', 'OS', 'Browser', 'B');
COMMIT;
