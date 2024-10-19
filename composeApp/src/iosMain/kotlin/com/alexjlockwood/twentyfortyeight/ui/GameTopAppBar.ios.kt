package com.alexjlockwood.twentyfortyeight.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
actual fun GameTopAppBar(
    title: @Composable () -> Unit,
    contentColor: Color,
    backgroundColor: Color,
    actions: @Composable () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = title,
        contentColor = contentColor,
        backgroundColor = backgroundColor,
        actions = actions,
    )
}
