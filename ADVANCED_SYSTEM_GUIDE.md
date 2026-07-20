# دليل النظام المتقدم - OmniMind 1x

## نظرة عامة على النظام المتطور

OmniMind 1x الآن يحتوي على نظام متقدم جداً يتضمن:

1. **نماذج ذكية متطورة** - تتعلم وتتحسن مع كل عملية
2. **نقاش وتفاوض بين النماذج** - تتخذ قرارات جماعية
3. **إضافة API سهلة جداً** - واجهة بسيطة لإضافة مفاتيح API
4. **قاعدة بيانات موحدة** - جميع النماذج تستخدم نفس قاعدة البيانات
5. **اختبار أمان تلقائي** - اختبارات واختراق واصلاح
6. **تصدير شامل** - دعم جميع أنواع الملفات

---

## 1. النماذج الذكية المتطورة

### ميزات النموذج

```kotlin
data class AdvancedSwarmModel(
    val name: String,              // اسم النموذج
    val type: String,              // النوع: architect, coder, tester, security, optimizer
    val apiProvider: String,       // مزود API: gemini, openai, anthropic, custom, open_source
    val learningRate: Float,       // معدل التعلم
    val experiencePoints: Long,    // نقاط الخبرة
    val successRate: Float,        // معدل النجاح
    val adaptabilityScore: Float,  // درجة التكيف
    val parallelCapacity: Int,     // عدد المهام المتوازية
    val knowledgeBase: String,     // قاعدة المعرفة (JSON)
    val trainingData: String,      // بيانات التدريب
    val performanceMetrics: String // مقاييس الأداء
)
```

### أنواع النماذج

| النوع | الوصف | الدور |
|------|-------|-------|
| **architect** | مهندس معماري | تصميم البنية والهيكل |
| **coder** | مبرمج | كتابة الكود |
| **tester** | مختبر | اختبار الكود |
| **security** | خبير أمان | اختبار الأمان |
| **optimizer** | محسّن أداء | تحسين الأداء |
| **custom** | مخصص | أدوار أخرى |

### إضافة نموذج جديد

```kotlin
// إضافة نموذج من OpenAI
val result = easyApiManager.addModelEasy(
    name = "GPT-4 Architect",
    type = "architect",
    provider = "openai",
    apiKeyId = 1,
    description = "نموذج GPT-4 للتصميم المعماري",
    systemPrompt = "أنت مهندس معماري متخصص..."
)

// إضافة نموذج مفتوح المصدر
val result = easyApiManager.addOpenSourceModel(
    name = "Llama 2",
    modelPath = "/models/llama2",
    description = "نموذج Llama 2 محلي"
)
```

---

## 2. إضافة API بسهولة جداً

### الموفرون المدعومون

```kotlin
// الحصول على قائمة الموفرين
val providers = easyApiManager.getSupportedProviders()

// النتيجة:
// - Google Gemini
// - OpenAI
// - Anthropic Claude
// - نماذج مخصصة
// - نماذج مفتوحة المصدر
```

### إضافة مفتاح API

```kotlin
// إضافة مفتاح Google Gemini
val result = easyApiManager.addApiKeyEasy(
    name = "Gemini API Key",
    provider = "gemini",
    apiKey = "your_api_key_here",
    baseUrl = "https://generativelanguage.googleapis.com"
)

// إضافة مفتاح OpenAI
val result = easyApiManager.addApiKeyEasy(
    name = "OpenAI API Key",
    provider = "openai",
    apiKey = "sk-...",
    baseUrl = "https://api.openai.com/v1"
)
```

### إدارة API Keys

```kotlin
data class AdvancedApiKey(
    val name: String,
    val provider: String,
    val apiKey: String,           // مشفر
    val isActive: Boolean,
    val priority: Int,            // الأولوية في التوازن
    val loadBalancingWeight: Float,
    val monthlyBudgetCents: Long, // الميزانية الشهرية
    val monthlyUsageCents: Long,  // الاستخدام الحالي
    val successRate: Float,       // معدل النجاح
    val rateLimit: Int,           // حد المعدل
    val maxConcurrentRequests: Int
)
```

---

## 3. قاعدة البيانات الموحدة والمشتركة

### إنشاء قاعدة بيانات موحدة

```kotlin
val unifiedDb = UnifiedDatabase(
    name = "OmniMind Database",
    type = "sqlite",  // أو mysql, postgresql, mongodb
    connectionString = "jdbc:sqlite:omnimind.db",
    isShared = true,  // جميع النماذج تستخدمها
    readOnly = false,
    connectionPoolSize = 10,
    queryTimeoutSeconds = 30,
    encryptionEnabled = true,
    backupEnabled = true,
    retentionDays = 30
)
```

### أنواع قواعد البيانات المدعومة

| النوع | الوصف |
|------|-------|
| **sqlite** | قاعدة بيانات محلية خفيفة |
| **mysql** | MySQL Server |
| **postgresql** | PostgreSQL |
| **mongodb** | MongoDB NoSQL |
| **firestore** | Google Firestore |
| **custom** | قاعدة بيانات مخصصة |

---

## 4. نقاش وتفاوض بين النماذج

### بدء نقاش

```kotlin
// بدء نقاش بين عدة نماذج
val discussion = modelDiscussionEngine.startDiscussion(
    taskId = 1,
    topic = "تصميم نظام معالجة الدفع",
    modelIds = listOf(1, 2, 3, 4),  // architect, coder, tester, security
    userContext = "نريد نظام دفع آمن وسريع"
)
```

### مراحل النقاش

1. **المرحلة الأولى**: كل نموذج يعطي رأيه
2. **المرحلة الثانية**: النماذج تناقش الآراء
3. **المرحلة الثالثة**: التوصل إلى توافق
4. **الموافقة**: المستخدم يوافق أو يرفض

### نموذج النقاش

```kotlin
data class ModelDiscussion(
    val taskId: Long,
    val round: Int,
    val participantModelIds: String,  // JSON array
    val discussionTopic: String,
    val messages: String,             // JSON array
    val consensus: String,            // النتيجة المتفق عليها
    val disagreements: String,        // الاختلافات
    val decision: String,
    val votingResults: String         // نتائج التصويت
)
```

### الموافقة على النتيجة

```kotlin
// المستخدم يوافق على نتيجة النقاش
modelDiscussionEngine.approveDiscussionResult(
    taskId = 1,
    approved = true
)

// أو يرفضها
modelDiscussionEngine.approveDiscussionResult(
    taskId = 1,
    approved = false
)
```

---

## 5. اختبار الأمان والاختراق التلقائي

### بدء اختبار أمان شامل

```kotlin
// بدء اختبار أمان شامل
val tests = autoSecurityTester.startSecurityAudit(taskId = 1)
```

### أنواع الاختبارات

| النوع | الوصف | الخطورة |
|------|-------|--------|
| **SQL Injection** | اختبار حقن SQL | Critical |
| **XSS** | اختبار Cross-Site Scripting | High |
| **CSRF** | اختبار Cross-Site Request Forgery | High |
| **Authentication** | اختبار المصادقة | Critical |
| **Encryption** | اختبار التشفير | High |
| **Permissions** | اختبار الأذونات | Medium |

### نموذج الاختبار

```kotlin
data class SecurityTest(
    val taskId: Long,
    val testType: String,
    val severity: String,           // low, medium, high, critical
    val testDescription: String,
    val testPayload: String,
    val testResult: String,         // passed, failed, vulnerable
    val vulnerabilityFound: Boolean,
    val vulnerabilityDescription: String,
    val suggestedFix: String
)
```

### الحصول على اقتراحات الإصلاح

```kotlin
// الحصول على اقتراحات الإصلاح
val fixes = autoSecurityTester.getFixSuggestions(taskId = 1)

// تطبيق الإصلاحات تلقائياً
autoSecurityTester.applyAutoFixes(taskId = 1)
```

---

## 6. التصدير الشامل لجميع الملفات

### الصيغ المدعومة

| الصيغة | الوصف | الاستخدام |
|------|-------|----------|
| **PDF** | مستند PDF | التقارير |
| **JSON** | ملف JSON | البيانات المنظمة |
| **CSV** | جدول CSV | البيانات الجدولية |
| **XML** | ملف XML | البيانات المنظمة |
| **TXT** | ملف نصي | النصوص |
| **ZIP** | ملف مضغوط | ملفات متعددة |

### التصدير إلى PDF

```kotlin
val pdf = universalExporter.exportToPdf(
    taskId = 1,
    title = "تقرير المشروع",
    content = "محتوى التقرير..."
)
```

### التصدير إلى JSON

```kotlin
val json = universalExporter.exportToJson(
    taskId = 1,
    title = "بيانات المشروع",
    data = mapOf(
        "name" to "OmniMind",
        "version" to "2.0.0",
        "status" to "active"
    )
)
```

### التصدير إلى CSV

```kotlin
val csv = universalExporter.exportToCsv(
    taskId = 1,
    title = "جدول البيانات",
    headers = listOf("الاسم", "البريد", "الحالة"),
    rows = listOf(
        listOf("أحمد", "ahmed@example.com", "نشط"),
        listOf("فاطمة", "fatima@example.com", "نشط")
    )
)
```

### التصدير المتعدد

```kotlin
// تصدير إلى عدة صيغ في نفس الوقت
val files = universalExporter.exportMultiFormat(
    taskId = 1,
    title = "المشروع",
    content = "محتوى المشروع",
    formats = listOf("pdf", "json", "csv", "txt")
)
```

---

## 7. سير العمل الكامل

### مثال عملي شامل

```kotlin
// 1. إضافة مفتاح API
val apiKey = easyApiManager.addApiKeyEasy(
    name = "Gemini API",
    provider = "gemini",
    apiKey = "your_key"
)

// 2. إضافة نماذج متعددة
val architect = easyApiManager.addModelEasy(
    name = "Architect",
    type = "architect",
    provider = "gemini",
    apiKeyId = apiKey.id
)

val coder = easyApiManager.addModelEasy(
    name = "Coder",
    type = "coder",
    provider = "gemini",
    apiKeyId = apiKey.id
)

val tester = easyApiManager.addModelEasy(
    name = "Tester",
    type = "tester",
    provider = "gemini",
    apiKeyId = apiKey.id
)

// 3. بدء مهمة
val task = AdvancedSwarmTask(
    projectId = 1,
    title = "بناء نظام دفع",
    description = "نريد نظام دفع آمن",
    assignedModelIds = "[${architect.id}, ${coder.id}, ${tester.id}]"
)

// 4. بدء نقاش بين النماذج
val discussion = modelDiscussionEngine.startDiscussion(
    taskId = task.id,
    topic = "تصميم نظام الدفع",
    modelIds = listOf(architect.id, coder.id, tester.id)
)

// 5. المستخدم يوافق على النتيجة
modelDiscussionEngine.approveDiscussionResult(
    taskId = task.id,
    approved = true
)

// 6. اختبار الأمان
val securityTests = autoSecurityTester.startSecurityAudit(task.id)

// 7. تطبيق الإصلاحات
autoSecurityTester.applyAutoFixes(task.id)

// 8. تصدير النتائج
val exports = universalExporter.exportMultiFormat(
    taskId = task.id,
    title = "نتائج المشروع",
    content = "النتائج النهائية",
    formats = listOf("pdf", "json", "csv")
)
```

---

## 8. الإحصائيات والتقارير

### تتبع الأداء

```kotlin
data class AdvancedSwarmModel(
    val totalTasksCompleted: Long,    // عدد المهام المكتملة
    val totalTokensUsed: Long,        // عدد الـ tokens المستخدمة
    val totalCostCents: Long,         // التكلفة الإجمالية
    val averageExecutionTimeMs: Long, // متوسط وقت التنفيذ
    val successRate: Float,           // معدل النجاح
    val adaptabilityScore: Float      // درجة التكيف
)
```

### تقارير الأداء

- معدل النجاح لكل نموذج
- متوسط وقت التنفيذ
- التكلفة الإجمالية
- عدد المهام المكتملة
- درجة التكيف والتعلم

---

## 9. الأمان والخصوصية

### تشفير البيانات

- ✅ تشفير مفاتيح API
- ✅ تشفير بيانات المستخدم
- ✅ تشفير قاعدة البيانات
- ✅ تسجيل الأنشطة (Audit Logging)

### النسخ الاحتياطية

- ✅ نسخ احتياطية تلقائية
- ✅ احتفاظ بـ 30 يوم
- ✅ استعادة سريعة

---

## 10. الخطوات التالية

### قريباً (v2.1.0)
- [ ] دعم قواعد بيانات متعددة
- [ ] نظام الإضافات (Plugins)
- [ ] التعاون في الوقت الفعلي

### المستقبل (v3.0.0)
- [ ] تطبيق ويب
- [ ] تطبيق سطح المكتب
- [ ] API عام

---

## الدعم والمساعدة

- 📖 [دليل التطوير](./README_DEVELOPMENT.md)
- 🚀 [دليل البناء](./BUILD_GUIDE.md)
- 📦 [دليل النشر](./DEPLOYMENT.md)
- ⚡ [البدء السريع](./QUICK_START.md)

---

**آخر تحديث**: يوليو 2026
**الإصدار**: 2.0.0 Advanced
