fun main() {

    fun List<String>.parse() = map { it.split("-") }
        .map { Pair(it.first(), it.last()) }
        .let { edges ->
            val map = mutableMapOf<String, MutableSet<String>>()
            edges.forEach { edge ->
                map[edge.first] = (map[edge.first] ?: HashSet()).apply { add(edge.second) }
                map[edge.second] = (map[edge.second] ?: HashSet()).apply { add(edge.first) }
            }
            return@let map
        }

    fun dfs(map: Map<String, Set<String>>, path: List<String>, tolerance: Int): Long {
        val nodes = map[path.last()] ?: return 0
        return nodes.sumOf { node ->
            if (node == "end") return@sumOf 1
            if (node == "start") return@sumOf 0
            if (node.all { it.isLowerCase() } && path.contains(node)) {
                if (tolerance == 0) return@sumOf 0
                else return@sumOf dfs(map, path + node, tolerance - 1)
            }
            dfs(map, path + node, tolerance)
        }
    }

    fun part1(
        input: List<String>
    ) = dfs(map = input.parse(), path = listOf("start"), tolerance = 0)

    fun part2(
        input: List<String>
    ) = dfs(map = input.parse(), path = listOf("start"), tolerance = 1)

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}
