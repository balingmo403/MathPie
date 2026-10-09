package com.mathhelp.app.data

import android.content.Context
import com.mathhelp.app.BuildConfig

data class DeepSeekSettings(
    val baseUrl: String,
    val model: String,
    val apiKey: String
)

class AppSettings(context: Context) {
    private val preferences = context.getSharedPreferences(
        "math_help_settings",
        Context.MODE_PRIVATE
    )

    fun loadDeepSeek(): DeepSeekSettings = DeepSeekSettings(
        baseUrl = preferences.getString(KEY_BASE_URL, BuildConfig.DEFAULT_DEEPSEEK_BASE_URL)
            ?: BuildConfig.DEFAULT_DEEPSEEK_BASE_URL,
        model = preferences.getString(KEY_MODEL, BuildConfig.DEFAULT_DEEPSEEK_MODEL)
            ?: BuildConfig.DEFAULT_DEEPSEEK_MODEL,
        apiKey = preferences.getString(KEY_API_KEY, BuildConfig.DEFAULT_DEEPSEEK_API_KEY)
            ?: BuildConfig.DEFAULT_DEEPSEEK_API_KEY
    )

    fun saveDeepSeek(settings: DeepSeekSettings) {
        preferences.edit()
            .putString(KEY_BASE_URL, settings.baseUrl)
            .putString(KEY_MODEL, settings.model)
            .putString(KEY_API_KEY, settings.apiKey)
            .apply()
    }

    private companion object {
        const val KEY_BASE_URL = "deepseek_base_url"
        const val KEY_MODEL = "deepseek_model"
        const val KEY_API_KEY = "deepseek_api_key"
    }
}
