# دليل المساهمة في OmniMind 1x

شكراً لاهتمامك بالمساهمة!

## قواعد السلوك
- احترام جميع المساهمين
- تقديم ملاحظات بناءة

## الإبلاغ عن الأخطاء
1. تحقق من عدم وجود issue مشابه
2. افتح Issue بعنوان واضح وخطوات إعادة الإنتاج

## تقديم Pull Request

```bash
# Fork → Clone → Branch
git checkout -b feature/your-feature

# تأكد من الاختبارات
./gradlew testDebugUnitTest
./gradlew lintDebug

# Commit و Push
git push origin feature/your-feature
# ثم افتح PR
```

## معايير الكود

### Android (Kotlin)
- camelCase للمتغيرات والدوال
- PascalCase للفئات
- استخدم 4 مسافات للمحاذاة

### Hybrid (TypeScript - إن وجد)
- اتبع معايير TypeScript/React Native
- ESLint و Prettier

## الاختبارات
```bash
./gradlew testDebugUnitTest   # Android unit tests
./gradlew connectedAndroidTest # Android instrumented tests
```

## الإبلاغ عن مشكلة أمان
- لا تفتح Issue عام
- أرسل بريداً إلى mohamedchaouni0098@gmail.com

---

شكراً على مساهمتك!
