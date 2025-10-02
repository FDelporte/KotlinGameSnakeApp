package be.webtechie.emojisnakeapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.webtechie.emojisnakeapp.model.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SnakeGameViewModel : ViewModel() {
    companion object {
        const val GRID_SIZE = 20
        private const val GAME_SPEED = 150L // milliseconds
    }

    var snake by mutableStateOf(
        Snake(
            body = listOf(
                Position(GRID_SIZE / 2, GRID_SIZE / 2),
                Position(GRID_SIZE / 2 - 1, GRID_SIZE / 2),
                Position(GRID_SIZE / 2 - 2, GRID_SIZE / 2)
            ),
            direction = Direction.RIGHT
        )
    )
        private set

    var food by mutableStateOf(Food.random(GRID_SIZE, snake))
        private set

    var gameState by mutableStateOf<GameState>(GameState.NotStarted)
        private set

    var score by mutableStateOf(0)
        private set

    var highScore by mutableStateOf(0)
        private set

    private var currentDirection = Direction.RIGHT
    private var gameJob: Job? = null

    fun startGame() {
        resetGame()
        gameState = GameState.Running
        startGameLoop()
    }

    fun pauseGame() {
        if (gameState is GameState.Running) {
            gameState = GameState.Paused
            gameJob?.cancel()
        }
    }

    fun resumeGame() {
        if (gameState is GameState.Paused) {
            gameState = GameState.Running
            startGameLoop()
        }
    }

    fun changeDirection(newDirection: Direction) {
        if (gameState is GameState.Running) {
            // Prevent 180-degree turns
            if (newDirection.opposite() != currentDirection) {
                currentDirection = newDirection
            }
        }
    }

    private fun resetGame() {
        snake = Snake(
            body = listOf(
                Position(GRID_SIZE / 2, GRID_SIZE / 2),
                Position(GRID_SIZE / 2 - 1, GRID_SIZE / 2),
                Position(GRID_SIZE / 2 - 2, GRID_SIZE / 2)
            ),
            direction = Direction.RIGHT
        )
        currentDirection = Direction.RIGHT
        food = Food.random(GRID_SIZE, snake)
        score = 0
        gameJob?.cancel()
    }

    private fun startGameLoop() {
        gameJob = viewModelScope.launch {
            while (gameState is GameState.Running) {
                delay(GAME_SPEED)
                updateGame()
            }
        }
    }

    private fun updateGame() {
        // Check if snake eats food
        val newHead = snake.head + currentDirection.offset

        if (newHead == food.position) {
            // Grow snake
            snake = snake.grow(currentDirection)
            score += 10
            food = Food.random(GRID_SIZE, snake)
        } else {
            // Move snake
            snake = snake.move(currentDirection)
        }

        // Check collisions
        if (snake.hasCollision(GRID_SIZE)) {
            gameOver()
        }
    }

    private fun gameOver() {
        gameJob?.cancel()
        if (score > highScore) {
            highScore = score
        }
        gameState = GameState.GameOver(score)
    }

    override fun onCleared() {
        super.onCleared()
        gameJob?.cancel()
    }
}