package com.example.omnimind.domain.export

import java.io.File

class UniversalExporter {
    
    fun exportToJson(data: Any, outputFile: File): Boolean {
        return try {
            outputFile.writeText(data.toString())
            true
        } catch (e: Exception) {
            false
        }
    }
    
    fun exportToText(content: String, outputFile: File): Boolean {
        return try {
            outputFile.writeText(content)
            true
        } catch (e: Exception) {
            false
        }
    }
    
    fun exportToCsv(data: List<Map<String, String>>, outputFile: File): Boolean {
        return try {
            val csvContent = data.joinToString("\n") { row ->
                row.values.joinToString(",")
            }
            outputFile.writeText(csvContent)
            true
        } catch (e: Exception) {
            false
        }
    }
}
