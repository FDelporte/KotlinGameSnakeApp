import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.input.key.*
import androidx.compose.ui.window.rememberWindowState
import androidx.compose.ui.unit.dp
import be.webtechie.emojisnakeapp.SnakeGame
import be.webtechie.emojisnakeapp.model.Direction
import be.webtechie.emojisnakeapp.model.GameViewModel

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "🐍 Snake Game",
        state = rememberWindowState(width = 600.dp, height = 1000.dp),
        onKeyEvent = { keyEvent ->
            if (keyEvent.type == KeyEventType.KeyDown) {
                when (keyEvent.key) {
                    Key.DirectionUp, Key.W -> {
                        GameViewModel.changeDirection(Direction.UP)
                        true
                    }
                    Key.DirectionDown, Key.S -> {
                        GameViewModel.changeDirection(Direction.DOWN)
                        true
                    }
                    Key.DirectionLeft, Key.A -> {
                        GameViewModel.changeDirection(Direction.LEFT)
                        true
                    }
                    Key.DirectionRight, Key.D -> {
                        GameViewModel.changeDirection(Direction.RIGHT)
                        true
                    }
                    Key.Spacebar -> {
                        when (GameViewModel.state) {
                            is GameViewModel.GameState.NotStarted,
                            is GameViewModel.GameState.GameOver -> GameViewModel.startGame()
                            is GameViewModel.GameState.Running -> GameViewModel.pauseGame()
                            is GameViewModel.GameState.Paused -> GameViewModel.resumeGame()
                        }
                        true
                    }
                    else -> false
                }
            } else {
                false
            }
        }
    ) {
        SnakeGame()
    }
}