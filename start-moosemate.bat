@echo off
REM MooseMate Startup Script
REM Dette scriptet starter både backend (REST API) og frontend (JavaFX UI)

echo =====================================
echo   Starting MooseMate Application
echo =====================================
echo.

cd /d "%~dp0moosemate"

echo Starting Backend (REST API)...
start "MooseMate Backend" cmd /k "mvn spring-boot:run -pl rest"

echo Waiting for backend to start...
timeout /t 5 /nobreak >nul

echo Starting Frontend (JavaFX UI)...
echo.
echo =====================================
echo   MooseMate is now running!
echo   Backend: http://localhost:8080
echo   Frontend: JavaFX Window
echo =====================================
echo.
echo Close the JavaFX window to stop the application
echo.

call mvn javafx:run -pl ui

echo.
echo MooseMate frontend closed.
echo Please close the backend window manually if it's still running.
pause
