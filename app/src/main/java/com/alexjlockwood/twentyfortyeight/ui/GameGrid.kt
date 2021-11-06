package com.alexjlockwood.twentyfortyeight.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alexjlockwood.twentyfortyeight.domain.GridTileMovement
import com.alexjlockwood.twentyfortyeight.viewmodel.GRID_SIZE
import com.alexjlockwood.twentyfortyeight.R
import kotlin.math.min

private val GRID_TILE_RADIUS = 4.dp

/**
 * Renders a grid of tiles that animates when game moves are made.
 */
@Composable
fun GameGrid(
    modifier: Modifier = Modifier,
    gridTileMovements: List<GridTileMovement>,
    moveCount: Int,
) {
    BoxWithConstraints(modifier) {
        val width = with(LocalDensity.current) { maxWidth.toPx() }
        val height = with(LocalDensity.current) { maxHeight.toPx() }
        val tileMarginPx = with(LocalDensity.current) { 4.dp.toPx() }
        val tileSizePx =
            ((min(
                width,
                height
            ) - tileMarginPx * (GRID_SIZE - 1)) / GRID_SIZE).coerceAtLeast(0f)
        val tileSizeDp = Dp(tileSizePx / LocalDensity.current.density)
        val tileOffsetPx = tileSizePx + tileMarginPx
        val emptyTileColor = colorResource(id = getEmptyTileColor(isSystemInDarkTheme()))
        Box(
            modifier = Modifier.drawBehind {
                // Draw the background empty tiles.
                for (row in 0 until GRID_SIZE) {
                    for (col in 0 until GRID_SIZE) {
                        drawRoundRect(
                            color = emptyTileColor,
                            topLeft = Offset(col * tileOffsetPx, row * tileOffsetPx),
                            size = Size(tileSizePx, tileSizePx),
                            cornerRadius = CornerRadius(GRID_TILE_RADIUS.toPx()),
                        )
                    }
                }
            }
        ) {
            for (gridTileMovement in gridTileMovements) {
                // Each grid tile is laid out at (0,0) in the box. Shifting tiles are then translated
                // to their correct position in the grid, and added tiles are scaled from 0 to 1.
                val (fromGridTile, toGridTile) = gridTileMovement
                val fromScale = if (fromGridTile == null) 0f else 1f
                val toOffset =
                    Offset(
                        toGridTile.cell.col * tileOffsetPx,
                        toGridTile.cell.row * tileOffsetPx
                    )
                val fromOffset = fromGridTile?.let {
                    Offset(
                        it.cell.col * tileOffsetPx,
                        it.cell.row * tileOffsetPx
                    )
                }
                    ?: toOffset

                // In 2048, tiles are frequently being removed and added to the grid. As a result,
                // the order in which grid tiles are rendered is constantly changing after each
                // recomposition. In order to ensure that each tile animates from its correct
                // starting position, it is critical that we assign each tile a unique ID using
                // the key() function.
                key(toGridTile.tile.id) {
                    GridTileText(
                        num = toGridTile.tile.num,
                        size = tileSizeDp,
                        fromScale = fromScale,
                        fromOffset = fromOffset,
                        toOffset = toOffset,
                        moveCount = moveCount,
                    )
                }
            }
        }
    }
}

@Composable
private fun GridTileText(
    num: Int,
    size: Dp,
    fromScale: Float,
    fromOffset: Offset,
    toOffset: Offset,
    moveCount: Int,
) {
    val animatedScale = remember { Animatable(fromScale) }
    val animatedOffset = remember { Animatable(fromOffset, Offset.VectorConverter) }
    //val animationScope = rememberCoroutineScope()


    Text(
        text = "$num",
        modifier = Modifier
            .size(size)
            .graphicsLayer(
                scaleX = animatedScale.value,
                scaleY = animatedScale.value,
                translationX = animatedOffset.value.x,
                translationY = animatedOffset.value.y,
            )
            .background(
                color = getTileColor(num, isSystemInDarkTheme()),
                shape = RoundedCornerShape(GRID_TILE_RADIUS),
            )
            .wrapContentSize(),
        color = Color.White,
        fontSize = 18.sp,
    )

    LaunchedEffect(moveCount) {
        animatedScale.snapTo(if (moveCount == 0) 1f else fromScale)
        animatedScale.animateTo(1f, tween(durationMillis = 100, delayMillis = 30))

        animatedOffset.animateTo(toOffset, tween(durationMillis = 100))
    }
}

private fun getTileColor(num: Int, isDarkTheme: Boolean): Color {
    return when (num) {
        2 -> Color(if (isDarkTheme) 0xff4e6cef else 0xff50c0e9)
        4 -> Color(if (isDarkTheme) 0xff3f51b5 else 0xff1da9da)
        8 -> Color(if (isDarkTheme) 0xff8e24aa else 0xffcb97e5)
        16 -> Color(if (isDarkTheme) 0xff673ab7 else 0xffb368d9)
        32 -> Color(if (isDarkTheme) 0xffc00c23 else 0xffff5f5f)
        64 -> Color(if (isDarkTheme) 0xffa80716 else 0xffe92727)
        128 -> Color(if (isDarkTheme) 0xff0a7e07 else 0xff92c500)
        256 -> Color(if (isDarkTheme) 0xff056f00 else 0xff7caf00)
        512 -> Color(if (isDarkTheme) 0xffe37c00 else 0xffffc641)
        1024 -> Color(if (isDarkTheme) 0xffd66c00 else 0xffffa713)
        2048 -> Color(if (isDarkTheme) 0xffcf5100 else 0xffff8a00)
        4096 -> Color(if (isDarkTheme) 0xff80020a else 0xffcc0000)
        8192 -> Color(if (isDarkTheme) 0xff303f9f else 0xff0099cc)
        16384 -> Color(if (isDarkTheme) 0xff512da8 else 0xff9933cc)
        else -> Color.Black
    }
}

private fun getEmptyTileColor(isDarkTheme: Boolean): Int {
    return if (isDarkTheme) {
        R.color.md_theme_dark_card_background
    } else {
        R.color.md_theme_light_card_background
    }
}
