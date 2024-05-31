package com.ali.appjuegos.data

data class UserData(
    var username: String,
    var email: String,
    var password: String,
    var points: Int,
    var sudokuCompleted: Int,
    var puzzleCompleted: Int,
    var memoryCompleted: Int,
    var wordSearchCompleted: Int
)