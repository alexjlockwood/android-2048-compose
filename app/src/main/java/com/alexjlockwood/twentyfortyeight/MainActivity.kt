package com.alexjlockwood.twentyfortyeight

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Surface
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alexjlockwood.twentyfortyeight.domain.UserData
import com.alexjlockwood.twentyfortyeight.repository.GameRepository
import com.alexjlockwood.twentyfortyeight.repository.USER_DATA_FILE_NAME
import com.alexjlockwood.twentyfortyeight.ui.AppTheme
import com.alexjlockwood.twentyfortyeight.ui.GameUi
import com.alexjlockwood.twentyfortyeight.viewmodel.GameViewModel
import io.github.xxfast.kstore.file.storeOf
import okio.Path.Companion.toPath

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val store = storeOf(
            file = application.filesDir.absolutePath.toPath().resolve(USER_DATA_FILE_NAME),
            default = UserData.EMPTY_USER_DATA,
        )
        val repository = GameRepository(store)

        setContent {
            val gameViewModel = viewModel { GameViewModel(repository) }
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
