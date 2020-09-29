package com.alexjlockwood.twentyfortyeight

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

import androidx.compose.ui.graphics.Color

private val purple200 = Color(0xFFBB86FC)
private val purple500 = Color(0xFF6200EE)
private val purple700 = Color(0xFF3700B3)
private val teal200 = Color(0xFF03DAC5)

private val DarkColorPalette = darkColors()

private val LightColorPalette = lightColors()

@Composable
fun TwentyFortyEightTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) DarkColorPalette else LightColorPalette
    MaterialTheme(
        colors = colors,
        content = content,
    )
}