# Quiz Management System - Setup & Compilation Guide

## Prerequisites
- Java 8 or higher (JDK)
- Oracle 11G Express Edition (XE) or higher
- ojdbc8.jar (Oracle JDBC Driver) - included in lib/ folder
- Windows Command Prompt or Unix Terminal

## STEP 1: Database Setup

### 1.1 Start Oracle Database
```bash
# Windows:
OraclXETNSListener.exe start
# or use Services panel

# Linux:
sudo systemctl start oracle-xe-11g
```

### 1.2 Execute SQL Script
1. Open SQL*Plus or SQL Developer
2. Connect as: system / admin
3. Run: `@database_setup.sql`
4. Verify tables created:
   ```sql
   SELECT table_name FROM user_tables;
   SELECT * FROM users;
   SELECT COUNT(*) FROM questions;
   ```

## STEP 2: Project Structure
```
Quiz Manager App/
├── src/
│   ├── db/
│   │   ├── DBConnection.java
│   │   └── QuizDAO.java
│   ├── gui/
│   │   ├── LoginWindow.java
│   │   └── QuizWindow.java
│   ├── lib/
│   │   └── ojdbc8.jar
│   ├── model/
│   │   ├── IQuestion.java
│   │   ├── MCQQuestion.java
│   │   ├── Question.java
│   │   ├── QuizException.java
│   │   └── User.java
│   ├── service/
│   │   ├── QuizManager.java
│   │   └── TimerThread.java
│   └── Main.java
└── bin/  (create this folder)
```

## STEP 3: Compilation Guide

### For Windows Command Prompt:
```batch
cd "path\to\Quiz Manager App"

REM Compile in order of dependencies:
REM 1. Compile model classes
javac -cp lib/ojdbc8.jar -d bin src/model/IQuestion.java
javac -cp lib/ojdbc8.jar -d bin src/model/Question.java
javac -cp lib/ojdbc8.jar -d bin src/model/MCQQuestion.java
javac -cp lib/ojdbc8.jar -d bin src/model/User.java
javac -cp lib/ojdbc8.jar -d bin src/model/QuizException.java

REM 2. Compile db classes
javac -cp lib/ojdbc8.jar;bin -d bin src/db/DBConnection.java
javac -cp lib/ojdbc8.jar;bin -d bin src/db/QuizDAO.java

REM 3. Compile service classes
javac -cp lib/ojdbc8.jar;bin -d bin src/service/QuizManager.java
javac -cp lib/ojdbc8.jar;bin -d bin src/service/TimerThread.java

REM 4. Compile gui classes
javac -cp lib/ojdbc8.jar;bin -d bin src/gui/LoginWindow.java
javac -cp lib/ojdbc8.jar;bin -d bin src/gui/QuizWindow.java

REM 5. Compile Main class
javac -cp lib/ojdbc8.jar;bin -d bin src/Main.java
```

### For Linux/Mac Terminal:
```bash
cd "path/to/Quiz Manager App"

# Compile in order of dependencies:
# 1. Compile model classes
javac -cp lib/ojdbc8.jar -d bin src/model/IQuestion.java
javac -cp lib/ojdbc8.jar -d bin src/model/Question.java
javac -cp lib/ojdbc8.jar -d bin src/model/MCQQuestion.java
javac -cp lib/ojdbc8.jar -d bin src/model/User.java
javac -cp lib/ojdbc8.jar -d bin src/model/QuizException.java

# 2. Compile db classes
javac -cp lib/ojdbc8.jar:bin -d bin src/db/DBConnection.java
javac -cp lib/ojdbc8.jar:bin -d bin src/db/QuizDAO.java

# 3. Compile service classes
javac -cp lib/ojdbc8.jar:bin -d bin src/service/QuizManager.java
javac -cp lib/ojdbc8.jar:bin -d bin src/service/TimerThread.java

# 4. Compile gui classes
javac -cp lib/ojdbc8.jar:bin -d bin src/gui/LoginWindow.java
javac -cp lib/ojdbc8.jar:bin -d bin src/gui/QuizWindow.java

# 5. Compile Main class
javac -cp lib/ojdbc8.jar:bin -d bin src/Main.java
```

## STEP 4: Execution Guide

### For Windows:
```batch
cd bin
java -cp ..\lib\ojdbc8.jar;. Main
```

### For Linux/Mac:
```bash
cd bin
java -cp ../lib/ojdbc8.jar:. Main
```

## STEP 5: Troubleshooting

### Issue: ClassNotFoundException: oracle.jdbc.driver.OracleDriver
**Solution:** Ensure ojdbc8.jar is in classpath during compilation and execution

### Issue: SQLException: connection refused
**Solution:** 
- Check if Oracle database is running
- Verify database credentials (system / admin)
- Check connection string: jdbc:oracle:thin:@localhost:1521:xe

### Issue: Cannot find symbol errors
**Solution:** Compile files in dependency order as shown above

## Default Credentials
- Database User: system
- Database Password: admin
- Sample Admin: admin / admin123
- Sample User: student1 / pass123

## Database Credentials for Login
Verify in DBConnection.java:
```java
private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
private static final String USER = "system";
private static final String PASS = "admin";
```

## Quick Start Summary
1. Create bin folder in src directory
2. Execute database_setup.sql in Oracle
3. Run Windows/Linux compilation commands above
4. Execute java command to launch application
