package com.alexjlockwood.twentyfortyeight.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Color(0xff4e6cef),
    primaryVariant = Color(0xff3f51b5),
)

private val LightColorPalette = lightColors(
    primary = Color(0xff50c0e9),
    primaryVariant = Color(0xff1da9da),
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    val colors = if (isSystemInDarkTheme()) DarkColorPalette else LightColorPalette
    MaterialTheme(
        colors = colors,
        content = content,
    )
}
