# دليل النشر - OmniMind 1x

## تنبيه

هذا الدليل يصف عملية النشر المحتملة. التطبيق حالياً في مرحلة التطوير (v1.0.0) ولم يُنشر بعد على أي متجر.

---

## النشر على Google Play Store

### المتطلبات
- حساب Google Play Developer ($25 تسجيل لمرة واحدة)
- AAB موقّع (Android App Bundle)
- Assets: أيقونة 512x512، Feature Graphic 1024x500، صور (4-8)
- سياسة خصوصية

### البناء للنشر
```bash
# 1. تعيين متغيرات التوقيع الصحيحة
export OMNIMIND_KEYSTORE_PATH=/path/to/keystore.jks
export OMNIMIND_KEYSTORE_PASSWORD=...
export OMNIMIND_KEY_ALIAS=...
export OMNIMIND_KEY_PASSWORD=...

# 2. بناء AAB
./gradlew bundleRelease

# النتيجة: app/build/outputs/bundle/release/app-release.aab
```

### رفع إلى Google Play Console
1. افتح [Google Play Console](https://play.google.com/console)
2. أنشئ تطبيقاً جديداً
3. اذهب إلى Production > Create release
4. حمّل AAB
5. أكمل استمارة المتجر (وصف، تصنيف، إجابات Data safety)
6. اختبر داخلياً (Internal testing track) أولاً
7. انشر للإنتاج بعد الموافقة

> ملاحظة: المراجعة قد تستغرق ساعات إلى أيام. اختبر داخلياً أولاً.

## إدارة الإصدارات

### الترقيع
```bash
# تحديث versionCode و versionName في app/build.gradle.kts
# إنشاء tag
git tag -a v1.0.1 -m "Release v1.0.1"
```

### الفروع
- الفرع الرئيسي: `master`
- ينصح بإنشاء `release/v*.*.*` للإصدارات

## متغيرات التوقيع

```
OMNIMIND_KEYSTORE_PATH
OMNIMIND_KEYSTORE_PASSWORD
OMNIMIND_KEY_ALIAS
OMNIMIND_KEY_PASSWORD
```

## متاجر أخرى (مستقبلية)

- Samsung Galaxy Store
- Amazon Appstore
- Huawei AppGallery
- F-Droid (يتطلب ترخيص مفتوح المصدر)

---

**آخر تحديث**: يوليو 2026
