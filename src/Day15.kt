fun main() {

    fun List<String>.parse() = map { line -> line.map { it.digitToInt() }.toIntArray() }.toTypedArray()

    fun Array<IntArray>.multiply(times: Int): Array<IntArray> {
        val height = size
        val width = first().size

        return Array(height * times) { row ->
            IntArray(width * times) { column ->
                val value = this[row % height][column % width] + (row / height) + (column / width)
                if (value > 9) value % 9 else value
            }
        }
    }

    fun solve(map: Array<IntArray>): Int {
        val height = map.size
        val width = map.first().size

        var isChanged = true
        val d = Array(height) { IntArray(width) { Int.MAX_VALUE } }
        d[0][0] = 0

        while (isChanged) {
            isChanged = false
            for (i in 0 until height) {
                for (j in 0 until width) {
                    if (i == 0 && j == 0) continue

                    val newValue = map[i][j] + listOf(i - 1 to j, i + 1 to j, i to j - 1, i to j + 1)
                        .filter { it.first in 0 until height && it.second in 0 until width }
                        .minOf { d[it.first][it.second] }

                    if (d[i][j] != newValue) {
                        d[i][j] = newValue
                        isChanged = true
                    }
                }
            }
        }

        return d[height - 1][width - 1]
    }

    fun part1(
        input: List<String>
    ) = solve(input.parse())

    fun part2(
        input: List<String>
    ) = solve(input.parse().multiply(5))

    val input = readInput("Day15")
    println(part1(input))
    println(part2(input))
}
