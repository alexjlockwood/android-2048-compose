package com.alexjlockwood.twentyfortyeight.ui

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.alexjlockwood.twentyfortyeight.domain.Direction
import com.alexjlockwood.twentyfortyeight.domain.GridTileMovement
import kotlin.math.PI
import kotlin.math.atan2

/**
 * Renders the 2048 game's home screen UI.
 */
@Composable
fun GameUi(
    gridTileMovements: List<GridTileMovement>,
    currentScore: Int,
    bestScore: Int,
    isGameOver: Boolean,
    onNewGameRequested: () -> Unit,
    onSwipeListener: (direction: Direction) -> Unit,
    modifier: Modifier = Modifier,
) {
    var shouldShowNewGameDialog by remember { mutableStateOf(false) }
    var swipeAngle by remember { mutableDoubleStateOf(0.0) }
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = "2048 Compose") },
                contentColor = Color.White,
                backgroundColor = MaterialTheme.colors.primaryVariant,
                actions = {
                    IconButton(onClick = { shouldShowNewGameDialog = true }) { Icon(Icons.Filled.Add, contentDescription = null) }
                }
            )
        },
        contentWindowInsets = WindowInsets.safeDrawing,
    ) { innerPadding ->
        GameLayout(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDrag = { change, dragAmount ->
                            change.consume()
                            swipeAngle = with(dragAmount) { (atan2(-y, x) * 180 / PI + 360) % 360 }
                        },
                        onDragEnd = {
                            onSwipeListener(
                                when {
                                    45 <= swipeAngle && swipeAngle < 135 -> Direction.NORTH
                                    135 <= swipeAngle && swipeAngle < 225 -> Direction.WEST
                                    225 <= swipeAngle && swipeAngle < 315 -> Direction.SOUTH
                                    else -> Direction.EAST
                                }
                            )
                        }
                    )
                },
            gameGrid = { gridSize ->
                GameGrid(gridTileMovements = gridTileMovements, gridSize = gridSize)
            },
            currentScoreText = { TextLabel(text = "$currentScore", fontSize = 36.sp) },
            currentScoreLabel = { TextLabel(text = "Score", fontSize = 18.sp) },
            bestScoreText = { TextLabel(text = "$bestScore", fontSize = 36.sp) },
            bestScoreLabel = { TextLabel(text = "Best", fontSize = 18.sp) },
        )
    }
    if (isGameOver) {
        GameDialog(
            title = "Game over",
            message = "Start a new game?",
            onConfirmListener = { onNewGameRequested() },
            onDismissListener = {
                // TODO: allow user to dismiss the dialog so they can take a screenshot
            },
        )
    } else if (shouldShowNewGameDialog) {
        GameDialog(
            title = "Start a new game?",
            message = "Starting a new game will erase your current game",
            onConfirmListener = {
                onNewGameRequested()
                shouldShowNewGameDialog = false
            },
            onDismissListener = {
                shouldShowNewGameDialog = false
            },
        )
    }
}

@Composable
private fun TextLabel(
    text: String,
    fontSize: TextUnit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier,
        fontSize = fontSize,
        fontWeight = FontWeight.Light,
    )
}
