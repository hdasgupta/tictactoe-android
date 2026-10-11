package com.example.tictactoe.online

import com.example.tictactoe.model.Player
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Production Firebase Realtime Database implementation template.
 *
 * Database Structure:
 * /rooms
 *    /{roomId}
 *        /host: "PlayerName"
 *        /guest: "PlayerName"
 *        /currentTurn: "X"
 *        /board: ["", "", "X", "O", ...]
 *        /status: "WAITING" | "PLAYING" | "FINISHED"
 *        /rematchHost: false
 *        /rematchGuest: false
 *
 * To enable:
 * 1. Add google-services.json to the app/ directory.
 * 2. Add implementation("com.google.firebase:firebase-database-ktx:21.0.0") to app/build.gradle.kts.
 * 3. Swap SimulatedOnlineService with FirebaseOnlineService in your ViewModel or DI module.
 */
class FirebaseOnlineService : OnlineGameService {
    private val _roomState = MutableStateFlow<RoomState?>(null)
    override val roomState: StateFlow<RoomState?> = _roomState.asStateFlow()

    override suspend fun createRoom(playerName: String): String {
        // e.g. FirebaseDatabase.getInstance().getReference("rooms").child(roomId).setValue(...)
        return "123456"
    }

    override suspend fun joinRoom(roomId: String, playerName: String): Boolean {
        // e.g. Listen for ValueEventListener on room ref
        return true
    }

    override suspend fun makeMove(cellIndex: Int): Boolean {
        // e.g. roomRef.child("board").child(cellIndex.toString()).setValue(symbol)
        return true
    }

    override suspend fun requestRematch() {
        // e.g. roomRef.child("rematch").setValue(true)
    }

    override suspend fun leaveRoom() {
        _roomState.value = null
    }
}
