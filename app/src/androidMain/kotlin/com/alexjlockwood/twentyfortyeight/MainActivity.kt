package com.alexjlockwood.twentyfortyeight

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.alexjlockwood.twentyfortyeight.repository.GameRepository
import com.alexjlockwood.twentyfortyeight.repository.USER_DATA_FILE_NAME
import getStore
import okio.Path.Companion.toPath

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val store = getStore(application.filesDir.absolutePath.toPath().resolve(USER_DATA_FILE_NAME))

        setContent {
            App(
                repository = GameRepository(store),
            )
        }
    }
}
