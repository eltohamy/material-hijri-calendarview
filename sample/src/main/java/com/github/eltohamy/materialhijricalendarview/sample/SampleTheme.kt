package com.github.eltohamy.materialhijricalendarview.sample

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val SamplePrimary = Color(0xFF4590BD)
private val SampleAccent = Color(0xFF88BFCB)

private val LightColors = lightColorScheme(
    primary = SamplePrimary,
    secondary = SampleAccent,
)

private val DarkColors = darkColorScheme(
    primary = SampleAccent,
    secondary = SamplePrimary,
)

@Composable
fun SampleTheme(content: @Composable () -> Unit) {
    val colors = if (isSystemInDarkTheme()) DarkColors else LightColors
    MaterialTheme(colorScheme = colors, content = content)
}
