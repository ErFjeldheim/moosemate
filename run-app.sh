
# MooseMate Startup Script for macOS/Linux
# The script starts both backend (REST API) and frontend (JavaFX UI)

echo "====================================="
echo "  Starting MooseMate Application"
echo "====================================="
echo ""

# Gets the directory where the script is located
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd "$SCRIPT_DIR/moosemate"

echo "Starting Backend (REST API)..."
# Starts backend in a new terminal window
# Detect OS and use appropriate terminal emulator
if [[ "$OSTYPE" == "darwin"* ]]; then
    # macOS
    osascript -e 'tell application "Terminal" to do script "cd '"$SCRIPT_DIR"'/moosemate && mvn spring-boot:run -pl rest"'
elif command -v ptyxis &> /dev/null; then
    # Linux with Ptyxis (GNOME Console)
    ptyxis -- bash -c "cd '$SCRIPT_DIR/moosemate' && mvn spring-boot:run -pl rest; exec bash"
elif command -v gnome-terminal &> /dev/null; then
    # Linux with GNOME Terminal
    gnome-terminal -- bash -c "cd '$SCRIPT_DIR/moosemate' && mvn spring-boot:run -pl rest; exec bash"
elif command -v xterm &> /dev/null; then
    # Linux with xterm
    xterm -e "cd '$SCRIPT_DIR/moosemate' && mvn spring-boot:run -pl rest; exec bash" &
elif command -v konsole &> /dev/null; then
    # Linux with Konsole (KDE)
    konsole -e bash -c "cd '$SCRIPT_DIR/moosemate' && mvn spring-boot:run -pl rest; exec bash" &
else
    # Fallback: run in background
    echo "No compatible terminal emulator found. Starting backend in background..."
    cd "$SCRIPT_DIR/moosemate"
    mvn spring-boot:run -pl rest > backend.log 2>&1 &
    echo "Backend started in background (PID: $!). Logs in backend.log"
    cd "$SCRIPT_DIR"
fi

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

# Starts frontend in the current terminal
mvn javafx:run -pl ui

echo ""
echo "MooseMate frontend closed."
echo "Please close the backend terminal window manually if it's still running."
echo ""
