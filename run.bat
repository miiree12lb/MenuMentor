@echo off
REM Compile and run MenuMentor (Windows)
if not exist out mkdir out
javac -encoding UTF-8 -cp "lib\*" -d out src\*.java || exit /b 1
java -cp "out;resources;lib\*" Main
