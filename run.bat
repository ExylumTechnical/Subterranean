@echo off
setlocal

:: Create bin directory if it doesn't exist
if not exist bin (
    mkdir bin
)

:: Compile Java files with library
echo Compiling Java files...
javac -d bin -cp "lib\Misc.jar" src\*.java 

if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b
)

:: Run the main class
echo Running program...
java -cp "bin;lib\Misc.jar" subter.Main

endlocal
pause
