@echo off
setlocal EnableDelayedExpansion

REM ============================================================
REM Omni Mind 1x - Build Script (Windows)
REM Requires: Java 17+, Android SDK (API 34)
REM ============================================================

echo.
echo ========================================
echo    Omni Mind 1x - Build
echo ========================================
echo.

REM --- Detect Java 17+ ---
set "JAVA_FOUND=0"
if defined JAVA_HOME (
    for /f "tokens=3" %%a in ('"%JAVA_HOME%\bin\java" -version 2^>^&1 ^| findstr /i "version"') do (
        set "JAVA_VER=%%~a"
        set "JAVA_VER=!JAVA_VER:.=!"
        set "JAVA_VER=!JAVA_VER:~0,2!"
        if !JAVA_VER! GEQ 17 (
            echo [INFO] Using JAVA_HOME=%JAVA_HOME%
            set "JAVA_FOUND=1"
        )
    )
)

if !JAVA_FOUND! EQU 0 (
    REM Check common paths
    for %%p in (
        "%ProgramFiles%\Microsoft\jdk-17*"
        "%ProgramFiles%\Eclipse Adoptium\jdk-17*"
        "%ProgramFiles%\Java\jdk-17*"
        "%USERPROFILE%\.sdkman\candidates\java\17*"
    ) do (
        for /d %%d in (%%p) do (
            if exist "%%d\bin\java.exe" (
                set "JAVA_HOME=%%d"
                echo [INFO] Found Java 17 at %%d
                set "JAVA_FOUND=1"
                goto :java_ok
            )
        )
    )
)

if !JAVA_FOUND! EQU 0 (
    echo [ERROR] Java 17+ not found!
    echo Install from: https://adoptium.net/
    echo Or run: scoop install openjdk17
    exit /b 1
)

:java_ok

REM --- Detect Android SDK ---
if not defined ANDROID_HOME (
    if exist "%LOCALAPPDATA%\Android\Sdk" (
        set "ANDROID_HOME=%LOCALAPPDATA%\Android\Sdk"
        echo [INFO] Using Android SDK at %LOCALAPPDATA%\Android\Sdk
    ) else if exist "%USERPROFILE%\AppData\Local\Android\Sdk" (
        set "ANDROID_HOME=%USERPROFILE%\AppData\Local\Android\Sdk"
        echo [INFO] Using Android SDK
    ) else (
        echo [ERROR] Android SDK not found!
        echo Install Android Studio or set ANDROID_HOME
        exit /b 1
    )
) else (
    echo [INFO] Using ANDROID_HOME=%ANDROID_HOME%
)

REM --- Parse args ---
set "BUILD_TYPE=all"
if "%~1"=="--debug" set "BUILD_TYPE=debug"
if "%~1"=="--release" set "BUILD_TYPE=release"
if "%~1"=="--clean" (
    echo [INFO] Cleaning...
    call gradlew.bat clean
)

REM --- Build ---
if "%BUILD_TYPE%"=="debug" (
    echo [INFO] Building DEBUG APK...
    call gradlew.bat assembleDebug
    echo [INFO] APK: app\build\outputs\apk\debug\app-debug.apk
) else if "%BUILD_TYPE%"=="release" (
    echo [INFO] Building RELEASE APK...
    call gradlew.bat assembleRelease
    echo [INFO] APK: app\build\outputs\apk\release\app-release-unsigned.apk
) else (
    echo [INFO] Building DEBUG APK...
    call gradlew.bat assembleDebug
    echo [INFO] Building RELEASE APK...
    call gradlew.bat assembleRelease
    echo.
    echo [INFO] APKs:
    echo   Debug:   app\build\outputs\apk\debug\app-debug.apk
    echo   Release: app\build\outputs\apk\release\app-release-unsigned.apk
)

echo.
echo [INFO] Build completed successfully!
