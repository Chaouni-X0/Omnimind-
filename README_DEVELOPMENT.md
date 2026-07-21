# OmniMind 1x - دليل التطوير

## نظرة عامة

تطبيق Android متعدد الوكلاء (Swarm) مع واجهة Glassmorphic بلغة Kotlin/Jetpack Compose.

### الميزات الرئيسية
- **نظام Swarm المتسلسل**: 5 أدوار (Architect, Analyst, Coder, Tester, Guardian)
- **إدارة API Keys**: تشفير عبر Android Keystore، دعم 6 مزودين
- **محرر نصوص**: استعراض، تعديل، حفظ، إنشاء، نقل/حذف ملفات
- **Terminal**: أوامر مدمجة (cd, ls, cat, mkdir, etc.) + Git للقراءة
- **تكامل GitHub**: عرض مستودعات، ملفات، استيراد (لا يدعم commit/push)
- **ثلاث سمات**: Obsidian, Aurora, Ember
- **دعم RTL**: عربي، إنجليزي، إسباني، فرنسي

## البنية المعمارية

```
app/src/main/java/com/example/
├── presentation/          # UI (screens, navigation, ViewModel)
│   ├── screens/           # Onboarding, Dashboard, Workspace, Chat, Editor, Terminal, GitHub, ApiCenter
│   ├── workspace/         # مساحة العمل (مكونات تجريبية)
│   ├── components/        # مكونات قابلة لإعادة الاستخدام
│   └── viewmodel/         # OmniMindViewModel
├── domain/                # منطق الأعمال
│   ├── models/            # SwarmModel, AdvancedSwarmModel
│   ├── swarm/             # SwarmEngine (متسلسل), ModelDiscussionEngine (نصوص ثابتة)
│   ├── terminal/          # TerminalService (أوامر مدمجة + Git read-only)
│   ├── editor/            # CodeEditorService
│   ├── github/            # GitHubService (read + import)
│   ├── sandbox/           # SandboxManager (stub)
│   ├── security/          # AutoSecurityTester (فحص 3 أنماط)
│   ├── export/            # UniversalExporter (نص + CSV)
│   └── api/               # EasyApiManager, ApiPoolManager
├── data/                  # طبقة البيانات
│   ├── db/                # Room (OmniMindDatabase, OmniMindDao)
│   ├── network/           # Retrofit (Gemini, OpenAI, Generic, GitHub)
│   ├── repository/        # OmniMindRepository
│   ├── apipool/           # إدارة مفاتيح API
│   └── security/          # SecurityManager (AES-GCM + Keystore)
└── ui/theme/              # Theme, Color, Type, Shape, Motion, Translations
```

## المتطلبات

- Android Studio
- JDK 17
- Android SDK 34
- Gradle wrapper 8.5

## البناء

```bash
./gradlew assembleDebug
./gradlew testDebugUnitTest
./gradlew lintDebug
```

## المزودون المدعومون

- Google Gemini (gemini-2.0-flash)
- OpenAI
- Anthropic
- OpenRouter
- Groq
- Custom (OpenAI-compatible via HTTPS)

## الميزات والحدود

| الميزة | الحالة |
|--------|--------|
| Swarm متسلسل (5 أدوار) | ✅ يعمل |
| تشفير API Keys (Keystore) | ✅ يعمل |
| محرر النصوص (CRUD ملفات) | ✅ يعمل |
| Terminal (أوامر مدمجة) | ✅ يعمل |
| GitHub (قراءة + استيراد) | ✅ يعمل |
| ModelDiscussion | ⚠️ نصوص ثابتة |
| AutoSecurityTester | ⚠️ 3 أنماط فقط |
| UniversalExporter | ⚠️ نص + CSV فقط |
| واجهات Settings/Profile/AppBuilder | ❌ غير موصولة |
| Hybrid frontend/backend | ❌ نماذج أولية معطلة |

## الاختبارات

```bash
./gradlew testDebugUnitTest
./gradlew connectedAndroidTest
```

الاختبارات الحالية: اختبار وحدة واحد (SecurityTest) واختبار جهاز واحد (ExampleInstrumentedTest).

## الترخيص

هذا المشروع خاص وسري.

---

**آخر تحديث**: يوليو 2026
