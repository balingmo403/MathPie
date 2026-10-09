package com.mathhelp.app.ui.screens

import android.net.Uri
import android.view.ViewGroup
import android.widget.ImageView
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
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Refresh
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.unit.dp

@Composable
fun OcrReviewScreen(
    photoUri: Uri?,
    onBack: () -> Unit,
    onRetake: () -> Unit,
    onStartOcr: () -> Unit
) {
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
            Text("确认识别范围", style = MaterialTheme.typography.titleLarge)
        }

        Card(
            modifier = Modifier.fillMaxWidth().weight(1f),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            if (photoUri != null) {
                AndroidView(
                    factory = { context ->
                        ImageView(context).apply {
                            scaleType = ImageView.ScaleType.FIT_CENTER
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                            setImageURI(photoUri)
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("没有读取到照片")
                }
            }
        }

        Text(
            text = "下一步将使用手机本地 OCR 识别中文与数字。数学公式 LaTeX 模型接口已预留，接入模型后会自动加入公式识别。",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF627D98)
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
                onClick = onRetake,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Outlined.Refresh, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text("重拍")
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(
                onClick = onStartOcr,
                modifier = Modifier.weight(1.4f),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1769AA))
            ) {
                Icon(Icons.Outlined.Check, contentDescription = null)
                Spacer(modifier = Modifier.padding(4.dp))
                Text("开始识题")
            }
        }
    }
}
