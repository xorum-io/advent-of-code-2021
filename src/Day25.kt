fun main() {

    fun List<String>.parse() = Array(size) { rowIndex -> this[rowIndex].toCharArray() }

    fun Array<CharArray>.move(): Array<CharArray> {
        val height = size
        val width = first().size

        val result = Array(height) { CharArray(width) { '.' } }

        for (i in 0 until height) {
            for (j in 0 until width) {
                if (this[i][j] == '>') {
                    if (this[i][(j + 1) % width] == '.') {
                        result[i][(j + 1) % width] = '>'
                    } else {
                        result[i][j] = '>'
                    }
                }
            }
        }

        for (i in 0 until height) {
            for (j in 0 until width) {
                if (this[i][j] == 'v') {
                    if (
                        (this[(i + 1) % height][j] == '.' && result[(i + 1) % height][j] == '.' ) ||
                        (this[(i + 1) % height][j] == '>' && result[(i + 1) % height][j] == '.')
                    ) {
                        result[(i + 1) % height][j] = 'v'
                    } else {
                        result[i][j] = 'v'
                    }
                }
            }
        }

        return result
    }

    fun part1(
        input: List<String>
    ) = input.parse().let { array ->
        var steps = 0
        var result = array

        while (true) {
            steps++
            val movedArray = result.move()
            if (movedArray.contentDeepEquals(result)) break
            result = movedArray
        }

        return@let steps
    }

    fun part2(
        input: List<String>
    ) = 0

    val input = readInput("Day25")
    println(part1(input))
    println(part2(input))
}
