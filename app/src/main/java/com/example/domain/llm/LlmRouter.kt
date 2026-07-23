package com.example.omnimind.domain.llm

import com.example.omnimind.data.db.OmniMindDao
import com.example.omnimind.data.model.ApiKeyConfig
import com.example.omnimind.data.network.GeminiApiService
import com.example.omnimind.data.network.GeminiContent
import com.example.omnimind.data.network.GeminiGenerateRequest
import com.example.omnimind.data.network.GeminiPart
import com.example.omnimind.data.network.GenericLlmApiService
import com.example.omnimind.data.security.SecurityManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import java.io.IOException

data class LlmReply(val text: String, val provider: String, val model: String)

class LlmRouter(
    private val dao: OmniMindDao,
    private val securityManager: SecurityManager,
    private val geminiApi: GeminiApiService,
    private val genericApi: GenericLlmApiService
) {
    suspend fun generate(prompt: String, minimumTier: Int = 1): Result<LlmReply> =
        withContext(Dispatchers.IO) {
            val eligible = dao.getEnabledApiKeys().firstOrNull()
                ?.filter { it.providerName.lowercase() != "github" }
                ?.filter { it.monthlyBudgetCents == null || it.currentSpentCents < it.monthlyBudgetCents }
                ?: emptyList()

            val tierCandidates = eligible.filter { it.modelTier >= minimumTier }
            val candidates = tierCandidates.ifEmpty { eligible }.sortedWith(
                compareBy<ApiKeyConfig> { it.modelTier }
                    .thenByDescending { it.priorityWeight }
                    .thenBy { it.lastUsedTimestamp }
            )

            if (candidates.isEmpty()) {
                return@withContext Result.failure(
                    IllegalStateException("لا توجد نماذج AI مفعلة لهذا المستوى")
                )
            }

            val failures = mutableListOf<String>()
            for (candidate in candidates) {
                val key = securityManager.decrypt(candidate.encryptedKey)
                if (key.isBlank()) {
                    failures += "${candidate.providerName}: تعذر فك المفتاح"
                    continue
                }
                runCatching { request(candidate, key, prompt) }
                    .onSuccess {
                        dao.updateApiKey(
                            candidate.copy(
                                lastUsedTimestamp = System.currentTimeMillis(),
                                lastError = null
                            )
                        )
                        return@withContext Result.success(it)
                    }
                    .onFailure { error ->
                        val message = safeError(error)
                        failures += "${candidate.providerName}/${candidate.modelId}: $message"
                        dao.updateApiKey(candidate.copy(lastError = message))
                    }
            }
            return@withContext Result.failure(
                IllegalStateException(failures.joinToString("\n"))
            )
        }

    private suspend fun request(config: ApiKeyConfig, key: String, prompt: String): LlmReply {
        val provider = config.providerName.trim().lowercase()
        val model = config.modelId.ifBlank { defaultModel(provider) }
        val text = when (provider) {
            "gemini", "google" -> requestGemini(key, model, prompt)
            "anthropic" -> requestAnthropic(config.baseUrl, key, model, prompt)
            "groq" -> requestGroq(config, key, model, prompt)
            "openrouter" -> requestOpenRouter(config, key, model, prompt)
            else -> requestOpenAiCompatible(config, key, model, prompt)
        }
        require(text.isNotBlank()) { "أعاد المزود استجابة فارغة" }
        return LlmReply(text.trim(), config.providerName, model)
    }

    private suspend fun requestGemini(key: String, model: String, prompt: String): String {
        val response = geminiApi.generateContent(
            model = model,
            apiKey = key,
            request = GeminiGenerateRequest(
                contents = listOf(GeminiContent(parts = listOf(GeminiPart(prompt))))
            )
        )
        return response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text.orEmpty()
    }

    private suspend fun requestOpenAiCompatible(
        config: ApiKeyConfig, key: String, model: String, prompt: String
    ): String {
        val baseUrl = when (config.providerName.trim().lowercase()) {
            "openai" -> config.baseUrl ?: "https://api.openai.com/v1/"
            else -> requireNotNull(config.baseUrl) { "يجب إدخال رابط HTTPS للمزود المخصص" }
        }
        require(baseUrl.startsWith("https://")) { "يسمح فقط بروابط HTTPS" }
        val response = genericApi.post(
            endpoint(baseUrl, "chat/completions"),
            mapOf("Authorization" to "Bearer $key", "Content-Type" to "application/json"),
            mapOf(
                "model" to model,
                "messages" to listOf(mapOf("role" to "user", "content" to prompt))
            )
        )
        check(response.isSuccessful) { "فشل المزود برمز HTTP ${response.code()}" }
        return response.body()?.getAsJsonArray("choices")?.firstOrNull()?.asJsonObject
            ?.getAsJsonObject("message")?.get("content")?.asString.orEmpty()
    }

    private suspend fun requestGroq(
        config: ApiKeyConfig, key: String, model: String, prompt: String
    ): String {
        val baseUrl = config.baseUrl ?: "https://api.groq.com/openai/v1/"
        require(baseUrl.startsWith("https://")) { "يسمح فقط بروابط HTTPS" }
        val response = genericApi.post(
            endpoint(baseUrl, "chat/completions"),
            mapOf("Authorization" to "Bearer $key", "Content-Type" to "application/json"),
            mapOf(
                "model" to model,
                "messages" to listOf(mapOf("role" to "user", "content" to prompt))
            )
        )
        check(response.isSuccessful) { "فشل Groq برمز HTTP ${response.code()}" }
        return response.body()?.getAsJsonArray("choices")?.firstOrNull()?.asJsonObject
            ?.getAsJsonObject("message")?.get("content")?.asString.orEmpty()
    }

    private suspend fun requestOpenRouter(
        config: ApiKeyConfig, key: String, model: String, prompt: String
    ): String {
        val baseUrl = config.baseUrl ?: "https://openrouter.ai/api/v1/"
        require(baseUrl.startsWith("https://")) { "يسمح فقط بروابط HTTPS" }
        val response = genericApi.post(
            endpoint(baseUrl, "chat/completions"),
            mapOf("Authorization" to "Bearer $key", "Content-Type" to "application/json"),
            mapOf(
                "model" to model,
                "messages" to listOf(mapOf("role" to "user", "content" to prompt))
            )
        )
        check(response.isSuccessful) { "فشل OpenRouter برمز HTTP ${response.code()}" }
        return response.body()?.getAsJsonArray("choices")?.firstOrNull()?.asJsonObject
            ?.getAsJsonObject("message")?.get("content")?.asString.orEmpty()
    }

    private suspend fun requestAnthropic(
        baseUrl: String?, key: String, model: String, prompt: String
    ): String {
        val base = baseUrl ?: "https://api.anthropic.com/"
        require(base.startsWith("https://")) { "يسمح فقط بروابط HTTPS" }
        val response = genericApi.post(
            endpoint(base, "v1/messages"),
            mapOf(
                "x-api-key" to key,
                "anthropic-version" to "2023-06-01",
                "Content-Type" to "application/json"
            ),
            mapOf(
                "model" to model,
                "max_tokens" to 2048,
                "messages" to listOf(mapOf("role" to "user", "content" to prompt))
            )
        )
        check(response.isSuccessful) { "فشل Anthropic برمز HTTP ${response.code()}" }
        return response.body()?.getAsJsonArray("content")?.firstOrNull()?.asJsonObject
            ?.get("text")?.asString.orEmpty()
    }

    private fun endpoint(baseUrl: String, path: String) =
        baseUrl.trimEnd('/') + "/" + path.trimStart('/')

    private fun defaultModel(provider: String): String = when (provider) {
        "gemini", "google" -> "gemini-2.0-flash"
        "openai" -> "gpt-4o-mini"
        "anthropic" -> "claude-3-5-haiku-latest"
        "openrouter" -> "google/gemini-2.0-flash-exp:free"
        "groq" -> "llama-3.1-8b-instant"
        else -> throw IllegalArgumentException("يجب تحديد معرّف النموذج")
    }

    private fun safeError(error: Throwable): String = when (error) {
        is java.net.SocketTimeoutException -> "انتهت مهلة الاتصال"
        is IOException -> "تعذر الاتصال بالمزود"
        is IllegalArgumentException -> error.message?.take(160) ?: "إعداد المزود غير صالح"
        is IllegalStateException -> error.message?.take(160) ?: "رفض المزود الطلب"
        else -> "فشل طلب النموذج"
    }
}
