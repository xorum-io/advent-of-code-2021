fun main() {
    fun part1(input: List<String>) = input.map { it.toInt() }
        .let { array ->
            array.drop(1).foldIndexed(0) { index, acc, value ->
                if (array[index] < value) acc + 1 else acc
            }
        }

    fun part2(input: List<String>) = input.map { it.toInt() }
        .let { array ->
            array.drop(3).foldIndexed(0) { index, acc, value ->
                val leftTriple = array[index] + array[index + 1] + array[index + 2]
                val rightTriple = array[index + 1] + array[index + 2] + value
                if (leftTriple < rightTriple) acc + 1 else acc
            }
        }

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
