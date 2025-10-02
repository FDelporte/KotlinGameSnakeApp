package be.webtechie.emojisnakeapp

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import be.webtechie.emojisnakeapp.model.Direction
import be.webtechie.emojisnakeapp.model.GameViewModel
import kotlin.math.abs

@Composable
fun GameScreen() {
    val model = GameViewModel
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Score display
            ScoreBoard(
                score = model.score,
                highScore = model.highScore,
                modifier = Modifier.padding(16.dp)
            )

            // Game board
            Box(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .padding(16.dp)
            ) {
                GameBoard(model = model)
            }

            // Control buttons
            GameControls(
                model = model,
                onStart = { model.startGame() },
                onPause = { model.pauseGame() },
                onResume = { model.resumeGame() },
                modifier = Modifier.padding(16.dp)
            )
        }

        // Game over dialog
        if (model.state is GameViewModel.GameState.GameOver) {
            GameOverDialog(
                score = (model.state as GameViewModel.GameState.GameOver).score,
                onRestart = { model.startGame() }
            )
        }
    }
}

@Composable
fun ScoreBoard(score: Int, highScore: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ScoreItem(label = "Score", value = score)
        ScoreItem(label = "High Score", value = highScore)
    }
}

@Composable
fun ScoreItem(label: String, value: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Gray
        )
        Text(
            text = value.toString(),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
fun GameBoard(model: GameViewModel) {
    var dragOffset by remember { mutableStateOf(Offset.Zero) }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2A2A2A))
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        val direction = when {
                            abs(dragOffset.x) > abs(dragOffset.y) -> {
                                if (dragOffset.x > 0) Direction.RIGHT else Direction.LEFT
                            }
                            else -> {
                                if (dragOffset.y > 0) Direction.DOWN else Direction.UP
                            }
                        }
                        model.changeDirection(direction)
                        dragOffset = Offset.Zero
                    },
                    onDrag = { _, dragAmount ->
                        dragOffset += dragAmount
                    }
                )
            }
    ) {
        val cellSize = size.width / GameViewModel.GRID_SIZE

        // Draw grid
        for (i in 0..GameViewModel.GRID_SIZE) {
            val offset = i * cellSize
            drawLine(
                color = Color(0xFF3A3A3A),
                start = Offset(offset, 0f),
                end = Offset(offset, size.height),
                strokeWidth = 1f
            )
            drawLine(
                color = Color(0xFF3A3A3A),
                start = Offset(0f, offset),
                end = Offset(size.width, offset),
                strokeWidth = 1f
            )
        }

        // Draw food
        drawRect(
            color = Color(0xFFFF5252),
            topLeft = Offset(
                model.food.position.x * cellSize,
                model.food.position.y * cellSize
            ),
            size = Size(cellSize, cellSize)
        )

        // Draw snake
        model.snake.body.forEachIndexed { index, position ->
            val color = if (index == 0) {
                Color(0xFF4CAF50) // Head
            } else {
                Color(0xFF66BB6A) // Body
            }

            drawRect(
                color = color,
                topLeft = Offset(
                    position.x * cellSize,
                    position.y * cellSize
                ),
                size = Size(cellSize, cellSize)
            )
        }
    }
}

@Composable
fun GameControls(
    model: GameViewModel,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        when (model.state) {
            is GameViewModel.GameState.NotStarted -> {
                Button(
                    onClick = onStart,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50)
                    )
                ) {
                    Text("Start Game", fontSize = 18.sp)
                }
            }
            is GameViewModel.GameState.Running -> {
                Button(
                    onClick = onPause,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFA726)
                    )
                ) {
                    Text("Pause", fontSize = 18.sp)
                }
            }
            is GameViewModel.GameState.Paused -> {
                Button(
                    onClick = onResume,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50)
                    )
                ) {
                    Text("Resume", fontSize = 18.sp)
                }
            }
            is GameViewModel.GameState.GameOver -> {
                // Handled by dialog
            }
        }
    }
}

@Composable
fun GameOverDialog(score: Int, onRestart: () -> Unit) {
    AlertDialog(
        onDismissRequest = {},
        title = {
            Text(
                text = "Game Over!",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text("Your score: $score")
        },
        confirmButton = {
            Button(
                onClick = onRestart,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50)
                )
            ) {
                Text("Play Again")
            }
        },
        containerColor = Color(0xFF2A2A2A),
        textContentColor = Color.White,
        titleContentColor = Color.White
    )
}