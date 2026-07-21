package com.example.omnimind.domain.editor

import java.io.File

/**
 * محرر أكواد حقيقي يعمل على مجلد المشروع الخاص بالتطبيق (نفس مجلد الترمنال).
 */
class CodeEditorService(private val rootDir: File) {

    private val canonicalRoot = rootDir.apply { if (!exists()) mkdirs() }.canonicalFile

    init {
        if (!rootDir.exists()) rootDir.mkdirs()
    }

    data class FileEntry(val name: String, val relativePath: String, val isDirectory: Boolean)

    fun listFiles(relativePath: String = ""): List<FileEntry> {
        val dir = resolveInside(relativePath) ?: return emptyList()
        if (!dir.exists() || !dir.isDirectory) return emptyList()
        return dir.listFiles()
            ?.sortedWith(compareByDescending<File> { it.isDirectory }.thenBy { it.name })
            ?.map {
                FileEntry(
                    name = it.name,
                    relativePath = it.relativeTo(canonicalRoot).path,
                    isDirectory = it.isDirectory
                )
            } ?: emptyList()
    }

    fun readFile(relativePath: String): String {
        val file = resolveInside(relativePath) ?: return ""
        return if (file.exists() && file.isFile) file.readText() else ""
    }

    fun writeFile(relativePath: String, content: String): Boolean {
        return try {
            val file = resolveInside(relativePath) ?: return false
            file.parentFile?.mkdirs()
            file.writeText(content)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun createFile(relativePath: String): Boolean {
        return try {
            val file = resolveInside(relativePath) ?: return false
            file.parentFile?.mkdirs()
            file.exists() || file.createNewFile()
        } catch (e: Exception) {
            false
        }
    }

    fun deleteFile(relativePath: String): Boolean {
        val file = resolveInside(relativePath) ?: return false
        return file.exists() && file.deleteRecursively()
    }

    fun moveFile(fromRelative: String, toRelative: String): Boolean {
        return try {
            val source = resolveInside(fromRelative) ?: return false
            val destination = resolveInside(toRelative) ?: return false
            if (!source.exists()) return false
            destination.parentFile?.mkdirs()
            source.renameTo(destination) || run {
                source.copyRecursively(destination, overwrite = true)
                source.deleteRecursively()
            }
        } catch (_: Exception) {
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

    private fun resolveInside(relativePath: String): File? {
        val candidate = if (relativePath.isBlank()) canonicalRoot else File(canonicalRoot, relativePath).canonicalFile
        return candidate.takeIf {
            it.path == canonicalRoot.path || it.path.startsWith(canonicalRoot.path + File.separator)
        }
    }
}
