package com.alexjlockwood.twentyfortyeight.repository

import com.alexjlockwood.twentyfortyeight.domain.Tile
import com.alexjlockwood.twentyfortyeight.domain.UserData
import io.github.xxfast.kstore.KStore

const val USER_DATA_FILE_NAME = "user_data.json"

/**
 * Repository class that persists the current 2048 game to KStore.
 */
class GameRepository(private val store: KStore<UserData>) {

    suspend fun fetch(): UserData {
        return store.get() ?: UserData.EMPTY_USER_DATA
    }

    suspend fun update(grid: List<List<Tile?>>, currentScore: Int, bestScore: Int) {
        store.set(UserData(grid, currentScore, bestScore))
    }
}
