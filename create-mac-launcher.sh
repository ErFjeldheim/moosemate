#!/bin/bash
# MooseMate Launcher Creator for macOS
# Creates a .command file that can be double-clicked to launch MooseMate

echo "====================================="
echo "  MooseMate Launcher Creator (macOS)"
echo "====================================="
echo ""

# Get the directory where this script is located
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
LAUNCH_SCRIPT="$SCRIPT_DIR/run-app.sh"

# Check if run-app.sh exists
if [ ! -f "$LAUNCH_SCRIPT" ]; then
    echo "ERROR: run-app.sh not found!"
    echo "Please ensure this script is in the project root directory."
    read -p "Press Enter to exit..."
    exit 1
fi

# Use AppleScript to show folder picker dialog
SELECTED_FOLDER=$(osascript <<EOF
try
    set selectedFolder to choose folder with prompt "Select where to create MooseMate launcher:" default location (path to desktop folder)
    return POSIX path of selectedFolder
on error
    return ""
end try
EOF
)

# Check if user cancelled
if [ -z "$SELECTED_FOLDER" ]; then
    echo "Folder selection cancelled."
    read -p "Press Enter to exit..."
    exit 0
fi

# Remove trailing slash
SELECTED_FOLDER="${SELECTED_FOLDER%/}"
LAUNCHER_PATH="$SELECTED_FOLDER/MooseMate.command"

echo "Selected folder: $SELECTED_FOLDER"
echo ""

# Remove existing launcher if it exists
if [ -f "$LAUNCHER_PATH" ]; then
    echo "Removing existing MooseMate.command..."
    rm -f "$LAUNCHER_PATH"
fi

echo "Creating MooseMate.command launcher..."

# Create the .command launcher with absolute path
cat > "$LAUNCHER_PATH" <<LAUNCHER_EOF
#!/bin/bash
# MooseMate Application Launcher

# Project root stored as absolute path
PROJECT_ROOT="$SCRIPT_DIR"

if [ ! -f "\$PROJECT_ROOT/run-app.sh" ]; then
    echo ""
    echo "ERROR: Could not find MooseMate project files at:"
    echo "\$PROJECT_ROOT"
    echo ""
    echo "The project may have been moved or deleted."
    echo ""
    read -p "Press Enter to exit..."
    exit 1
fi

# Execute the run-app.sh script
cd "\$PROJECT_ROOT"
bash "\$PROJECT_ROOT/run-app.sh"
LAUNCHER_EOF

# Make the launcher executable
chmod +x "$LAUNCHER_PATH"

echo ""
echo "====================================="
echo "  SUCCESS!"
echo "====================================="
echo ""
echo "MooseMate.command created at:"
echo "  $LAUNCHER_PATH"
echo ""
echo "You can now double-click MooseMate.command"
echo "to launch the application!"
echo ""

read -p "Press Enter to exit..."
