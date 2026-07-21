# دليل النظام - OmniMind 1x

## تنبيه

هذا الدليل يوثق **التصميم المقترح** للنظام المتقدم. بعض الواجهات الموضحة أدناه غير منفذة بعد أو منفذة بشكل جزئي. راجع القسم الأخير "حالة التنفيذ" لمعرفة ما هو فعلي.

---

## 1. نماذج Swarm

### النموذج الفعلي

```kotlin
data class AdvancedSwarmModel(
    val baseModel: String,
    val capabilities: List<String>,
    val priority: Int,
    val maxConcurrentTasks: Int
)
```

### أنواع النماذج المدعومة فعلياً

| النوع | الوصف |
|------|-------|
| **architect** | مهندس معماري |
| **analyst** | محلل |
| **coder** | مبرمج |
| **tester** | مختبر |
| **guardian** | Guardian |

## 2. إدارة API Keys

### الموفرون المدعومون فعلياً

- Google Gemini
- OpenAI
- Anthropic Claude
- OpenRouter
- Groq
- مزود مخصص (متوافق مع OpenAI عبر HTTPS)

### الواجهة الفعلية

```kotlin
// إضافة مفتاح (عبر ApiPoolManager / واجهة API Center)
// يدعم التشفير، الفشل التلقائي، ترتيب حسب tier
```

## 3. قاعدة البيانات

### الفعلية

- **Room** (SQLite محلي)
- الجداول: projects, agent_tasks, agent_messages, api_keys, sandbox_runs
- لا يدعم MySQL/PostgreSQL/MongoDB/Firestore

## 4. نقاش بين النماذج

### الحالة الفعلية

`ModelDiscussionEngine` يعيد نصوصاً ثابتة. لا يوجد نقاش أو تفاوض حقيقي.

## 5. اختبار الأمان

### الحالة الفعلية

`AutoSecurityTester` يفحص 3 أنماط نصية:
1. كلمات المرور في النص
2. مفاتيح API في النص
3. روابط داخلية

لا يوجد اختبار اختراق أو إصلاح تلقائي فعلي.

## 6. التصدير

### الحالة الفعلية

`UniversalExporter` يدعم:
- **نص** (TXT)
- **CSV** (جداول)
- **JSON** (عبر `toString()` - ليس serialization حقيقياً)

لا يدعم PDF/XML/ZIP.

---

## حالة التنفيذ

| الواجهة | الحالة |
|---------|--------|
| AdvancedSwarmModel (بسيط) | ✅ منفذ |
| SwarmEngine (متسلسل) | ✅ منفذ |
| ApiPoolManager + تشفير | ✅ منفذ |
| CodeEditorService | ✅ منفذ |
| TerminalService (مقيد) | ✅ منفذ |
| GitHubService (قراءة فقط) | ✅ منفذ |
| ModelDiscussion (نقاش) | ⚠️ جزئي (نصوص ثابتة) |
| AutoSecurityTester | ⚠️ جزئي (3 أنماط) |
| UniversalExporter | ⚠️ جزئي (نص + CSV) |
| قواعد بيانات متعددة | ❌ غير منفذ |
| OAuth | ❌ غير منفذ |
| تعاون فوري | ❌ غير منفذ |
| نسخ احتياطية تلقائية | ❌ غير منفذ |
| نماذج تتعلم | ❌ غير منفذ |

---

**آخر تحديث**: يوليو 2026
**ملاحظة**: هذا الدليل يصف التصميم المقترح، وليس كل ما هو موثق هنا منفذ في الكود الحالي.
