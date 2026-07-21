# OmniMind 2.0 - النموذج الهجين

## ⚠️ حالة المشروع

**هذا النموذج الأولي غير قابل للبناء حالياً.** الملف مخزن كسطر واحد بمحارف `\n` حرفية مما يمنع عرض Markdown بشكل صحيح. ينصح بدمج هذا المحتوى مع `README_HYBRID.md`.

## مشكلات معروفة

- ملفات TypeScript/TSX تحتوي `\n` حرفية
- أصول مفقودة
- إضافات Expo غير معلنة
- مكانس EAS مفقود
- لا توجد اختبارات أو lockfile
- لا يوجد OAuth (يستخدم PAT)
- بيانات الدردشة في الذاكرة (لا persistence)

## البنية

```text
frontend/          # Expo/React Native prototype
  app/             # (tabs), terminal, editor, chat, github
  components/      # Button, Input, Card, Badge, ChatMessage
  hooks/           # useTheme
  lib/             # api.ts, utils.ts
  constants/       # theme.ts
backend/           # Express/Socket.IO prototype
  src/
    index.ts
    routes/        # terminal, editor, github, chat
    services/      # terminal, editor, github, chat
```

## الميزات التصميمية

| الشاشة | الوصف |
|--------|-------|
| Dashboard | عرض المشاريع |
| Terminal | تنفيذ الأوامر |
| Editor | تحرير الملفات |
| Chat | دردشة فورية (في الذاكرة) |
| GitHub | إدارة المستودعات (PAT) |

## للتشغيل (بعد إصلاح الملفات)

```bash
npm install
npm run dev  # backend + frontend معاً
```

---

**حالة المشروع**: نموذج أولي غير قابل للبناء. يحتاج إصلاح الملفات التالفة.
