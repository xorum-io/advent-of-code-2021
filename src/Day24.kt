fun main() {

    fun List<String>.parse() = chunked(18).map { chunk ->
        listOf(chunk[4], chunk[5], chunk[15]).map { line ->
            line.split(" ").last().toInt()
        }.let { (divZ, addX, addY) ->
            Triple(divZ, addX, addY)
        }
    }

    fun calculateZ(w: Int, prevZ: Int, divZ: Int, addX: Int, addY: Int): Int {
        var z = prevZ

        var x = z
        x %= 26
        z /= divZ
        x += addX
        x = if (x != w) 1 else 0
        var y = 25
        y *= x
        y += 1
        z *= y
        y = w
        y += addY
        y *= x
        z += y

        return z
    }

    fun List<Triple<Int, Int, Int>>.calculateLimits(): Array<Map<Int, Set<Int>>> {
        val limits = Array<MutableMap<Int, MutableSet<Int>>>(size) { mutableMapOf() }
        var zSet = setOf(0)

        reversed().forEachIndexed { index, instruction ->
            val zValidSet = mutableSetOf<Int>()
            for (w in 1..9) {
                for (z in 0..10000000) {
                    if (calculateZ(w, z, instruction.first, instruction.second, instruction.third) in zSet) {
                        limits[lastIndex - index].getOrPut(w) { mutableSetOf() }.add(z)
                        zValidSet.add(z)
                    }
                }
            }
            zSet = zValidSet
        }

        return Array(limits.size) { limits[it] }
    }

    fun List<Triple<Int, Int, Int>>.calculateSerials(
        limits: Array<Map<Int, Set<Int>>>,
        index: Int,
        prevZ: Int
    ): List<String> = if (index == 14) {
        listOf("")
    } else {
        limits[index].entries.filter { prevZ in it.value }.flatMap { (digit, _) ->
            val z = calculateZ(digit, prevZ, this[index].first, this[index].second, this[index].third)
            calculateSerials(limits, index + 1, z).map { digit.toString() + it }
        }
    }

    fun List<Triple<Int, Int, Int>>.solve() = calculateSerials(calculateLimits(), 0, 0).map { it.toLong() }

    fun part1(
        input: List<String>
    ) = input.parse().solve().maxOf { it }

    fun part2(
        input: List<String>
    ) = input.parse().solve().minOf { it }

    val input = readInput("Day24")
    println(part1(input))
    println(part2(input))
}
