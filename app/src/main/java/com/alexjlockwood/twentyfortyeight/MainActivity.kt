package com.alexjlockwood.twentyfortyeight

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Surface
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alexjlockwood.twentyfortyeight.repository.GameRepository
import com.alexjlockwood.twentyfortyeight.ui.AppTheme
import com.alexjlockwood.twentyfortyeight.ui.GameUi
import com.alexjlockwood.twentyfortyeight.viewmodel.GameViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val gameViewModel = viewModel { GameViewModel(GameRepository(application)) }
            AppTheme {
                Surface {
                    GameUi(
                        gridTileMovements = gameViewModel.gridTileMovements,
                        currentScore = gameViewModel.currentScore,
                        bestScore = gameViewModel.bestScore,
                        moveCount = gameViewModel.moveCount,
                        isGameOver = gameViewModel.isGameOver,
                        onNewGameRequested = { gameViewModel.startNewGame() },
                        onSwipeListener = { direction -> gameViewModel.move(direction) },
                    )
                }
            }
        }
    }
}
