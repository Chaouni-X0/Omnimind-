# دليل البناء - OmniMind 1x

## المحتويات

1. [المتطلبات](#المتطلبات)
2. [البناء المحلي](#البناء-المحلي)
3. [البناء للإصدار](#البناء-للإصدار)
4. [التثبيت على الجهاز](#التثبيت-على-الجهاز)
5. [الأوامر المفيدة](#الأوامر-المفيدة)
6. [CI/CD](#cicd)

## المتطلبات

### البرامج المطلوبة

- **JDK**: 17
- **Android SDK**: API 34 (compileSdk)
- **Gradle Wrapper**: 8.5 (مضمن)
- **Git**

### التحقق من التثبيت

```bash
java -version
echo $ANDROID_HOME
./gradlew --version
```

## الإعداد الأولي

```bash
git clone <repo-url>
cd Omni-mind-1x-
```

> لا يحتاج Android إلى ملف `.env` — يتم إدخال مفاتيح API من واجهة التطبيق.

## البناء المحلي

### Debug APK

```bash
./gradlew assembleDebug
```

**المخرجات**: `app/build/outputs/apk/debug/` (عدة APKs حسب ABI: arm64-v8a, armeabi-v7a, universal)

### Release APK

```bash
export OMNIMIND_KEYSTORE_PATH=/path/to/keystore.jks
export OMNIMIND_KEYSTORE_PASSWORD=...
export OMNIMIND_KEY_ALIAS=...
export OMNIMIND_KEY_PASSWORD=...
./gradlew assembleRelease
```

**المخرجات**: `app/build/outputs/apk/release/`

## التثبيت على الجهاز

```bash
adb install -r app/build/outputs/apk/debug/*-universal.apk
adb shell am start -n com.example.omnimind/.MainActivity
```

## الأوامر المفيدة

| الأمر | الوصف |
|------|-------|
| `./gradlew assembleDebug` | بناء APK اختباري |
| `./gradlew assembleRelease` | بناء APK إنتاجي |
| `./gradlew testDebugUnitTest` | اختبارات الوحدة |
| `./gradlew lintDebug` | تحليل الكود |
| `./gradlew clean` | تنظيف البناء |

## CI/CD

### GitHub Actions

الـ workflow الموجود يبني Android APK تلقائياً:

- الملف: `.github/workflows/omnimind-build.yml`
- `Java 17` + `Android SDK 34`
- `assembleDebug` + `testDebugUnitTest` + `lintDebug`
- رفع artifact باسم `OmniMind-debug-apks`
- Release يُبنى فقط عند توفر متغيرات التوقيع (`OMNIMIND_*`)

---

**آخر تحديث**: يوليو 2026
