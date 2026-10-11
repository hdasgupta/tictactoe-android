package com.example.tictactoe.model

enum class Player(val symbol: String) {
    X("X"),
    O("O"),
    NONE("");

    fun opponent(): Player = when (this) {
        X -> O
        O -> X
        NONE -> NONE
    }
}
