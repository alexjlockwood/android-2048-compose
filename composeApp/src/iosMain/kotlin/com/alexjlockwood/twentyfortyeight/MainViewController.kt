package com.alexjlockwood.twentyfortyeight

import androidx.compose.ui.window.ComposeUIViewController
import com.alexjlockwood.twentyfortyeight.domain.UserData
import com.alexjlockwood.twentyfortyeight.repository.GameRepository
import com.alexjlockwood.twentyfortyeight.repository.USER_DATA_FILE_NAME
import io.github.xxfast.kstore.file.storeOf
import okio.Path.Companion.toPath
import io.github.xxfast.kstore.file.utils.DocumentDirectory
import io.github.xxfast.kstore.utils.ExperimentalKStoreApi
import platform.Foundation.NSFileManager

@OptIn(ExperimentalKStoreApi::class)
fun MainViewController() = ComposeUIViewController {
    val filesDir = NSFileManager.defaultManager.DocumentDirectory?.relativePath!!.toPath()
    val store = storeOf(file = filesDir.resolve(USER_DATA_FILE_NAME), default = UserData.EMPTY_USER_DATA)
    App(repository = GameRepository(store))
}
