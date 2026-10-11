package com.example.tictactoe.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tictactoe.model.GameMode
import com.example.tictactoe.ui.components.ScoreBoard
import com.example.tictactoe.ui.components.TicTacToeBoard
import com.example.tictactoe.ui.components.TurnIndicator
import com.example.tictactoe.ui.theme.DarkBackground
import com.example.tictactoe.ui.theme.NeonBlue
import com.example.tictactoe.ui.theme.TextLight
import com.example.tictactoe.ui.theme.TextMuted
import com.example.tictactoe.viewmodel.UiState

@Composable
fun GameScreen(
    state: UiState,
    onCellClicked: (Int) -> Unit,
    onResetGame: () -> Unit,
    onBackToMenu: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackToMenu) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = TextLight)
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = state.gameMode.displayName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextLight
                    )
                    if (state.gameMode == GameMode.ONLINE_PVP && state.onlineRoomId.isNotEmpty()) {
                        Text(
                            text = "Room: #${state.onlineRoomId}",
                            fontSize = 12.sp,
                            color = NeonBlue
                        )
                    }
                }

                IconButton(onClick = onResetGame) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = "Restart", tint = TextLight)
                }
            }

            // Scoreboard
            val p1Name = when (state.gameMode) {
                GameMode.VS_COMPUTER -> "You (X)"
                GameMode.ONLINE_PVP -> "You (${state.humanPlayer.symbol})"
                GameMode.PASS_AND_PLAY -> "Player X"
            }
            val p2Name = when (state.gameMode) {
                GameMode.VS_COMPUTER -> "AI (O)"
                GameMode.ONLINE_PVP -> state.onlineOpponentName.ifEmpty { "Opponent" }
                GameMode.PASS_AND_PLAY -> "Player O"
            }

            ScoreBoard(score = state.score, playerXLabel = p1Name, playerOLabel = p2Name)

            // Turn & Timer Banner
            TurnIndicator(
                currentTurn = state.currentTurn,
                timerSeconds = state.turnTimerSeconds,
                isTimerRunning = state.isTimerRunning
            )

            // 3x3 Grid
            TicTacToeBoard(
                board = state.board,
                gameStatus = state.gameStatus,
                onCellClicked = onCellClicked
            )

            if (state.onlineStatusText.isNotEmpty() && state.gameMode == GameMode.ONLINE_PVP) {
                Text(
                    text = state.onlineStatusText,
                    fontSize = 13.sp,
                    color = TextMuted,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            } else {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}
