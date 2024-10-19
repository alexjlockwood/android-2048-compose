package com.alexjlockwood.twentyfortyeight.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alexjlockwood.twentyfortyeight.domain.GridTileMovement
import com.alexjlockwood.twentyfortyeight.viewmodel.GRID_SIZE

/**
 * Renders a grid of tiles that animates when game moves are made.
 */
@Composable
fun GameGrid(
    gridTileMovements: List<GridTileMovement>,
    modifier: Modifier = Modifier,
    gridSize: Dp = 320.dp,
    tileMargin: Dp = 4.dp,
    tileRadius: Dp = 4.dp,
) {
    val tileSize = ((gridSize - tileMargin * (GRID_SIZE - 1)) / GRID_SIZE).coerceAtLeast(0.dp)
    val tileOffset = with(LocalDensity.current) { (tileSize + tileMargin).toPx() }
    val emptyTileColor = getEmptyTileColor(isSystemInDarkTheme())
    Box(
        modifier = modifier
            .size(gridSize)
            .drawBehind {
                // Draw the background empty tiles.
                for (row in 0 until GRID_SIZE) {
                    for (col in 0 until GRID_SIZE) {
                        drawRoundRect(
                            color = emptyTileColor,
                            topLeft = Offset(col * tileOffset, row * tileOffset),
                            size = Size(tileSize.toPx(), tileSize.toPx()),
                            cornerRadius = CornerRadius(tileRadius.toPx()),
                        )
                    }
                }
            },
    ) {
        for (gridTileMovement in gridTileMovements) {
            val (num, id) = gridTileMovement.toGridTile.tile
            // In 2048, tiles are frequently being removed and added to the grid. As a result,
            // the order in which grid tiles are rendered is constantly changing after each
            // recomposition. In order to ensure that each tile animates from its correct
            // starting position, it is critical that we assign each tile a unique ID using
            // the key() function.
            key(id) {
                GridTileText(
                    modifier = Modifier.animateGridTilePlacement(gridTileMovement, tileOffset),
                    num = num,
                    tileSize = tileSize,
                    tileRadius = tileRadius,
                    tileColor = getTileColor(num, isSystemInDarkTheme()),
                )
            }
        }
    }
}

@Composable
private fun GridTileText(
    num: Int,
    modifier: Modifier = Modifier,
    tileSize: Dp = 80.dp,
    fontSize: TextUnit = 24.sp,
    tileRadius: Dp = 4.dp,
    tileColor: Color = Color.Black,
    fontColor: Color = Color.White,
) {
    Text(
        text = "$num",
        modifier = modifier
            .background(tileColor, RoundedCornerShape(tileRadius))
            .size(tileSize)
            .wrapContentSize(),
        color = fontColor,
        fontSize = fontSize,
    )
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

private fun getEmptyTileColor(isDarkTheme: Boolean): Color {
    return Color(if (isDarkTheme) 0xff444444 else 0xffdddddd)
}
