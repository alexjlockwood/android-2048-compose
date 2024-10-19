package com.alexjlockwood.twentyfortyeight.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
actual fun GameTopAppBar(
    title: @Composable () -> Unit,
    contentColor: Color,
    backgroundColor: Color,
    actions: @Composable () -> Unit,
) {
    TopAppBar(
        title = title,
//        colors = TopAppBarDefaults.topAppBarColors(
//            containerColor = backgroundColor,
//            titleContentColor = contentColor,
//            actionIconContentColor = contentColor,
//        ),
        actions = { actions() },
    )
}
