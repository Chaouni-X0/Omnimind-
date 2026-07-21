# تعليمات البناء - OmniMind 1x

## الحالة الحالية

المشروع قيد التطوير النشط. الإصدار الحالي: **1.0.0** (Android).

### الميزات المدمجة (Android)
- نظام Swarm متسلسل (5 أدوار: Architect, Analyst, Coder, Tester, Guardian)
- إدارة مفاتيح API مع تشفير عبر Android Keystore
- محرر نصوص (عرض، تعديل، حفظ، إنشاء ملفات)
- Terminal مع أوامر مدمجة وأوامر Git للقراءة
- تكامل GitHub (عرض المستودعات، استعراض الملفات، استيراد)
- ثلاث سمات (Obsidian, Aurora, Ember)
- دعم RTL للعربية

### قيد التطوير/غير مكتمل
- نظام الدردشة (جزئي)
- ModelDiscussionEngine (نصوص ثابتة)
- AutoSecurityTester (فحص 3 أنماط فقط)
- UniversalExporter (نص + CSV فقط)
- واجهة Settings/Profile/AppBuilder (غير موصولة)
- Hybrid frontend/backend (نماذج أولية غير قابلة للبناء)

---

## المتطلبات

```bash
# JDK 17
java -version

# Android SDK 34
echo $ANDROID_HOME

# Gradle wrapper
./gradlew --version
```

## خطوات البناء

### تنظيف
```bash
./gradlew clean
```

### بناء Debug
```bash
./gradlew assembleDebug
```

### بناء Release
```bash
export OMNIMIND_KEYSTORE_PATH=/path/to/keystore.jks
export OMNIMIND_KEYSTORE_PASSWORD=...
export OMNIMIND_KEY_ALIAS=...
export OMNIMIND_KEY_PASSWORD=...
./gradlew assembleRelease
```

### التثبيت
```bash
adb install -r app/build/outputs/apk/debug/*-universal.apk
adb shell am start -n com.example.omnimind/.MainActivity
```

## إنشاء Keystore

```bash
keytool -genkey -v -keystore omnimind-key.jks \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias omnimind-key
```

## متغيرات التوقيع الصحيحة

```
OMNIMIND_KEYSTORE_PATH
OMNIMIND_KEYSTORE_PASSWORD
OMNIMIND_KEY_ALIAS
OMNIMIND_KEY_PASSWORD
```

## معلومات الإصدار

| الخاصية | القيمة |
|---------|--------|
| الإصدار | 1.0.0 |
| الحد الأدنى API | 24 (Android 7.0) |
| compileSdk | 34 |
| targetSdk | 34 |
| applicationId | com.example.omnimind |

---

**آخر تحديث**: يوليو 2026
**الحالة**: قيد التطوير
