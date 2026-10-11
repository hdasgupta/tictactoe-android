package com.example.tictactoe.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tictactoe.model.Score
import com.example.tictactoe.ui.theme.NeonBlue
import com.example.tictactoe.ui.theme.NeonPink
import com.example.tictactoe.ui.theme.SurfaceDark
import com.example.tictactoe.ui.theme.TextMuted

@Composable
fun ScoreBoard(
    score: Score,
    playerXLabel: String = "Player X",
    playerOLabel: String = "Player O",
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceDark)
            .padding(vertical = 12.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ScoreItem(label = playerXLabel, count = score.playerX, color = NeonBlue)
        ScoreItem(label = "Ties", count = score.draws, color = TextMuted)
        ScoreItem(label = playerOLabel, count = score.playerO, color = NeonPink)
    }
}

@Composable
fun ScoreItem(label: String, count: Int, color: androidx.compose.ui.graphics.Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, color = TextMuted, fontSize = 12.sp, fontWeight = FontWeight.Medium)
        Text(text = "$count", color = color, fontSize = 22.sp, fontWeight = FontWeight.Bold)
    }
}
