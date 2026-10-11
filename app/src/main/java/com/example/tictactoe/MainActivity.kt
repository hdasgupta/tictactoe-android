package com.example.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tictactoe.model.GameStatus
import com.example.tictactoe.ui.screens.GameOverDialog
import com.example.tictactoe.ui.screens.GameScreen
import com.example.tictactoe.ui.screens.MainMenuScreen
import com.example.tictactoe.ui.screens.OnlineLobbyDialog
import com.example.tictactoe.ui.theme.DarkBackground
import com.example.tictactoe.ui.theme.TicTacToeTheme
import com.example.tictactoe.viewmodel.TicTacToeViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicTacToeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = DarkBackground
                ) {
                    val vm: TicTacToeViewModel = viewModel()
                    TicTacToeApp(vm)
                }
            }
        }
    }
}

@Composable
fun TicTacToeApp(viewModel: TicTacToeViewModel) {
    val state by viewModel.uiState.collectAsState()
    var isPlaying by remember { mutableStateOf(false) }

    if (!isPlaying) {
        MainMenuScreen(
            currentDifficulty = state.difficulty,
            onSelectMode = { mode ->
                viewModel.setGameMode(mode)
                isPlaying = true
            },
            onSelectDifficulty = { diff ->
                viewModel.setDifficulty(diff)
            },
            onStartOnline = {
                viewModel.openOnlineLobby()
            }
        )
    } else {
        GameScreen(
            state = state,
            onCellClicked = { index -> viewModel.onCellClicked(index) },
            onResetGame = { viewModel.resetGame(preserveScore = true) },
            onBackToMenu = {
                viewModel.resetGame(preserveScore = false)
                isPlaying = false
            }
        )

        // Game Over Dialog
        if (state.gameStatus is GameStatus.Won || state.gameStatus is GameStatus.Draw) {
            GameOverDialog(
                gameStatus = state.gameStatus,
                onRematch = { viewModel.resetGame(preserveScore = true) },
                onBackToMenu = {
                    viewModel.resetGame(preserveScore = false)
                    isPlaying = false
                }
            )
        }
    }

    // Online Lobby Dialog
    if (state.isOnlineLobbyVisible) {
        OnlineLobbyDialog(
            onDismiss = { viewModel.closeOnlineLobby() },
            onCreateRoom = { playerName ->
                viewModel.createOnlineRoom(playerName)
                isPlaying = true
            },
            onJoinRoom = { roomId, playerName ->
                viewModel.joinOnlineRoom(roomId, playerName)
                isPlaying = true
            }
        )
    }
}
