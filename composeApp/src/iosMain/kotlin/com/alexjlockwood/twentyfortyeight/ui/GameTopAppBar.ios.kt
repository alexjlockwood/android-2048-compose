package com.alexjlockwood.twentyfortyeight.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
actual fun GameTopAppBar(
    title: @Composable () -> Unit,
    contentColor: Color,
    backgroundColor: Color,
    actions: @Composable () -> Unit,
) {
    // Custom app bar to ensure the title is center aligned on iOS.
    Surface(
        color = backgroundColor,
        contentColor = contentColor,
        elevation = AppBarDefaults.TopAppBarElevation,
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .windowInsetsPadding(AppBarDefaults.topAppBarWindowInsets)
                .padding(AppBarDefaults.ContentPadding)
                .height(56.dp),
        ) {
            Box(Modifier.align(Alignment.Center)) {
                ProvideTextStyle(value = MaterialTheme.typography.h6) {
                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
                        title()
                    }
                }
            }

            Box(Modifier.align(Alignment.CenterEnd)) {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    actions()
                }
            }
        }
    }
}
