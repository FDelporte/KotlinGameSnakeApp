package com.snakegame.models

data class Food(val position: Position) {
    companion object {
        fun random(gridSize: Int, snake: Snake): Food {
            var position: Position
            do {
                position = Position(
                    x = (0 until gridSize).random(),
                    y = (0 until gridSize).random()
                )
            } while (snake.body.contains(position))

            return Food(position)
        }
    }
}