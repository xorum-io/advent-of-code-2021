import java.util.*

fun main() {

    data class Point(
        val row: Int,
        val column: Int,
        val value: Int
    )

    fun List<String>.parse() = map { row -> row.map { it.digitToInt() } }

    fun List<List<Int>>.getOrMaxInt(
        rowIndex: Int,
        columnIndex: Int
    ) = getOrElse(rowIndex) { emptyList() }.getOrElse(columnIndex) { Int.MAX_VALUE }

    fun List<List<Int>>.getOrMinInt(
        rowIndex: Int,
        columnIndex: Int
    ) = getOrElse(rowIndex) { emptyList() }.getOrElse(columnIndex) { Int.MIN_VALUE }.takeUnless { it == 9 }
        ?: Int.MIN_VALUE

    fun List<List<Int>>.findLowestPoints() = mapIndexed { rowIndex, row ->
        row.mapIndexedNotNull { columnIndex, value ->
            if (value < getOrMaxInt(rowIndex - 1, columnIndex)
                && value < getOrMaxInt(rowIndex + 1, columnIndex)
                && value < getOrMaxInt(rowIndex, columnIndex - 1)
                && value < getOrMaxInt(rowIndex, columnIndex + 1)
            ) {
                Point(rowIndex, columnIndex, value)
            } else null
        }
    }.flatten()

    fun List<List<Int>>.findBasin(lowestPoint: Point): List<Point> {
        val visited = Array(size) { BooleanArray(first().size) { false } }
        val basin = mutableListOf<Point>()

        val queue = LinkedList<Point>()
        queue.add(lowestPoint)
        basin.add(lowestPoint)
        visited[lowestPoint.row][lowestPoint.column] = true

        while (queue.isNotEmpty()) {
            val point = queue.removeFirst()

            listOf(
                Point(point.row - 1, point.column, getOrMinInt(point.row - 1, point.column)),
                Point(point.row + 1, point.column, getOrMinInt(point.row + 1, point.column)),
                Point(point.row, point.column - 1, getOrMinInt(point.row, point.column - 1)),
                Point(point.row, point.column + 1, getOrMinInt(point.row, point.column + 1))
            )
                .filter { point.value < getOrMinInt(it.row, it.column) && !visited[it.row][it.column] }
                .forEach {
                    queue.add(it)
                    basin.add(it)
                    visited[it.row][it.column] = true
                }
        }

        return basin
    }

    fun part1(
        input: List<String>
    ) = input.parse().let { array -> array.findLowestPoints().sumOf { it.value + 1 } }

    fun part2(
        input: List<String>
    ) = input.parse().let { array ->
        array.findLowestPoints()
            .map { array.findBasin(it).count() }
            .sortedDescending()
            .take(3)
            .reduce { acc, value -> acc * value }
    }

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}
