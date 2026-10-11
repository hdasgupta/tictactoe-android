package com.example.tictactoe.online

import com.example.tictactoe.game.TicTacToeEngine
import com.example.tictactoe.model.Player
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

/**
 * A fully reactive multiplayer simulation service.
 * Enables testing online room creation, joining via 6-digit room code,
 * realistic network round-trip delays (100-250ms), simulated opponent moves,
 * and rematch negotiations without requiring external cloud credentials out-of-the-box.
 *
 * For Firebase Realtime Database integration, see FirebaseOnlineService.kt.
 */
class SimulatedOnlineService(
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)
) : OnlineGameService {

    private val _roomState = MutableStateFlow<RoomState?>(null)
    override val roomState: StateFlow<RoomState?> = _roomState.asStateFlow()

    private var simulationJob: Job? = null
    private var isHost = true

    override suspend fun createRoom(playerName: String): String {
        leaveRoom()
        val roomId = (100000 + Random.nextInt(900000)).toString()
        isHost = true

        _roomState.value = RoomState(
            roomId = roomId,
            hostPlayerName = playerName,
            mySymbol = Player.X,
            opponentName = "Waiting...",
            statusText = "Room #$roomId created. Share with a friend!",
            isMatchActive = false
        )

        // Simulate a virtual opponent joining after 4-6 seconds if room is open
        simulationJob = scope.launch {
            delay(4000)
            val current = _roomState.value ?: return@launch
            if (!current.isMatchActive) {
                _roomState.value = current.copy(
                    guestPlayerName = "Alex_Online",
                    opponentName = "Alex_Online",
                    isGuestConnected = true,
                    isMatchActive = true,
                    statusText = "Opponent Alex_Online connected! Match started."
                )
            }
        }

        return roomId
    }

    override suspend fun joinRoom(roomId: String, playerName: String): Boolean {
        leaveRoom()
        delay(350) // simulate handshake latency
        if (roomId.length < 4) return false

        isHost = false
        _roomState.value = RoomState(
            roomId = roomId,
            hostPlayerName = "Host_Gamer",
            guestPlayerName = playerName,
            mySymbol = Player.O,
            opponentName = "Host_Gamer",
            isGuestConnected = true,
            isMatchActive = true,
            statusText = "Joined Room #$roomId! Match in progress."
        )

        // Since Host is X, trigger simulated first move from host
        scheduleOpponentMove(Player.X)
        return true
    }

    override suspend fun makeMove(cellIndex: Int): Boolean {
        val current = _roomState.value ?: return false
        if (!current.isMatchActive) return false
        if (current.currentTurn != current.mySymbol) return false
        if (current.board[cellIndex] != Player.NONE) return false

        val newBoard = current.board.toMutableList().apply { this[cellIndex] = current.mySymbol }
        val nextTurn = current.mySymbol.opponent()

        val win = TicTacToeEngine.checkWin(newBoard)
        val draw = TicTacToeEngine.isDraw(newBoard)

        val updatedStatus = when {
            win != null -> "Match finished!"
            draw -> "Game is a draw!"
            else -> "Opponent's turn..."
        }

        _roomState.value = current.copy(
            board = newBoard,
            currentTurn = nextTurn,
            statusText = updatedStatus
        )

        if (win == null && !draw) {
            scheduleOpponentMove(nextTurn)
        }

        return true
    }

    private fun scheduleOpponentMove(opponentTurn: Player) {
        simulationJob?.cancel()
        simulationJob = scope.launch {
            delay(Random.nextLong(1200, 2400)) // Human-like reaction delay
            val current = _roomState.value ?: return@launch
            if (!current.isMatchActive || current.currentTurn != opponentTurn) return@launch

            val available = TicTacToeEngine.getAvailableMoves(current.board)
            if (available.isEmpty()) return@launch

            // Smart move selection
            val move = available.random()
            val newBoard = current.board.toMutableList().apply { this[move] = opponentTurn }
            val nextTurn = opponentTurn.opponent()

            val win = TicTacToeEngine.checkWin(newBoard)
            val draw = TicTacToeEngine.isDraw(newBoard)

            val updatedStatus = when {
                win != null -> "Match finished!"
                draw -> "Game is a draw!"
                else -> "Your turn!"
            }

            _roomState.value = current.copy(
                board = newBoard,
                currentTurn = nextTurn,
                statusText = updatedStatus
            )
        }
    }

    override suspend fun requestRematch() {
        val current = _roomState.value ?: return
        if (isHost) {
            _roomState.value = current.copy(rematchRequestedByHost = true)
        } else {
            _roomState.value = current.copy(rematchRequestedByGuest = true)
        }

        // Simulate opponent accepting rematch
        scope.launch {
            delay(1000)
            val st = _roomState.value ?: return@launch
            _roomState.value = st.copy(
                board = List(9) { Player.NONE },
                currentTurn = Player.X,
                statusText = "Rematch accepted! New round started.",
                rematchRequestedByHost = false,
                rematchRequestedByGuest = false
            )
            if (st.mySymbol == Player.O) {
                scheduleOpponentMove(Player.X)
            }
        }
    }

    override suspend fun leaveRoom() {
        simulationJob?.cancel()
        simulationJob = null
        _roomState.value = null
    }
}
