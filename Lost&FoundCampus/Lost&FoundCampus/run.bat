@echo off
title Lost & Found Campus - Server Runner
cd /d "%~dp0"

echo ========================================================
echo       MEMULAI SISTEM LOST & FOUND CAMPUS
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
echo [*] JAVA_HOME : %JAVA_HOME%

:: 2. Cek & Jalankan MySQL XAMPP jika belum aktif
netstat -ano | findstr :3306 >nul
if %ERRORLEVEL% NEQ 0 (
    echo [*] MySQL belum aktif. Menjalankan daemon MySQL XAMPP...
    if exist "C:\xampp\mysql\bin\mysqld.exe" (
        pushd "C:\xampp"
        start /B "" "C:\xampp\mysql\bin\mysqld.exe" --defaults-file="mysql\bin\my.ini" --standalone
        popd
        timeout /t 3 /nobreak >nul
    )
) else (
    echo [*] MySQL sudah berjalan di port 3306.
)

:: 3. Buka browser otomatis setelah aplikasi siap
start "" powershell -NoProfile -Command "Start-Sleep -Seconds 6; Start-Process 'http://localhost:8080/login'"

:: 4. Jalankan Spring Boot dengan Maven bawaan proyek
echo.
echo [*] Menjalankan Spring Boot Server...
echo [*] URL Login: http://localhost:8080/login
echo [*] Tekan Ctrl+C untuk menghentikan server.
echo.
call ".\apache-maven-3.9.6\bin\mvn.cmd" spring-boot:run
pause
