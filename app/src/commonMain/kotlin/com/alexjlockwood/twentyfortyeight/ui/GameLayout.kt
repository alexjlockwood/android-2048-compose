package com.alexjlockwood.twentyfortyeight.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
) {
    BoxWithConstraints(modifier = Modifier) {
        val isPortrait = maxWidth < maxHeight
        val gridSize = min(maxWidth, maxHeight) - 32.dp
        if (isPortrait) {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Box(
                    modifier = Modifier
                        .padding(start = 16.dp, top = 16.dp, end = 16.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) { gameGrid(gridSize) }
                Row(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp)
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
                modifier = modifier,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center,
                ) { gameGrid(gridSize) }
                Column(
                    modifier = Modifier
                        .padding(top = 16.dp, bottom = 16.dp)
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
