package be.webtechie.emojisnakeapp

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun SnakeGame() {
    var gameState by remember { mutableStateOf(GameState()) }
    var isRunning by remember { mutableStateOf(false) }

    LaunchedEffect(isRunning) {
        while (isRunning) {
            delay(100L) // Game speed
            gameState = gameState.update()
            if (gameState.isGameOver) {
                isRunning = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E1E1E))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Score: ${gameState.score}",
            color = Color.White,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Canvas(
            modifier = Modifier
                .size(400.dp)
                .background(Color.Black)
        ) {
            val cellSize = size.width / 20

            // Draw snake
            gameState.snake.forEach { point ->
                drawRect(
                    color = Color.Green,
                    topLeft = Offset(point.x * cellSize, point.y * cellSize),
                    size = Size(cellSize, cellSize)
                )
            }

            // Draw food
            drawRect(
                color = Color.Red,
                topLeft = Offset(gameState.food.x * cellSize, gameState.food.y * cellSize),
                size = Size(cellSize, cellSize)
            )
        }

        Row(
            modifier = Modifier.padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = { isRunning = true }) {
                Text("Start")
            }
            Button(onClick = {
                isRunning = false
                gameState = GameState()
            }) {
                Text("Reset")
            }
        }

        if (gameState.isGameOver) {
            Text(
                "Game Over!",
                color = Color.Red,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

fun GameState.update(): GameState {
    val head = snake.first()
    val newHead = when (direction) {
        Direction.UP -> Point(head.x, head.y - 1)
        Direction.DOWN -> Point(head.x, head.y + 1)
        Direction.LEFT -> Point(head.x - 1, head.y)
        Direction.RIGHT -> Point(head.x + 1, head.y)
    }

    // Check collision with walls or self
    if (newHead.x < 0 || newHead.x >= 20 ||
        newHead.y < 0 || newHead.y >= 20 ||
        snake.contains(newHead)) {
        return copy(isGameOver = true)
    }

    // Check if food is eaten
    val newSnake = if (newHead == food) {
        listOf(newHead) + snake
    } else {
        listOf(newHead) + snake.dropLast(1)
    }

    val newFood = if (newHead == food) {
        generateFood(newSnake)
    } else {
        food
    }

    val newScore = if (newHead == food) score + 10 else score

    return copy(
        snake = newSnake,
        food = newFood,
        score = newScore
    )
}

private fun generateFood(snake: List): Point {
    var food: Point
    do {
        food = Point((0..19).random(), (0..19).random())
    } while (snake.contains(food))
    return food
}