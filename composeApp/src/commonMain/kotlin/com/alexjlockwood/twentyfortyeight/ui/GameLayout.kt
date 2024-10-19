package com.alexjlockwood.twentyfortyeight.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min

@Composable
fun GameLayout(
    gameGrid: @Composable ((Dp) -> Unit),
    currentScoreText: @Composable (() -> Unit),
    currentScoreLabel: @Composable (() -> Unit),
    bestScoreText: @Composable (() -> Unit),
    bestScoreLabel: @Composable (() -> Unit),
    modifier: Modifier = Modifier,
    padding: Dp = 16.dp,
) {
    BoxWithConstraints(modifier = modifier) {
        val isPortrait = maxWidth < maxHeight
        val gridSize = min(maxWidth, maxHeight) - padding * 2
        if (isPortrait) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(padding),
            ) {
                Box(
                    modifier = Modifier
                        .padding(start = padding, top = padding, end = padding)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) { gameGrid(gridSize) }
                Row(
                    modifier = Modifier
                        .padding(start = padding, end = padding)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column {
                        currentScoreText()
                        currentScoreLabel()
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        bestScoreText()
                        bestScoreLabel()
                    }
                }
            }
        } else {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(padding)
            ) {
                Box(
                    modifier = Modifier
                        .padding(start = padding, top = padding, bottom = padding)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center,
                ) { gameGrid(gridSize) }
                Column(
                    modifier = Modifier
                        .padding(top = padding, bottom = padding)
                        .align(Alignment.Bottom)
                ) {
                    currentScoreText()
                    currentScoreLabel()
                    bestScoreText()
                    bestScoreLabel()
                }
            }
        }
    }
}
