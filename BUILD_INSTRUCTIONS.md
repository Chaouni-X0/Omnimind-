# Omni Mind - Build Instructions

## Goal: Generate a working APK

This guide covers building the Omni Mind Android app.

---

## Prerequisites

- Android Studio (latest)
- JDK 17
- Android SDK 34
- Gradle wrapper 8.5 (included)

Verify:
```bash
java -version   # Should show version 17
```

---

## Build Steps

### 1. Clone
```bash
git clone <repo-url>
cd Omni-mind-1x-
chmod +x gradlew
```

### 2. Debug APK
```bash
./gradlew assembleDebug
```
APK outputs (ABI-split): `app/build/outputs/apk/debug/`

### 3. Release APK
```bash
export OMNIMIND_KEYSTORE_PATH=/path/to/keystore.jks
export OMNIMIND_KEYSTORE_PASSWORD=...
export OMNIMIND_KEY_ALIAS=...
export OMNIMIND_KEY_PASSWORD=...
./gradlew assembleRelease
```
APK outputs: `app/build/outputs/apk/release/`

Without signing vars the release APK will be unsigned.

---

## Troubleshooting

### Gradle sync failed
```bash
./gradlew clean && ./gradlew build --refresh-dependencies
```

### Kotlin compilation errors
- Package: `com.example.omnimind`
- Kotlin version (in `app/build.gradle.kts`): `1.9.22`

### Missing dependencies
```bash
./gradlew build --refresh-dependencies
```

### Full clean
```bash
rm -rf .gradle app/build
./gradlew clean assembleDebug
```

---

## Project structure (Android)

```
app/src/main/
├── java/com/example/       # Kotlin sources
├── res/                    # Resources (strings, themes, drawables)
└── AndroidManifest.xml
```

The app uses Jetpack Compose (no XML layouts), Room database, and Material 3 theming.
