fun main() {

    fun List<String>.parse(): Pair<Set<Pair<Int, Int>>, List<Pair<Char, Int>>> {
        val dots = subList(0, indexOf(""))
            .map { it.split(",") }
            .map { Pair(it.first().toInt(), it.last().toInt()) }
            .toSet()
        val folds = subList(indexOf("") + 1, size)
            .map { it.split("=") }
            .map { Pair(it.first().last(), it.last().toInt()) }
        return Pair(dots, folds)
    }

    fun fold(
        dots: Set<Pair<Int, Int>>,
        folds: List<Pair<Char, Int>>
    ) = folds.fold(dots) { acc, fold ->
        when (fold.first) {
            'x' -> acc.mapNotNull {
                when {
                    it.first < fold.second -> it
                    it.first == fold.second -> null
                    else -> Pair(2 * fold.second - it.first, it.second)
                }
            }.toSet()
            'y' -> acc.mapNotNull {
                when {
                    it.second < fold.second -> it
                    it.second == fold.second -> null
                    else -> Pair(it.first, 2 * fold.second - it.second)
                }
            }.toSet()
            else -> acc
        }
    }

    fun part1(
        input: List<String>
    ) = input.parse().let { (dots, folds) -> fold(dots, folds.subList(0, 1)).count() }

    fun part2(
        input: List<String>
    ) = input.parse().let { (dots, folds) -> fold(dots, folds) }.let { dots ->
        val height = dots.maxOf { it.second } + 1
        val width = dots.maxOf { it.first } + 1
        val array = Array(height) { CharArray(width) { '.' } }

        dots.forEach { (x, y) -> array[y][x] = '#' }

        array.joinToString("\n") { it.joinToString("") }
    }

    val input = readInput("Day13")
    println(part1(input))
    println(part2(input))
}
