package com.alexjlockwood.twentyfortyeight.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.node.LayoutModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.unit.Constraints
import com.alexjlockwood.twentyfortyeight.domain.Cell
import com.alexjlockwood.twentyfortyeight.domain.GridTileMovement
import kotlinx.coroutines.launch

fun Modifier.animateGridTilePlacement(gridTileMovement: GridTileMovement, offset: Float): Modifier {
    return this then AnimateGridTilePlacementElement(gridTileMovement, offset)
}

private data class AnimateGridTilePlacementElement(
    private val gridTileMovement: GridTileMovement,
    private val offset: Float,
) : ModifierNodeElement<AnimateGridTilePlacementNode>() {

    override fun create(): AnimateGridTilePlacementNode {
        return AnimateGridTilePlacementNode(gridTileMovement, offset)
    }

    override fun update(node: AnimateGridTilePlacementNode) {
        node.update(gridTileMovement.toGridTile.cell, offset)
    }
}

private class AnimateGridTilePlacementNode(
    gridTileMovement: GridTileMovement,
    offset: Float,
) : Modifier.Node(),
    LayoutModifierNode {

    // Each grid tile is laid out at (0,0) in the box. Shifting tiles are then translated
    // to their correct position in the grid, and added tiles are scaled from 0 to 1.
    private val initialCell = with(gridTileMovement) { fromGridTile?.cell ?: toGridTile.cell }
    private var currentOffset = with(initialCell) { Offset(col * offset, row * offset) }
    private val animatedOffset = Animatable(currentOffset, Offset.VectorConverter)
    private val animatedScale = Animatable(if (gridTileMovement.fromGridTile == null) 0f else 1f)

    override fun onAttach() {
        move(currentOffset)
    }

    private fun move(targetOffset: Offset) {
        currentOffset = targetOffset
        coroutineScope.apply {
            launch { animatedOffset.animateTo(targetOffset, tween(100)) }
            launch { animatedScale.animateTo(1f, tween(200, 50)) }
        }
    }

    fun update(cell: Cell, offset: Float) {
        val targetOffset = with(cell) { Offset(col * offset, row * offset) }
        if (currentOffset != targetOffset) {
            move(targetOffset)
        }
    }

    override fun MeasureScope.measure(measurable: Measurable, constraints: Constraints): MeasureResult {
        val placeable = measurable.measure(constraints)
        return layout(placeable.width, placeable.height) {
            placeable.placeWithLayer(
                x = 0,
                y = 0,
            ) {
                scaleX = animatedScale.value
                scaleY = animatedScale.value
                translationX = animatedOffset.value.x
                translationY = animatedOffset.value.y
            }
        }
    }
}
