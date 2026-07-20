package com.example.omnimind.domain.terminal

import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit

/**
 * ترمنال حقيقي: ينفّذ أوامر النظام الفعلية عبر ProcessBuilder داخل
 * مجلد المشروع الخاص بالتطبيق، بالإضافة إلى أوامر مدمجة سريعة
 * (ls, cd, pwd, cat...) تُنفَّذ محليًا بأمان داخل الـ Sandbox.
 *
 * الأوامر الحقيقية (git, echo, ...الخ) تُمرَّر إلى shell النظام
 * عندما يكون متاحًا على الجهاز.
 */
class TerminalService(private val rootDir: File) {

    private var currentDir: File = rootDir.apply { if (!exists()) mkdirs() }
    private val commandHistory = mutableListOf<String>()

    /** الأوامر المدمجة التي ننفّذها محليًا دون استدعاء النظام. */
    private val builtins = setOf(
        "help", "pwd", "cd", "ls", "cat", "echo", "mkdir",
        "touch", "rm", "mv", "cp", "clear", "history", "find", "grep", "write", "head", "tail"
    )

    fun executeCommand(command: String): String {
        val trimmed = command.trim()
        if (trimmed.isEmpty()) return ""
        commandHistory.add(trimmed)

        val parts = tokenize(trimmed)
        val cmd = parts.first()
        val args = parts.drop(1)

        return try {
            if (cmd in builtins) {
                runBuiltin(cmd, args, trimmed)
            } else {
                // محاولة تنفيذ الأمر عبر النظام الحقيقي
                runSystemCommand(trimmed)
            }
        } catch (e: Exception) {
            "خطأ: ${e.message}"
        }
    }

    private fun runBuiltin(cmd: String, args: List<String>, raw: String): String {
        return when (cmd) {
            "help" -> """
                الأوامر المدمجة:
                  ls [dir]        عرض الملفات
                  pwd             المسار الحالي
                  cd <dir>        تغيير المجلد
                  cat <file>      عرض محتوى ملف
                  head <file>     أول 10 أسطر
                  tail <file>     آخر 10 أسطر
                  echo <text>     طباعة نص
                  write <f> <txt> كتابة نص في ملف
                  mkdir <dir>     إنشاء مجلد
                  touch <file>    إنشاء ملف
                  rm <path>       حذف
                  mv <a> <b>      نقل/إعادة تسمية
                  cp <a> <b>      نسخ
                  find <pattern>  بحث عن ملفات
                  grep <pat> <f>  بحث داخل ملف
                  history         سجل الأوامر
                  clear           مسح الشاشة
                أي أمر آخر (مثل git) سيُنفَّذ عبر نظام الجهاز إن كان متاحًا.
            """.trimIndent()

            "pwd" -> relativePath(currentDir)

            "ls" -> {
                val target = if (args.isEmpty()) currentDir else resolve(args[0])
                if (!target.exists()) "ls: غير موجود"
                else target.listFiles()
                    ?.sortedWith(compareByDescending<File> { it.isDirectory }.thenBy { it.name })
                    ?.joinToString("\n") { if (it.isDirectory) "${it.name}/" else it.name }
                    ?: "لا يمكن قراءة المجلد"
            }

            "cd" -> {
                val target = if (args.isEmpty()) rootDir else resolve(args[0])
                if (target.exists() && target.isDirectory && isInsideRoot(target)) {
                    currentDir = target
                    relativePath(currentDir)
                } else "cd: لا يوجد مجلد بهذا الاسم"
            }

            "cat" -> readFileSafe(args, whole = true)
            "head" -> readFileSafe(args, whole = false, take = 10, fromEnd = false)
            "tail" -> readFileSafe(args, whole = false, take = 10, fromEnd = true)

            "echo" -> args.joinToString(" ")

            "write" -> {
                if (args.size < 2) "write: الاستخدام write <ملف> <نص>"
                else {
                    val f = resolve(args[0])
                    f.parentFile?.mkdirs()
                    f.writeText(args.drop(1).joinToString(" "))
                    "تمت الكتابة في ${args[0]}"
                }
            }

            "mkdir" -> {
                if (args.isEmpty()) "mkdir: حدد اسم المجلد"
                else if (resolve(args[0]).mkdirs()) "تم إنشاء المجلد" else "mkdir: فشل الإنشاء"
            }

            "touch" -> {
                if (args.isEmpty()) "touch: حدد اسم الملف"
                else {
                    val f = resolve(args[0])
                    f.parentFile?.mkdirs()
                    if (f.exists() || f.createNewFile()) "تم إنشاء الملف" else "touch: فشل الإنشاء"
                }
            }

            "rm" -> {
                if (args.isEmpty()) "rm: حدد المسار"
                else {
                    val target = resolve(args[0])
                    if (target.exists() && isInsideRoot(target)) {
                        if (target.deleteRecursively()) "تم الحذف" else "rm: فشل الحذف"
                    } else "rm: غير موجود"
                }
            }

            "mv" -> {
                if (args.size < 2) "mv: الاستخدام mv <مصدر> <وجهة>"
                else {
                    val src = resolve(args[0]); val dst = resolve(args[1])
                    if (src.exists() && isInsideRoot(src) && isInsideRoot(dst)) {
                        dst.parentFile?.mkdirs()
                        if (src.renameTo(dst)) "تم النقل" else "mv: فشل النقل"
                    } else "mv: مصدر غير موجود"
                }
            }

            "cp" -> {
                if (args.size < 2) "cp: الاستخدام cp <مصدر> <وجهة>"
                else {
                    val src = resolve(args[0]); val dst = resolve(args[1])
                    if (src.exists() && isInsideRoot(src) && isInsideRoot(dst)) {
                        src.copyRecursively(dst, overwrite = true)
                        "تم النسخ"
                    } else "cp: مصدر غير موجود"
                }
            }

            "find" -> {
                if (args.isEmpty()) "find: حدد نمط البحث"
                else {
                    val pattern = args[0]
                    val matches = currentDir.walkTopDown()
                        .filter { it.name.contains(pattern, ignoreCase = true) }
                        .map { relativePath(it) }
                        .toList()
                    if (matches.isEmpty()) "لا توجد نتائج" else matches.joinToString("\n")
                }
            }

            "grep" -> {
                if (args.size < 2) "grep: الاستخدام grep <نمط> <ملف>"
                else {
                    val pattern = args[0]
                    val file = resolve(args[1])
                    if (file.exists() && file.isFile) {
                        val lines = file.readLines()
                            .mapIndexed { i, l -> i + 1 to l }
                            .filter { it.second.contains(pattern, ignoreCase = true) }
                        if (lines.isEmpty()) "لا توجد مطابقة"
                        else lines.joinToString("\n") { "${it.first}: ${it.second}" }
                    } else "grep: الملف غير موجود"
                }
            }

            "history" -> commandHistory.mapIndexed { i, c -> "${i + 1}  $c" }.joinToString("\n")

            "clear" -> "" // handled by UI

            else -> "$cmd: أمر غير معروف"
        }
    }

    /** تنفيذ أمر حقيقي عبر shell النظام إن كان متاحًا. */
    private fun runSystemCommand(command: String): String {
        return try {
            val process = ProcessBuilder("/system/bin/sh", "-c", command)
                .directory(currentDir)
                .redirectErrorStream(true)
                .start()

            val output = StringBuilder()
            BufferedReader(InputStreamReader(process.inputStream)).use { reader ->
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    output.append(line).append("\n")
                }
            }
            val finished = process.waitFor(30, TimeUnit.SECONDS)
            if (!finished) {
                process.destroy()
                return "انتهت مهلة التنفيذ (30 ثانية)"
            }
            val result = output.toString().trimEnd()
            if (result.isEmpty() && process.exitValue() == 0) "تم التنفيذ (بدون خرج)"
            else if (result.isEmpty()) "${command.split(" ").first()}: أمر غير متاح على هذا الجهاز"
            else result
        } catch (e: Exception) {
            "${command.split(" ").first()}: تعذّر التنفيذ (${e.message})"
        }
    }

    fun currentPath(): String = relativePath(currentDir)

    fun history(): List<String> = commandHistory.toList()

    private fun readFileSafe(
        args: List<String>,
        whole: Boolean,
        take: Int = 10,
        fromEnd: Boolean = false
    ): String {
        if (args.isEmpty()) return "حدد اسم الملف"
        val target = resolve(args[0])
        if (!target.exists() || !target.isFile) return "الملف غير موجود"
        val lines = target.readLines()
        return when {
            whole -> lines.joinToString("\n")
            fromEnd -> lines.takeLast(take).joinToString("\n")
            else -> lines.take(take).joinToString("\n")
        }
    }

    /** تقسيم يحترم علامات التنصيص لتمرير نصوص فيها مسافات. */
    private fun tokenize(input: String): List<String> {
        val tokens = mutableListOf<String>()
        val sb = StringBuilder()
        var inQuotes = false
        var quoteChar = ' '
        for (c in input) {
            when {
                inQuotes -> if (c == quoteChar) inQuotes = false else sb.append(c)
                c == '"' || c == '\'' -> { inQuotes = true; quoteChar = c }
                c.isWhitespace() -> if (sb.isNotEmpty()) { tokens.add(sb.toString()); sb.clear() }
                else -> sb.append(c)
            }
        }
        if (sb.isNotEmpty()) tokens.add(sb.toString())
        return tokens
    }

    private fun resolve(path: String): File {
        val f = if (path.startsWith("/")) File(rootDir, path.removePrefix("/")) else File(currentDir, path)
        return f.canonicalFile
    }

    private fun isInsideRoot(file: File): Boolean =
        file.canonicalPath.startsWith(rootDir.canonicalPath)

    private fun relativePath(dir: File): String {
        val rel = dir.canonicalPath.removePrefix(rootDir.canonicalPath)
        return if (rel.isEmpty()) "/" else rel
    }
}
