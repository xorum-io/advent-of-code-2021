fun main() {

    val symbolsCount = 26
    val pairsCount = symbolsCount * symbolsCount

    fun List<String>.parse(): Pair<List<Int>, Map<Int, Int>> {
        val template = first().map { it - 'A' }
        val rules = drop(2)
            .map { it.split(" -> ") }
            .associate { pair ->
                val leftChar = pair.first().first() - 'A'
                val rightChar = pair.first().last() - 'A'
                val middleChar = pair.last().first() - 'A'
                leftChar * symbolsCount + rightChar to middleChar
            }
        return Pair(template, rules)
    }

    fun solve(template: List<Int>, rules: Map<Int, Int>, steps: Int): Long {
        val symbolsRange = 0 until symbolsCount
        val pairsRange = 0 until pairsCount

        val d = Array(steps + 1) { Array(pairsCount) { LongArray(symbolsCount) { 0L } } }

        for (i in pairsRange) {
            for (j in symbolsRange) {
                d[0][i][j] += if (j == i / symbolsCount) 1L else 0L
                d[0][i][j] += if (j == i % symbolsCount) 1L else 0L
            }
        }

        for (step in 1..steps) {
            for (i in pairsRange) {
                val leftChar = i / symbolsCount
                val rightChar = i % symbolsCount
                val middleChar = rules[i] ?: continue

                val leftPair = leftChar * symbolsCount + middleChar
                val rightPair = middleChar * symbolsCount + rightChar

                for (j in symbolsRange) {
                    d[step][i][j] = d[step - 1][leftPair][j] + d[step - 1][rightPair][j]
                }

                d[step][i][middleChar] -= 1L
            }
        }

        val result = LongArray(symbolsCount) { 0L }
        template.dropLast(1).forEachIndexed { index, value ->
            val pair = value * symbolsCount + template[index + 1]
            for (i in symbolsRange) {
                result[i] += d[steps][pair][i]
            }
        }
        template.drop(1).dropLast(1).forEach { result[it] -= 1L }

        return result.filterNot { it == 0L }.maxOf { it } - result.filterNot { it == 0L }.minOf { it }
    }

    fun part1(
        input: List<String>
    ) = input.parse().let { (template, rules) -> solve(template, rules, 10) }

    fun part2(
        input: List<String>
    ) = input.parse().let { (template, rules) -> solve(template, rules, 40) }

    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}
