package com.example.tictactoe.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.example.tictactoe.model.GameStatus
import com.example.tictactoe.model.Player
import com.example.tictactoe.ui.theme.AccentGold
import com.example.tictactoe.ui.theme.NeonBlue
import com.example.tictactoe.ui.theme.NeonPink
import com.example.tictactoe.ui.theme.SurfaceDark
import com.example.tictactoe.ui.theme.SurfaceLightDark

@Composable
fun TicTacToeBoard(
    board: List<Player>,
    gameStatus: GameStatus,
    onCellClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val winningIndices = (gameStatus as? GameStatus.Won)?.winningLine?.indices ?: emptyList()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(24.dp))
            .background(SurfaceDark)
            .padding(12.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            userScrollEnabled = false
        ) {
            items(9) { index ->
                val player = board[index]
                val isWinningCell = winningIndices.contains(index)

                BoardCell(
                    player = player,
                    isWinningCell = isWinningCell,
                    onClick = { onCellClicked(index) }
                )
            }
        }
    }
}

@Composable
fun BoardCell(
    player: Player,
    isWinningCell: Boolean,
    onClick: () -> Unit
) {
    val scaleAnim = remember { Animatable(0f) }

    LaunchedEffect(player) {
        if (player != Player.NONE) {
            scaleAnim.snapTo(0f)
            scaleAnim.animateTo(1f, animationSpec = tween(250))
        }
    }

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(16.dp))
            .background(if (isWinningCell) AccentGold.copy(alpha = 0.25f) else SurfaceLightDark)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize().padding(18.dp)) {
            val strokeWidth = size.width * 0.14f
            val currentScale = scaleAnim.value

            when (player) {
                Player.X -> {
                    val inset = size.width * 0.15f
                    val animatedWidth = (size.width - 2 * inset) * currentScale

                    // Diagonal 1
                    drawLine(
                        color = NeonBlue,
                        start = Offset(inset, inset),
                        end = Offset(inset + animatedWidth, inset + animatedWidth),
                        strokeWidth = strokeWidth,
                        cap = StrokeCap.Round
                    )
                    // Diagonal 2
                    drawLine(
                        color = NeonBlue,
                        start = Offset(size.width - inset, inset),
                        end = Offset(size.width - inset - animatedWidth, inset + animatedWidth),
                        strokeWidth = strokeWidth,
                        cap = StrokeCap.Round
                    )
                }
                Player.O -> {
                    val radius = (size.width * 0.35f) * currentScale
                    drawCircle(
                        color = NeonPink,
                        radius = radius,
                        style = androidx.compose.ui.graphics.drawscope.Stroke(
                            width = strokeWidth,
                            cap = StrokeCap.Round
                        )
                    )
                }
                Player.NONE -> { /* empty cell */ }
            }
        }
    }
}
