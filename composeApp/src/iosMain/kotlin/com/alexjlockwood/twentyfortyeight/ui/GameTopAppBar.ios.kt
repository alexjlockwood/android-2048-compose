package com.alexjlockwood.twentyfortyeight.ui

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
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
    CenterAlignedTopAppBar(
        title = { Text("2048") },
//        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
//            titleContentColor = contentColor,
//            actionIconContentColor = contentColor,
//        ),
        actions = { actions() },
    )

    // Custom app bar to ensure the title is center aligned on iOS.
//    Surface(
//        color = backgroundColor,
//        contentColor = contentColor,
//    ) {
//        Box(
//            Modifier
//                .fillMaxWidth()
//                .windowInsetsPadding(AppBarDefaults.topAppBarWindowInsets)
//                .padding(AppBarDefaults.ContentPadding)
//                .height(56.dp),
//        ) {
//            Box(Modifier.align(Alignment.Center)) {
//                ProvideTextStyle(value = MaterialTheme.typography.h6) {
//                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
//                        title()
//                    }
//                }
//            }
//
//            Box(Modifier.align(Alignment.CenterEnd)) {
//                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
//                    actions()
//                }
//            }
//        }
//    }
}
