# البدء السريع - OmniMind 1x

## في 5 دقائق فقط!

### الخطوة 1: استنساخ المشروع

```bash
git clone https://github.com/Chaouni-X0/Omni-mind-1x-.git
cd Omni-mind-1x-
```

### الخطوة 2: بناء APK

```bash
# للاختبار (Debug)
./build.sh debug

# أو للإصدار (Release)
export KEYSTORE_PATH=/path/to/keystore.jks
export STORE_PASSWORD=your_password
export KEY_PASSWORD=your_password
./build.sh release
```

### الخطوة 3: تثبيت على الجهاز

```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

### الخطوة 4: تشغيل التطبيق

```bash
adb shell am start -n com.omnimind.app/.MainActivity
```

## الأوامر الأساسية

| الأمر | الوصف |
|------|-------|
| `./build.sh debug` | بناء APK للاختبار |
| `./build.sh release` | بناء APK للإصدار |
| `./gradlew test` | تشغيل الاختبارات |
| `./gradlew clean` | تنظيف البناء |
| `adb devices` | عرض الأجهزة المتصلة |
| `adb logcat` | عرض السجلات |

## المتطلبات الأساسية

- Java 17+
- Android SDK 26+
- Git

## الملفات المهمة

```
.
├── app/                          # مجلد التطبيق
│   ├── build.gradle.kts         # إعدادات البناء
│   └── src/main/java/           # الكود المصدري
├── build.gradle.kts             # إعدادات البناء الرئيسية
├── gradle.properties            # خصائص Gradle
├── build.sh                     # سكريبت البناء
├── BUILD_GUIDE.md              # دليل البناء الشامل
├── DEPLOYMENT.md               # دليل النشر
└── README.md                   # الملف الرئيسي
```

## استكشاف الأخطاء السريع

### خطأ: "gradlew not found"

```bash
chmod +x gradlew
```

### خطأ: "Java not found"

```bash
# تثبيت Java 17
sudo apt-get install openjdk-17-jdk
```

### خطأ: "Android SDK not found"

```bash
export ANDROID_HOME=/path/to/android-sdk
export PATH=$PATH:$ANDROID_HOME/tools
```

### خطأ: "Device not found"

```bash
# تحقق من الأجهزة
adb devices

# أعد تشغيل adb
adb kill-server
adb start-server
```

## الخطوات التالية

1. اقرأ [دليل التطوير](./README_DEVELOPMENT.md)
2. اقرأ [دليل البناء الشامل](./BUILD_GUIDE.md)
3. اقرأ [دليل النشر](./DEPLOYMENT.md)
4. اقرأ [دليل المساهمة](./CONTRIBUTING.md)

## الروابط المفيدة

- [GitHub Repository](https://github.com/Chaouni-X0/Omni-mind-1x-)
- [Android Developer Guide](https://developer.android.com/)
- [Kotlin Documentation](https://kotlinlang.org/docs/)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)

## الدعم

- 📧 البريد الإلكتروني: support@omnimind.app
- 🐛 الأخطاء: [GitHub Issues](https://github.com/Chaouni-X0/Omni-mind-1x-/issues)
- 💬 النقاشات: [GitHub Discussions](https://github.com/Chaouni-X0/Omni-mind-1x-/discussions)

---

**نصيحة**: احفظ هذا الملف في المفضلة للعودة إليه لاحقاً! 📌
