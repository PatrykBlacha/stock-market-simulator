@echo off
setlocal

if "%~1"=="" (
    echo Error: Port argument is missing.
    echo Usage: start.bat ^<PORT^>
    exit /b 1
)

set PORT=%~1

echo Building and starting app on port %PORT%...

docker-compose up --build -d

echo =====================================================
echo Application successfully started
echo API is available at: http://localhost:%PORT%
echo =====================================================

endlocal