import java.lang.Integer.min
import java.lang.Long.max

fun main() {

    fun List<String>.parse() = Pair(
        first().split(":").last().trim().toInt(),
        last().split(":").last().trim().toInt()
    )

    fun Int.roll() = if (this == 100) 1 else this + 1

    fun Int.rollPosition(dice: Int): Pair<Int, Int> {
        var rolledDice = dice
        var rolledPosition = this

        rolledDice = rolledDice.roll()
        rolledPosition += rolledDice
        rolledDice = rolledDice.roll()
        rolledPosition += rolledDice
        rolledDice = rolledDice.roll()
        rolledPosition += rolledDice

        rolledPosition = if (rolledPosition % 10 == 0) 10 else rolledPosition % 10

        return rolledDice to rolledPosition
    }

    fun part1(
        input: List<String>
    ) = input.parse().let { (first, second) ->
        var rolls = 0
        var dice = 0
        var firstPosition = first
        var secondPosition = second
        var firstScore = 0
        var secondScore = 0

        while (true) {
            rolls += 3
            val (firstRolledDice, firstRolledPosition) = firstPosition.rollPosition(dice)
            dice = firstRolledDice
            firstPosition = firstRolledPosition
            firstScore += firstPosition

            if (firstScore >= 1000) break

            rolls += 3
            val (secondRolledDice, secondRolledPosition) = secondPosition.rollPosition(dice)
            dice = secondRolledDice
            secondPosition = secondRolledPosition
            secondScore += secondPosition

            if (secondScore >= 1000) break
        }

        return@let rolls * min(firstScore, secondScore)
    }

    data class Roll(
        val value: Int,
        val probability: Int
    ) {

        fun roll(position: Int) = (position + value).let { if (it % 10 == 0) 10 else it % 10 }
    }

    fun part2(
        input: List<String>
    ) = input.parse().let { (first, second) ->
        var firstPlayerWins = 0L
        var secondPlayerWins = 0L

        val maxSteps = 21
        val maxPosition = 10
        val maxScore = 30

        val rolls = listOf(
            Roll(value = 3, probability = 1),
            Roll(value = 4, probability = 3),
            Roll(value = 5, probability = 6),
            Roll(value = 6, probability = 7),
            Roll(value = 7, probability = 6),
            Roll(value = 8, probability = 3),
            Roll(value = 9, probability = 1)
        )

        val d = Array(maxSteps + 1) {
            Array(maxPosition + 1) {
                Array(maxPosition + 1) {
                    Array(maxScore + 1) {
                        LongArray(maxScore + 1) { 0 }
                    }
                }
            }
        }

        d[0][first][second][0][0] = 1

        for (step in 1..maxSteps) {
            for (firstPosition in 1..maxPosition) {
                for (secondPosition in 1..maxPosition) {
                    for (firstScore in 0..20) {
                        for (secondScore in 0..20) {
                            for (firstRoll in rolls) {
                                val firstRolledPosition = firstRoll.roll(firstPosition)
                                val firstRolledScore = firstScore + firstRolledPosition

                                if (firstRolledScore >= 21) {
                                    d[step][firstRolledPosition][secondPosition][firstRolledScore][secondScore] +=
                                        firstRoll.probability * d[step - 1][firstPosition][secondPosition][firstScore][secondScore]
                                } else {
                                    for (secondRoll in rolls) {
                                        val secondRolledPosition = secondRoll.roll(secondPosition)
                                        val secondRolledScore = secondScore + secondRolledPosition

                                        d[step][firstRolledPosition][secondRolledPosition][firstRolledScore][secondRolledScore] +=
                                            firstRoll.probability * secondRoll.probability * d[step - 1][firstPosition][secondPosition][firstScore][secondScore]
                                    }
                                }
                            }
                        }
                    }
                }
            }
            for (firstPosition in 1..maxPosition) {
                for (secondPosition in 1..maxPosition) {
                    for (firstScore in 0..20) {
                        for (secondScore in 21..maxScore) {
                            secondPlayerWins += d[step][firstPosition][secondPosition][firstScore][secondScore]
                        }
                    }
                    for (firstScore in 21..maxScore) {
                        for (secondScore in 0..maxScore) {
                            firstPlayerWins += d[step][firstPosition][secondPosition][firstScore][secondScore]
                        }
                    }
                }
            }
        }

        return@let max(firstPlayerWins, secondPlayerWins)
    }

    val input = readInput("Day21")
    println(part1(input))
    println(part2(input))
}
