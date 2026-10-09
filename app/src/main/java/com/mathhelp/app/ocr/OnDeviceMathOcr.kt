package com.mathhelp.app.ocr

import android.content.Context
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions
import com.google.mlkit.vision.text.TextRecognition
import kotlinx.coroutines.tasks.await

data class OcrResult(
    val rawText: String,
    val latex: String,
    val confidence: Float,
    val isModelReady: Boolean,
    val source: String
)

interface OnDeviceMathOcr {
    suspend fun recognize(imageUri: Uri): OcrResult
}

class OnDeviceMathOcrImpl(
    private val context: Context
) : OnDeviceMathOcr {
    override suspend fun recognize(imageUri: Uri): OcrResult {
        val image = InputImage.fromFilePath(context, imageUri)
        val recognizer = TextRecognition.getClient(
            ChineseTextRecognizerOptions.Builder().build()
        )
        return try {
            val result = recognizer.process(image).await()
            val text = result.text.trim()
            OcrResult(
                rawText = text,
                latex = "",
                confidence = if (text.isBlank()) 0f else 0.72f,
                isModelReady = true,
                source = "ML Kit 本地中文 OCR"
            )
        } finally {
            recognizer.close()
        }
    }
}
