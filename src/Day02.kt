fun main() {

    fun part1(
        input: List<String>
    ) = input.map { it.split(' ').let { array -> Pair(array[0], array[1].toInt()) } }
        .let { array ->
            (array.foldMove("down") - array.foldMove("up")) * array.foldMove("forward")
        }

    fun part2(
        input: List<String>
    ) = input.map { it.split(' ').let { array -> Pair(array[0], array[1].toInt()) } }
        .let { array ->
            var (aim, position, depth) = listOf(0, 0, 0)
            array.forEach { move ->
                when (move.first) {
                    "down" -> aim += move.second
                    "up" -> aim -= move.second
                    "forward" -> {
                        position += move.second
                        depth += aim * move.second
                    }
                }
            }
            return@let position * depth
        }

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}

private fun List<Pair<String, Int>>.foldMove(direction: String) = fold(0) { acc, pair ->
    if (pair.first == direction) acc + pair.second else acc
}