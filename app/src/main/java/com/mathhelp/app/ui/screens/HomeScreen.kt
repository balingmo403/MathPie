package com.mathhelp.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.CloudDone
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onScanClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F8FB))
            .padding(horizontal = 24.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "数学派",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF102A43)
                )
                Text(
                    text = "本地识别，清晰解题",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF627D98)
                )
            }
            IconButton(onClick = onSettingsClick) {
                Icon(Icons.Outlined.Settings, contentDescription = "设置")
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F1F8))
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.WifiOff,
                    contentDescription = null,
                    tint = Color(0xFF1769AA)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "隐私优先",
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF102A43)
                    )
                    Text(
                        text = "照片与 OCR 默认只保存在本机",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF486581)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onScanClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1769AA))
        ) {
            Icon(Icons.Outlined.CameraAlt, contentDescription = null)
            Spacer(modifier = Modifier.width(10.dp))
            Text("拍照扫描试卷", fontWeight = FontWeight.SemiBold)
        }

        OutlinedButton(
            onClick = onSettingsClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(Icons.Outlined.Settings, contentDescription = null)
            Spacer(modifier = Modifier.width(10.dp))
            Text("配置 DeepSeek")
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "当前工作流",
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF102A43)
                )
                Spacer(modifier = Modifier.height(12.dp))
                WorkflowRow("01", "手机本地拍照与裁剪")
                WorkflowRow("02", "本地中文与数字 OCR")
                WorkflowRow("03", "DeepSeek 生成多种解法")
                WorkflowRow("04", "SymPy 校验计算结果")
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Outlined.CloudDone,
                contentDescription = null,
                tint = Color(0xFF627D98)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "联网仅用于调用解题模型",
                style = MaterialTheme.typography.labelMedium,
                color = Color(0xFF627D98)
            )
        }
    }
}

@Composable
private fun WorkflowRow(index: String, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = index,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1769AA)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = text, color = Color(0xFF486581))
    }
}
