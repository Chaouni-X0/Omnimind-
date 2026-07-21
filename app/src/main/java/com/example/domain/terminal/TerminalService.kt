package com.example.omnimind.domain.terminal

import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Terminal operations stay inside the project sandbox. Built-ins perform common
 * file operations, while other commands execute directly without a shell.
 */
class TerminalService(private val rootDir: File) {

    private val canonicalRoot = rootDir.apply { if (!exists()) mkdirs() }.canonicalFile
    private var currentDir: File = canonicalRoot
    private val commandHistory = mutableListOf<String>()

    suspend fun executeCommand(command: String): String = withContext(Dispatchers.IO) {
        val trimmed = command.trim()
        if (trimmed.isEmpty()) return@withContext ""
        commandHistory += trimmed
        val parts = tokenize(trimmed)
        val cmd = parts.first()
        val args = parts.drop(1)

        return@withContext try {
            when (cmd) {
                "help" -> "Available commands: ls, pwd, cd, cat, echo, mkdir, touch, rm, history, clear, git status/log/diff/branch."
                "pwd" -> relativePath(currentDir)
                "ls" -> {
                    val target = if (args.isEmpty()) currentDir else resolve(args[0])
                    target.listFiles()
                        ?.sortedBy { it.name }
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
                "cat" -> {
                    if (args.isEmpty()) "cat: حدد اسم الملف"
                    else {
                        val target = resolve(args[0])
                        if (target.exists() && target.isFile) target.readText()
                        else "cat: الملف غير موجود"
                    }
                }
                "echo" -> args.joinToString(" ")
                "mkdir" -> {
                    if (args.isEmpty()) "mkdir: حدد اسم المجلد"
                    else {
                        val ok = resolve(args[0]).mkdirs()
                        if (ok) "تم إنشاء المجلد" else "mkdir: فشل الإنشاء"
                    }
                }
                "touch" -> {
                    if (args.isEmpty()) "touch: حدد اسم الملف"
                    else {
                        val f = resolve(args[0])
                        f.parentFile?.mkdirs()
                        val ok = f.exists() || f.createNewFile()
                        if (ok) "تم إنشاء الملف" else "touch: فشل الإنشاء"
                    }
                }
                "rm" -> {
                    if (args.isEmpty()) "rm: حدد اسم الملف"
                    else {
                        val target = resolve(args[0])
                        if (target.exists() && isInsideRoot(target)) {
                            val ok = target.deleteRecursively()
                            if (ok) "تم الحذف" else "rm: فشل الحذف"
                        } else "rm: غير موجود"
                    }
                }
                "clear" -> "" // handled by UI
                "history" -> commandHistory.mapIndexed { index, item -> "${index + 1}  $item" }.joinToString("\n")
                else -> runSystemCommand(parts)
            }
        } catch (e: Exception) {
            "خطأ: ${e.message}"
        }
    }

    fun currentPath(): String = relativePath(currentDir)

    fun history(): List<String> = commandHistory.toList()

    private fun resolve(path: String): File {
        val file = if (path.startsWith("/")) File(canonicalRoot, path.removePrefix("/")) else File(currentDir, path)
        return file.canonicalFile.also { require(isInsideRoot(it)) { "المسار خارج مساحة المشروع" } }
    }

    private fun isInsideRoot(file: File): Boolean {
        return file.canonicalPath == canonicalRoot.path ||
            file.canonicalPath.startsWith(canonicalRoot.path + File.separator)
    }

    private fun relativePath(dir: File): String {
        val rel = dir.canonicalPath.removePrefix(canonicalRoot.path)
        return if (rel.isEmpty()) "/" else rel
    }

    private fun tokenize(input: String): List<String> {
        val tokens = mutableListOf<String>()
        val current = StringBuilder()
        var quote: Char? = null
        input.forEach { char ->
            when {
                quote != null && char == quote -> quote = null
                quote != null -> current.append(char)
                char == '\'' || char == '"' -> quote = char
                char.isWhitespace() && current.isNotEmpty() -> {
                    tokens += current.toString()
                    current.clear()
                }
                !char.isWhitespace() -> current.append(char)
            }
        }
        if (current.isNotEmpty()) tokens += current.toString()
        return tokens
    }

    private fun runSystemCommand(parts: List<String>): String {
        val allowedGitCommands = setOf("status", "log", "diff", "branch", "show", "rev-parse")
        if (parts.firstOrNull() != "git" || parts.getOrNull(1) !in allowedGitCommands) {
            return "الأمر غير مسموح. تتوفر أوامر Git للقراءة فقط داخل المشروع"
        }
        if (parts.any { value ->
                value.startsWith("/") || value.replace('\\', '/').split('/').any { it == ".." } ||
                    value.any { it in "|;&`><\n\r" }
            }) return "تم رفض الأمر: المسارات الخارجية وعوامل shell غير مسموحة"
        return try {
            val process = ProcessBuilder(parts).directory(currentDir).redirectErrorStream(true).start()
            val output = StringBuilder()
            val reader = Thread {
                BufferedReader(InputStreamReader(process.inputStream)).useLines { lines ->
                    lines.forEach { output.append(it).append('\n') }
                }
            }.apply { start() }
            if (!process.waitFor(30, TimeUnit.SECONDS)) {
                process.destroy()
                reader.join(1_000)
                "انتهت مهلة التنفيذ (30 ثانية)"
            } else {
                reader.join(1_000)
                output.toString().trimEnd().ifEmpty {
                    if (process.exitValue() == 0) "تم التنفيذ (بدون خرج)" else "${parts.first()}: أمر غير متاح"
                }
            }
        } catch (error: Exception) {
            "${parts.first()}: تعذر التنفيذ (${error.message})"
        }
    }
}
