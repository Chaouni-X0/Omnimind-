# OmniMind 1x - دليل التطوير الشامل

## نظرة عامة

**OmniMind 1x** هو تطبيق أندرويد متقدم يوفر بيئة تطوير متعددة الوكلاء (Swarm) مع واجهة مستخدم احترافية بأسلوب Manus Glassmorphic.

### الميزات الرئيسية

- **نظام Swarm المتقدم**: تنسيق متعدد الوكلاء للعمل على مهام معقدة
- **إدارة API Keys المرنة**: دعم عدد غير محدود من مفاتيح API من موفرين مختلفين
- **قواعد بيانات مخصصة**: تخصيص كامل لقواعد البيانات
- **Terminal متقدم**: تنفيذ أوامر Shell مباشرة
- **محرر نصوص ذكي**: تحرير الملفات مع دعم لغات برمجة متعددة
- **تكامل GitHub الكامل**: Commit, Push, Pull, Branching
- **واجهة Glassmorphic**: ثلاث ألوان (Obsidian, Aurora, Ember)
- **دعم RTL كامل**: دعم اللغة العربية

## البنية المعمارية

```
app/
├── src/main/java/com/example/
│   ├── presentation/          # طبقة العرض (UI)
│   │   ├── screens/           # الشاشات الرئيسية
│   │   ├── workspace/         # مساحة العمل
│   │   ├── components/        # مكونات قابلة لإعادة الاستخدام
│   │   └── viewmodel/         # إدارة الحالة
│   ├── domain/                # طبقة المنطق (Business Logic)
│   │   ├── models/            # نماذج البيانات
│   │   ├── swarm/             # محرك Swarm
│   │   ├── terminal/          # خدمة Terminal
│   │   ├── editor/            # خدمة محرر النصوص
│   │   └── github/            # خدمة GitHub
│   ├── data/                  # طبقة البيانات
│   │   ├── db/                # قاعدة البيانات (Room)
│   │   ├── network/           # الاتصالات (API)
│   │   ├── repository/        # مستودع البيانات
│   │   ├── apipool/           # إدارة مفاتيح API
│   │   └── security/          # التشفير والأمان
│   └── ui/theme/              # نظام الألوان والنصوص
└── build.gradle.kts           # إعدادات البناء
```

## المتطلبات

- **Android Studio**: Ladybug أو أحدث
- **Java**: 17 أو أحدث
- **Gradle**: 8.0 أو أحدث
- **Android SDK**: API 26 أو أحدث

## الإعداد والتثبيت

### 1. استنساخ المستودع

```bash
git clone https://github.com/Chaouni-X0/Omni-mind-1x-.git
cd Omni-mind-1x-
```

### 2. فتح المشروع في Android Studio

```bash
# أو يمكنك فتح المشروع مباشرة من Android Studio
```

### 3. مزامنة التبعيات

```bash
./gradlew build
```

### 4. تشغيل التطبيق

```bash
# على محاكي أو جهاز فعلي
./gradlew installDebug
```

## البناء والنشر

### بناء APK للاختبار

```bash
./gradlew assembleDebug
```

### بناء APK للإصدار

```bash
./gradlew assembleRelease
```

### استخدام GitHub Actions للبناء التلقائي

يتم البناء تلقائياً عند كل push إلى `main` أو `develop`:

```yaml
# .github/workflows/build-apk.yml
# يتم تشغيل البناء تلقائياً
```

## المتغيرات البيئية

أنشئ ملف `.env` في جذر المشروع:

```env
# API Keys
GEMINI_API_KEY=your_gemini_key
OPENAI_API_KEY=your_openai_key

# GitHub
GITHUB_TOKEN=your_github_token

# Database
DATABASE_URL=your_database_url
```

## الميزات الرئيسية وكيفية الاستخدام

### 1. نظام Swarm

```kotlin
// تشغيل مهمة Swarm
val result = swarmEngine.runSwarmTask(
    projectId = 1,
    title = "تطوير ميزة جديدة",
    description = "قم بتطوير نظام الدفع",
    modelIds = listOf(1, 2, 3)
)
```

### 2. إدارة API Keys

```kotlin
// إضافة مفتاح API جديد
val apiKey = ApiKey(
    name = "Gemini API",
    provider = "gemini",
    apiKey = "your_api_key",
    monthlyBudget = 10000 // 100 دولار
)
dao.insertApiKey(apiKey)
```

### 3. Terminal

```kotlin
// تنفيذ أمر
val result = terminalService.executeCommand("ls -la")

// تنفيذ سكريبت
val scriptResult = terminalService.executeScript("""
    #!/bin/bash
    echo "Hello World"
    pwd
""")
```

### 4. محرر النصوص

```kotlin
// فتح ملف
codeEditorService.openFile("/path/to/file.kt")

// حفظ الملف
codeEditorService.saveFile()

// البحث والاستبدال
codeEditorService.findAndReplace("old", "new", replaceAll = true)
```

### 5. GitHub Integration

```kotlin
// تهيئة المستودع
githubService.initRepository("/project/path")

// Commit التغييرات
githubService.commit("/project/path", "Initial commit")

// Push
githubService.push("/project/path", "main")

// الحصول على سجل الـ Commits
val commits = githubService.getCommitHistory("/project/path")
```

## الاختبار

### تشغيل الاختبارات

```bash
./gradlew test
```

### الاختبارات على الجهاز

```bash
./gradlew connectedAndroidTest
```

## التوثيق الإضافية

- [نموذج البيانات](./docs/DATA_MODEL.md)
- [API Reference](./docs/API_REFERENCE.md)
- [دليل المساهمة](./CONTRIBUTING.md)

## استكشاف الأخطاء

### المشكلة: فشل البناء

```bash
# تنظيف والبناء من جديد
./gradlew clean build
```

### المشكلة: خطأ في الاتصال بـ API

تحقق من:
1. مفتاح API صحيح في `.env`
2. الاتصال بالإنترنت
3. حدود الـ API لم تتجاوز

### المشكلة: مشاكل في قاعدة البيانات

```bash
# حذف قاعدة البيانات وإعادة إنشاؤها
./gradlew cleanBuildCache
```

## المساهمة

نرحب بالمساهمات! يرجى:

1. Fork المستودع
2. إنشاء فرع للميزة الجديدة (`git checkout -b feature/amazing-feature`)
3. Commit التغييرات (`git commit -m 'Add amazing feature'`)
4. Push إلى الفرع (`git push origin feature/amazing-feature`)
5. فتح Pull Request

## الترخيص

هذا المشروع خاص وسري. تم تطويره بواسطة Mohamed Chaouni.

## الدعم

للمشاكل والاستفسارات، يرجى فتح Issue على GitHub.

## الخارطة الزمنية

### v2.0.0 (الحالي)
- ✅ نظام Swarm متقدم
- ✅ إدارة API Keys المرنة
- ✅ Terminal وmحرر النصوص
- ✅ تكامل GitHub الكامل
- ✅ واجهة Glassmorphic

### v2.1.0 (قادم)
- 🔄 دعم قواعد بيانات متعددة
- 🔄 نظام الإضافات (Plugins)
- 🔄 التعاون في الوقت الفعلي

### v3.0.0 (المستقبل)
- 🔄 دعم الويب
- 🔄 تطبيق سطح المكتب
- 🔄 API عام

---

**آخر تحديث**: يوليو 2026
