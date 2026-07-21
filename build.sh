#!/bin/bash

# Omni Mind Build Script
echo "Starting Omni Mind build process..."

# Clean previous builds
echo "Cleaning previous builds..."
./gradlew clean

# Refresh dependencies
echo "Refreshing dependencies..."
./gradlew build --refresh-dependencies

# Check for errors
if [ $? -ne 0 ]; then
    echo "Error: Dependency refresh failed"
    exit 1
fi

# Build debug APK
echo "Building debug APK..."
./gradlew assembleDebug

if [ $? -ne 0 ]; then
    echo "Error: Debug build failed"
    exit 1
fi

# Build release APK
echo "Building release APK..."
./gradlew assembleRelease

if [ $? -ne 0 ]; then
    echo "Error: Release build failed"
    exit 1
fi

echo ""
echo "Build successful!"
echo ""
echo "APK files location:"
echo "   Debug: app/build/outputs/apk/debug/ (ABI-split: arm64-v8a, armeabi-v7a, universal)"
echo "   Release: app/build/outputs/apk/release/ (ABI-split)"