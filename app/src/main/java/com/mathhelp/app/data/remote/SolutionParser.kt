package com.mathhelp.app.data.remote

import org.json.JSONArray
import org.json.JSONObject

data class ParsedSolution(
    val answer: String,
    val methods: List<SolutionMethod>,
    val verification: String,
    val confidence: String
)

data class SolutionMethod(
    val title: String,
    val steps: List<String>
)

object SolutionParser {
    fun parse(rawContent: String): Result<ParsedSolution> = runCatching {
        val json = JSONObject(extractJsonObject(rawContent))
        val answer = json.optString("answer").trim()
        require(answer.isNotBlank()) {
            "返回结果中缺少 answer 字段"
        }

        ParsedSolution(
            answer = answer,
            methods = json.optJSONArray("methods").toSolutionMethods(),
            verification = json.optString("verification").trim(),
            confidence = json.optString("confidence").trim().ifBlank { "未提供" }
        )
    }

    private fun extractJsonObject(rawContent: String): String {
        val withoutMarkdown = rawContent
            .replace(Regex("^\\s*```(?:json)?\\s*", RegexOption.IGNORE_CASE), "")
            .replace(Regex("\\s*```\\s*$"), "")
            .trim()

        val start = withoutMarkdown.indexOf('{')
        val end = withoutMarkdown.lastIndexOf('}')
        require(start >= 0 && end > start) {
            "没有找到完整的 JSON 对象"
        }
        return withoutMarkdown.substring(start, end + 1)
    }

    private fun JSONArray?.toSolutionMethods(): List<SolutionMethod> {
        if (this == null) return emptyList()

        return buildList {
            for (index in 0 until length()) {
                val method = optJSONObject(index) ?: continue
                add(
                    SolutionMethod(
                        title = method.optString("title").ifBlank { "解法 ${index + 1}" },
                        steps = method.optJSONArray("steps").toStringList()
                    )
                )
            }
        }
    }

    private fun JSONArray?.toStringList(): List<String> {
        if (this == null) return emptyList()

        return buildList {
            for (index in 0 until length()) {
                val value = opt(index)?.toString()?.trim().orEmpty()
                if (value.isNotBlank()) add(value)
            }
        }
    }
}
