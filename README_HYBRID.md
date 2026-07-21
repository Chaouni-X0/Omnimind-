# OmniMind Hybrid - نموذج أولي (قيد التطوير)

## حالة المشروع

**⚠️ هذا النموذج الأولي غير قابل للبناء حالياً.**

يوجد مشكلات هيكلية تمنع تشغيله:
- ملفات TypeScript تحتوي `\n` حرفية بدل أسطر فعلية
- أصول (assets) مفقودة في `frontend/assets/`
- إضافات Expo (`expo-audio`, `expo-build-properties`) غير معلنة في package.json
- لا يوجد `eas.json`
- وضع `npm test` يتطلب Jest لكنه غير مثبت

## البنية

```
frontend/          # Expo/React Native (prototype)
  app/             # شاشات: Dashboard, Terminal, Editor, Chat, GitHub
  components/      # مكونات UI
  hooks/           # React hooks
  lib/             # api.ts, utils.ts
  constants/       # theme.ts
  app.config.ts
  package.json
  tsconfig.json

backend/           # Node.js/Express/Socket.IO (prototype)
  src/
    index.ts       # نقطة الدخول (سليم جزئياً)
    routes/        # terminalRoutes, editorRoutes, githubRoutes, chatRoutes
    services/      # terminalService, editorService, githubService, chatService
  package.json
  tsconfig.json
```

## للتشغيل (بعد الإصلاح)

```bash
npm install
npm run dev:backend  # http://localhost:3000
npm run dev:frontend # http://localhost:8081
```

## المساهمة

نرحب بالإصلاحات! الأولوية:
1. إصلاح الملفات التالفة (`\n` → أسطر حقيقية)
2. إضافة الأصول والإضافات المفقودة
3. إعداد EAS للبناء

## الترخيص

خاص وسري.
