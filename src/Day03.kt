fun main() {

    fun part1(
        input: List<String>
    ) = input.first().mapIndexed { index, _ -> input.map { it[index] }.joinToString(separator = "").count { it == '1' } }
        .let { ones ->
            val gamma = ones.map { if (it > input.size / 2) '1' else '0' }.joinToString(separator = "").toInt(2)
            val epsilon = ones.map { if (it <= input.size / 2) '1' else '0' }.joinToString(separator = "").toInt(2)
            gamma * epsilon
        }

    fun List<String>.reduce(
        index: Int,
        leftBit: Char,
        rightBit: Char
    ): Int {
        if (size == 1) return first().toInt(2)
        val oneCount = map { it[index] }.joinToString(separator = "").count { it == '1' }
        val bitCriteria = if (oneCount >= size - oneCount) leftBit else rightBit
        return filter { it[index] == bitCriteria }.reduce(index + 1, leftBit, rightBit)
    }

    fun part2(
        input: List<String>
    ): Int {
        val oxygen = input.reduce(index = 0, leftBit = '1', rightBit = '0')
        val co2 = input.reduce(index = 0, leftBit = '0', rightBit = '1')
        return oxygen * co2
    }

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
