package com.alexjlockwood.twentyfortyeight.domain

data class GridTileMovement(val fromGridTile: GridTile?, val toGridTile: GridTile) {
    companion object {
        fun add(gridTile: GridTile): GridTileMovement {
            return GridTileMovement(null, gridTile)
        }

        fun shift(fromGridTile: GridTile, toGridTile: GridTile): GridTileMovement {
            return GridTileMovement(fromGridTile, toGridTile)
        }

        fun noop(gridTile: GridTile): GridTileMovement {
            return GridTileMovement(gridTile, gridTile)
        }
    }
}