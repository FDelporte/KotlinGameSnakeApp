package be.webtechie.emojisnakeapp.model

object GameViewModel {
    var score by mutableStateOf(0)
    var gameOver by mutableStateOf(false)
    var highScore by mutableStateOf(0)
    data object NotStarted : GameState()
    data object Running : GameState()
    data object Paused : GameState()
    data class GameOver(val score: Int) : GameState()

    fun resetGame() {
        score = 0
        gameOver = false
    }
}