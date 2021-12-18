sealed class Node {

    abstract val magnitude: Int

    data class Pair(
        var left: Node,
        var right: Node
    ) : Node() {

        override val magnitude = 3 * left.magnitude + 2 * right.magnitude

        override fun toString() = "[$left,$right]"
    }

    data class Number(
        var value: Int
    ) : Node() {

        override val magnitude = value

        override fun toString() = value.toString()
    }
}

fun String.parse() = if (startsWith('[') && endsWith(']')) {
    drop(1).dropLast(1).parsePairs()
} else {
    parseNumbers()
}

private fun String.parseNumbers(): Node {
    val numbers = split(',').map { it.toInt() }
    return if (numbers.size == 1) {
        Node.Number(numbers.first())
    } else {
        Node.Pair(
            left = Node.Number(numbers.first()),
            right = Node.Number(numbers.last())
        )
    }
}

fun String.parsePairs(): Node {
    var bracketsParity = 0
    var commaIndex = 0

    forEachIndexed { index, symbol ->
        when (symbol) {
            '[' -> bracketsParity++
            ']' -> bracketsParity--
            ',' -> if (bracketsParity == 0) commaIndex = index
        }
    }

    return Node.Pair(
        left = slice(0 until commaIndex).parse(),
        right = slice(commaIndex + 1 until length).parse()
    )
}

fun Node.reduce(): Node {
    var latestNode = this

    while (true) {
        var reducedNode = latestNode.explode(level = 0)
        if (reducedNode == latestNode) {
            reducedNode = latestNode.split()
        } else {
            val flatLatestNode = latestNode.toString()
            var flatReducedNode = reducedNode.toString()

            val explodedNodeStartIndex = flatReducedNode.indexOf("-1")
            val (left, right) = flatLatestNode.parseExplodedNode(explodedNodeStartIndex)
            flatReducedNode = flatReducedNode.explodeLeft(left)
            flatReducedNode = flatReducedNode.explodeRight(right)
            flatReducedNode = flatReducedNode.replace("-1", "0")

            reducedNode = flatReducedNode.parse()
        }
        if (reducedNode == latestNode) break
        latestNode = reducedNode
    }

    return latestNode
}

private fun String.parseExplodedNode(explodedNodeStartIndex: Int): Pair<Int, Int> {
    val explodedNodeEndIndex = explodedNodeStartIndex + substring(explodedNodeStartIndex).indexOfFirst { it == ']' } + 1
    val (left, right) = substring(
        startIndex = explodedNodeStartIndex,
        endIndex = explodedNodeEndIndex
    ).drop(1).dropLast(1).split(',').map { it.toInt() }
    return left to right
}

private fun String.explodeLeft(value: Int): String {
    val nearestLeftDigitIndex = substringBefore("-1").indexOfLast { it.isDigit() }
    return if (nearestLeftDigitIndex != -1) {
        val indexRight = nearestLeftDigitIndex
        var indexLeft = indexRight
        while (this[indexLeft - 1].isDigit()) {
            indexLeft--
        }
        return replaceRange(
            range = indexLeft..indexRight,
            replacement = (substring(indexLeft..indexRight).toInt() + value).toString()
        )
    } else this
}

private fun String.explodeRight(value: Int): String {
    val nearestRightDigitIndex = substringAfter("-1").indexOfFirst { it.isDigit() }
    return if (nearestRightDigitIndex != -1) {
        val indexLeft = indexOf("-1") + 2 + nearestRightDigitIndex
        var indexRight = indexLeft
        while (this[indexRight + 1].isDigit()) {
            indexRight++
        }
        replaceRange(
            range = indexLeft..indexRight,
            replacement = (substring(indexLeft..indexRight).toInt() + value).toString()
        )
    } else this
}

fun Node.explode(level: Int): Node = when (this) {
    is Node.Number -> this
    is Node.Pair -> {
        if (level == 4) {
            Node.Number(-1)
        } else {
            val reducedLeft = left.explode(level + 1)
            val reducedRight = if (reducedLeft == left) right.explode(level + 1) else right
            Node.Pair(left = reducedLeft, right = reducedRight)
        }
    }
}

fun Node.split(): Node = when (this) {
    is Node.Number -> splitIfNeeded()
    is Node.Pair -> {
        val reducedLeft = left.split()
        val reducedRight = if (reducedLeft == left) right.split() else right
        Node.Pair(left = reducedLeft, right = reducedRight)
    }
}

private fun Node.Number.splitIfNeeded() = if (value > 9) Node.Pair(
    left = Node.Number(value / 2),
    right = Node.Number(value / 2 + value % 2)
) else this

fun main() {

    fun List<String>.parse() = map { line -> line.parse() }

    fun part1(
        input: List<String>
    ) = input.parse().reduce { sum, node -> Node.Pair(left = sum, right = node).reduce() }.magnitude

    fun part2(
        input: List<String>
    ) = input.parse().let { nodes ->
        nodes.map { left ->
            nodes.filterNot { left == it }.map { right ->
                Node.Pair(left, right).reduce()
            }
        }.flatten().maxOf { it.magnitude }
    }

    val input = readInput("Day18")
    println(part1(input))
    println(part2(input))
}
