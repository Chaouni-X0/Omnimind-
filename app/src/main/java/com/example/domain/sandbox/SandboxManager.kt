package com.example.omnimind.domain.sandbox

class SandboxManager {
    fun createSandbox(name: String): String {
        return "Sandbox $name created"
    }
    
    fun destroySandbox(name: String): Boolean {
        return true
    }
    
    fun listSandboxes(): List<String> {
        return listOf("sandbox1", "sandbox2")
    }
    
    fun executeInSandbox(sandboxName: String, command: String): String {
        return "Command $command executed in $sandboxName"
    }
}