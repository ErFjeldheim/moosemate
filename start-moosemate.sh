#!/bin/bash
# MooseMate Startup Script for macOS/Linux
# This script starts both backend (REST API) and frontend (JavaFX UI)

echo "====================================="
echo "  Starting MooseMate Application"
echo "====================================="
echo ""

# Get the directory where the script is located
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd "$SCRIPT_DIR/moosemate"

echo "Starting Backend (REST API)..."
# Start backend in a new terminal window
osascript -e 'tell application "Terminal" to do script "cd '"$SCRIPT_DIR"'/moosemate && mvn spring-boot:run -pl rest"'

echo "Waiting for backend to start..."
sleep 2

echo "Starting Frontend (JavaFX UI)..."
echo ""
echo "====================================="
echo "  MooseMate is now running!"
echo "  Backend: http://localhost:8080"
echo "  Frontend: JavaFX Window"
echo "====================================="
echo ""
echo "Close the JavaFX window to stop the application"
echo ""

# Start frontend in the current terminal
mvn javafx:run -pl ui

echo ""
echo "MooseMate frontend closed."
echo "Please close the backend terminal window manually if it's still running."
echo ""
