package com.alexjlockwood.twentyfortyeight.domain

data class Tile constructor(val num: Int, val id: Int) {
    companion object {
        private var tileIdCounter = 0
    }

    constructor(num: Int) : this(num, tileIdCounter++)

    operator fun times(operand: Int): Tile = Tile(num * operand)
}

