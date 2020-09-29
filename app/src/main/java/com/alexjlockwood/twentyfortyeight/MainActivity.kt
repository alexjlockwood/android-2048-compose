package com.alexjlockwood.twentyfortyeight

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.platform.setContent

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gameViewModel by viewModels<GameViewModel>()

        setContent {
            TwentyFortyEightTheme {
                Surface(color = MaterialTheme.colors.background) {
                    GameGrid(
                        tileMoveInfos = gameViewModel.tileMoveInfos,
                        onSwipeListener = { direction -> gameViewModel.move(direction) },
                    )
                }
            }
        }
    }
}
