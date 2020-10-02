package com.alexjlockwood.twentyfortyeight

import androidx.compose.animation.VectorConverter
import androidx.compose.animation.animatedValue
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.*
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
    tileMoveInfos: List<TileMoveInfo>,
    moveCount: Int,
    onSwipeListener: (direction: Direction) -> Unit,
) {
    val minTouchSlop = with(DensityAmbient.current) { TouchSlop.toPx() }
    val minSwipeVelocity = with(DensityAmbient.current) { MinFlingVelocity.toPx() }
    val tileMargin = with(DensityAmbient.current) { 4.dp.toPx() }

    Box(
        modifier = Modifier.fillMaxSize().dragGestureFilter(
            dragObserver = SwipeDragObserver(minTouchSlop, minSwipeVelocity, onSwipeListener),
        ),
        alignment = Alignment.Center,
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
            }.onSizeChanged { size = it },
        ) {
            val tileSize = (min(size.width, size.height) - tileMargin * (GRID_SIZE - 1)) / GRID_SIZE

            val deletedTileMoveInfos = tileMoveInfos.filterIsInstance<TileDeleted>()
            val deletedTiles = deletedTileMoveInfos.map { it.tile }
            val shiftedTileMoveInfos =
                tileMoveInfos.filterIsInstance<TileShifted>().filter { !deletedTiles.contains(it.to) }
            val addedTiles = tileMoveInfos.filterIsInstance<TileAdded>().map { it.tile }

            for (shiftedTileMoveInfo in shiftedTileMoveInfos) {
                val (fromTile, toTile) = shiftedTileMoveInfo
                val (fromRow, fromCol) = fromTile
                val (toRow, toCol, toNum) = toTile

                val fromTranslationX = fromCol * (tileSize + tileMargin)
                val toTranslationX = toCol * (tileSize + tileMargin)
                val fromTranslationY = fromRow * (tileSize + tileMargin)
                val toTranslationY = toRow * (tileSize + tileMargin)
                val fromOffset = Offset(fromTranslationX, fromTranslationY)
                val toOffset = Offset(toTranslationX, toTranslationY)

                println("===== ($fromRow, $fromCol) -> ($toRow, $toCol)")
                ShiftedTileText(
                    text = "$toNum",
                    color = getColorForTile(toTile),
                    fromOffset = fromOffset,
                    toOffset = toOffset,
                    moveCount = moveCount,
                )
            }

            for (addedTile in addedTiles) {
                val (row, col, num) = addedTile
                val translationX = col * (tileSize + tileMargin)
                val translationY = row * (tileSize + tileMargin)

                AddedTileText(
                    text = "$num",
                    modifier = Modifier.drawLayer(
                        translationX = translationX,
                        translationY = translationY,
                    ),
                    color = getColorForTile(addedTile),
                    moveCount = moveCount,
                )
            }
        }
    }
}

private fun getColorForTile(tile: Tile): Color {
    return when (tile.num) {
        2 -> Color.Red
        4 -> Color.Blue
        8 -> Color.Green
        16 -> Color.Yellow
        32 -> Color.Cyan
        64 -> Color.Magenta
        else -> Color.Black
    }
}

@Composable
private fun GridLayout(
    modifier: Modifier = Modifier,
    children: @Composable () -> Unit,
) {
    val tileMargin = with(DensityAmbient.current) { 4.dp.toPx().toInt() }
    Layout(children = children, modifier = modifier) { measurables, constraints ->
        val maxWidth = constraints.maxWidth
        val maxHeight = constraints.maxHeight

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
private fun AddedTileText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color,
    moveCount: Int,
) {
//    val alpha = animatedFloat(initVal = 0f)
    Text(
        text = text,
        modifier = modifier.background(
            color = color,//.copy(alpha = alpha.value),
            shape = RoundedCornerShape(4.dp),
        ).wrapContentSize(),
    )
//    onCommit(moveCount) {
//        alpha.animateTo(1f, TweenSpec(durationMillis = 400))
//    }
}

@Composable
private fun ShiftedTileText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color,
    fromOffset: Offset,
    toOffset: Offset,
    moveCount: Int,
) {
    val animatedOffset = animatedValue(fromOffset, Offset.VectorConverter)
    Text(
        text = text,
        modifier = modifier
            .drawLayer(
                translationX = animatedOffset.value.x,
                translationY = animatedOffset.value.y,
            ).background(
                color = color,
                shape = RoundedCornerShape(4.dp),
            ).wrapContentSize(),
    )
    onCommit(moveCount) {
        println("===== onCommit ${animatedOffset.value.x} ${animatedOffset.value.y}")
        animatedOffset.animateTo(toOffset, TweenSpec(durationMillis = 4000))
    }
}
