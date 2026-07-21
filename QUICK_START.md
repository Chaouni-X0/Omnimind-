# البدء السريع - OmniMind 1x

### الخطوة 1: استنساخ المشروع

```bash
git clone <repo-url>
cd Omni-mind-1x-
```

### الخطوة 2: بناء APK

```bash
./gradlew assembleDebug
```

**المخرجات**: `app/build/outputs/apk/debug/` (ABI-split APKs)

### الخطوة 3: تثبيت على الجهاز

```bash
adb install -r app/build/outputs/apk/debug/*-universal.apk
```

### الخطوة 4: تشغيل التطبيق

```bash
adb shell am start -n com.example.omnimind/.MainActivity
```

## الأوامر الأساسية

| الأمر | الوصف |
|------|-------|
| `./gradlew assembleDebug` | بناء APK للاختبار |
| `./gradlew assembleRelease` | بناء APK للإصدار |
| `./gradlew testDebugUnitTest` | تشغيل اختبارات الوحدة |
| `./gradlew clean` | تنظيف البناء |

## المتطلبات الأساسية

- JDK 17
- Android SDK 34
- Git

## الملفات المهمة

```
├── app/                          # تطبيق Android
├── frontend/                     # نموذج Expo أولي (قيد التطوير)
├── backend/                      # نموذج Node.js أولي (قيد التطوير)
├── build.gradle.kts
├── gradle.properties
├── build.sh
└── README.md
```

## الدعم

- البريد الإلكتروني: mohamedchaouni0098@gmail.com

---

**آخر تحديث**: يوليو 2026
