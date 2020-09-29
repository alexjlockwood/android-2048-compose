package com.alexjlockwood.twentyfortyeight

import androidx.annotation.IntRange
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

const val GRID_SIZE = 4

class GameViewModel : ViewModel() {

    var grid by mutableStateOf((0 until GRID_SIZE).map { arrayOfNulls<Int?>(GRID_SIZE).toList() })
        private set

//    var tilesToDrawImmediately by mutableStateOf<List<Tile>>(mutableListOf())
//        private set
//    var tilesToShift by mutableStateOf<List<Tile>>(mutableListOf())
//        private set
//    var tilesToShiftAndMerge by mutableStateOf<List<Tile>>(mutableListOf())
//        private set

    init {
        addRandomTileToGrid()
        addRandomTileToGrid()
    }

    fun move(direction: Direction) {
        val numRotations = when (direction) {
            Direction.WEST -> 0
            Direction.SOUTH -> 1
            Direction.EAST -> 2
            Direction.NORTH -> 3
        }
        grid = rotateGrid(grid, numRotations)

        val tileMoveInfos = mutableListOf<TileMoveInfo>()
        val inverseNumRotations = floorMod(-numRotations, GRID_SIZE)

        grid = grid.mapIndexed { currentRowIndex, _ ->
            val tiles = grid[currentRowIndex].toMutableList()
            var lastSeenTileIndex: Int? = null
            var lastSeenEmptyIndex: Int? = null
            for (currentColIndex in tiles.indices) {
                val rotatedRowIndex = getRotatedRowAt(currentRowIndex, currentColIndex, numRotations)
                val rotatedColIndex = getRotatedColAt(currentRowIndex, currentColIndex, numRotations)
                val currentTileNum = tiles[currentColIndex]
                if (currentTileNum == null) {
                    // We are looking at an empty cell in the grid.
                    if (lastSeenEmptyIndex == null) {
                        // Keep track of the first empty index we find.
                        lastSeenEmptyIndex = currentColIndex
                    }
                } else {
                    val currentTile = Tile(rotatedRowIndex, rotatedColIndex, currentTileNum)

                    // We are looking at a tile that could either be shifted,
                    // merged, or not moved at all.
                    if (lastSeenTileIndex == null) {
                        // This is the first tile in the list that we've found.
                        if (lastSeenEmptyIndex == null) {
                            // Shift the tile to its same location.
                            tileMoveInfos.add(TileShifted(currentTile, currentTile))

                            lastSeenTileIndex = currentColIndex
                        } else {
                            // Shift the tile to the location of the furthest
                            // empty cell in the list.
                            tileMoveInfos.add(TileShifted(currentTile,
                                Tile(rotatedRowIndex,
                                    getRotatedColAt(currentRowIndex, lastSeenEmptyIndex, numRotations),
                                    currentTileNum)))

                            tiles[lastSeenEmptyIndex] = currentTileNum
                            tiles[currentColIndex] = null
                            lastSeenTileIndex = lastSeenEmptyIndex
                            lastSeenEmptyIndex++
                        }
                    } else {
                        // There is a previous tile in the list that we need to process.
                        if (tiles[lastSeenTileIndex] == currentTileNum) {
                            // Shift the tile to the location where it will be merged.
                            tileMoveInfos.add(TileShifted(currentTile,
                                Tile(rotatedRowIndex,
                                    getRotatedColAt(currentRowIndex, lastSeenTileIndex, numRotations),
                                    currentTileNum)))

                            // Merge the current tile with the previous tile.
                            tileMoveInfos.add(TileAdded(Tile(rotatedRowIndex,
                                    getRotatedColAt(currentRowIndex, lastSeenTileIndex, numRotations),
                                    currentTileNum * 2)))

                            // Merge the current tile with the previous tile.
                            tileMoveInfos.add(TileAdded(Tile(rotatedRowIndex,
                                getRotatedColAt(currentRowIndex, lastSeenTileIndex, numRotations),
                                currentTileNum * 2)))

                            tiles[lastSeenTileIndex] = currentTileNum * 2
                            tiles[currentColIndex] = null
                            lastSeenTileIndex = null
                            if (lastSeenEmptyIndex == null) {
                                lastSeenEmptyIndex = currentColIndex
                            }
                        } else {
                            if (lastSeenEmptyIndex == null) {
                                // Shift the tile to its same location.
                                tileMoveInfos.add(TileShifted(currentTile, currentTile))
                            } else {
                                // Shift the current tile towards the previous tile.
                                tileMoveInfos.add(TileShifted(currentTile,
                                    Tile(rotatedRowIndex,
                                        getRotatedColAt(currentRowIndex, lastSeenEmptyIndex, numRotations),
                                        currentTileNum)))

                                tiles[lastSeenEmptyIndex] = currentTileNum
                                lastSeenEmptyIndex++
                            }
                            lastSeenTileIndex++
                        }
                    }
                }
            }
            tiles
        }

        grid = rotateGrid(grid, floorMod(-numRotations, GRID_SIZE))

        addRandomTileToGrid()
    }

    private fun addRandomTileToGrid(): Boolean {
        val (row, col) = getRandomEmptyGridCell() ?: return false
        grid = grid.mapIndexed { r, tiles ->
            if (row != r) {
                return@mapIndexed tiles
            }
            tiles.mapIndexed { c, tile ->
                if (col != c) {
                    return@mapIndexed tile
                }
                if (Math.random() < 0.9f) 2 else 4
            }
        }
        return true
    }

    private fun getRandomEmptyGridCell(): Cell? {
        val emptyCells = mutableListOf<Cell>()
        for (r in grid.indices) {
            val cells = grid[r]
            for (c in cells.indices) {
                if (cells[c] == null) {
                    emptyCells.add(Cell(r, c))
                }
            }
        }
        return emptyCells.getOrNull((Math.random() * emptyCells.size).toInt())
    }
}

private fun <T> rotateGrid(grid: List<List<T>>, @IntRange(from = 0, to = 3) numRotations: Int): List<List<T>> {
    return grid.mapIndexed { row, rowTiles ->
        rowTiles.mapIndexed { col, _ ->
            val rotatedRow = getRotatedRowAt(row, col, numRotations)
            val rotatedCol = getRotatedColAt(row, col, numRotations)
            grid[rotatedRow][rotatedCol]
        }
    }
}

private fun getRotatedRowAt(row: Int, col: Int, @IntRange(from = 0, to = 3) numRotations: Int): Int {
    return when (numRotations) {
        0 -> row
        1 -> GRID_SIZE - 1 - col
        2 -> GRID_SIZE - 1 - row
        3 -> col
        else -> throw IllegalArgumentException("numRotations must be an integer in [0,3]")
    }
}

private fun getRotatedColAt(row: Int, col: Int, @IntRange(from = 0, to = 3) numRotations: Int): Int {
    return when (numRotations) {
        0 -> col
        1 -> row
        2 -> GRID_SIZE - 1 - col
        3 -> GRID_SIZE - 1 - row
        else -> throw IllegalArgumentException("numRotations must be an integer in [0,3]")
    }
}

sealed class TileMoveInfo
data class TileAdded(val tile: Tile) : TileMoveInfo()
data class TileShifted(val from: Tile, val to: Tile) : TileMoveInfo()
data class TileDeleted(val from: Tile, val to: Tile) : TileMoveInfo()

data class Tile(val row: Int, val col: Int, val num: Int)

private data class Cell(val row: Int, val col: Int)

private fun floorMod(x: Int, y: Int): Int {
    var r = x / y
    if (x xor y < 0 && r * y != x) {
        r--
    }
    return x - r * y
}