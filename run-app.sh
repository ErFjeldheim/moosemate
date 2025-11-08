
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
if command -v osascript &> /dev/null; then
    # macOS
    osascript -e 'tell application "Terminal" to do script "cd '"$SCRIPT_DIR"'/moosemate && mvn spring-boot:run -pl rest"'
    # Linux terminals
elif command -v ptyxis &> /dev/null; then
    # Ptyxis terminal (GNOME)
    ptyxis -- bash -c "cd '$SCRIPT_DIR/moosemate' && mvn spring-boot:run -pl rest; exec bash" &
elif command -v gnome-terminal &> /dev/null; then
    # GNOME Terminal
    gnome-terminal -- bash -c "cd '$SCRIPT_DIR/moosemate' && mvn spring-boot:run -pl rest; exec bash" &
elif command -v konsole &> /dev/null; then
    # KDE Konsole
    konsole -e bash -c "cd '$SCRIPT_DIR/moosemate' && mvn spring-boot:run -pl rest; exec bash" &
elif command -v xterm &> /dev/null; then
    # xterm fallback
    xterm -e bash -c "cd '$SCRIPT_DIR/moosemate' && mvn spring-boot:run -pl rest; exec bash" &
else
    echo "Error: No supported terminal emulator found"
    echo "Please install one of: ptyxis, gnome-terminal, konsole, or xterm"
    exit 1
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
