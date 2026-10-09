package com.mathhelp.app.ui

import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.mathhelp.app.ui.screens.CameraScreen
import com.mathhelp.app.ui.screens.HomeScreen
import com.mathhelp.app.ui.screens.OcrReviewScreen
import com.mathhelp.app.ui.screens.OcrProcessingScreen
import com.mathhelp.app.ui.screens.QuestionReviewScreen
import com.mathhelp.app.ui.screens.SettingsScreen
import com.mathhelp.app.ui.screens.SolutionScreen

private enum class AppRoute {
    HOME,
    CAMERA,
    OCR_REVIEW,
    OCR_PROCESSING,
    QUESTION_REVIEW,
    SOLUTION,
    SETTINGS
}

@Composable
fun MathHelpApp(
    cameraGranted: Boolean,
    requestCameraPermission: () -> Unit
) {
    var route by remember { mutableStateOf(AppRoute.HOME) }
    var capturedPhoto by remember { mutableStateOf<Uri?>(null) }
    var recognizedText by remember { mutableStateOf("") }
    var ocrConfidence by remember { mutableStateOf(0f) }
    var ocrSource by remember { mutableStateOf("") }
    var ocrError by remember { mutableStateOf<String?>(null) }
    var questionForSolution by remember { mutableStateOf("") }
    val context = LocalContext.current

    Surface(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
            when (route) {
                AppRoute.HOME -> HomeScreen(
                    onScanClick = {
                        if (cameraGranted) route = AppRoute.CAMERA
                        else requestCameraPermission()
                    },
                    onSettingsClick = { route = AppRoute.SETTINGS }
                )

                AppRoute.CAMERA -> CameraScreen(
                    onBack = { route = AppRoute.HOME },
                    onPhotoCaptured = {
                        capturedPhoto = it
                        route = AppRoute.OCR_REVIEW
                    }
                )

                AppRoute.OCR_REVIEW -> OcrReviewScreen(
                    photoUri = capturedPhoto,
                    onBack = { route = AppRoute.HOME },
                    onRetake = { route = AppRoute.CAMERA },
                    onStartOcr = {
                        if (capturedPhoto == null) {
                            ocrError = "没有可识别的照片"
                            return@OcrReviewScreen
                        }
                        ocrError = null
                        route = AppRoute.OCR_PROCESSING
                    }
                )

                AppRoute.OCR_PROCESSING -> if (capturedPhoto != null) {
                    OcrProcessingScreen(
                        photoUri = capturedPhoto!!,
                        onCompleted = { text, confidence, source ->
                            recognizedText = text
                            ocrConfidence = confidence
                            ocrSource = source
                            route = AppRoute.QUESTION_REVIEW
                        },
                        onFailed = {
                            ocrError = it
                            route = AppRoute.OCR_REVIEW
                        }
                    )
                }

                AppRoute.QUESTION_REVIEW -> QuestionReviewScreen(
                    initialText = recognizedText,
                    confidence = ocrConfidence,
                    source = ocrSource,
                    onBack = { route = AppRoute.OCR_REVIEW },
                    onSolve = {
                        questionForSolution = it
                        route = AppRoute.SOLUTION
                    }
                )

                AppRoute.SOLUTION -> SolutionScreen(
                    context = context,
                    question = questionForSolution,
                    onBack = { route = AppRoute.QUESTION_REVIEW }
                )

                AppRoute.SETTINGS -> SettingsScreen(
                    onBack = { route = AppRoute.HOME }
                )
            }
        }
    }
}
