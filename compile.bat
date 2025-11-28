@echo off
REM Compilation script for Quiz Management System
REM Make sure you have Java JDK 25+ installed and Oracle JDBC driver in lib folder

echo Compiling Quiz Management System...

REM Set paths
set SOURCE_DIR=Quiz Manager App\src
set LIB_DIR=Quiz Manager App\lib
set OUTPUT_DIR=Quiz Manager App\bin

REM Create bin directory if not exists
if not exist %OUTPUT_DIR% mkdir %OUTPUT_DIR%

REM Compile all Java files
javac -d %OUTPUT_DIR% -cp %LIB_DIR%\* %SOURCE_DIR%\db\*.java %SOURCE_DIR%\model\*.java %SOURCE_DIR%\gui\*.java %SOURCE_DIR%\service\*.java %SOURCE_DIR%\Main.java

if %ERRORLEVEL% equ 0 (
  echo Compilation successful! Run RUN.bat to start the application.
) else (
  echo Compilation failed. Check the errors above.
  pause
)

pause
