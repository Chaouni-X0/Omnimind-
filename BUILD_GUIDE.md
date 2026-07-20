# دليل البناء والنشر - OmniMind 1x

## المحتويات

1. [المتطلبات](#المتطلبات)
2. [الإعداد الأولي](#الإعداد-الأولي)
3. [البناء المحلي](#البناء-المحلي)
4. [البناء للإصدار](#البناء-للإصدار)
5. [التثبيت على الجهاز](#التثبيت-على-الجهاز)
6. [استكشاف الأخطاء](#استكشاف-الأخطاء)
7. [CI/CD والبناء التلقائي](#cicd-والبناء-التلقائي)

## المتطلبات

### البرامج المطلوبة

- **Java Development Kit (JDK)**: 17 أو أحدث
- **Android SDK**: API 26 أو أحدث
- **Gradle**: 8.0 أو أحدث (يتم توفيره عبر Gradle Wrapper)
- **Git**: لإدارة الإصدارات

### التحقق من التثبيت

```bash
# التحقق من Java
java -version

# التحقق من Android SDK
echo $ANDROID_HOME

# التحقق من Gradle
./gradlew --version
```

## الإعداد الأولي

### 1. استنساخ المستودع

```bash
git clone https://github.com/Chaouni-X0/Omni-mind-1x-.git
cd Omni-mind-1x-
```

### 2. إنشاء ملف .env

```bash
cat > .env << 'EOF'
# API Keys
GEMINI_API_KEY=your_gemini_api_key
OPENAI_API_KEY=your_openai_api_key
ANTHROPIC_API_KEY=your_anthropic_api_key

# GitHub
GITHUB_TOKEN=your_github_token

# Database
DATABASE_URL=your_database_url
EOF
```

### 3. تحديث gradle.properties

```bash
# تعديل gradle.properties إذا لزم الأمر
nano gradle.properties
```

### 4. مزامنة التبعيات

```bash
./gradlew sync
```

## البناء المحلي

### بناء APK للاختبار (Debug)

#### الطريقة 1: استخدام السكريبت

```bash
./build.sh debug
```

#### الطريقة 2: استخدام Gradle مباشرة

```bash
./gradlew assembleDebug
```

#### الطريقة 3: استخدام Android Studio

1. افتح المشروع في Android Studio
2. اختر `Build > Build Bundle(s) / APK(s) > Build APK(s)`

### موقع الملف الناتج

```
app/build/outputs/apk/debug/app-debug.apk
```

### حجم APK

عادة ما يكون بين 50-100 MB

## البناء للإصدار

### الخطوة 1: إنشاء Keystore

إذا لم تكن تملك keystore بعد:

```bash
keytool -genkey -v -keystore my-upload-key.jks \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias upload
```

### الخطوة 2: تعيين متغيرات البيئة

```bash
export KEYSTORE_PATH=/path/to/my-upload-key.jks
export STORE_PASSWORD=your_keystore_password
export KEY_PASSWORD=your_key_password
```

### الخطوة 3: بناء APK للإصدار

#### الطريقة 1: استخدام السكريبت

```bash
./build.sh release
```

#### الطريقة 2: استخدام Gradle مباشرة

```bash
./gradlew assembleRelease
```

### موقع الملف الناتج

```
app/build/outputs/apk/release/app-release.apk
```

## التثبيت على الجهاز

### المتطلبات

- جهاز أندرويد أو محاكي متصل
- USB Debugging مفعل (للأجهزة الفعلية)

### التثبيت التلقائي

```bash
# تثبيت Debug APK
./gradlew installDebug

# تثبيت Release APK
./gradlew installRelease
```

### التثبيت اليدوي

```bash
# استخدام adb
adb install -r app/build/outputs/apk/debug/app-debug.apk

# أو
adb install -r app/build/outputs/apk/release/app-release.apk
```

### التحقق من التثبيت

```bash
adb shell pm list packages | grep omnimind
```

### تشغيل التطبيق

```bash
adb shell am start -n com.omnimind.app/.MainActivity
```

## استكشاف الأخطاء

### المشكلة: فشل البناء مع خطأ Gradle

**الحل:**

```bash
# تنظيف والبناء من جديد
./gradlew clean build

# أو حذف مجلد .gradle
rm -rf .gradle
./gradlew build
```

### المشكلة: خطأ في التوقيع

**الحل:**

```bash
# تحقق من متغيرات البيئة
echo $KEYSTORE_PATH
echo $STORE_PASSWORD
echo $KEY_PASSWORD

# أو استخدم الخيارات المباشرة
./gradlew assembleRelease \
  -Pandroid.injected.signing.store.file=/path/to/keystore.jks \
  -Pandroid.injected.signing.store.password=password \
  -Pandroid.injected.signing.key.alias=alias \
  -Pandroid.injected.signing.key.password=password
```

### المشكلة: خطأ في الذاكرة

**الحل:**

```bash
# زيادة حجم الذاكرة المخصصة
export GRADLE_OPTS="-Xmx2048m"
./gradlew build
```

### المشكلة: فشل الاختبارات

**الحل:**

```bash
# تشغيل الاختبارات مع تفاصيل أكثر
./gradlew test --stacktrace

# تخطي الاختبارات
./gradlew build -x test
```

### المشكلة: فشل التثبيت على الجهاز

**الحل:**

```bash
# تحقق من الأجهزة المتصلة
adb devices

# أعد تشغيل adb
adb kill-server
adb start-server

# حاول التثبيت مرة أخرى
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

## CI/CD والبناء التلقائي

### إعداد يدوي للبناء التلقائي

يمكنك استخدام خدمات مثل:

- **GitHub Actions** (مجاني)
- **GitLab CI/CD**
- **Jenkins**
- **CircleCI**

### مثال: GitHub Actions

```yaml
name: Build APK

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main, develop ]

jobs:
  build:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v4
    
    - name: Set up JDK 17
      uses: actions/setup-java@v4
      with:
        java-version: '17'
        distribution: 'temurin'
    
    - name: Build APK
      run: ./gradlew assembleDebug
    
    - name: Upload APK
      uses: actions/upload-artifact@v4
      with:
        name: app-debug
        path: app/build/outputs/apk/debug/app-debug.apk
```

### تشغيل البناء محلياً (محاكاة CI/CD)

```bash
# تنظيف كامل
./gradlew clean

# بناء كامل مع الاختبارات
./gradlew build

# بناء APK فقط
./gradlew assembleDebug assembleRelease

# بناء مع التحليل
./gradlew build --profile
```

## الأوامر المفيدة

### معلومات البناء

```bash
# عرض إصدار Gradle
./gradlew --version

# عرض معلومات المشروع
./gradlew projects

# عرض المهام المتاحة
./gradlew tasks

# عرض التبعيات
./gradlew dependencies
```

### الاختبارات

```bash
# تشغيل جميع الاختبارات
./gradlew test

# تشغيل اختبارات محددة
./gradlew test --tests com.example.MyTest

# تشغيل اختبارات الجهاز
./gradlew connectedAndroidTest
```

### التحليل والجودة

```bash
# تحليل الكود
./gradlew lint

# تقرير التغطية
./gradlew jacocoTestReport

# التحقق من الأداء
./gradlew build --profile
```

## نصائح مهمة

1. **استخدم Gradle Daemon**: يسرع البناء
   ```bash
   org.gradle.daemon=true
   ```

2. **استخدم Parallel Builds**: بناء متوازي
   ```bash
   org.gradle.parallel=true
   ```

3. **استخدم Build Cache**: تخزين مؤقت للبناء
   ```bash
   org.gradle.caching=true
   ```

4. **حافظ على الـ Keystore آمناً**: لا تضعه في Git
   ```bash
   echo "*.jks" >> .gitignore
   ```

5. **استخدم متغيرات البيئة**: للمعلومات الحساسة
   ```bash
   export KEYSTORE_PATH=/path/to/keystore.jks
   ```

## الدعم والمساعدة

إذا واجهت مشاكل:

1. تحقق من [دليل استكشاف الأخطاء](#استكشاف-الأخطاء)
2. ابحث في [المشاكل المعروفة](https://github.com/Chaouni-X0/Omni-mind-1x-/issues)
3. افتح issue جديد مع التفاصيل الكاملة

---

**آخر تحديث**: يوليو 2026
