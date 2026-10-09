package com.mathhelp.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun QuestionReviewScreen(
    initialText: String,
    confidence: Float,
    source: String,
    onBack: () -> Unit,
    onSolve: (String) -> Unit
) {
    var question by remember { mutableStateOf(initialText) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F8FB))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.Outlined.ArrowBack, contentDescription = "返回")
            }
            Text("校正识别结果", style = MaterialTheme.typography.titleLarge)
        }

        Text(
            text = "$source · 置信度 ${(confidence * 100).toInt()}%",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF627D98)
        )

        OutlinedTextField(
            value = question,
            onValueChange = { question = it },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            label = { Text("题目内容") },
            placeholder = { Text("如果公式识别不完整，请先手动补全") }
        )

        Button(
            onClick = { onSolve(question.trim()) },
            enabled = question.isNotBlank(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(Icons.Outlined.AutoAwesome, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("交给 DeepSeek 解题")
        }
    }
}
