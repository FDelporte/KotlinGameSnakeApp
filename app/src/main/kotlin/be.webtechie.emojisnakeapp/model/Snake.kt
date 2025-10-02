package be.webtechie.emojisnakeapp.model

data class Snake(
    val body: List<Position>,
    val direction: Direction = Direction.RIGHT
) {
    val head: Position get() = body.first()

    fun move(newDirection: Direction): Snake {
        val actualDirection = if (newDirection.opposite() == direction) {
            direction // Ignore opposite direction
        } else {
            newDirection
        }

        val newHead = head + actualDirection.offset
        val newBody = listOf(newHead) + body.dropLast(1)
        return copy(body = newBody, direction = actualDirection)
    }

    fun grow(newDirection: Direction): Snake {
        val actualDirection = if (newDirection.opposite() == direction) {
            direction
        } else {
            newDirection
        }

        val newHead = head + actualDirection.offset
        val newBody = listOf(newHead) + body
        return copy(body = newBody, direction = actualDirection)
    }

    fun hasCollision(gridSize: Int): Boolean {
        // Check wall collision
        if (head.x < 0 || head.x >= gridSize || head.y < 0 || head.y >= gridSize) {
            return true
        }

        // Check self collision
        return body.drop(1).contains(head)
    }
}
