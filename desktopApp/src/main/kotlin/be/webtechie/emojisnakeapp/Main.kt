import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import be.webtechie.emojisnakeapp.GameScreen

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Snake Game"
    ) {
        GameScreen()
    }
}