package be.webtechie.emojisnakeapp.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.*

object GameViewModel {
    const val GRID_SIZE = 20

    // Game state
    sealed class GameState {
        data object NotStarted : GameState()
        data object Running : GameState()
        data object Paused : GameState()
        data class GameOver(val score: Int) : GameState()
    }

    var state by mutableStateOf<GameState>(GameState.NotStarted)
        private set

    var score by mutableStateOf(0)
        private set

    var highScore by mutableStateOf(0)
        private set

    var snake by mutableStateOf(Snake())
        private set

    var food by mutableStateOf(Food(Position(10, 10)))
        private set

    private var gameJob: Job? = null
    private val gameScope = CoroutineScope(Dispatchers.Default)

    fun startGame() {
        score = 0
        snake = Snake()
        food = generateFood()
        state = GameState.Running

        gameJob?.cancel()
        gameJob = gameScope.launch {
            while (state is GameState.Running) {
                delay(200) // Game speed
                moveSnake()
            }
        }
    }

    fun pauseGame() {
        if (state is GameState.Running) {
            state = GameState.Paused
        }
    }

    fun resumeGame() {
        if (state is GameState.Paused) {
            state = GameState.Running
            startGameLoop()
        }
    }

    fun changeDirection(newDirection: Direction) {
        if (state is GameState.Running) {
            snake = snake.changeDirection(newDirection)
        }
    }

    private fun moveSnake() {
        snake = snake.move()

        // Check collision with walls
        val head = snake.body.first()
        if (head.x < 0 || head.x >= GRID_SIZE || head.y < 0 || head.y >= GRID_SIZE) {
            endGame()
            return
        }

        // Check self collision
        if (snake.body.drop(1).contains(head)) {
            endGame()
            return
        }

        // Check food collision
        if (head == food.position) {
            score++
            snake = snake.grow()
            food = generateFood()
        }
    }

    private fun endGame() {
        state = GameState.GameOver(score)
        if (score > highScore) {
            highScore = score
        }
        gameJob?.cancel()
    }

    private fun generateFood(): Food {
        var position: Position
        do {
            position = Position(
                x = (0 until GRID_SIZE).random(),
                y = (0 until GRID_SIZE).random()
            )
        } while (snake.body.contains(position))
        return Food(position)
    }

    private fun startGameLoop() {
        gameJob?.cancel()
        gameJob = gameScope.launch {
            while (state is GameState.Running) {
                delay(200)
                moveSnake()
            }
        }
    }
}