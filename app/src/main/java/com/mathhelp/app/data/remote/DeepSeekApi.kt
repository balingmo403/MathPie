package com.mathhelp.app.data.remote

import com.squareup.moshi.Json
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface DeepSeekApi {
    @POST("chat/completions")
    suspend fun createChatCompletion(
        @Header("Authorization") authorization: String,
        @Body request: ChatCompletionRequest
    ): ChatCompletionResponse
}

data class ChatCompletionRequest(
    val model: String,
    val messages: List<ChatMessage>,
    val temperature: Double = 0.2,
    @Json(name = "max_tokens") val maxTokens: Int = 8192,
    val stream: Boolean = false
)

data class ChatMessage(
    val role: String,
    val content: String
)

data class ChatCompletionResponse(
    val choices: List<ChatChoice> = emptyList()
)

data class ChatChoice(
    val message: ChatMessage
)
