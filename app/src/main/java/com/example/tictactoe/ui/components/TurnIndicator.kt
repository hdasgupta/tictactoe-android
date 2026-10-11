package com.example.tictactoe.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tictactoe.model.Player
import com.example.tictactoe.ui.theme.NeonBlue
import com.example.tictactoe.ui.theme.NeonPink
import com.example.tictactoe.ui.theme.SurfaceDark
import com.example.tictactoe.ui.theme.TextLight

@Composable
fun TurnIndicator(
    currentTurn: Player,
    timerSeconds: Int,
    isTimerRunning: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(30.dp))
            .background(SurfaceDark)
            .padding(horizontal = 20.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        val symbolColor = if (currentTurn == Player.X) NeonBlue else NeonPink
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(symbolColor)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "${currentTurn.symbol}'s Turn",
            color = TextLight,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
        if (isTimerRunning) {
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "${timerSeconds}s",
                color = if (timerSeconds <= 5) NeonPink else NeonBlue,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
