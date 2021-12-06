fun main() {

    val days = 256
    val dp = LongArray(days)

    for (i in 0 until days) {
        var t = i / 7 + 1L
        for (j in i - 9 downTo 0 step 7) {
            t += dp[j]
        }
        dp[i] = t
    }

    fun spawn(
        initial: Int,
        days: Int
    ) = dp.getOrElse(days - 1 - initial) { 0 }.toLong()

    fun solve(
        input: List<String>,
        days: Int
    ) = input.first().split(",").map { it.toInt() }.let { initial ->
        initial.sumOf { spawn(it, days) + 1 }
    }

    fun part1(
        input: List<String>
    ) = solve(input, 80)

    fun part2(
        input: List<String>
    ) = solve(input, 256)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}