package com.example.omnimind.domain.models

data class SwarmModel(
    val id: String,
    val name: String,
    val description: String,
    val version: String = "1.0",
    val isActive: Boolean = true
) {
    // Additional properties and methods can be added here
    fun getFullDescription(): String {
        return "$name: $description (v$version)"
    }
}