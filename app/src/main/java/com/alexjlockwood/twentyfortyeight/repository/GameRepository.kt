package com.alexjlockwood.twentyfortyeight.repository

import android.content.Context
import android.provider.Settings.Global.getString
import com.alexjlockwood.twentyfortyeight.R
import com.alexjlockwood.twentyfortyeight.domain.Tile
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.games.Games
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

private const val KEY_SHARED_PREFS = "key_shared_prefs"
private const val KEY_GRID = "key_grid"
private const val KEY_CURRENT_SCORE = "key_current_score"
private const val KEY_BEST_SCORE = "key_best_score"

/**
 * Repository class that persists the current 2048 game to shared preferences.
 */
class GameRepository(private val context: Context) {

    private val sharedPrefs = context.getSharedPreferences(KEY_SHARED_PREFS, Context.MODE_PRIVATE)

    var grid: List<List<Int?>>? = sharedPrefs.getString(KEY_GRID, null)?.let { Gson().fromJson(it) }
        private set

    var currentScore: Int = sharedPrefs.getInt(KEY_CURRENT_SCORE, 0)
        private set

    var bestScore: Int = sharedPrefs.getInt(KEY_BEST_SCORE, 0)
        private set

    fun saveState(grid: List<List<Tile?>>, currentScore: Int, bestScore: Int) {
        this.grid = grid.map { tiles -> tiles.map { it?.num } }
        this.currentScore = currentScore
        this.bestScore = bestScore
        sharedPrefs.edit()
            .putString(KEY_GRID, Gson().toJson(this.grid))
            .putInt(KEY_CURRENT_SCORE, currentScore)
            .putInt(KEY_BEST_SCORE, bestScore)
            .apply()

        saveLeaderboard(context, currentScore.toLong())
    }

    private fun saveLeaderboard(context: Context, score: Long) {
        if (score < 0) return

        GoogleSignIn.getLastSignedInAccount(context)?.let {
            Games.getLeaderboardsClient(context, it)
                .submitScore(context.getString(R.string.leaderboard), score)
        }
    }
}

private inline fun <reified T> Gson.fromJson(json: String) =
    fromJson<T>(json, object : TypeToken<T>() {}.type)
