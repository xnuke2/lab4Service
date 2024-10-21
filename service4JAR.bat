

cd /d %~dp0
call mvn package -Dmaven.test.skip
pause>nul
