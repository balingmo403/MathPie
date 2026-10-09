package com.mathhelp.app.data.remote

import android.content.Context
import com.mathhelp.app.data.AppSettings
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class DeepSeekRepository(context: Context) {
    private val settings = AppSettings(context)

    private fun api(baseUrl: String): DeepSeekApi {
        val logger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()

        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl.ensureTrailingSlash())
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(DeepSeekApi::class.java)
    }

    suspend fun solve(question: String): Result<String> = runCatching {
        val config = settings.loadDeepSeek()
        require(config.apiKey.isNotBlank()) {
            "请先在 deepseek.properties 或应用设置中配置 API Key"
        }
        val response = api(config.baseUrl).createChatCompletion(
            authorization = "Bearer ${config.apiKey}",
            request = ChatCompletionRequest(
                model = config.model,
                messages = listOf(
                    ChatMessage(
                        role = "system",
                        content = """
                            你是严谨的数学解题助手。
                            只返回一个合法、完整、可直接解析的 JSON 对象，不要返回 Markdown 代码块，
                            不要添加“解题结果”等前后说明，不要省略任何引号或逗号。
                            JSON 字段必须为：
                            answer: 字符串，填写最终答案；
                            methods: 数组，每个元素包含 title 字符串和 steps 字符串数组；
                            verification: 字符串，说明如何核对结果；
                            confidence: 字符串，只能是 high、medium 或 low。
                            methods 至少给出一种解法；如果存在不同且有教育意义的解法，再给出其他解法。
                            不要编造题目中不存在的条件；无法验证时明确说明。
                        """.trimIndent()
                    ),
                    ChatMessage(role = "user", content = question)
                )
            )
        )
        response.choices.firstOrNull()?.message?.content
            ?: error("模型没有返回解题内容")
    }
}

private fun String.ensureTrailingSlash(): String =
    if (endsWith("/")) this else "$this/"
