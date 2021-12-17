import java.lang.Integer.max
import java.lang.Integer.min

fun main() {

    data class Area(
        val x1: Int,
        val y1: Int,
        val x2: Int,
        val y2: Int
    )

    data class Shot(
        val xVelocity: Int,
        val yVelocity: Int,
        val maxY: Int
    )

    fun List<String>.parse() = first()
        .removePrefix("target area: ")
        .split(", ")
        .map { it.removeRange(0..1).split("..") }
        .flatten()
        .map { it.toInt() }
        .let {
            Area(
                x1 = min(it[0], it[1]),
                y1 = min(it[2], it[3]),
                x2 = max(it[0], it[1]),
                y2 = max(it[2], it[3])
            )
        }

    fun Area.shoot(xVelocity: Int, yVelocity: Int): Shot? {
        var x = 0
        var y = 0
        var maxY = 0

        var xVel = xVelocity
        var yVel = yVelocity

        while (x < x2 && y > y1) {
            x += xVel
            xVel = max(0, xVel - 1)

            y += yVel
            yVel -= 1

            maxY = max(maxY, y)
            if (x in x1..x2 && y in y1..y2) {
                return Shot(xVelocity, yVelocity, maxY)
            }
        }

        return null
    }

    fun Area.solve() = (0..1000).map { xVelocity ->
        (-1000..1000).mapNotNull { yVelocity ->
            shoot(xVelocity, yVelocity)
        }
    }.flatten()

    fun part1(
        input: List<String>
    ) = input.parse().solve().maxOf { it.maxY }

    fun part2(
        input: List<String>
    ) = input.parse().solve().size

    val input = readInput("Day17")
    println(part1(input))
    println(part2(input))
}
