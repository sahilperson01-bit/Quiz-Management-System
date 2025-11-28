@echo off
REM Run script for Quiz Management System

echo Starting Quiz Management System...

REM Set paths
set BIN_DIR=Quiz Manager App\bin
set LIB_DIR=Quiz Manager App\lib

REM Check if bin directory exists
if not exist %BIN_DIR% (
  echo Error: Compiled classes not found. Please run compile.bat first.
  pause
  exit /b 1
)

REM Run the application
java -cp %BIN_DIR%;%LIB_DIR%\* Main

pause
