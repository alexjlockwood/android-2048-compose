package com.alexjlockwood.twentyfortyeight.viewmodel

import androidx.annotation.IntRange
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.alexjlockwood.twentyfortyeight.domain.*
import com.alexjlockwood.twentyfortyeight.repository.GameRepository
import com.google.android.material.math.MathUtils.floorMod
import kotlin.math.max

const val GRID_SIZE = 4
private const val NUM_INITIAL_TILES = 2

class GameViewModel(private val gameRepository: GameRepository) : ViewModel() {

    private var grid: List<List<Tile?>> = (0 until GRID_SIZE).map { arrayOfNulls<Tile?>(GRID_SIZE).toList() }
    var moveCount by mutableStateOf(0)
        private set
    var isGameOver by mutableStateOf(false)
        private set
    var gridTileMovements by mutableStateOf<List<GridTileMovement>>(listOf())
        private set
    var currentScore by mutableStateOf(gameRepository.currentScore)
        private set
    var bestScore by mutableStateOf(gameRepository.bestScore)
        private set


    init {
        val grid = gameRepository.grid
        if (grid == null) {
            startNewGame()
        } else {
            this.grid = grid.map { tiles -> tiles.map { if (it == null) null else Tile(it) } }
            this.gridTileMovements = grid.flatMapIndexed { row, rowTiles ->
                rowTiles.mapIndexed { col, it ->
                    if (it == null) null else GridTileMovement.noop(GridTile(Cell(row, col), Tile(it)))
                }
            }.filterNotNull()
        }
        this.currentScore = gameRepository.currentScore
        this.bestScore = gameRepository.bestScore
        this.moveCount = 0
        this.isGameOver = checkIsGameOver(this.grid)
    }

    fun startNewGame() {
        this.grid = (0 until GRID_SIZE).map { arrayOfNulls<Tile?>(GRID_SIZE).toList() }
        this.currentScore = 0
        this.gridTileMovements = addRandomTilesToGrid(NUM_INITIAL_TILES)
        this.moveCount = 0
        this.isGameOver = false
        this.gameRepository.saveState(
            this.grid.map { tiles -> tiles.map { it?.num } },
            this.currentScore,
            this.bestScore,
        )
    }

    fun move(direction: Direction) {
        val (updatedGrid, gridTileMovements) = makeMove(grid, direction)

        if (!hasGridChanged(gridTileMovements)) {
            // No move made.
            return
        }

        val scoreIncrement = gridTileMovements.filter { it.fromGridTile == null }.sumBy { it.toGridTile.tile.num }
        currentScore += scoreIncrement
        bestScore = max(bestScore, currentScore)

        this.grid = updatedGrid
        val addedTileMovements = addRandomTilesToGrid()

        this.gridTileMovements = (gridTileMovements + addedTileMovements).sortedWith { a, _ -> if (a.fromGridTile == null) 1 else -1 }
        this.moveCount++
        this.isGameOver = checkIsGameOver(grid)

        this.gameRepository.saveState(
            this.grid.map { tiles -> tiles.map { it?.num }},
            this.currentScore,
            this.bestScore,
        )
    }

    private fun addRandomTilesToGrid(numTilesToAdd: Int = 1): List<GridTileMovement> {
        val gridTileMovements = mutableListOf<GridTileMovement>()
        for (i in 0 until numTilesToAdd) {
            val addedGridTile = getRandomEmptyGridTile() ?: return gridTileMovements
            addTileToGrid(addedGridTile)
            gridTileMovements.add(GridTileMovement.add(addedGridTile))
        }
        return gridTileMovements
    }

    private fun addTileToGrid(addedGridTile: GridTile): GridTileMovement {
        val (addedCell, addedTile) = addedGridTile
        val (addedRow, addedCol) = addedCell
        grid = grid.map { row, col, it -> if (row == addedRow && col == addedCol) addedTile else it }
        return GridTileMovement.add(addedGridTile)
    }

    private fun getRandomEmptyGridTile(): GridTile? {
        val emptyCells = mutableListOf<Cell>()
        for (r in grid.indices) {
            val tiles = grid[r]
            for (c in tiles.indices) {
                if (tiles[c] == null) {
                    emptyCells.add(Cell(r, c))
                }
            }
        }
        val emptyCell = emptyCells.getOrNull((0 until emptyCells.size).random()) ?: return null
        return GridTile(emptyCell, if (Math.random() < 0.9f) Tile(2) else Tile(4))
    }
}

private fun makeMove(origGrid: List<List<Tile?>>, direction: Direction): Pair<List<List<Tile?>>, List<GridTileMovement>> {
    val numRotations = when (direction) {
        Direction.WEST -> 0
        Direction.SOUTH -> 1
        Direction.EAST -> 2
        Direction.NORTH -> 3
    }
    var grid = origGrid.rotate(numRotations)

    val gridTileMovements = mutableListOf<GridTileMovement>()

    grid = grid.mapIndexed { currentRowIndex, _ ->
        val tiles = grid[currentRowIndex].toMutableList()
        var lastSeenTileIndex: Int? = null
        var lastSeenEmptyIndex: Int? = null
        for (currentColIndex in tiles.indices) {
            val currentTile = tiles[currentColIndex]
            if (currentTile == null) {
                // We are looking at an empty cell in the grid.
                if (lastSeenEmptyIndex == null) {
                    // Keep track of the first empty index we find.
                    lastSeenEmptyIndex = currentColIndex
                }
                continue
            }

            // Otherwise, we have encountered a tile that could either be shifted,
            // merged, or not moved at all.
            val currentGridTile = GridTile(getRotatedCellAt(currentRowIndex, currentColIndex, numRotations), currentTile)

            if (lastSeenTileIndex == null) {
                // This is the first tile in the list that we've found.
                if (lastSeenEmptyIndex == null) {
                    // Keep the tile at its same location.
                    gridTileMovements.add(GridTileMovement.noop(currentGridTile))
                    lastSeenTileIndex = currentColIndex
                } else {
                    // Shift the tile to the location of the furthest empty cell in the list.
                    val targetCell = getRotatedCellAt(currentRowIndex, lastSeenEmptyIndex, numRotations)
                    val targetGridTile = GridTile(targetCell, currentTile)
                    gridTileMovements.add(GridTileMovement.shift(currentGridTile, targetGridTile))

                    tiles[lastSeenEmptyIndex] = currentTile
                    tiles[currentColIndex] = null
                    lastSeenTileIndex = lastSeenEmptyIndex
                    lastSeenEmptyIndex++
                }
            } else {
                // There is a previous tile in the list that we need to process.
                if (tiles[lastSeenTileIndex]!!.num == currentTile.num) {
                    // Shift the tile to the location where it will be merged.
                    val targetCell = getRotatedCellAt(currentRowIndex, lastSeenTileIndex, numRotations)
                    gridTileMovements.add(GridTileMovement.shift(currentGridTile, GridTile(targetCell, currentTile)))

                    // Merge the current tile with the previous tile.
                    val addedTile = currentTile * 2
                    gridTileMovements.add(GridTileMovement.add(GridTile(targetCell, addedTile)))

                    tiles[lastSeenTileIndex] = addedTile
                    tiles[currentColIndex] = null
                    lastSeenTileIndex = null
                    if (lastSeenEmptyIndex == null) {
                        lastSeenEmptyIndex = currentColIndex
                    }
                } else {
                    if (lastSeenEmptyIndex == null) {
                        // Keep the tile at its same location.
                        gridTileMovements.add(GridTileMovement.noop(currentGridTile))
                    } else {
                        // Shift the current tile towards the previous tile.
                        val targetCell = getRotatedCellAt(currentRowIndex, lastSeenEmptyIndex, numRotations)
                        val targetGridTile = GridTile(targetCell, currentTile)
                        gridTileMovements.add(GridTileMovement.shift(currentGridTile, targetGridTile))

                        tiles[lastSeenEmptyIndex] = currentTile
                        tiles[currentColIndex] = null
                        lastSeenEmptyIndex++
                    }
                    lastSeenTileIndex++
                }
            }
        }
        tiles
    }

    grid = grid.rotate(floorMod(-numRotations, GRID_SIZE))

    return Pair(grid, gridTileMovements)
}

private fun checkIsGameOver(grid: List<List<Tile?>>): Boolean {
    return !hasGridChanged(makeMove(grid, Direction.NORTH).second)
            && !hasGridChanged(makeMove(grid, Direction.SOUTH).second)
            && !hasGridChanged(makeMove(grid, Direction.EAST).second)
            && !hasGridChanged(makeMove(grid, Direction.WEST).second)
}

private fun hasGridChanged(gridTileMovements: List<GridTileMovement>): Boolean {
    return gridTileMovements.any {
        val (fromTile, toTile) = it
        fromTile == null || fromTile.cell != toTile.cell
    }
}

private fun <T> List<List<T>>.rotate(@IntRange(from = 0, to = 3) numRotations: Int): List<List<T>> {
    return map { row, col, _ ->
        val (rotatedRow, rotatedCol) = getRotatedCellAt(row, col, numRotations)
        this[rotatedRow][rotatedCol]
    }
}

private fun <T> List<List<T>>.map(transform: (row: Int, col: Int, T) -> T): List<List<T>> {
    return mapIndexed { row, rowTiles -> rowTiles.mapIndexed { col, it -> transform(row, col, it) } }
}

private fun getRotatedCellAt(row: Int, col: Int, @IntRange(from = 0, to = 3) numRotations: Int): Cell {
    return when (numRotations) {
        0 -> Cell(row, col)
        1 -> Cell(GRID_SIZE - 1 - col, row)
        2 -> Cell(GRID_SIZE - 1 - row, GRID_SIZE - 1 - col)
        3 -> Cell(col, GRID_SIZE - 1 - row)
        else -> throw IllegalArgumentException("numRotations must be an integer in [0,3]")
    }
}