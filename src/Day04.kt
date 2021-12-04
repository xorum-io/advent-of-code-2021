fun main() {

    fun List<String>.parse() = Pair(
        first = first().split(',').map { it.toInt() },
        second = drop(1).chunked(6) { it.drop(1) }
            .map { board ->
                board.map { row ->
                    row.trim().split("[\\s]+".toRegex()).map { it.toInt() }
                }
            }
    )

    fun solve(numbers: List<Int>, boards: List<List<List<Int>>>): List<Pair<Int, Int>> {
        val result = mutableListOf<Pair<Int, Int>>()

        val boards = boards.map { it.map { it.toMutableList() }.toMutableList() }

        numbers.forEach { number ->
            boards.forEachIndexed { boardIndex, board ->
                if (result.any { it.first == boardIndex }) return@forEachIndexed
                board.indexOfFirst { it.contains(number) }.takeUnless { it == -1 }?.let { rowIndex ->
                    board[rowIndex].indexOf(number).takeUnless { it == -1 }?.let { columnIndex ->
                        board[rowIndex][columnIndex] = -1
                        val isRowCompleted = board[rowIndex].count { it == -1 } == board[rowIndex].count()
                        val isColumnCompleted = board.count { row -> row[columnIndex] == -1 } == board[rowIndex].count()
                        if (isRowCompleted || isColumnCompleted) {
                            result += Pair(boardIndex, number * board.flatten().sumOf { if (it == -1) 0 else it })
                        }
                    }
                }
            }
        }

        return result
    }

    fun part1(
        input: List<String>
    ): Int {
        val (numbers, boards) = input.parse()
        val result = solve(numbers, boards)
        return result.first().second
    }

    fun part2(
        input: List<String>
    ): Int {
        val (numbers, boards) = input.parse()
        val result = solve(numbers, boards)
        return result.last().second
    }

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
