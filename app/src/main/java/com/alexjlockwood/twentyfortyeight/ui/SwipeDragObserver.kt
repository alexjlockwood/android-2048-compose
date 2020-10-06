package com.alexjlockwood.twentyfortyeight.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.gesture.DragObserver
import com.alexjlockwood.twentyfortyeight.domain.Direction
import kotlin.math.sqrt

/**
 * A [DragObserver] that listens for swipe events in each of the 4 directions.
 */
class SwipeDragObserver(
    private val minTouchSlop: Float,
    private val minSwipeVelocity: Float,
    private val onSwipeListener: (direction: Direction) -> Unit,
) : DragObserver {
    private var totalDragDistance: Offset = Offset.Zero

    override fun onStart(downPosition: Offset) {
        totalDragDistance = Offset.Zero
    }

    override fun onDrag(dragDistance: Offset): Offset {
        totalDragDistance += dragDistance
        return Offset.Zero
    }

    override fun onStop(velocity: Offset) {
        val (dx, dy) = totalDragDistance
        val swipeDistance = dist(dx, dy)
        if (swipeDistance < minTouchSlop) {
            return
        }

        val (vx, vy) = velocity
        val swipeVelocity = dist(vx, vy)
        if (swipeVelocity < minSwipeVelocity) {
            return
        }

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
