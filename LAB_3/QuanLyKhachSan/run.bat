@echo off
chcp 65001 > nul
call mvn clean package
if errorlevel 1 (
    echo BUILD THAT BAI.
    pause
    exit /b 1
)
java -jar target\QuanLyKhachSanJava-1.0.0.jar
pause
