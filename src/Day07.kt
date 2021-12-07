import kotlin.math.abs

fun main() {

    val maxPositionChange = 2000

    fun solve(
        positions: List<Int>,
        fuelBurn: List<Long>
    ): Long {
        val minPosition = positions.minOf { it }
        val maxPosition = positions.maxOf { it }

        return (minPosition..maxPosition).map { position ->
            positions.sumOf {
                val positionChange = abs(position - it)
                fuelBurn[positionChange]
            }
        }.minOf { it }
    }

    fun List<String>.parse() = first().split(",").map { it.toInt() }

    fun part1(
        input: List<String>
    ) = input.parse().let { positions ->
        val fuelBurn = LongArray(maxPositionChange) { 0 }
        for (i in fuelBurn.indices.drop(1)) {
            fuelBurn[i] = fuelBurn[i - 1] + 1
        }

        solve(positions, fuelBurn.toList())
    }

    fun part2(
        input: List<String>
    ) = input.parse().let { positions ->
        val fuelBurn = LongArray(maxPositionChange) { 0 }
        for (i in fuelBurn.indices.drop(1)) {
            fuelBurn[i] = fuelBurn[i - 1] + i
        }

        solve(positions, fuelBurn.toList())
    }

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}
