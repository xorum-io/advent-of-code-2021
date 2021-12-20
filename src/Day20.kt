fun main() {

    fun List<String>.parse(): Pair<String, Array<CharArray>> {
        val enhancer = first()
        val input = Array(size - 2) { CharArray(this[3].length) { '.' } }

        drop(2).forEachIndexed { rowIndex, row ->
            row.forEachIndexed { columnIndex, c -> input[rowIndex][columnIndex] = c }
        }

        return enhancer to input
    }

    fun Array<CharArray>.pad(padding: Int): Array<CharArray> {
        val height = size
        val width = first().size

        val output = Array(height + 2 * padding) {
            CharArray(width + 2 * padding) { '.' }
        }

        forEachIndexed { rowIndex, row ->
            row.forEachIndexed { columnIndex, c -> output[padding + rowIndex][padding + columnIndex] = c }
        }

        return output
    }

    fun Array<CharArray>.get(i: Int, j: Int, default: Char) = (getOrNull(i)?.getOrNull(j) ?: default)
        .let { if (it == '.') '0' else '1' }

    fun Array<CharArray>.enhance(enhancer: String, default: Char): Array<CharArray> {
        val output = Array(size) { CharArray(first().size) { '.' } }

        for (i in output.indices) {
            for (j in output.first().indices) {
                val top = "${get(i - 1, j - 1, default)}${get(i - 1, j, default)}${get(i - 1, j + 1, default)}"
                val middle = "${get(i, j - 1, default)}${get(i, j, default)}${get(i, j + 1, default)}"
                val bottom = "${get(i + 1, j - 1, default)}${get(i + 1, j, default)}${get(i + 1, j + 1, default)}"
                val enhancedPixelIndex = "$top$middle$bottom".toInt(radix = 2)
                output[i][j] = enhancer[enhancedPixelIndex]
            }
        }

        return output
    }

    fun solve(input: Array<CharArray>, enhancer: String, steps: Int): Int {
        var output = input.pad(steps)
        repeat(steps) {
            output = output.enhance(enhancer, default = output[0][0])
        }
        return output.sumOf { row -> row.count { it == '#' } }
    }

    fun part1(
        input: List<String>
    ) = input.parse().let { (enhancer, input) -> solve(input, enhancer, 2) }

    fun part2(
        input: List<String>
    ) = input.parse().let { (enhancer, input) -> solve(input, enhancer, 50) }

    val input = readInput("Day20")
    println(part1(input))
    println(part2(input))
}
