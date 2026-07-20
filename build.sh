#!/bin/bash
set -e

# ============================================================
# Omni Mind 1x - Build Script
# Requires: Java 17+, Android SDK (API 34)
# ============================================================

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

log_info()  { echo -e "${GREEN}[INFO]${NC} $1"; }
log_warn()  { echo -e "${YELLOW}[WARN]${NC} $1"; }
log_error() { echo -e "${RED}[ERROR]${NC} $1"; }

# --- Detect Java 17+ ---
detect_java() {
    if [ -n "$JAVA_HOME" ]; then
        local java_ver=$("$JAVA_HOME/bin/java" -version 2>&1 | head -1 | grep -oP '"\K[0-9.]+' | cut -d. -f1)
        if [ "$java_ver" -ge 17 ] 2>/dev/null; then
            log_info "Using JAVA_HOME=$JAVA_HOME (Java $java_ver)"
            return 0
        fi
    fi

    for jdir in /usr/lib/jvm/java-17* /usr/lib/jvm/java-21* /Library/Java/JavaVirtualMachines/jdk-17*/Contents/Home /Library/Java/JavaVirtualMachines/jdk-21*/Contents/Home "$HOME/.sdkman/candidates/java/17"* "$HOME/.sdkman/candidates/java/21"*; do
        if [ -d "$jdir" ] && [ -f "$jdir/bin/java" ]; then
            local ver=$("$jdir/bin/java" -version 2>&1 | head -1 | grep -oP '"\K[0-9.]+' | cut -d. -f1)
            if [ "$ver" -ge 17 ] 2>/dev/null; then
                export JAVA_HOME="$jdir"
                log_info "Found Java $ver at $jdir"
                return 0
            fi
        fi
    done

    # Try system java
    if command -v java &>/dev/null; then
        local ver=$(java -version 2>&1 | head -1 | grep -oP '"\K[0-9.]+' | cut -d. -f1)
        if [ "$ver" -ge 17 ] 2>/dev/null; then
            log_info "Using system Java $ver"
            return 0
        fi
    fi

    log_error "Java 17+ not found!"
    log_error "Install it:"
    log_error "  Ubuntu/Debian: sudo apt install openjdk-17-jdk"
    log_error "  macOS:         brew install openjdk@17"
    log_error "  Windows:       scoop install openjdk17"
    exit 1
}

# --- Detect Android SDK ---
detect_android_sdk() {
    if [ -n "$ANDROID_HOME" ] && [ -d "$ANDROID_HOME" ]; then
        log_info "Using ANDROID_HOME=$ANDROID_HOME"
        return 0
    fi
    if [ -n "$ANDROID_SDK_ROOT" ] && [ -d "$ANDROID_SDK_ROOT" ]; then
        export ANDROID_HOME="$ANDROID_SDK_ROOT"
        log_info "Using ANDROID_SDK_ROOT=$ANDROID_SDK_ROOT"
        return 0
    fi

    local candidates=(
        "$HOME/Android/Sdk"
        "$HOME/Library/Android/sdk"
        "/opt/android-sdk"
        "/usr/local/android-sdk"
        "$ANDROID_HOME"
    )
    for dir in "${candidates[@]}"; do
        if [ -d "$dir/platforms" ]; then
            export ANDROID_HOME="$dir"
            log_info "Found Android SDK at $dir"
            return 0
        fi
    done

    log_error "Android SDK not found!"
    log_error "Install Android Studio or set ANDROID_HOME"
    exit 1
}

# --- Parse args ---
BUILD_TYPE="all"
CLEAN=false
while [ $# -gt 0 ]; do
    case $1 in
        --debug)    BUILD_TYPE="debug"; shift ;;
        --release)  BUILD_TYPE="release"; shift ;;
        --clean)    CLEAN=true; shift ;;
        --help|-h)
            echo "Usage: $0 [--debug|--release|--clean|--help]"
            echo "  --debug    Build debug APK only"
            echo "  --release  Build release APK only"
            echo "  --clean    Clean before building"
            echo "  (default)  Build both debug and release"
            exit 0 ;;
        *) log_error "Unknown option: $1"; exit 1 ;;
    esac
done

# --- Main ---
echo ""
echo "========================================"
echo "   Omni Mind 1x - Build"
echo "========================================"
echo ""

detect_java
detect_android_sdk

chmod +x gradlew

if [ "$CLEAN" = true ]; then
    log_info "Cleaning..."
    ./gradlew clean
fi

case $BUILD_TYPE in
    debug)
        log_info "Building DEBUG APK..."
        ./gradlew assembleDebug
        log_info "APK: app/build/outputs/apk/debug/app-debug.apk"
        ;;
    release)
        log_info "Building RELEASE APK..."
        ./gradlew assembleRelease
        log_info "APK: app/build/outputs/apk/release/app-release-unsigned.apk"
        ;;
    all)
        log_info "Building DEBUG APK..."
        ./gradlew assembleDebug
        log_info "Building RELEASE APK..."
        ./gradlew assembleRelease
        echo ""
        log_info "APKs:"
        log_info "  Debug:   app/build/outputs/apk/debug/app-debug.apk"
        log_info "  Release: app/build/outputs/apk/release/app-release-unsigned.apk"
        ;;
esac

echo ""
log_info "Build completed successfully!"
