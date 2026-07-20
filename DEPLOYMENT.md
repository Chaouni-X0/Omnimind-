# دليل النشر - OmniMind 1x

## محتويات الدليل

1. [النشر على Google Play Store](#النشر-على-google-play-store)
2. [النشر على متاجر أخرى](#النشر-على-متاجر-أخرى)
3. [التوزيع المباشر](#التوزيع-المباشر)
4. [إدارة الإصدارات](#إدارة-الإصدارات)

## النشر على Google Play Store

### المتطلبات

- حساب Google Play Developer (رسم تسجيل لمرة واحدة: $25)
- APK موقع بشكل صحيح
- صور وفيديوهات للتطبيق
- وصف التطبيق والميزات

### الخطوة 1: إعداد حساب Google Play

1. اذهب إلى [Google Play Console](https://play.google.com/console)
2. انقر على "Create app"
3. أدخل اسم التطبيق والمعلومات الأساسية
4. اقبل شروط الخدمة

### الخطوة 2: إعداد التطبيق

1. انتقل إلى **Setup > App releases**
2. اختر **Production** أو **Testing**
3. انقر على **Create release**

### الخطوة 3: تحميل APK

```bash
# بناء APK موقع للإصدار
./gradlew bundleRelease

# أو استخدم AAB (Android App Bundle)
# الذي يوصى به من Google
```

### الخطوة 4: ملء معلومات التطبيق

- **العنوان**: OmniMind 1x
- **الوصف القصير**: بيئة تطوير متعددة الوكلاء
- **الوصف الكامل**: 
  ```
  OmniMind 1x هو تطبيق أندرويد متقدم يوفر بيئة تطوير متعددة الوكلاء (Swarm).
  
  الميزات:
  - نظام Swarm متقدم للعمل التعاوني
  - إدارة مرنة لمفاتيح API
  - Terminal متقدم
  - محرر نصوص ذكي
  - تكامل GitHub الكامل
  - واجهة Glassmorphic احترافية
  ```

### الخطوة 5: إضافة الصور والفيديوهات

- **Icon**: 512x512 PNG
- **Feature Graphic**: 1024x500 PNG
- **Screenshots**: 4-8 صور (1080x1920 أو 1440x2560)
- **Video**: اختياري (YouTube link)

### الخطوة 6: تصنيف التطبيق

- **Content Rating**: اختر التصنيف المناسب
- **Category**: Productivity أو Development
- **Target Audience**: Teens و Adults

### الخطوة 7: النشر

1. راجع جميع المعلومات
2. انقر على **Review release**
3. انقر على **Publish**

### المراجعة والموافقة

- تستغرق المراجعة عادة 1-3 ساعات
- قد تحتاج إلى تعديلات إضافية
- بعد الموافقة، سيكون التطبيق متاحاً على Google Play

## النشر على متاجر أخرى

### 1. Samsung Galaxy Store

```
https://seller.samsungapps.com/
```

### 2. Amazon Appstore

```
https://developer.amazon.com/
```

### 3. Huawei AppGallery

```
https://appgallery.huawei.com/
```

### 4. F-Droid (مفتوح المصدر)

```
https://f-droid.org/
```

## التوزيع المباشر

### الطريقة 1: GitHub Releases

```bash
# إنشاء release على GitHub
git tag -a v2.0.0 -m "Release version 2.0.0"
git push origin v2.0.0

# تحميل APK يدويًا على GitHub
# ثم إنشاء Release وإضافة APK
```

### الطريقة 2: موقع الويب الخاص

```bash
# تحميل APK على خادم الويب
scp app/build/outputs/apk/release/app-release.apk user@server:/var/www/html/

# إنشاء صفحة تحميل
cat > index.html << 'EOF'
<!DOCTYPE html>
<html>
<head>
    <title>OmniMind 1x - تحميل</title>
</head>
<body>
    <h1>OmniMind 1x</h1>
    <a href="app-release.apk">تحميل APK</a>
</body>
</html>
EOF
```

### الطريقة 3: البريد الإلكتروني أو الرسائل

```bash
# إرسال APK مباشرة للمستخدمين
# لكن تأكد من أن حجم الملف مقبول
```

## إدارة الإصدارات

### تحديث الإصدار

```bash
# تحديث رقم الإصدار في build.gradle
versionCode = 3
versionName = "2.1.0"

# بناء وتحميل الإصدار الجديد
./gradlew assembleRelease
```

### إدارة الفروع

```bash
# إنشاء فرع للإصدار
git checkout -b release/v2.1.0

# إجراء التعديلات النهائية
git commit -am "Prepare release v2.1.0"

# دمج مع main
git checkout main
git merge release/v2.1.0

# إنشاء tag
git tag -a v2.1.0 -m "Release v2.1.0"
git push origin main v2.1.0
```

### ملاحظات الإصدار

```markdown
# OmniMind 1x v2.1.0

## الميزات الجديدة
- إضافة دعم قواعد بيانات متعددة
- تحسين أداء Swarm Engine
- واجهة محسّنة

## الإصلاحات
- إصلاح خطأ في Terminal
- تحسين استقرار التطبيق

## المعروف
- بعض المشاكل مع أجهزة قديمة

## الشكر
شكراً للمساهمين والمستخدمين!
```

## أفضل الممارسات

### 1. الاختبار قبل النشر

```bash
# اختبار شامل
./gradlew test
./gradlew connectedAndroidTest

# اختبار يدوي على أجهزة مختلفة
```

### 2. الأمان

- استخدم Keystore آمن
- لا تضع المفاتيح الحساسة في الكود
- استخدم ProGuard/R8 للتشويش

### 3. الأداء

- قلل حجم APK
- استخدم ProGuard
- اختبر على أجهزة بطيئة

### 4. التوثيق

- اكتب ملاحظات إصدار واضحة
- وثّق الميزات الجديدة
- اذكر الإصلاحات والتحسينات

## مثال: دورة حياة الإصدار الكاملة

```bash
# 1. إنشاء فرع الإصدار
git checkout -b release/v2.1.0

# 2. تحديث الإصدار
sed -i 's/versionCode = 2/versionCode = 3/' app/build.gradle.kts
sed -i 's/versionName = "2.0.0"/versionName = "2.1.0"/' app/build.gradle.kts

# 3. تحديث ملاحظات الإصدار
echo "# v2.1.0 - تحسينات الأداء" >> CHANGELOG.md

# 4. Commit
git add -A
git commit -m "chore: bump version to 2.1.0"

# 5. بناء APK
./gradlew clean assembleRelease

# 6. اختبار
./gradlew test

# 7. دمج مع main
git checkout main
git merge release/v2.1.0

# 8. إنشاء tag
git tag -a v2.1.0 -m "Release v2.1.0"

# 9. دفع إلى GitHub
git push origin main v2.1.0

# 10. نشر على Google Play
# (يدويًا عبر Google Play Console)
```

## الدعم

للمزيد من المعلومات:
- [Google Play Console Help](https://support.google.com/googleplay/android-developer)
- [Android App Publishing](https://developer.android.com/studio/publish)
- [App Signing](https://developer.android.com/studio/publish/app-signing)

---

**آخر تحديث**: يوليو 2026
