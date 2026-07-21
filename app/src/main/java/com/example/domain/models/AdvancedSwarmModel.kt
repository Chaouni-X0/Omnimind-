package com.example.omnimind.domain.models

import com.example.omnimind.domain.models.SwarmModel

data class AdvancedSwarmModel(
    val baseModel: SwarmModel,
    val capabilities: List<String> = emptyList(),
    val priority: Int = 1,
    val maxConcurrentTasks: Int = 5
) {
    fun canHandleTask(task: String): Boolean {
        return capabilities.contains(task) || baseModel.isActive
    }

    fun getEffectivePriority(): Int {
        return priority * if (baseModel.isActive) 2 else 1
    }
}