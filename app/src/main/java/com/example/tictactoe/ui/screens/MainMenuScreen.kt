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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tictactoe.model.Difficulty
import com.example.tictactoe.model.GameMode
import com.example.tictactoe.ui.theme.DarkBackground
import com.example.tictactoe.ui.theme.NeonBlue
import com.example.tictactoe.ui.theme.NeonPink
import com.example.tictactoe.ui.theme.SurfaceDark
import com.example.tictactoe.ui.theme.TextLight
import com.example.tictactoe.ui.theme.TextMuted

@Composable
fun MainMenuScreen(
    currentDifficulty: Difficulty,
    onSelectMode: (GameMode) -> Unit,
    onSelectDifficulty: (Difficulty) -> Unit,
    onStartOnline: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "TIC TAC TOE",
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                color = NeonBlue,
                letterSpacing = 2.sp
            )
            Text(
                text = "Multiplayer & Smart AI",
                fontSize = 14.sp,
                color = TextMuted,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))

            MenuOptionCard(
                icon = Icons.Default.Person,
                title = "Play vs Computer",
                subtitle = "Challenge offline AI (${currentDifficulty.title})",
                accentColor = NeonBlue,
                onClick = { onSelectMode(GameMode.VS_COMPUTER) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // AI Difficulty Selector
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Difficulty.values().forEach { diff ->
                    val isSelected = diff == currentDifficulty
                    Button(
                        onClick = { onSelectDifficulty(diff) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) NeonBlue else SurfaceDark,
                            contentColor = if (isSelected) DarkBackground else TextLight
                        )
                    ) {
                        Text(text = diff.title, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            MenuOptionCard(
                icon = Icons.Default.Share,
                title = "Online Multiplayer",
                subtitle = "Host or Join via 6-digit room code",
                accentColor = NeonPink,
                onClick = onStartOnline
            )

            Spacer(modifier = Modifier.height(16.dp))

            MenuOptionCard(
                icon = Icons.Default.PlayArrow,
                title = "Pass & Play",
                subtitle = "Two players on one device",
                accentColor = TextLight,
                onClick = { onSelectMode(GameMode.PASS_AND_PLAY) }
            )
        }
    }
}

@Composable
fun MenuOptionCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    accentColor: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = accentColor,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextLight
                )
                Text(
                    text = subtitle,
                    fontSize = 13.sp,
                    color = TextMuted
                )
            }
        }
    }
}
