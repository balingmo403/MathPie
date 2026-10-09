package com.mathhelp.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val AppColors = lightColorScheme(
    primary = Color(0xFF1769AA),
    onPrimary = Color.White,
    secondary = Color(0xFF486581),
    background = Color(0xFFF6F8FB),
    surface = Color.White,
    onSurface = Color(0xFF102A43)
)

@Composable
fun MathHelpTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AppColors,
        content = content
    )
}
