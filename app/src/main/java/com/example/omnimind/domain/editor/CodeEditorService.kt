package com.example.omnimind.domain.editor

import java.io.File

/**
 * محرر أكواد حقيقي يعمل على مجلد المشروع الخاص بالتطبيق (نفس مجلد الترمنال).
 */
class CodeEditorService(private val rootDir: File) {

    init {
        if (!rootDir.exists()) rootDir.mkdirs()
    }

    data class FileEntry(val name: String, val relativePath: String, val isDirectory: Boolean)

    fun listFiles(relativePath: String = ""): List<FileEntry> {
        val dir = if (relativePath.isBlank()) rootDir else File(rootDir, relativePath)
        if (!dir.exists() || !dir.isDirectory) return emptyList()
        return dir.listFiles()
            ?.sortedWith(compareByDescending<File> { it.isDirectory }.thenBy { it.name })
            ?.map {
                FileEntry(
                    name = it.name,
                    relativePath = it.relativeTo(rootDir).path,
                    isDirectory = it.isDirectory
                )
            } ?: emptyList()
    }

    fun readFile(relativePath: String): String {
        val file = File(rootDir, relativePath)
        return if (file.exists() && file.isFile) file.readText() else ""
    }

    fun writeFile(relativePath: String, content: String): Boolean {
        return try {
            val file = File(rootDir, relativePath)
            file.parentFile?.mkdirs()
            file.writeText(content)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun createFile(relativePath: String): Boolean {
        return try {
            val file = File(rootDir, relativePath)
            file.parentFile?.mkdirs()
            file.exists() || file.createNewFile()
        } catch (e: Exception) {
            false
        }
    }

    fun deleteFile(relativePath: String): Boolean {
        val file = File(rootDir, relativePath)
        return file.exists() && file.deleteRecursively()
    }

    /** نقل/إعادة تسمية ملف داخل مجلد المشروع (يُستخدم للسحب والإفلات). */
    fun moveFile(fromRelative: String, toRelative: String): Boolean {
        return try {
            val src = File(rootDir, fromRelative)
            val dst = File(rootDir, toRelative)
            if (!src.exists()) return false
            if (src.canonicalPath == dst.canonicalPath) return true
            dst.parentFile?.mkdirs()
            if (src.renameTo(dst)) return true
            // بديل في حال فشل renameTo عبر أنظمة الملفات
            src.copyRecursively(dst, overwrite = true)
            src.deleteRecursively()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getSupportedLanguages(): List<String> {
        return listOf("kotlin", "java", "python", "javascript", "markdown", "json", "xml", "text")
    }

    fun detectLanguage(fileName: String): String {
        return when (fileName.substringAfterLast('.', "")) {
            "kt" -> "kotlin"
            "java" -> "java"
            "py" -> "python"
            "js", "ts" -> "javascript"
            "md" -> "markdown"
            "json" -> "json"
            "xml" -> "xml"
            else -> "text"
        }
    }
}
