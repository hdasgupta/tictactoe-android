package com.example.tictactoe.model

enum class Difficulty(val title: String, val description: String) {
    EASY("Easy", "Random choices"),
    MEDIUM("Medium", "Smart blocks & winning moves"),
    HARD("Impossible", "Unbeatable Minimax AI")
}
