@echo off
title Lost & Found Campus - Standalone JAR
cd /d "%~dp0"

echo ========================================================
echo     LOST & FOUND CAMPUS - STANDALONE JAR RUNNER
echo ========================================================
echo.

:: 1. Cek & Set JAVA_HOME otomatis
if "%JAVA_HOME%"=="" (
    if exist "C:\Program Files\Java\jdk-25.0.4.1" (
        set "JAVA_HOME=C:\Program Files\Java\jdk-25.0.4.1"
    ) else if exist "C:\Program Files\Java\latest" (
        set "JAVA_HOME=C:\Program Files\Java\latest"
    )
)

:: 2. Cek & Jalankan MySQL XAMPP jika belum aktif
netstat -ano | findstr :3306 >nul
if %ERRORLEVEL% NEQ 0 (
    echo [*] Menyalakan MySQL XAMPP...
    if exist "C:\xampp\mysql\bin\mysqld.exe" (
        pushd "C:\xampp"
        start /B "" "C:\xampp\mysql\bin\mysqld.exe" --defaults-file="mysql\bin\my.ini" --standalone
        popd
        timeout /t 3 /nobreak >nul
    )
)

:: 3. Buka browser otomatis
start "" powershell -NoProfile -Command "Start-Sleep -Seconds 4; Start-Process 'http://localhost:8080/login'"

:: 4. Jalankan executable JAR langsung
echo [*] Memulai server dari JAR (target\campus-0.0.1-SNAPSHOT.jar)...
echo [*] URL Login: http://localhost:8080/login
echo.
"%JAVA_HOME%\bin\java.exe" -jar target\campus-0.0.1-SNAPSHOT.jar
pause
