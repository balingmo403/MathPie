package com.mathhelp.app.ui.screens

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.mathhelp.app.ocr.OnDeviceMathOcrImpl

@Composable
fun OcrProcessingScreen(
    photoUri: Uri,
    onCompleted: (String, Float, String) -> Unit,
    onFailed: (String) -> Unit
) {
    val context = LocalContext.current
    var started by remember { mutableStateOf(false) }

    LaunchedEffect(photoUri) {
        if (started) return@LaunchedEffect
        started = true
        runCatching {
            OnDeviceMathOcrImpl(context.applicationContext).recognize(photoUri)
        }.onSuccess { result ->
            onCompleted(result.rawText, result.confidence, result.source)
        }.onFailure { error ->
            onFailed(error.message ?: "本地 OCR 失败")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F8FB))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(color = Color(0xFF1769AA))
        Text(
            text = "正在本地识别题目",
            modifier = Modifier.padding(top = 18.dp),
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "照片不会在这一步上传",
            modifier = Modifier.padding(top = 8.dp),
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF627D98)
        )
    }
}
