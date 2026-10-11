package com.example.tictactoe.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tictactoe.model.GameStatus
import com.example.tictactoe.model.Player
import com.example.tictactoe.ui.theme.AccentGold
import com.example.tictactoe.ui.theme.DarkBackground
import com.example.tictactoe.ui.theme.NeonBlue
import com.example.tictactoe.ui.theme.NeonPink
import com.example.tictactoe.ui.theme.SurfaceDark
import com.example.tictactoe.ui.theme.TextLight
import com.example.tictactoe.ui.theme.TextMuted

@Composable
fun GameOverDialog(
    gameStatus: GameStatus,
    onRematch: () -> Unit,
    onBackToMenu: () -> Unit
) {
    val (title, color, message) = when (gameStatus) {
        is GameStatus.Won -> {
            val winnerSymbol = gameStatus.winner.symbol
            Triple("$winnerSymbol Won!", if (gameStatus.winner == Player.X) NeonBlue else NeonPink, "Great match!")
        }
        is GameStatus.Draw -> {
            Triple("It's a Draw!", AccentGold, "Well played by both sides.")
        }
        else -> return
    }

    AlertDialog(
        onDismissRequest = {},
        containerColor = SurfaceDark,
        shape = RoundedCornerShape(20.dp),
        title = {
            Text(
                text = title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        },
        text = {
            Column {
                Text(text = message, fontSize = 15.sp, color = TextLight)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Would you like to play another round?", fontSize = 13.sp, color = TextMuted)
            }
        },
        confirmButton = {
            Button(
                onClick = onRematch,
                colors = ButtonDefaults.buttonColors(containerColor = color),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "Play Again", color = DarkBackground, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onBackToMenu) {
                Text(text = "Main Menu", color = TextMuted)
            }
        }
    )
}
