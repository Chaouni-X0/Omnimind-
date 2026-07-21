# تحميل التطبيق من الهاتف

## الطريقة الموصى بها: GitHub Actions

البناء يتم تلقائياً على GitHub. لا تحتاج إلى كمبيوتر.

## خطوات التحميل

### 1. فتح المستودع
```
https://github.com/Chaouni-X0/Omni-mind-1x-
```

### 2. الذهاب إلى Actions
1. افتح **Actions** (التاب الثالث)
2. اختر أحدث build ناجح (علامة ✅ خضراء)
3. workflow: **Build Android APK**

### 3. تحميل APK
1. انزل إلى **Artifacts**
2. حمّل **OmniMind-debug-apks** (ملف ZIP)
3. فك الضغط، ستجد عدة APKs:
   - `app-arm64-v8a-debug.apk` (للمعالجات الحديثة)
   - `app-armeabi-v7a-debug.apk` (للمعالجات القديمة)
   - `app-universal-debug.apk` (يعمل على جميع الأجهزة)

### 4. تثبيت
1. افتح APK من مدير الملفات
2. انقر Install

---

## الطرق البديلة

### Termux (بناء على الهاتف)

```bash
pkg update && pkg upgrade
pkg install git openjdk-17
# يتطلب أيضاً تثبيت Android SDK و cmdline-tools
git clone <repo-url>
cd Omni-mind-1x-
./gradlew assembleDebug
```

> ⚠️ البناء على Termux معقد ويتطلب إعداد Android SDK. يُفضّل استخدام GitHub Actions.

### Codemagic
خدمة بناء سحابية (يتطلب إعداد مشروع - غير مهيأ حالياً في هذا المستودع).

---

## معلومات البناء التلقائي

### متى يشتغل CI؟
- ✅ عند كل push إلى أي فرع
- ✅ عند كل pull request
- ✅ يدوياً عبر "Run workflow"

### المخرجات
| الملف | الاستخدام |
|------|----------|
| OmniMind-debug-apks (ZIP) | للاختبار |
| Release (مع توقيع) | فقط عند توفر `OMNIMIND_*` env vars |

---

**آخر تحديث**: يوليو 2026
