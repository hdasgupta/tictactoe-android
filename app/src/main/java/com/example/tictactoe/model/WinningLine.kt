package com.example.tictactoe.model

data class WinningLine(
    val indices: List<Int>,
    val type: Type
) {
    enum class Type {
        ROW_0, ROW_1, ROW_2,
        COL_0, COL_1, COL_2,
        DIAGONAL_MAIN, DIAGONAL_ANTI
    }
}
