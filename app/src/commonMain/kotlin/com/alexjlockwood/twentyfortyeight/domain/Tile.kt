package com.alexjlockwood.twentyfortyeight.domain

import kotlinx.serialization.Serializable

/**
 * Container class that wraps a number and a unique ID for use in the grid.
 */
@Serializable
data class Tile(val num: Int, val id: Int) {
    companion object {
        // We assign each tile a unique ID and use it to efficiently
        // animate tile objects within the compose UI.
        private var tileIdCounter = 0
    }

    constructor(num: Int) : this(num, tileIdCounter++)

    operator fun times(operand: Int): Tile = Tile(num * operand)
}
