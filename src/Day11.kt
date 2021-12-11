import java.util.*

fun main() {

    fun List<String>.parse() = Array(size) { rowIndex ->
        IntArray(this[rowIndex].length) { columnIndex ->
            this[rowIndex][columnIndex].digitToInt()
        }
    }

    fun Array<IntArray>.iterate() {
        val height = size
        val width = first().size

        val flashes = LinkedList<Pair<Int, Int>>()

        indices.forEach { rowIndex ->
            first().indices.forEach { columnIndex ->
                this[rowIndex][columnIndex]++
                if (this[rowIndex][columnIndex] >= 10) {
                    this[rowIndex][columnIndex] = 0
                    flashes.add(Pair(rowIndex, columnIndex))
                }
            }
        }

        while (flashes.isNotEmpty()) {
            val position = flashes.removeFirst()

            val wave = listOf(
                Pair(position.first + 1, position.second),
                Pair(position.first + 1, position.second + 1),
                Pair(position.first + 1, position.second - 1),
                Pair(position.first, position.second + 1),
                Pair(position.first, position.second - 1),
                Pair(position.first - 1, position.second),
                Pair(position.first - 1, position.second + 1),
                Pair(position.first - 1, position.second - 1),
            )
                .filter { it.first in 0 until height && it.second in 0 until width }
                .filterNot { this[it.first][it.second] == 0 }

            wave.forEach { (rowIndex, columnIndex) ->
                this[rowIndex][columnIndex]++
                if (this[rowIndex][columnIndex] >= 10) {
                    this[rowIndex][columnIndex] = 0
                    flashes.add(Pair(rowIndex, columnIndex))
                }
            }
        }
    }

    fun Array<IntArray>.flashCount() = sumOf { row -> row.count { it == 0 } }

    fun part1(
        input: List<String>
    ) = input.parse().let { array ->
        (1..100).fold(0) { acc, _ ->
            array.iterate()
            return@fold acc + array.flashCount()
        }
    }

    fun part2(
        input: List<String>
    ) = input.parse().let { array ->
        var step = 0
        do {
            step++
            array.iterate()
        } while (array.size * array.first().size != array.flashCount())
        return@let step
    }

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}
