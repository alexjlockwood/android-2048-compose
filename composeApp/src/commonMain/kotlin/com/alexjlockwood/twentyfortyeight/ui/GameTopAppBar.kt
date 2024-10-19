package com.alexjlockwood.twentyfortyeight.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
expect fun GameTopAppBar(
    title: @Composable () -> Unit,
    contentColor: Color,
    backgroundColor: Color,
    actions: @Composable () -> Unit,
)
