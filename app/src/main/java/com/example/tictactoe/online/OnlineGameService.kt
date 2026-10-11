package com.example.tictactoe.online

import com.example.tictactoe.model.Player
import kotlinx.coroutines.flow.StateFlow

data class RoomState(
    val roomId: String = "",
    val hostPlayerName: String = "",
    val guestPlayerName: String = "",
    val isHostConnected: Boolean = true,
    val isGuestConnected: Boolean = false,
    val mySymbol: Player = Player.NONE,
    val opponentName: String = "",
    val board: List<Player> = List(9) { Player.NONE },
    val currentTurn: Player = Player.X,
    val statusText: String = "Waiting for opponent...",
    val isMatchActive: Boolean = false,
    val rematchRequestedByHost: Boolean = false,
    val rematchRequestedByGuest: Boolean = false
)

interface OnlineGameService {
    val roomState: StateFlow<RoomState?>

    suspend fun createRoom(playerName: String): String
    suspend fun joinRoom(roomId: String, playerName: String): Boolean
    suspend fun makeMove(cellIndex: Int): Boolean
    suspend fun requestRematch()
    suspend fun leaveRoom()
}
