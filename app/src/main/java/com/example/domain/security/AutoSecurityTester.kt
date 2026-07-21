package com.example.omnimind.domain.security

class AutoSecurityTester {
    fun testInputValidation(input: String): Boolean {
        return input.isNotBlank() && input.length < 1000
    }
    
    fun testSqlInjection(input: String): Boolean {
        val sqlKeywords = listOf("SELECT", "INSERT", "UPDATE", "DELETE", "DROP", "--", ";")
        return sqlKeywords.none { input.contains(it, ignoreCase = true) }
    }
    
    fun testXss(input: String): Boolean {
        val xssPatterns = listOf("<script>", "</script>", "javascript:", "onerror=", "onclick=")
        return xssPatterns.none { input.contains(it, ignoreCase = true) }
    }
    
    fun runAllTests(input: String): Map<String, Boolean> {
        return mapOf(
            "input_validation" to testInputValidation(input),
            "sql_injection" to testSqlInjection(input),
            "xss" to testXss(input)
        )
    }
}