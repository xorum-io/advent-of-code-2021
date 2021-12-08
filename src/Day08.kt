fun main() {

    fun List<String>.parse() = map { line ->
        val (digits, number) = line.split('|')
            .map { part ->
                part.trim().split(" ").map {
                    it.toSortedSet().joinToString("")
                }
            }

        val v1 = digits.first { it.length == 2 }
        val v4 = digits.first { it.length == 4 }
        val v7 = digits.first { it.length == 3 }
        val v8 = digits.first { it.length == 7 }

        val bd = v4.filter { it !in v1 }
        val v5 = digits.first { it.length == 5 && it.contains(bd.first()) && it.contains(bd.last()) }

        val a = v7.first { it !in v1 }
        val c = v1.first { it !in v5 }
        val f = v1.first { it in v5 }

        val v3 = digits.first { it.length == 5 && it.contains(c) && it.contains(f) }
        val v2 = digits.first { it.length == 5 && it !in listOf(v5, v3) }
        val v6 = digits.first { it.length == 6 && !it.contains(c) }

        val d = bd.first { it in v2 }

        val v9 = digits.first { it.length == 6 && it != v6 && it.contains(d) }
        val v0 = digits.first { it.length == 6 && it != v6 && !it.contains(d) }

        val v = listOf(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9)

        return@map number.map { v.indexOf(it) }
    }

    fun part1(
        input: List<String>
    ) = input.parse().flatten().count { it in listOf(1, 4, 7, 8) }

    fun part2(
        input: List<String>
    ) = input.parse().sumOf { it.joinToString("").toInt() }

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}
