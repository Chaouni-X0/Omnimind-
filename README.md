# Omni Mind 1x

## Android Application

A monorepo containing an Android AI assistant app (Kotlin/Jetpack Compose) plus experimental Expo/Node.js prototypes.

**Current status:** Android app in active development (v1.0.0). Hybrid prototypes exist under `frontend/` and `backend/` but are not yet buildable.

---

## Prerequisites

- Android Studio (latest)
- JDK 17
- Android SDK 34
- Gradle wrapper 8.5 (included)

---

## Quick Start

### 1. Clone
```bash
git clone <repo-url>
cd Omni-mind-1x-
```

### 2. Open in Android Studio
- "Open an Existing Project" → select cloned repo

### 3. Sync & Build
```bash
./gradlew assembleDebug
```

**Output:** `app/build/outputs/apk/debug/` (multiple APKs per ABI: arm64-v8a, armeabi-v7a, universal)

---

## Building the APK

### Debug (testing)
```bash
./gradlew assembleDebug
```
Output APKs under `app/build/outputs/apk/debug/` (ABI-split + universal).

### Release (production)
```bash
export OMNIMIND_KEYSTORE_PATH=/path/to/keystore.jks
export OMNIMIND_KEYSTORE_PASSWORD=...
export OMNIMIND_KEY_ALIAS=...
export OMNIMIND_KEY_PASSWORD=...
./gradlew assembleRelease
```
Output APKs under `app/build/outputs/apk/release/`.

> Without signing env vars, release build produces an unsigned APK.

---

## Project Structure

```
Omni-mind-1x-
├── app/                          # Android app module
│   ├── build.gradle.kts
│   └── src/main/
│       ├── java/com/example/     # Kotlin sources (package com.example.omnimind.*)
│       │   ├── presentation/     # UI layer (screens, navigation, ViewModel)
│       │   ├── domain/           # Business logic (swarm, terminal, editor, github)
│       │   └── data/             # Data layer (Room, network, security)
│       ├── res/                  # Resources (strings, themes, drawables)
│       └── AndroidManifest.xml
├── frontend/                     # Expo/React Native prototype (WIP, not buildable)
├── backend/                      # Express/Socket.IO prototype (WIP, not buildable)
├── gradle/                       # Gradle wrapper & version catalog
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
└── build.sh
```

### Key Android packages

| Area | Package |
|------|---------|
| Application | `com.example.omnimind` |
| MainActivity | `com.example.omnimind.MainActivity` |

---

## Hybrid prototypes (experimental)

- **`frontend/`**: Expo/React Native project — currently has structural issues preventing a build.
- **`backend/`**: Node.js/Express/Socket.IO server — TypeScript files are malformed; not runnable as-is.

See `README_HYBRID.md` for details.

---

## Troubleshooting

1. **Gradle sync fails**: `./gradlew clean && ./gradlew build --refresh-dependencies`
2. **AAPT errors**: Check resource files in `res/`
3. **Kotlin errors**: Verify `build.gradle.kts` Kotlin version (1.9.22)

---

## License

This project is proprietary. Developed by Mohamed Chaouni.

---

## Contact

mohamedchaouni0098@gmail.com
