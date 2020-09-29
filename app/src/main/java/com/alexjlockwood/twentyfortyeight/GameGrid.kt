package com.alexjlockwood.twentyfortyeight

import androidx.compose.foundation.Box
import androidx.compose.foundation.ContentGravity
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Layout
import androidx.compose.ui.Modifier
import androidx.compose.ui.drawBehind
import androidx.compose.ui.drawLayer
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Radius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.gesture.MinFlingVelocity
import androidx.compose.ui.gesture.TouchSlop
import androidx.compose.ui.gesture.dragGestureFilter
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.DensityAmbient
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlin.math.min

@Composable
fun GameGrid(
    grid: List<List<Int?>>,
    onSwipeListener: (direction: Direction) -> Unit,
) {
    val minTouchSlop = with(DensityAmbient.current) { TouchSlop.toPx() }
    val minSwipeVelocity = with(DensityAmbient.current) { MinFlingVelocity.toPx() }
    val tileMargin = with(DensityAmbient.current) { 4.dp.toPx() }

    Box(
        modifier = Modifier.fillMaxSize().dragGestureFilter(
            dragObserver = SwipeDragObserver(minTouchSlop, minSwipeVelocity, onSwipeListener),
        ),
        gravity = ContentGravity.Center,
    ) {
        var size by remember { mutableStateOf(IntSize.Zero) }
        GridLayout(
            modifier = Modifier.aspectRatio(1f).fillMaxSize().padding(16.dp).drawBehind {
                for (row in 0 until GRID_SIZE) {
                    for (col in 0 until GRID_SIZE) {
                        val tileSize = (min(size.width, size.height) - tileMargin * (GRID_SIZE - 1)) / GRID_SIZE
                        val translationX = col * (tileSize + tileMargin)
                        val translationY = row * (tileSize + tileMargin)
                        drawRoundRect(
                            color = Color.Gray,
                            topLeft = Offset(translationX, translationY),
                            size = Size(tileSize, tileSize),
                            radius = Radius(4.dp.toPx()),
                        )
                    }
                }
            },
            onSizeChanged = {
                // TODO: update this to use `Modifier.onSizeChanged` once it is merged
                size = it
            },
        ) {
            for (row in grid.indices) {
                val tiles = grid[row]
                for (col in tiles.indices) {
                    val tileSize = (min(size.width, size.height) - tileMargin * (GRID_SIZE - 1)) / GRID_SIZE
                    val translationX = col * (tileSize + tileMargin)
                    val translationY = row * (tileSize + tileMargin)

                    // TODO: handle all tile numbers
                    val tile = tiles[col] ?: continue
                    val color = when (tile) {
                        2 -> Color.Red
                        4 -> Color.Blue
                        8 -> Color.Green
                        16 -> Color.Yellow
                        32 -> Color.Cyan
                        64 -> Color.Magenta
                        else -> Color.Black
                    }

                    TileText(
                        text = "$tile",
                        modifier = Modifier.drawLayer(
                            translationX = translationX,
                            translationY = translationY,
                        ),
                        color = color,
                    )
                }
            }
        }
    }
}

@Composable
private fun GridLayout(
    modifier: Modifier = Modifier,
    onSizeChanged: (IntSize) -> Unit,
    children: @Composable () -> Unit,
) {
    val tileMargin = with(DensityAmbient.current) { 4.dp.toPx().toInt() }
    Layout(children = children, modifier = modifier) { measurables, constraints ->
        val maxWidth = constraints.maxWidth
        val maxHeight = constraints.maxHeight

        // TODO: only call this when the size has changed
        onSizeChanged(IntSize(maxWidth, maxHeight))

        val childSize = (min(maxWidth, maxHeight) - (GRID_SIZE - 1) * tileMargin) / GRID_SIZE
        val placeables = measurables.map { measurable ->
            measurable.measure(Constraints.fixed(childSize, childSize))
        }

        layout(maxWidth, maxHeight) {
            placeables.forEach { placeable ->
                placeable.place(0, 0)
            }
        }
    }
}

@Composable
private fun TileText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color,
) {
    Text(
        text = text,
        modifier = modifier.background(
            color = color,
            shape = RoundedCornerShape(4.dp),
        ).wrapContentSize(),
    )
}
