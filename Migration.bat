title Migration Module
@echo off
set CLASS_PATH=
set CLASS_MAIN=bizisolution.bin.Main
for %%i in (lib\*.jar) do call :SETCLASSPATH %%i
goto :RUN
:SETCLASSPATH
SET CLASS_PATH=%CLASS_PATH%;%1%
goto :EOF
:RUN
java -DName=%PROGRAM_NAME% -cp %CLASS_PATH% %CLASS_MAIN%
pause
