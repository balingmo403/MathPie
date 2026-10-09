package com.mathhelp.app.ui.screens

import android.content.Context
import android.net.Uri
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import java.io.File

@Composable
fun CameraScreen(
    onBack: () -> Unit,
    onPhotoCaptured: (Uri) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    var cameraError by remember { mutableStateOf<String?>(null) }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        AndroidView(
            factory = { viewContext ->
                PreviewView(viewContext).also { previewView ->
                    val providerFuture = ProcessCameraProvider.getInstance(viewContext)
                    providerFuture.addListener({
                        val provider = providerFuture.get()
                        val preview = Preview.Builder().build().also {
                            it.surfaceProvider = previewView.surfaceProvider
                        }
                        val capture = ImageCapture.Builder().build()
                        try {
                            provider.unbindAll()
                            provider.bindToLifecycle(
                                lifecycleOwner,
                                CameraSelector.DEFAULT_BACK_CAMERA,
                                preview,
                                capture
                            )
                            imageCapture = capture
                        } catch (error: Exception) {
                            cameraError = error.message ?: "相机初始化失败"
                        }
                    }, ContextCompat.getMainExecutor(viewContext))
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier.fillMaxSize().padding(18.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Outlined.ArrowBack, contentDescription = "返回", tint = Color.White)
                }
                Text("拍照扫描", color = Color.White)
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = cameraError ?: "将整道题放入取景框，保持纸面平整",
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(18.dp))
                IconButton(
                    onClick = { imageCapture?.takePhoto(context, onPhotoCaptured) },
                    modifier = Modifier
                        .size(72.dp)
                        .background(Color.White, CircleShape)
                ) {
                    Icon(
                        Icons.Outlined.CameraAlt,
                        contentDescription = "拍照",
                        tint = Color(0xFF1769AA),
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}

private fun ImageCapture.takePhoto(
    context: Context,
    onPhotoCaptured: (Uri) -> Unit
) {
    val photoFile = File.createTempFile("math-help-", ".jpg", context.cacheDir)
    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
    takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onError(exception: ImageCaptureException) = Unit

            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                onPhotoCaptured(Uri.fromFile(photoFile))
            }
        }
    )
}
