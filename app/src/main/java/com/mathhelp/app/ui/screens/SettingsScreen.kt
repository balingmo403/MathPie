package com.mathhelp.app.ui.screens

import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Save
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.mathhelp.app.data.AppSettings
import com.mathhelp.app.data.DeepSeekSettings

@Composable
fun SettingsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val appSettings = remember(context) { AppSettings(context) }
    val initialSettings = remember(appSettings) { appSettings.loadDeepSeek() }
    var baseUrl by remember { mutableStateOf(initialSettings.baseUrl) }
    var model by remember { mutableStateOf(initialSettings.model) }
    var apiKey by remember { mutableStateOf(initialSettings.apiKey) }
    var saved by remember { mutableStateOf(false) }

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
            Text("DeepSeek 配置", style = MaterialTheme.typography.titleLarge)
        }

        Text(
            text = "密钥只用于本机请求。正式发布时应迁移到自己的后端，避免把密钥暴露在 APK 中。",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF627D98)
        )

        OutlinedTextField(
            value = baseUrl,
            onValueChange = { baseUrl = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Base URL") },
            singleLine = true
        )
        OutlinedTextField(
            value = model,
            onValueChange = { model = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("模型名称") },
            singleLine = true
        )
        OutlinedTextField(
            value = apiKey,
            onValueChange = { apiKey = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("API Key") },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true
        )

        Button(
            onClick = {
                appSettings.saveDeepSeek(
                    DeepSeekSettings(
                        baseUrl = baseUrl.trim(),
                        model = model.trim(),
                        apiKey = apiKey.trim()
                    )
                )
                saved = true
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(Icons.Outlined.Save, contentDescription = null)
            Text("保存到本机")
        }

        if (saved) {
            Text(
                text = "配置已保存到本机。",
                color = Color(0xFF2F855A),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
