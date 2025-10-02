package com.snakegame.models

sealed class GameState {
    data object NotStarted : GameState()
    data object Running : GameState()
    data object Paused : GameState()
    data class GameOver(val score: Int) : GameState()
}