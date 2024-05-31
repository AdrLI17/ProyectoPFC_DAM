package com.ali.appjuegos.data

object LocalDatabase {
    var userData: UserData? = null
    private val users = mutableListOf<UserData>()

    init {
        users.addAll(
            listOf(
                UserData("user1", "user1@example.com", "password1", 100, 5, 3, 2, 1),
                UserData("user2", "user2@example.com", "password2", 200, 10, 6, 4, 2),
                UserData("user3", "user3@example.com", "password3", 300, 15, 9, 6, 3)
            )
        )
    }

    fun login(username: String, password: String): Boolean {
        for (user in users) {
            if (user.username == username && user.password == password) {
                userData = user
                return true
            }
        }
        return false
    }

    fun register(username: String, email: String, password: String): Boolean {
        // Simular registro fallido si el usuario ya existe en la lista de predefinidos
        for (user in users) {
            if (user.username == username) {
                return false
            }
        }
        // Simular el registro exitoso agregando el nuevo usuario a una lista adicional
        userData = UserData(username, email, password, 0, 0, 0, 0, 0)
        return true
    }

    fun addPoints(points: Int) {
        userData?.let {
            it.points += points
        }
    }

    fun incrementGameCount(gameType: String) {
        userData?.let {
            when (gameType) {
                "sudoku" -> it.sudokuCompleted += 1
                "puzzle" -> it.puzzleCompleted += 1
                "memory" -> it.memoryCompleted += 1
                "wordSearch" -> it.wordSearchCompleted += 1
            }
        }
    }

    fun getStats(): String {
        return userData?.let {
            """
            Usuario: ${it.username}
            Puntos: ${it.points}
            Sudokus completados: ${it.sudokuCompleted}
            Rompecabezas completados: ${it.puzzleCompleted}
            Memory completados: ${it.memoryCompleted}
            Sopa de letras completadas: ${it.wordSearchCompleted}
            """.trimIndent()
        } ?: "No hay datos del usuario"
    }

    fun getAllUsers(): List<UserData> {
        return users
    }
}
