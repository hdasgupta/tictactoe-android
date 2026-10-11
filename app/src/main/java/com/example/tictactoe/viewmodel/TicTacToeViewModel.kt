package com.example.tictactoe.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tictactoe.game.AiPlayer
import com.example.tictactoe.game.TicTacToeEngine
import com.example.tictactoe.model.Difficulty
import com.example.tictactoe.model.GameMode
import com.example.tictactoe.model.GameStatus
import com.example.tictactoe.model.Player
import com.example.tictactoe.model.Score
import com.example.tictactoe.model.WinningLine
import com.example.tictactoe.online.OnlineGameService
import com.example.tictactoe.online.SimulatedOnlineService
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class UiState(
    val gameMode: GameMode = GameMode.VS_COMPUTER,
    val difficulty: Difficulty = Difficulty.HARD,
    val humanPlayer: Player = Player.X,
    val currentTurn: Player = Player.X,
    val board: List<Player> = List(9) { Player.NONE },
    val gameStatus: GameStatus = GameStatus.Idle,
    val score: Score = Score(),
    val turnTimerSeconds: Int = 20,
    val isTimerRunning: Boolean = false,
    val isOnlineLobbyVisible: Boolean = false,
    val onlineRoomId: String = "",
    val onlineOpponentName: String = "",
    val onlineStatusText: String = "",
    val soundEnabled: Boolean = true
)

class TicTacToeViewModel(
    val onlineService: OnlineGameService
) : ViewModel() {

    constructor() : this(SimulatedOnlineService())

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val aiPlayer = AiPlayer(aiSymbol = Player.O)
    private var timerJob: Job? = null
    private var onlineObserverJob: Job? = null

    init {
        observeOnlineService()
    }

    private fun observeOnlineService() {
        onlineObserverJob = viewModelScope.launch {
            onlineService.roomState.collect { roomState ->
                if (roomState != null && _uiState.value.gameMode == GameMode.ONLINE_PVP) {
                    val win = TicTacToeEngine.checkWin(roomState.board)
                    val draw = TicTacToeEngine.isDraw(roomState.board)
                    val status = when {
                        win != null -> GameStatus.Won(roomState.board[win.indices[0]], win)
                        draw -> GameStatus.Draw
                        roomState.isMatchActive -> GameStatus.InProgress
                        else -> GameStatus.Idle
                    }

                    _uiState.update { state ->
                        state.copy(
                            board = roomState.board,
                            currentTurn = roomState.currentTurn,
                            humanPlayer = roomState.mySymbol,
                            onlineOpponentName = roomState.opponentName,
                            onlineStatusText = roomState.statusText,
                            gameStatus = status
                        )
                    }

                    if (win != null || draw) {
                        stopTimer()
                        updateScore(win?.indices?.get(0)?.let { roomState.board[it] })
                    } else if (roomState.isMatchActive) {
                        startTurnTimer()
                    }
                }
            }
        }
    }

    fun setGameMode(mode: GameMode) {
        _uiState.update { it.copy(gameMode = mode) }
        resetGame(preserveScore = false)
    }

    fun setDifficulty(difficulty: Difficulty) {
        _uiState.update { it.copy(difficulty = difficulty) }
        if (_uiState.value.gameMode == GameMode.VS_COMPUTER) {
            resetGame(preserveScore = false)
        }
    }

    fun onCellClicked(index: Int) {
        val state = _uiState.value
        if (state.board[index] != Player.NONE) return
        if (state.gameStatus !is GameStatus.InProgress && state.gameStatus !is GameStatus.Idle) return

        when (state.gameMode) {
            GameMode.PASS_AND_PLAY -> {
                executeMove(index, state.currentTurn)
            }
            GameMode.VS_COMPUTER -> {
                if (state.currentTurn == state.humanPlayer) {
                    executeMove(index, state.humanPlayer)
                    if (_uiState.value.gameStatus is GameStatus.InProgress) {
                        triggerAiMove()
                    }
                }
            }
            GameMode.ONLINE_PVP -> {
                if (state.currentTurn == state.humanPlayer) {
                    viewModelScope.launch {
                        onlineService.makeMove(index)
                    }
                }
            }
        }
    }

    private fun executeMove(index: Int, player: Player) {
        val currentBoard = _uiState.value.board.toMutableList()
        currentBoard[index] = player

        val win = TicTacToeEngine.checkWin(currentBoard)
        val draw = TicTacToeEngine.isDraw(currentBoard)

        val nextTurn = player.opponent()

        when {
            win != null -> {
                stopTimer()
                updateScore(player)
                _uiState.update {
                    it.copy(
                        board = currentBoard,
                        gameStatus = GameStatus.Won(player, win)
                    )
                }
            }
            draw -> {
                stopTimer()
                updateScore(null)
                _uiState.update {
                    it.copy(
                        board = currentBoard,
                        gameStatus = GameStatus.Draw
                    )
                }
            }
            else -> {
                _uiState.update {
                    it.copy(
                        board = currentBoard,
                        currentTurn = nextTurn,
                        gameStatus = GameStatus.InProgress
                    )
                }
                startTurnTimer()
            }
        }
    }

    private fun triggerAiMove() {
        viewModelScope.launch {
            delay(500)
            val board = _uiState.value.board
            val aiSymbol = _uiState.value.humanPlayer.opponent()
            val aiMove = aiPlayer.calculateMove(board, _uiState.value.difficulty)
            if (aiMove != -1 && _uiState.value.gameStatus is GameStatus.InProgress) {
                executeMove(aiMove, aiSymbol)
            }
        }
    }

    private fun updateScore(winner: Player?) {
        _uiState.update { state ->
            val curScore = state.score
            val newScore = when (winner) {
                Player.X -> curScore.copy(playerX = curScore.playerX + 1)
                Player.O -> curScore.copy(playerO = curScore.playerO + 1)
                null -> curScore.copy(draws = curScore.draws + 1)
                else -> curScore
            }
            state.copy(score = newScore)
        }
    }

    private fun startTurnTimer() {
        stopTimer()
        _uiState.update { it.copy(turnTimerSeconds = 20, isTimerRunning = true) }
        timerJob = viewModelScope.launch {
            while (_uiState.value.turnTimerSeconds > 0) {
                delay(1000)
                _uiState.update { it.copy(turnTimerSeconds = it.turnTimerSeconds - 1) }
            }
            handleTimerExpired()
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
        _uiState.update { it.copy(isTimerRunning = false) }
    }

    private fun handleTimerExpired() {
        stopTimer()
        val state = _uiState.value
        if (state.gameStatus is GameStatus.InProgress) {
            val timedOutPlayer = state.currentTurn
            val winner = timedOutPlayer.opponent()
            _uiState.update {
                it.copy(gameStatus = GameStatus.Won(winner, WinningLine(emptyList(), WinningLine.Type.ROW_0)))
            }
            updateScore(winner)
        }
    }

    fun resetGame(preserveScore: Boolean = true) {
        stopTimer()
        _uiState.update {
            it.copy(
                board = List(9) { Player.NONE },
                currentTurn = Player.X,
                gameStatus = GameStatus.Idle,
                turnTimerSeconds = 20,
                score = if (preserveScore) it.score else Score()
            )
        }
        if (_uiState.value.gameMode == GameMode.ONLINE_PVP) {
            viewModelScope.launch {
                onlineService.requestRematch()
            }
        }
    }

    fun openOnlineLobby() {
        _uiState.update { it.copy(isOnlineLobbyVisible = true) }
    }

    fun closeOnlineLobby() {
        _uiState.update { it.copy(isOnlineLobbyVisible = false) }
    }

    fun createOnlineRoom(playerName: String) {
        viewModelScope.launch {
            val roomId = onlineService.createRoom(playerName)
            _uiState.update {
                it.copy(
                    gameMode = GameMode.ONLINE_PVP,
                    onlineRoomId = roomId,
                    isOnlineLobbyVisible = false
                )
            }
            resetGame(preserveScore = false)
        }
    }

    fun joinOnlineRoom(roomId: String, playerName: String) {
        viewModelScope.launch {
            val success = onlineService.joinRoom(roomId, playerName)
            if (success) {
                _uiState.update {
                    it.copy(
                        gameMode = GameMode.ONLINE_PVP,
                        onlineRoomId = roomId,
                        isOnlineLobbyVisible = false
                    )
                }
                resetGame(preserveScore = false)
            }
        }
    }

    fun toggleSound() {
        _uiState.update { it.copy(soundEnabled = !it.soundEnabled) }
    }
}
