@echo off
setlocal
pushd "%~dp0" >nul || exit /b 1

rem Use PR4_JAVA_HOME to override this machine's known working JDK.
if defined PR4_JAVA_HOME (
    set "JAVA_HOME=%PR4_JAVA_HOME%"
) else (
    set "JAVA_HOME=C:\Program Files\Java\jdk-27.0.1.1"
)

if not exist "%JAVA_HOME%\bin\java.exe" (
    echo [ERROR] Java was not found at "%JAVA_HOME%".
    echo Set PR4_JAVA_HOME to your JDK 27+ folder and try again.
    popd
    exit /b 1
)

rem run.cmd        -> interactive CLI
rem run.cmd demo   -> original non-interactive demo
set "EXTRA_ARGS="
:parse_args
if "%~1"=="" goto run_app
if /i "%~1"=="demo" (
    set "EXTRA_ARGS=-Dexec.args=--demo"
) else if /i "%~1"=="--demo" (
    set "EXTRA_ARGS=-Dexec.args=--demo"
) else (
    set "EXTRA_ARGS=%EXTRA_ARGS% %~1"
)
shift
goto parse_args

:run_app
echo Starting PR4 Order Management...
call ".\mvnw.cmd" -B -pl Module/Order/app -am package exec:java %EXTRA_ARGS%
set "EXIT_CODE=%ERRORLEVEL%"
popd

if not "%EXIT_CODE%"=="0" (
    echo.
    echo [ERROR] The application did not finish successfully.
)
endlocal & exit /b %EXIT_CODE%
