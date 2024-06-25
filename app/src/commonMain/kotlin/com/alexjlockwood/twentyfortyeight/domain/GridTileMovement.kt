package com.alexjlockwood.twentyfortyeight.domain

/**
 * Container class describing how a tile has moved within the grid.
 */
data class GridTileMovement(val fromGridTile: GridTile?, val toGridTile: GridTile) {
    companion object {
        /**
         * Creates a [GridTileMovement] describing a tile that has been added to the grid.
         */
        fun add(gridTile: GridTile): GridTileMovement {
            return GridTileMovement(null, gridTile)
        }

        /**
         * Creates a [GridTileMovement] describing a tile that has shifted to a different location in the grid.
         */
        fun shift(fromGridTile: GridTile, toGridTile: GridTile): GridTileMovement {
            return GridTileMovement(fromGridTile, toGridTile)
        }

        /**
         * Creates a [GridTileMovement] describing a tile that has not moved in the grid.
         */
        fun noop(gridTile: GridTile): GridTileMovement {
            return GridTileMovement(gridTile, gridTile)
        }
    }
}