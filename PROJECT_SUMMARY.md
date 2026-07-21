# ملخص مشروع OmniMind 1x

## نظرة عامة

تطبيق Android متعدد الوكلاء (Swarm Intelligence) مع واجهة Glassmorphic.

## التقنيات المستخدمة

- **Kotlin** / **Jetpack Compose** / **Material 3**
- **Room Database** / **Retrofit** / **Gson**
- **Coroutines** / **Flow**
- **Android Keystore** (AES-GCM)
- **Gradle** 8.5 / AGP 8.2.2 / KSP

## الإصدار

| الخاصية | القيمة |
|---------|--------|
| الإصدار | 1.0.0 |
| applicationId | com.example.omnimind |
| compileSdk | 34 |
| minSdk | 24 (Android 7.0) |
| targetSdk | 34 |
| Java target | 17 |

## الميزات

### ✅ مكتملة عملياً
- Swarm متسلسل (5 أدوار: Architect, Analyst, Coder, Tester, Guardian)
- إدارة API Keys (تشفير Keystore، 6 مزودين، failover)
- محرر نصوص (CRUD، حماية path traversal)
- Terminal (12 أمراً مدمجاً + Git read-only)
- GitHub (استعراض + استيراد)
- 3 سمات (Obsidian, Aurora, Ember)
- دعم RTL (عربي، إنجليزي، إسباني، فرنسي)

### ⚠️ جزئية / تجريبية
- ModelDiscussionEngine (نصوص ثابتة)
- AutoSecurityTester (3 أنماط)
- UniversalExporter (نص + CSV)
- SandboxManager (stub)

### ❌ غير موصولة / غير مكتملة
- شاشات Settings, Profile, AppBuilder
- مكونات workspace التجريبية
- Hybrid frontend/backend (معطل)

## ملفات البناء

| الملف | الوصف |
|------|-------|
| `app/build.gradle.kts` | إعدادات بناء التطبيق |
| `gradle.properties` | خصائص Gradle |
| `build.sh` | سكريبت بناء (يبني debug + release معاً) |
| `.github/workflows/omnimind-build.yml` | CI (assembleDebug + test + lint) |

## متغيرات توقيع Release

```
OMNIMIND_KEYSTORE_PATH
OMNIMIND_KEYSTORE_PASSWORD
OMNIMIND_KEY_ALIAS
OMNIMIND_KEY_PASSWORD
```

## أمان

- تشفير API Keys عبر Android Keystore (AES-GCM)
- منع cleartext traffic
- workspace داخل private app storage
- منع path traversal في المحرر
- قيود Terminal (مهلة 30ث، منع خروج من workspace)
- backup معطل في Manifest

---

**آخر تحديث**: يوليو 2026
**الإصدار**: 1.0.0 (Android)
