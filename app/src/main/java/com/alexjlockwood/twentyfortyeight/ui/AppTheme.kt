package com.alexjlockwood.twentyfortyeight.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.alexjlockwood.twentyfortyeight.R


@Composable
fun AppTheme(content: @Composable () -> Unit) {
    val DarkColorPalette = darkColors(primaryVariant = colorResource(id = R.color.md_theme_light_primary))
    val LightColorPalette = lightColors(primaryVariant = colorResource(id = R.color.md_theme_dark_primary))

    val colors = if (isSystemInDarkTheme()) DarkColorPalette else LightColorPalette
    MaterialTheme(
            colors = colors,
            content = content,
    )
}