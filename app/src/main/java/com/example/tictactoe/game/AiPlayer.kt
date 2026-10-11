package com.example.tictactoe.game

import com.example.tictactoe.model.Difficulty
import com.example.tictactoe.model.Player
import kotlin.random.Random

class AiPlayer(private val aiSymbol: Player = Player.O) {
    private val humanSymbol = aiSymbol.opponent()

    fun calculateMove(board: List<Player>, difficulty: Difficulty): Int {
        val available = TicTacToeEngine.getAvailableMoves(board)
        if (available.isEmpty()) return -1

        return when (difficulty) {
            Difficulty.EASY -> chooseRandomMove(available)
            Difficulty.MEDIUM -> chooseMediumMove(board, available)
            Difficulty.HARD -> chooseHardMove(board)
        }
    }

    private fun chooseRandomMove(available: List<Int>): Int {
        return available[Random.nextInt(available.size)]
    }

    private fun chooseMediumMove(board: List<Player>, available: List<Int>): Int {
        // 1. Can AI win immediately?
        for (index in available) {
            val simulated = board.toMutableList()
            simulated[index] = aiSymbol
            if (TicTacToeEngine.checkWin(simulated) != null) {
                return index
            }
        }

        // 2. Can human win immediately? Block!
        for (index in available) {
            val simulated = board.toMutableList()
            simulated[index] = humanSymbol
            if (TicTacToeEngine.checkWin(simulated) != null) {
                return index
            }
        }

        // 3. Take center if available (70% chance)
        if (board[4] == Player.NONE && Random.nextFloat() < 0.7f) {
            return 4
        }

        // 4. Otherwise random move
        return chooseRandomMove(available)
    }

    /**
     * Unbeatable Minimax Algorithm
     */
    private fun chooseHardMove(board: List<Player>): Int {
        var bestScore = Int.MIN_VALUE
        var bestMove = -1

        val available = TicTacToeEngine.getAvailableMoves(board)
        // If board is empty, picking a corner or center is optimal and fast
        if (available.size == 9) {
            val corners = listOf(0, 2, 6, 8, 4)
            return corners[Random.nextInt(corners.size)]
        }

        val boardCopy = board.toMutableList()
        for (index in available) {
            boardCopy[index] = aiSymbol
            val score = minimax(boardCopy, depth = 0, isMaximizing = false, alpha = Int.MIN_VALUE, beta = Int.MAX_VALUE)
            boardCopy[index] = Player.NONE

            if (score > bestScore) {
                bestScore = score
                bestMove = index
            }
        }

        return if (bestMove != -1) bestMove else chooseRandomMove(available)
    }

    private fun minimax(
        board: MutableList<Player>,
        depth: Int,
        isMaximizing: Boolean,
        alpha: Int,
        beta: Int
    ): Int {
        var currentAlpha = alpha
        var currentBeta = beta

        val winResult = TicTacToeEngine.checkWin(board)
        if (winResult != null) {
            val winner = board[winResult.indices[0]]
            return if (winner == aiSymbol) (10 - depth) else (depth - 10)
        }

        if (TicTacToeEngine.isDraw(board)) {
            return 0
        }

        val available = TicTacToeEngine.getAvailableMoves(board)

        if (isMaximizing) {
            var maxEval = Int.MIN_VALUE
            for (index in available) {
                board[index] = aiSymbol
                val eval = minimax(board, depth + 1, false, currentAlpha, currentBeta)
                board[index] = Player.NONE
                maxEval = maxOf(maxEval, eval)
                currentAlpha = maxOf(currentAlpha, eval)
                if (currentBeta <= currentAlpha) break
            }
            return maxEval
        } else {
            var minEval = Int.MAX_VALUE
            for (index in available) {
                board[index] = humanSymbol
                val eval = minimax(board, depth + 1, true, currentAlpha, currentBeta)
                board[index] = Player.NONE
                minEval = minOf(minEval, eval)
                currentBeta = minOf(currentBeta, eval)
                if (currentBeta <= currentAlpha) break
            }
            return minEval
        }
    }
}
