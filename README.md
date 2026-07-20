# Omni Mind 1x

## 📱 Android Application

A comprehensive AI assistant application built with Kotlin and Android Jetpack components.

---

## ✅ Prerequisites

- Android Studio (latest version)
- Java JDK 21
- Android SDK 36
- Gradle 8.5+

---

## 🚀 Quick Start

### 1. Clone the repository
```bash
git clone https://github.com/Chaouni-X0/Omni-mind-1x-.git
cd Omni-mind-1x-
```

### 2. Open in Android Studio
- Open Android Studio
- Select "Open an Existing Project"
- Navigate to the cloned repository

### 3. Sync Gradle
- Click "Sync Now" in Android Studio
- Or run: `./gradlew build --refresh-dependencies`

---

## 📦 Building the APK

### Debug APK (for testing)
```bash
./gradlew assembleDebug
```
**Output:** `app/build/outputs/apk/debug/app-debug.apk`

### Release APK (for production)
```bash
./gradlew assembleRelease
```
**Output:** `app/build/outputs/apk/release/app-release.apk`

---

## 📱 Installing on Device

### Using ADB
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

### Manual Installation
1. Transfer the APK to your device
2. Enable "Unknown Sources" in Settings
3. Open the APK file and install

---

## 🔧 Troubleshooting

### Common Issues

1. **Gradle sync failed**
   - Run: `./gradlew clean` then `./gradlew build --refresh-dependencies`
   - Check internet connection

2. **AAPT errors**
   - Make sure all resource files exist
   - Check for typos in XML files

3. **Kotlin compilation errors**
   - Ensure all imports are correct
   - Check Kotlin version in build.gradle.kts

4. **Missing dependencies**
   - Run: `./gradlew build --refresh-dependencies`
   - Check repository URLs in settings.gradle.kts

---

## 📂 Project Structure

```
Omni-mind-1x-
├── app/                    # Main application module
│   ├── src/main/
│   │   ├── java/com/example/omnimind/  # Kotlin source files
│   │   ├── res/            # Resources (layouts, drawables, etc.)
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts    # App module configuration
├── build.gradle.kts        # Project-level configuration
├── settings.gradle.kts    # Repository configuration
├── proguard-rules.pro     # ProGuard rules
└── build.sh               # Build script
```

---

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Submit a pull request

---

## 📄 License

MIT License

---

## 📞 Contact

For questions or support, please contact: mohamedchaouni0098@gmail.com