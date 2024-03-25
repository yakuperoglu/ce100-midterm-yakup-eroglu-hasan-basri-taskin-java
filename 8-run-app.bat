@echo off
@setlocal enableextensions
@cd /d "%~dp0"

echo Running Application
java -jar bookshelforganizer-app/target/bookshelforganizer-app-1.0-SNAPSHOT.jar

echo Operation Completed!
pause