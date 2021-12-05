fun main() {

    fun List<String>.parse() = map { line ->
        line.replace(" -> ", ",").split(",").map { it.toInt() }
    }.map { (x1, y1, x2, y2) ->
        listOf(x1, x2, y1, y2)
    }

    fun solve(lines: List<List<Int>>): Int {
        val array = Array(1000) { IntArray(1000) { 0 } }

        lines.forEach { (x1, x2, y1, y2) ->
            var x = x1
            var y = y1

            array[x][y]++

            while (x != x2 || y != y2) {
                if (x != x2) if (x1 < x2) x++ else x--
                if (y != y2) if (y1 < y2) y++ else y--
                array[x][y]++
            }
        }

        return array.sumOf { row -> row.count { it > 1 } }
    }

    fun part1(
        input: List<String>
    ) = solve(input.parse().filterNot { (x1, x2, y1, y2) -> x1 != x2 && y1 != y2 })

    fun part2(
        input: List<String>
    ) = solve(input.parse())

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}