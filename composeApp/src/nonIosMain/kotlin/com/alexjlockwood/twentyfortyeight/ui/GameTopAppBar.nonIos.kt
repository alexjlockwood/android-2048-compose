package com.alexjlockwood.twentyfortyeight.ui

import androidx.compose.material.AppBarDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
actual fun GameTopAppBar(
    title: @Composable () -> Unit,
    contentColor: Color,
    backgroundColor: Color,
    actions: @Composable () -> Unit,
) {
    TopAppBar(
        windowInsets = AppBarDefaults.topAppBarWindowInsets,
        title = title,
        contentColor = contentColor,
        backgroundColor = backgroundColor,
        actions = { actions() },
    )
}
