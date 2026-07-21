package com.example.omnimind.domain.swarm

import android.util.Log
import com.example.omnimind.data.db.OmniMindDao
import com.example.omnimind.data.model.AgentMessage
import com.example.omnimind.data.model.AgentTask
import com.example.omnimind.domain.llm.LlmRouter
import java.util.UUID
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

class SwarmEngine(
    private val dao: OmniMindDao,
    private val llmRouter: LlmRouter,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private data class AgentRole(val id: String, val name: String)

    private val roles = listOf(
        AgentRole("architect", "المهندس المعماري"),
        AgentRole("analyst", "المحلل"),
        AgentRole("coder", "المطور"),
        AgentRole("tester", "المختبر"),
        AgentRole("guardian", "الحارس")
    )

    suspend fun runSwarmTask(
        projectId: String,
        title: String,
        description: String,
        onTaskCreated: (String) -> Unit = {}
    ): Result<AgentTask> = withContext(dispatcher) {
        val task = AgentTask(
            id = UUID.randomUUID().toString(),
            projectId = projectId,
            title = title,
            prompt = description,
            status = "Running",
            progress = 0,
            lastUpdated = System.currentTimeMillis()
        )
        try {
            dao.insertTask(task)
            onTaskCreated(task.id)
            val priorOutputs = mutableListOf<String>()
            val stepProgress = 100 / roles.size
            for ((index, role) in roles.withIndex()) {
                val reply = llmRouter.generate(
                    rolePrompt(role, title, description, priorOutputs),
                    when (role.id) {
                        "coder", "tester" -> 2
                        "guardian" -> 3
                        else -> 1
                    }
                ).getOrElse { error ->
                    val failed = task.copy(
                        status = "Failed",
                        progress = stepProgress * index,
                        lastUpdated = System.currentTimeMillis()
                    )
                    dao.insertMessage(
                        AgentMessage(
                            id = UUID.randomUUID().toString(),
                            taskId = task.id,
                            agentId = role.id,
                            agentName = role.name,
                            messageText = "تعذر تنفيذ دور ${role.name}: ${error.message}",
                            verdictType = "REJECT",
                            verdictReason = "لم ينجح أي نموذج مفعّل"
                        )
                    )
                    dao.updateTask(failed)
                    return@withContext Result.failure(error)
                }
                priorOutputs += "${role.name} (${reply.provider}/${reply.model}):\n${reply.text}"
                dao.insertMessage(
                    AgentMessage(
                        id = UUID.randomUUID().toString(),
                        taskId = task.id,
                        agentId = role.id,
                        agentName = role.name,
                        messageText = reply.text,
                        thinking = "${reply.provider} · ${reply.model}",
                        verdictType = "NONE",
                        verdictReason = if (role.id == "guardian") "مراجعة آلية تحتاج اعتماد المستخدم" else null
                    )
                )
                dao.updateTask(
                    task.copy(
                        status = role.name,
                        progress = stepProgress * (index + 1),
                        lastUpdated = System.currentTimeMillis()
                    )
                )
            }
            val completed = task.copy(status = "Completed", progress = 100, lastUpdated = System.currentTimeMillis())
            dao.updateTask(completed)
            Result.success(completed)
        } catch (error: CancellationException) {
            dao.updateTask(task.copy(status = "Cancelled", lastUpdated = System.currentTimeMillis()))
            throw error
        } catch (error: Exception) {
            Log.e(TAG, "Swarm task failed", error)
            runCatching { dao.updateTask(task.copy(status = "Failed", lastUpdated = System.currentTimeMillis())) }
            Result.failure(error)
        }
    }

    private fun rolePrompt(role: AgentRole, title: String, description: String, priorOutputs: List<String>): String {
        val instruction = when (role.id) {
            "architect" -> "ضع بنية وخطة تنفيذ دقيقة قابلة للتقسيم."
            "analyst" -> "حلل المتطلبات والمخاطر والثغرات في الخطة السابقة."
            "coder" -> "اقترح التنفيذ الفعلي والملفات والتغييرات البرمجية المطلوبة."
            "tester" -> "صمم اختبارات حقيقية وابحث عن أخطاء التنفيذ، ولا تدع تشغيل اختبار لم يشغل."
            else -> "راجع النتائج، اذكر العيوب المتبقية، وقدم قرارا مشروطا بالأدلة."
        }
        val context = priorOutputs.takeLast(4).joinToString("\n\n")
        return """
            أنت ${role.name} ضمن فريق هندسة برمجيات متعدد النماذج.
            $instruction
            عنوان المهمة: $title
            الوصف: $description
            ${if (context.isBlank()) "" else "نتائج الأدوار السابقة:\n$context"}
            أجب بالعربية بإيجاز ووضوح، ولا تدع تنفيذ أدوات أو اختبارات لم تنفذها فعلا.
        """.trimIndent()
    }

    fun getTaskStatus(taskId: String): Flow<AgentTask?> = dao.getTaskById(taskId)
    fun getProjectTasks(projectId: String): Flow<List<AgentTask>> = dao.getTasksByProject(projectId)
    fun getTaskMessages(taskId: String): Flow<List<AgentMessage>> = dao.getMessagesByTask(taskId)

    suspend fun cancelTask(taskId: String) {
        dao.getTaskById(taskId).firstOrNull()?.let {
            dao.updateTask(it.copy(status = "Cancelled", lastUpdated = System.currentTimeMillis()))
        }
    }

    fun cleanup() = Unit

    private companion object {
        const val TAG = "SwarmEngine"
    }
}
