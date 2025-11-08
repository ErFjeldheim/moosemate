@REM Generated with help of CoPilot Claude Sonnet 4.5 due to complicated code/syntax
@echo off
setlocal enabledelayedexpansion
REM MooseMate Desktop Launcher for Windows
REM This script creates a desktop shortcut with the MooseMate icon

echo =====================================
echo   MooseMate Shortcut Creator
echo =====================================
echo.
echo Select where you want to create the MooseMate shortcut...
echo.

REM Get the current directory
set "SCRIPT_DIR=%~dp0"
set "BATCH_FILE=%SCRIPT_DIR%run-app.bat"
set "ICON_FILE=%SCRIPT_DIR%moosemate\ui\src\main\resources\images\moosmate_icon.ico"
set "CUSTOM_PATH="

REM Use PowerShell to show folder browser dialog
for /f "delims=" %%i in ('powershell -NoProfile -Command "Add-Type -AssemblyName System.Windows.Forms; $folderBrowser = New-Object System.Windows.Forms.FolderBrowserDialog; $folderBrowser.Description = 'Select folder where you want to create the MooseMate shortcut'; $folderBrowser.RootFolder = 'MyComputer'; $folderBrowser.ShowNewFolderButton = $true; if($folderBrowser.ShowDialog() -eq 'OK') { $folderBrowser.SelectedPath }"') do set "CUSTOM_PATH=%%i"

REM Check if user cancelled
if not defined CUSTOM_PATH (
    echo.
    echo Folder selection cancelled.
    echo.
    pause
    exit /b 0
)

echo Selected folder: !CUSTOM_PATH!
echo.

REM Check if run-app.bat exists
if not exist "%BATCH_FILE%" (
    echo ERROR: run-app.bat not found!
    echo Please ensure this script is in the project root directory.
    pause
    exit /b 1
)

REM Check if icon exists
if not exist "%ICON_FILE%" (
    echo WARNING: Icon file not found at %ICON_FILE%
    echo Shortcut will be created without icon.
    set "ICON_FILE="
)

echo Creating shortcut...

REM Create temporary VBScript with error handling
echo Set WshShell = CreateObject("WScript.Shell") > "%TEMP%\MooseMate-shortcut.vbs"
echo Set fso = CreateObject("Scripting.FileSystemObject") >> "%TEMP%\MooseMate-shortcut.vbs"
echo. >> "%TEMP%\MooseMate-shortcut.vbs"
echo On Error Resume Next >> "%TEMP%\MooseMate-shortcut.vbs"
echo. >> "%TEMP%\MooseMate-shortcut.vbs"
echo DesktopPath = "%CUSTOM_PATH%" >> "%TEMP%\MooseMate-shortcut.vbs"
echo. >> "%TEMP%\MooseMate-shortcut.vbs"
echo Set oShellLink = WshShell.CreateShortcut(DesktopPath ^& "\MooseMate.lnk") >> "%TEMP%\MooseMate-shortcut.vbs"
echo oShellLink.TargetPath = "%BATCH_FILE%" >> "%TEMP%\MooseMate-shortcut.vbs"
echo oShellLink.WorkingDirectory = "%SCRIPT_DIR%" >> "%TEMP%\MooseMate-shortcut.vbs"
if defined ICON_FILE echo oShellLink.IconLocation = "%ICON_FILE%" >> "%TEMP%\MooseMate-shortcut.vbs"
echo oShellLink.Description = "Launch MooseMate Application" >> "%TEMP%\MooseMate-shortcut.vbs"
echo oShellLink.Save >> "%TEMP%\MooseMate-shortcut.vbs"
echo. >> "%TEMP%\MooseMate-shortcut.vbs"
echo If Err.Number ^<^> 0 Then >> "%TEMP%\MooseMate-shortcut.vbs"
echo     WScript.Echo "ERROR: " ^& Err.Description >> "%TEMP%\MooseMate-shortcut.vbs"
echo     WScript.Quit 1 >> "%TEMP%\MooseMate-shortcut.vbs"
echo Else >> "%TEMP%\MooseMate-shortcut.vbs"
echo     WScript.Echo "SUCCESS: Shortcut created at " ^& DesktopPath ^& "\MooseMate.lnk" >> "%TEMP%\MooseMate-shortcut.vbs"
echo End If >> "%TEMP%\MooseMate-shortcut.vbs"

REM Run the VBScript and capture output
cscript //NoLogo "%TEMP%\MooseMate-shortcut.vbs"
set VBSCRIPT_ERROR=%ERRORLEVEL%

REM Clean up
del "%TEMP%\MooseMate-shortcut.vbs" 2>nul

REM Check result
if %VBSCRIPT_ERROR% EQU 0 (
    echo.
    if defined CUSTOM_PATH (
        echo You can now find the MooseMate shortcut at: !CUSTOM_PATH!\MooseMate.lnk
    ) else (
        echo You can now double-click the MooseMate icon on your Desktop to launch the app!
    )
    echo.
) else (
    echo.
    echo Please check that you have permission to create files in the selected location.
    echo.
)

pause
