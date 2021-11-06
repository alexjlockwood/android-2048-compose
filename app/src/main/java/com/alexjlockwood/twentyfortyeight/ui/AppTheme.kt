package com.alexjlockwood.twentyfortyeight.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable


@Composable
fun AppTheme(content: @Composable () -> Unit) {

    val colors = if (isSystemInDarkTheme()) DarkThemeColors else LightThemeColors
    MaterialTheme(
        colorScheme = colors,
        content = content,
    )
}