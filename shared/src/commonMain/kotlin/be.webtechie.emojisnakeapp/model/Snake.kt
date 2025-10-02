package be.webtechie.emojisnakeapp.model

data class Snake(
    val body: List<Position> = listOf(Position(10, 10)),
    val direction: Direction = Direction.RIGHT
) {
    val head: Position get() = body.first()

    fun move(): Snake {
        val head = body.first()
        val newHead = when (direction) {
            Direction.UP -> Position(head.x, head.y - 1)
            Direction.DOWN -> Position(head.x, head.y + 1)
            Direction.LEFT -> Position(head.x - 1, head.y)
            Direction.RIGHT -> Position(head.x + 1, head.y)
        }
        return copy(
            body = listOf(newHead) + body.dropLast(1),
            direction
        )
    }

    fun grow(): Snake {
        val head = body.first()
        val newHead = when (direction) {
            Direction.UP -> Position(head.x, head.y - 1)
            Direction.DOWN -> Position(head.x, head.y + 1)
            Direction.LEFT -> Position(head.x - 1, head.y)
            Direction.RIGHT -> Position(head.x + 1, head.y)
        }
        return copy(body = listOf(newHead) + body)
    }

    fun hasCollision(gridSize: Int): Boolean {
        // Check wall collision
        if (head.x < 0 || head.x >= gridSize || head.y < 0 || head.y >= gridSize) {
            return true
        }

        // Check self collision
        return body.drop(1).contains(head)
    }

    fun changeDirection(newDirection: Direction): Snake {
        // Prevent reversing direction (can't go directly opposite)
        if (isOppositeDirection(newDirection)) {
            return this
        }
        return copy(direction = newDirection)
    }

    private fun isOppositeDirection(newDirection: Direction): Boolean {
        return when (direction) {
            Direction.UP -> newDirection == Direction.DOWN
            Direction.DOWN -> newDirection == Direction.UP
            Direction.LEFT -> newDirection == Direction.RIGHT
            Direction.RIGHT -> newDirection == Direction.LEFT
        }
    }
}
