package com.alexjlockwood.twentyfortyeight

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alexjlockwood.twentyfortyeight.repository.GameRepository
import com.alexjlockwood.twentyfortyeight.ui.AppTheme
import com.alexjlockwood.twentyfortyeight.ui.GameUi
import com.alexjlockwood.twentyfortyeight.viewmodel.GameViewModel

@Composable
fun App(repository: GameRepository) {
    val gameViewModel = viewModel { GameViewModel(repository) }
    AppTheme {
        Surface {
            GameUi(
                gridTileMovements = gameViewModel.gridTileMovements,
                currentScore = gameViewModel.currentScore,
                bestScore = gameViewModel.bestScore,
                isGameOver = gameViewModel.isGameOver,
                onNewGameRequest = { gameViewModel.startNewGame() },
                onSwipeListener = { direction -> gameViewModel.move(direction) },
            )
        }
    }
}
