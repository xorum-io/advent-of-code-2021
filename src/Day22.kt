import java.lang.Long.max
import java.lang.Long.min

fun main() {

    val initDist = 50

    data class Command(
        val type: Int,
        val x1: Long,
        val y1: Long,
        val z1: Long,
        val x2: Long,
        val y2: Long,
        val z2: Long
    )

    fun List<String>.parse() = map { line ->
        val (type, ranges) = line.split(" ")
        val (xRange, yRange, zRange) = ranges.split(",").map { it.split("=").last() }
        val (x1, x2) = xRange.split("..").map { it.toLong() }.sorted()
        val (y1, y2) = yRange.split("..").map { it.toLong() }.sorted()
        val (z1, z2) = zRange.split("..").map { it.toLong() }.sorted()
        return@map Command(if (type == "on") 1 else -1, x1, y1, z1, x2, y2, z2)
    }

    fun List<Command>.solve(): Long {
        val cubes = mutableListOf<Command>()

        forEach { command ->
            val diffCubes = mutableListOf<Command>()
            cubes.forEach { cube ->
                val x1 = max(command.x1, cube.x1)
                val x2 = min(command.x2, cube.x2)
                val y1 = max(command.y1, cube.y1)
                val y2 = min(command.y2, cube.y2)
                val z1 = max(command.z1, cube.z1)
                val z2 = min(command.z2, cube.z2)

                if (x1 <= x2 && y1 <= y2 && z1 <= z2) {
                    diffCubes.add(Command(-cube.type, x1, y1, z1, x2, y2, z2))
                }
            }
            cubes.addAll(diffCubes)
            if (command.type > 0) cubes.add(command)
        }

        return cubes.sumOf { it.type * (it.x2 - it.x1 + 1) * (it.y2 - it.y1 + 1) * (it.z2 - it.z1 + 1) }
    }

    fun part1(
        input: List<String>
    ) = input.parse().filter { command ->
        listOf(command.x1, command.y1, command.z1, command.x2, command.y2, command.z2).all { it in -initDist..initDist }
    }.solve()

    fun part2(
        input: List<String>
    ) = input.parse().solve()

    val input = readInput("Day22")
    println(part1(input))
    println(part2(input))
}
