package com.alexjlockwood.twentyfortyeight.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColorScheme(
    primary = Color(0xff4e6cef),
    secondary = Color(0xff3f51b5),
)

private val LightColorPalette = lightColorScheme(
    primary = Color(0xff50c0e9),
    secondary = Color(0xff1da9da),
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    val colorScheme = if (isSystemInDarkTheme()) DarkColorPalette else LightColorPalette
    MaterialTheme(
        colorScheme = colorScheme,
        content = content,
    )
}
