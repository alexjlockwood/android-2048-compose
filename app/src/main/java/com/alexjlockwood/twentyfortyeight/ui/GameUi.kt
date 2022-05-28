package com.alexjlockwood.twentyfortyeight.ui

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import com.alexjlockwood.twentyfortyeight.R
import com.alexjlockwood.twentyfortyeight.domain.Direction
import com.alexjlockwood.twentyfortyeight.domain.GridTileMovement
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.games.Games.getAchievementsClient
import com.google.android.gms.games.Games.getLeaderboardsClient
import kotlin.math.sqrt

/**
 * Renders the 2048 game's home screen UI.
 */
@ExperimentalMaterial3Api
@Composable
fun GameUi(
    gridTileMovements: List<GridTileMovement>,
    currentScore: Int,
    bestScore: Int,
    moveCount: Int,
    isGameOver: Boolean,
    onNewGameRequested: () -> Unit,
    onSwipeListener: (direction: Direction) -> Unit,
    context: Context,
    startActivity: (intent: Intent) -> Unit
) {
    var shouldShowNewGameDialog by remember { mutableStateOf(false) }
    var totalDragDistance = remember { Offset.Zero }
    val minTouchSlop = 18.dp

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    shouldShowNewGameDialog = true
                }) {
                Icon(Icons.Default.Refresh, stringResource(R.string.accessibility_new_game))
            }
        }
    ) {
        BoxWithConstraints {
            val isPortrait = maxWidth < maxHeight

            ConstraintLayout(
                constraintSet = buildConstraints(isPortrait = isPortrait),
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(moveCount) {
                        detectDragGestures(
                            onDragStart = {
                                totalDragDistance = Offset.Zero
                            },
                            onDrag = { _, offset ->
                                totalDragDistance += offset
                            },
                            onDragCancel = {
                                totalDragDistance = Offset.Zero
                            },
                            onDragEnd = {
                                endDrag(totalDragDistance, minTouchSlop, onSwipeListener)
                            }
                        )
                    }
            ) {
                Image(
                    painterResource(id = getImageBackground(isSystemInDarkTheme())),
                    contentDescription = "",
                    Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.FillHeight
                )

                GameGrid(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .padding(16.dp)
                        .layoutId("gameGrid"),
                    gridTileMovements = gridTileMovements,
                    moveCount = moveCount,
                )
                TextLabel(text = "$currentScore", layoutId = "currentScoreText", fontSize = 36.sp)
                TextLabel(
                    text = stringResource(R.string.label_score),
                    layoutId = "currentScoreLabel",
                    fontSize = 18.sp
                )

                TextLabel(text = "$bestScore", layoutId = "bestScoreText", fontSize = 36.sp)
                TextLabel(
                    text = stringResource(R.string.label_best),
                    layoutId = "bestScoreLabel",
                    fontSize = 18.sp
                )
                Button(onClick = {
                    showLeaderBoard(context = context, startActivity)
                }, modifier = Modifier.layoutId("ratingBtn")) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(text = "Rating")
                }
                Button(onClick = {
                    showAchievements(context = context, startActivity)
                }, modifier = Modifier.layoutId("achievementsBtn")) {
                    Icon(
                        imageVector = Icons.Default.Face,
                        contentDescription = "Achievements",
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(text = "Achievements")
                }
            }
        }
    }

    if (isGameOver) {
        GameDialog(
            title = stringResource(R.string.dialog_game_over_title),
            message = stringResource(R.string.dialog_game_over_description),
            onConfirmListener = { onNewGameRequested.invoke() },
            onDismissListener = {
                shouldShowNewGameDialog = false
            },
        )
    } else if (shouldShowNewGameDialog) {
        GameDialog(
            title = stringResource(R.string.dialog_new_game_title),
            message = stringResource(R.string.dialog_new_game_description),
            onConfirmListener = {
                onNewGameRequested.invoke()
                shouldShowNewGameDialog = false
            },
            onDismissListener = {
                shouldShowNewGameDialog = false
            },
        )
    }
}

@Composable
private fun TextLabel(text: String, layoutId: String, fontSize: TextUnit) {
    Text(
        text = text,
        modifier = Modifier.layoutId(layoutId),
        fontSize = fontSize,
        fontWeight = FontWeight.Light,
    )
}

private fun buildConstraints(isPortrait: Boolean): ConstraintSet {
    return ConstraintSet {
        val gameGrid = createRefFor("gameGrid")
        val currentScoreText = createRefFor("currentScoreText")
        val currentScoreLabel = createRefFor("currentScoreLabel")
        val bestScoreText = createRefFor("bestScoreText")
        val bestScoreLabel = createRefFor("bestScoreLabel")
        val refresh = createRefFor("refresh")
        val ratingBtn = createRefFor("ratingBtn")
        val achievementsBtn = createRefFor("achievementsBtn")

        if (isPortrait) {
            constrain(gameGrid) {
                start.linkTo(parent.start)
                top.linkTo(achievementsBtn.bottom, 24.dp)
                end.linkTo(parent.end)
            }
            constrain(currentScoreText) {
                start.linkTo(gameGrid.start, 16.dp)
                top.linkTo(gameGrid.bottom)
            }
            constrain(currentScoreLabel) {
                start.linkTo(currentScoreText.start)
                top.linkTo(currentScoreText.bottom)
            }
            constrain(bestScoreText) {
                end.linkTo(gameGrid.end, 16.dp)
                top.linkTo(gameGrid.bottom)
            }
            constrain(bestScoreLabel) {
                end.linkTo(bestScoreText.end)
                top.linkTo(bestScoreText.bottom)
            }
            constrain(refresh) {
                end.linkTo(parent.end)
                start.linkTo(parent.start)
                top.linkTo(bestScoreLabel.bottom)
            }
            constrain(ratingBtn) {
                start.linkTo(parent.start, 16.dp)
                top.linkTo(parent.top, 24.dp)
            }
            constrain(achievementsBtn) {
                end.linkTo(parent.end, 16.dp)
                top.linkTo(parent.top, 24.dp)
            }

        } else {
            constrain(gameGrid) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }
            constrain(currentScoreText) {
                start.linkTo(currentScoreLabel.start)
                bottom.linkTo(currentScoreLabel.top)
            }
            constrain(currentScoreLabel) {
                start.linkTo(bestScoreText.start)
                bottom.linkTo(bestScoreText.top)
            }
            constrain(bestScoreText) {
                start.linkTo(bestScoreLabel.start)
                bottom.linkTo(bestScoreLabel.top)
            }
            constrain(bestScoreLabel) {
                start.linkTo(gameGrid.end)
                bottom.linkTo(refresh.top)
            }
            constrain(refresh) {
                start.linkTo(gameGrid.end)
                bottom.linkTo(gameGrid.bottom, 16.dp)
            }
            constrain(ratingBtn) {
                start.linkTo(parent.start, 24.dp)
                top.linkTo(parent.top, 16.dp)
            }
            constrain(achievementsBtn) {
                start.linkTo(parent.start, 24.dp)
                top.linkTo(ratingBtn.bottom, 8.dp)
            }
            createHorizontalChain(gameGrid, bestScoreLabel, chainStyle = ChainStyle.Packed)
        }
    }
}

private fun dist(x: Float, y: Float): Float {
    return sqrt(x * x + y * y)
}

private fun atan2(x: Float, y: Float): Float {
    var degrees = Math.toDegrees(kotlin.math.atan2(y, x).toDouble()).toFloat()
    if (degrees < 0) {
        degrees += 360
    }
    return degrees
}

private fun endDrag(
    totalDragDistance: Offset,
    minTouchSlop: Dp,
    onSwipeListener: (direction: Direction) -> Unit
) {
    val (dx, dy) = totalDragDistance
    val swipeDistance = dist(dx, dy)
    if (swipeDistance >= minTouchSlop.value) {

        val swipeAngle = atan2(dx, -dy)
        onSwipeListener(
            when {
                45 <= swipeAngle && swipeAngle < 135 -> Direction.NORTH
                135 <= swipeAngle && swipeAngle < 225 -> Direction.WEST
                225 <= swipeAngle && swipeAngle < 315 -> Direction.SOUTH
                else -> Direction.EAST
            }
        )
    }
}

private fun getImageBackground(isDarkTheme: Boolean): Int {
    return if (isDarkTheme) {
        R.drawable.background_night
    } else {
        R.drawable.background
    }
}

private fun showLeaderBoard(context: Context, startActivity: (intent: Intent) -> Unit) {
    GoogleSignIn.getLastSignedInAccount(context)?.let {
        getLeaderboardsClient(context, it)
            .getLeaderboardIntent(context.getString(R.string.leaderboard))
            .addOnSuccessListener { intent ->
                startActivity.invoke(intent)
            }
    }
}

private fun showAchievements(context: Context, startActivity: (intent: Intent) -> Unit) {
    GoogleSignIn.getLastSignedInAccount(context)?.let {
        getAchievementsClient(context, it)
            .achievementsIntent
            .addOnSuccessListener { intent ->
                startActivity.invoke(intent)
            }
    }
}