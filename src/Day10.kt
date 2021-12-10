import java.util.*

fun main() {

    fun part1(
        input: List<String>
    ) = input.sumOf { line ->
        val brackets = listOf(
            Triple('(', ')', 3L),
            Triple('[', ']', 57L),
            Triple('{', '}', 1197L),
            Triple('<', '>', 25137L)
        )

        val stack = LinkedList<Char>()
        line.forEach { char ->
            when (char) {
                in brackets.map { it.first } -> stack.add(char)
                in brackets.map { it.second } -> {
                    val bracket = brackets.first { it.second == char }
                    if (stack.last != bracket.first) return@sumOf bracket.third
                    stack.removeLast()
                }
            }
        }

        return@sumOf 0L
    }

    fun part2(
        input: List<String>
    ) = input.mapNotNull { line ->
        val brackets = listOf(
            Triple('(', ')', 1L),
            Triple('[', ']', 2L),
            Triple('{', '}', 3L),
            Triple('<', '>', 4L)
        )

        val stack = LinkedList<Char>()
        line.forEach { char ->
            when (char) {
                in brackets.map { it.first } -> stack.add(char)
                in brackets.map { it.second } -> {
                    val bracket = brackets.first { it.second == char }
                    if (stack.last != bracket.first) return@mapNotNull null
                    stack.removeLast()
                }
            }
        }

        return@mapNotNull stack.reversed().fold(0L) { acc, char ->
            acc * 5 + brackets.first { it.first == char }.third
        }
    }.sorted().let { array -> array[array.size / 2] }

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}
