import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import be.webtechie.emojisnakeapp.SnakeGame

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Snake Game"
    ) {
        SnakeGame()
    }
}