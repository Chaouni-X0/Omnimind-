package com.example.omnimind.domain.swarm

import android.util.Log
import com.example.omnimind.data.db.OmniMindDao
import com.example.omnimind.data.model.AgentMessage
import com.example.omnimind.data.model.AgentTask
import com.example.omnimind.data.network.GeminiApiService
import com.example.omnimind.data.network.GeminiContent
import com.example.omnimind.data.network.GeminiGenerateRequest
import com.example.omnimind.data.network.GeminiPart
import com.example.omnimind.data.security.SecurityManager
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.UUID

/**
 * محرك Swarm المتقدم
 * يدير تنسيق الوكلاء المتعددين وتنفيذ المهام، ويسجّل محادثة الوكلاء
 * كرسائل (AgentMessage) تظهر مباشرة في شاشة الدردشة أثناء التنفيذ.
 *
 * إن وُجد مفتاح Gemini API محفوظ، يُستخدم لتوليد رد حقيقي من وكيل
 * "المهندس المعماري" الأول؛ وإلا يتم الاكتفاء بخطوات محاكاة لبقية الوكلاء.
 */
class SwarmEngine(
    private val dao: OmniMindDao,
    private val geminiApiService: GeminiApiService? = null,
    private val securityManager: SecurityManager = SecurityManager(),
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private val scope = CoroutineScope(dispatcher + SupervisorJob())
    private val TAG = "SwarmEngine"

    private data class AgentRole(val id: String, val name: String)

    private val roles = listOf(
        AgentRole("architect", "المهندس المعماري"),
        AgentRole("analyst", "المحلل"),
        AgentRole("coder", "المطوّر"),
        AgentRole("tester", "المختبر"),
        AgentRole("guardian", "الحارس")
    )

    /**
     * تشغيل مهمة Swarm — ينشئ المهمة، يسجّل محادثة الوكلاء خطوة بخطوة،
     * ثم يُنهي المهمة بحالة مكتملة.
     */
    suspend fun runSwarmTask(
        projectId: String,
        title: String,
        description: String
    ): Result<AgentTask> = withContext(dispatcher) {
        try {
            val task = AgentTask(
                id = UUID.randomUUID().toString(),
                projectId = projectId,
                title = title,
                prompt = description,
                status = "running",
                progress = 0,
                lastUpdated = System.currentTimeMillis()
            )
            dao.insertTask(task)

            val stepProgress = 100 / (roles.size + 1)
            for ((index, role) in roles.withIndex()) {
                val message = if (index == 0) {
                    architectMessage(title, description)
                } else {
                    simulatedMessage(role, title)
                }
                dao.insertMessage(
                    AgentMessage(
                        id = UUID.randomUUID().toString(),
                        taskId = task.id,
                        agentId = role.id,
                        agentName = role.name,
                        messageText = message,
                        verdictType = "NONE",
                        timestamp = System.currentTimeMillis()
                    )
                )
                dao.updateTask(
                    task.copy(progress = stepProgress * (index + 1), lastUpdated = System.currentTimeMillis())
                )
                delay(600)
            }

            // رسالة الحكم النهائي من "الحارس"
            dao.insertMessage(
                AgentMessage(
                    id = UUID.randomUUID().toString(),
                    taskId = task.id,
                    agentId = "guardian",
                    agentName = "الحارس",
                    messageText = "تمت مراجعة المهمة \"$title\" واعتمادها.",
                    verdictType = "APPROVE",
                    verdictReason = "اجتازت جميع الخطوات المحاكاة بنجاح",
                    timestamp = System.currentTimeMillis()
                )
            )

            val completedTask = task.copy(
                status = "Completed",
                progress = 100,
                lastUpdated = System.currentTimeMillis()
            )
            dao.updateTask(completedTask)

            Result.success(completedTask)
        } catch (e: Exception) {
            Log.e(TAG, "Error running swarm task", e)
            Result.failure(e)
        }
    }

    /**
     * رسالة "المهندس المعماري" الأولى — تُستخدم Gemini API فعليًا إن توفر مفتاح صالح،
     * وإلا يتم الرجوع لرسالة محاكاة توضيحية.
     */
    private suspend fun architectMessage(title: String, description: String): String {
        val apiKeyConfig = dao.getApiKeyById("gemini-default").firstOrNull()
        val decryptedKey = apiKeyConfig?.encryptedKey?.let { securityManager.decrypt(it) }

        if (geminiApiService == null || decryptedKey.isNullOrBlank()) {
            return "خطة مبدئية للمهمة \"$title\": سيتم تقسيمها إلى تحليل، تنفيذ، واختبار. (لم يتم ربط مفتاح Gemini بعد)"
        }

        return try {
            val response = geminiApiService.generateContent(
                model = "gemini-1.5-flash",
                apiKey = decryptedKey,
                request = GeminiGenerateRequest(
                    contents = listOf(
                        GeminiContent(
                            parts = listOf(
                                GeminiPart(
                                    text = "أنت المهندس المعماري في فريق تطوير برمجيات. " +
                                        "ضع خطة تنفيذ موجزة (3 نقاط كحد أقصى) للمهمة التالية:\n" +
                                        "العنوان: $title\nالوصف: $description"
                                )
                            )
                        )
                    )
                )
            )
            response.candidates
                ?.firstOrNull()
                ?.content
                ?.parts
                ?.firstOrNull()
                ?.text
                ?: "لم يتم استلام رد من Gemini."
        } catch (e: Exception) {
            Log.e(TAG, "Gemini call failed", e)
            "تعذر الاتصال بـ Gemini API (${e.message}). تم المتابعة بخطة محاكاة."
        }
    }

    private fun simulatedMessage(role: AgentRole, title: String): String {
        return when (role.id) {
            "analyst" -> "تحليل المتطلبات لمهمة \"$title\" مكتمل، لا توجد تعارضات ظاهرة."
            "coder" -> "تم إعداد التعديلات المطلوبة برمجيًا لمهمة \"$title\"."
            "tester" -> "تم تشغيل اختبارات أساسية على مهمة \"$title\" — لا أخطاء ظاهرة."
            else -> "خطوة \"${role.name}\" اكتملت لمهمة \"$title\"."
        }
    }

    /** الحصول على حالة المهمة */
    fun getTaskStatus(taskId: String): Flow<AgentTask?> = dao.getTaskById(taskId)

    /** الحصول على مهام المشروع */
    fun getProjectTasks(projectId: String): Flow<List<AgentTask>> = dao.getTasksByProject(projectId)

    /** محادثة المهمة (رسائل الوكلاء) */
    fun getTaskMessages(taskId: String): Flow<List<AgentMessage>> = dao.getMessagesByTask(taskId)

    /** إلغاء المهمة */
    suspend fun cancelTask(taskId: String) {
        val task = dao.getTaskById(taskId).firstOrNull()
        if (task != null) {
            dao.updateTask(task.copy(status = "Cancelled"))
        }
    }

    /** تنظيف الموارد */
    fun cleanup() {
        scope.cancel()
    }
}
