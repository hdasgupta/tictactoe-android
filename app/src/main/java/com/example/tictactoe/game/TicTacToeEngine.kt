package com.example.tictactoe.game

import com.example.tictactoe.model.Player
import com.example.tictactoe.model.WinningLine

object TicTacToeEngine {
    const val BOARD_SIZE = 9

    val WINNING_COMBINATIONS = listOf(
        WinningLine(listOf(0, 1, 2), WinningLine.Type.ROW_0),
        WinningLine(listOf(3, 4, 5), WinningLine.Type.ROW_1),
        WinningLine(listOf(6, 7, 8), WinningLine.Type.ROW_2),
        WinningLine(listOf(0, 3, 6), WinningLine.Type.COL_0),
        WinningLine(listOf(1, 4, 7), WinningLine.Type.COL_1),
        WinningLine(listOf(2, 5, 8), WinningLine.Type.COL_2),
        WinningLine(listOf(0, 4, 8), WinningLine.Type.DIAGONAL_MAIN),
        WinningLine(listOf(2, 4, 6), WinningLine.Type.DIAGONAL_ANTI)
    )

    fun checkWin(board: List<Player>): WinningLine? {
        for (combo in WINNING_COMBINATIONS) {
            val p = board[combo.indices[0]]
            if (p != Player.NONE &&
                p == board[combo.indices[1]] &&
                p == board[combo.indices[2]]
            ) {
                return combo
            }
        }
        return null
    }

    fun isDraw(board: List<Player>): Boolean {
        return checkWin(board) == null && board.none { it == Player.NONE }
    }

    fun getAvailableMoves(board: List<Player>): List<Int> {
        return board.indices.filter { board[it] == Player.NONE }
    }
}
