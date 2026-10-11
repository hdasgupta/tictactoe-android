package com.example.tictactoe.model

sealed class GameStatus {
    object Idle : GameStatus()
    object InProgress : GameStatus()
    data class Won(val winner: Player, val winningLine: WinningLine) : GameStatus()
    object Draw : GameStatus()
}

data class Score(
    val playerX: Int = 0,
    val playerO: Int = 0,
    val draws: Int = 0
)

data class OnlinePlayerInfo(
    val id: String = "",
    val name: String = "",
    val assignedSymbol: Player = Player.NONE,
    val isReady: Boolean = false,
    val isConnected: Boolean = true
)
