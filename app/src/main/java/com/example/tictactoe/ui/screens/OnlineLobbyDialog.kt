package com.example.tictactoe.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tictactoe.ui.theme.DarkBackground
import com.example.tictactoe.ui.theme.NeonBlue
import com.example.tictactoe.ui.theme.NeonPink
import com.example.tictactoe.ui.theme.SurfaceDark
import com.example.tictactoe.ui.theme.TextLight
import com.example.tictactoe.ui.theme.TextMuted

@Composable
fun OnlineLobbyDialog(
    onDismiss: () -> Unit,
    onCreateRoom: (playerName: String) -> Unit,
    onJoinRoom: (roomId: String, playerName: String) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var playerName by remember { mutableStateOf("Player_${(100..999).random()}") }
    var roomCode by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceDark,
        shape = RoundedCornerShape(20.dp),
        title = {
            Text(
                text = "Online Multiplayer",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextLight
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = SurfaceDark,
                    contentColor = NeonPink
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("Create Room") }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("Join Room") }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = playerName,
                    onValueChange = { playerName = it },
                    label = { Text("Your Display Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonPink,
                        focusedLabelColor = NeonPink,
                        unfocusedTextColor = TextLight,
                        focusedTextColor = TextLight
                    )
                )

                if (selectedTab == 1) {
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = roomCode,
                        onValueChange = { roomCode = it },
                        label = { Text("6-Digit Room Code") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonBlue,
                            focusedLabelColor = NeonBlue,
                            unfocusedTextColor = TextLight,
                            focusedTextColor = TextLight
                        )
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (selectedTab == 0) {
                        onCreateRoom(playerName)
                    } else {
                        if (roomCode.isNotBlank()) {
                            onJoinRoom(roomCode.trim(), playerName)
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = NeonPink),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = if (selectedTab == 0) "Create & Host" else "Join Match",
                    color = DarkBackground,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancel", color = TextMuted)
            }
        }
    )
}
