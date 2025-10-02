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
fun SnakeGame(modifier: Modifier = Modifier) {
    val viewModel = remember { GameViewModel }

    // Keyboard controls
    LaunchedEffect(Unit) {
        // This will be handled differently per platform
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF1E1E1E))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header with score and controls info
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF2D2D2D))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "🐍 Snake Game",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Score: ${viewModel.score}",
                        color = Color.White,
                        fontSize = 20.sp
                    )
                    Text(
                        "High Score: ${viewModel.highScore}",
                        color = Color(0xFFFFD700),
                        fontSize = 20.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Game status message
        when (val state = viewModel.state) {
            is GameViewModel.GameState.NotStarted -> {
                Text(
                    "Press START to play",
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
            is GameViewModel.GameState.Paused -> {
                Text(
                    "PAUSED",
                    color = Color.Yellow,
                    fontSize = 18.sp
                )
            }
            is GameViewModel.GameState.GameOver -> {
                Text(
                    "GAME OVER! Score: ${state.score}",
                    color = Color.Red,
                    fontSize = 18.sp
                )
            }
            else -> {
                Text(
                    "Use arrow keys or buttons to play",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Game canvas
        Card(
            modifier = Modifier.size(400.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Black)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cellSize = size.width / GameViewModel.GRID_SIZE

                // Draw grid (optional)
                for (i in 0..GameViewModel.GRID_SIZE) {
                    drawLine(
                        color = Color(0xFF1A1A1A),
                        start = Offset(i * cellSize, 0f),
                        end = Offset(i * cellSize, size.height),
                        strokeWidth = 1f
                    )
                    drawLine(
                        color = Color(0xFF1A1A1A),
                        start = Offset(0f, i * cellSize),
                        end = Offset(size.width, i * cellSize),
                        strokeWidth = 1f
                    )
                }

                // Draw snake
                viewModel.snake.body.forEachIndexed { index, position ->
                    val color = if (index == 0) Color.Green else Color(0xFF00CC00)
                    drawRect(
                        color = color,
                        topLeft = Offset(position.x * cellSize, position.y * cellSize),
                        size = Size(cellSize - 2, cellSize - 2)
                    )
                }

                // Draw food
                drawRect(
                    color = Color.Red,
                    topLeft = Offset(
                        viewModel.food.position.x * cellSize,
                        viewModel.food.position.y * cellSize
                    ),
                    size = Size(cellSize - 2, cellSize - 2)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Control buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    when (viewModel.state) {
                        is GameViewModel.GameState.NotStarted,
                        is GameViewModel.GameState.GameOver -> viewModel.startGame()
                        is GameViewModel.GameState.Running -> viewModel.pauseGame()
                        is GameViewModel.GameState.Paused -> viewModel.resumeGame()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text(
                    when (viewModel.state) {
                        is GameViewModel.GameState.NotStarted,
                        is GameViewModel.GameState.GameOver -> "START"
                        is GameViewModel.GameState.Running -> "PAUSE"
                        is GameViewModel.GameState.Paused -> "RESUME"
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Direction controls (touch/click)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Up button
            DirectionButton(
                text = "▲",
                onClick = { viewModel.changeDirection(Direction.UP) }
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Left button
                DirectionButton(
                    text = "◄",
                    onClick = { viewModel.changeDirection(Direction.LEFT) }
                )

                Spacer(modifier = Modifier.width(80.dp))

                // Right button
                DirectionButton(
                    text = "►",
                    onClick = { viewModel.changeDirection(Direction.RIGHT) }
                )
            }

            // Down button
            DirectionButton(
                text = "▼",
                onClick = { viewModel.changeDirection(Direction.DOWN) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Instructions
        Text(
            "Desktop: Use Arrow Keys | Mobile: Tap Buttons",
            color = Color.Gray,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun DirectionButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.size(80.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
    ) {
        Text(
            text = text,
            fontSize = 32.sp
        )
    }
}