package com.alexjlockwood.twentyfortyeight

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.alexjlockwood.twentyfortyeight.domain.UserData
import com.alexjlockwood.twentyfortyeight.repository.GameRepository
import com.alexjlockwood.twentyfortyeight.repository.USER_DATA_FILE_NAME
import io.github.xxfast.kstore.file.storeOf
import okio.Path.Companion.toPath

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val filesDir = application.filesDir.absolutePath.toPath()
        val store = storeOf(file = filesDir.resolve(USER_DATA_FILE_NAME), default = UserData.EMPTY_USER_DATA)

        setContent {
            App(
                repository = GameRepository(store),
            )
        }
    }
}
