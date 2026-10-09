package com.mathhelp.app.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mathhelp.app.data.remote.DeepSeekRepository
import com.mathhelp.app.data.remote.ParsedSolution
import com.mathhelp.app.data.remote.SolutionMethod
import com.mathhelp.app.data.remote.SolutionParser

private val Ink = Color(0xFF102A43)
private val MutedInk = Color(0xFF627D98)
private val Blue = Color(0xFF1769AA)
private val Page = Color(0xFFF6F8FB)

@Composable
fun SolutionScreen(
    context: Context,
    question: String,
    onBack: () -> Unit
) {
    var solution by remember { mutableStateOf<ParsedSolution?>(null) }
    var rawContent by remember { mutableStateOf<String?>(null) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(question) {
        solution = null
        rawContent = null
        error = null

        DeepSeekRepository(context.applicationContext).solve(question)
            .onSuccess { raw ->
                rawContent = raw
                SolutionParser.parse(raw)
                    .onSuccess { solution = it }
                    .onFailure {
                        error = "模型返回内容不是完整 JSON：${it.message}"
                    }
            }
            .onFailure { error = it.message ?: "解题请求失败" }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Page)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Outlined.ArrowBack, contentDescription = "返回")
            }
            Text(
                text = "解题结果",
                style = MaterialTheme.typography.titleLarge,
                color = Ink,
                fontWeight = FontWeight.SemiBold
            )
        }

        when {
            solution != null -> SolutionContent(solution = solution!!)
            error != null -> ParseErrorContent(
                error = error.orEmpty(),
                rawContent = rawContent
            )
            else -> LoadingContent()
        }
    }
}

@Composable
private fun SolutionContent(solution: ParsedSolution) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        AnswerPanel(answer = solution.answer, confidence = solution.confidence)

        if (solution.methods.isNotEmpty()) {
            Text(
                text = "解题思路",
                style = MaterialTheme.typography.titleMedium,
                color = Ink,
                fontWeight = FontWeight.SemiBold
            )
            solution.methods.forEachIndexed { index, method ->
                MethodCard(index = index, method = method)
            }
        }

        if (solution.verification.isNotBlank()) {
            VerificationPanel(text = solution.verification)
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun AnswerPanel(answer: String, confidence: String) {
    val answers = answer.split(",").map { it.trim() }.filter { it.isNotEmpty() }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFFE8F1F8)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "最终答案",
                        style = MaterialTheme.typography.labelLarge,
                        color = Blue,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "共 ${answers.size} 项",
                        style = MaterialTheme.typography.bodySmall,
                        color = MutedInk
                    )
                }
                ConfidenceLabel(confidence = confidence)
            }

            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                answers.forEachIndexed { index, value ->
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.White
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "${index + 1}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MutedInk
                            )
                            Text(
                                text = value,
                                style = MaterialTheme.typography.titleMedium,
                                color = Ink,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ConfidenceLabel(confidence: String) {
    val isHigh = confidence.equals("high", ignoreCase = true)
    val isMedium = confidence.equals("medium", ignoreCase = true)
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = if (isHigh) Color(0xFFDDF4E8) else Color(0xFFFFF1D6)
    ) {
        Text(
            text = when {
                isHigh -> "高置信度"
                isMedium -> "中置信度"
                confidence.equals("low", ignoreCase = true) -> "低置信度"
                else -> confidence
            },
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelMedium,
            color = if (isHigh) Color(0xFF237A4B) else Color(0xFF9A6700)
        )
    }
}

@Composable
private fun MethodCard(index: Int, method: SolutionMethod) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Lightbulb,
                    contentDescription = null,
                    tint = Blue
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "解法 ${index + 1} · ${method.title}",
                    style = MaterialTheme.typography.titleMedium,
                    color = Ink,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            if (method.steps.isEmpty()) {
                Text(
                    text = "模型没有返回具体步骤。",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MutedInk
                )
            } else {
                method.steps.forEachIndexed { stepIndex, step ->
                    StepRow(index = stepIndex + 1, text = step)
                    if (stepIndex < method.steps.lastIndex) {
                        HorizontalDivider(
                            modifier = Modifier.padding(
                                start = 34.dp,
                                top = 8.dp,
                                bottom = 8.dp
                            ),
                            color = Color(0xFFE6EDF3)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StepRow(index: Int, text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Surface(
            shape = RoundedCornerShape(6.dp),
            color = Color(0xFFE8F1F8)
        ) {
            Text(
                text = index.toString(),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelMedium,
                color = Blue,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = text,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge,
            color = Ink
        )
    }
}

@Composable
private fun VerificationPanel(text: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Outlined.CheckCircle,
                contentDescription = null,
                tint = Color(0xFF2F855A)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "结果验证",
                    style = MaterialTheme.typography.titleMedium,
                    color = Ink,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MutedInk
                )
            }
        }
    }
}

@Composable
private fun LoadingContent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(color = Blue)
        Text(
            text = "正在生成解法",
            modifier = Modifier.padding(top = 16.dp),
            color = Ink
        )
    }
}

@Composable
private fun ParseErrorContent(
    error: String,
    rawContent: String?
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFFFFF1F1)
        ) {
            Row(modifier = Modifier.padding(16.dp)) {
                Icon(
                    imageVector = Icons.Outlined.ErrorOutline,
                    contentDescription = null,
                    tint = Color(0xFFC53030)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = error, color = Color(0xFFC53030))
            }
        }

        if (!rawContent.isNullOrBlank()) {
            Text(
                text = "原始返回",
                style = MaterialTheme.typography.titleMedium,
                color = Ink,
                fontWeight = FontWeight.SemiBold
            )
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                color = Color.White
            ) {
                Text(
                    text = rawContent,
                    modifier = Modifier.padding(14.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MutedInk
                )
            }
        }
    }
}
