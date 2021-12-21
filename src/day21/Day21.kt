package day21

import readInput

data class Player(val name: String, var pos: Int, var score: Int)
fun String.toPlayer(): Player {
    return Player(this.substringBefore(" starting"), this.split(" ").last().toInt(), 0)
}
fun getRolls(rollsNb: List<Int>): Int {
    return rollsNb.map {
        val nb = it % 100
        if (nb == 0) 100 else nb
    }.sum()
}

// Just execute the game manually
fun part1(lines: List<String>): Int {
    val players = listOf(lines.first().toPlayer(), lines.last().toPlayer())
    var currentPlayer = 0
    var rollsNb = 1

    while (!players.any { it.score >= 1000 }) {
        val p = players[currentPlayer]
        val rolled = getRolls(listOf(rollsNb, rollsNb + 1, rollsNb + 2))
        rollsNb += 3

        p.pos = (p.pos + rolled) % 10
        if (p.pos == 0) p.pos = 10
        p.score += p.pos

        currentPlayer = (currentPlayer + 1) % players.size
    }
    return players.first { it.score < 1000 }.score * (rollsNb - 1)
}

val waysMemo = mutableMapOf<String, Long>()
fun findWays(throws: Int, sum: Int, faces: Int = 3) : Long {
    if (sum == 0 && throws == 0) return 1
    if (sum < 0 || throws == 0) return 0

    val key = "$sum-$throws"
    if (!waysMemo.containsKey(key)) {
        waysMemo[key] = (1..faces).toList().sumOf { findWays(throws - 1, sum - it) }
    }
    return waysMemo[key]!!
}

fun Pair<Long, Long>.multiply(multiplier: Long) = Pair(first * multiplier, second * multiplier)
fun Pair<Long, Long>.add(other: Pair<Long, Long>) = Pair(first + other.first, second + other.second)

val memo = mutableMapOf<String, Pair<Long, Long>>()
fun numberOfWaysToWin(pos1: Int, score1: Int, pos2: Int, score2: Int, rolled: Int, player1turn: Boolean) : Pair<Long, Long> {
    val key = "$pos1-$score1-$pos2-$score2-$rolled-$player1turn"
    if (!memo.containsKey(key)) {
        if (player1turn) {
            var posAfterThrow = (pos1 + rolled) % 10
            if (posAfterThrow == 0) posAfterThrow = 10
            val scoreAfterThrow = score1 + posAfterThrow

            if (scoreAfterThrow >= 21) return Pair(1, 0)

            // 27 total possibilities to obtain a total between 3 and 9 rolling 3 times a 3 faced dice
            var totalWays = Pair(0L, 0L)
            for (possibleTotal in 3..9) {
                totalWays = totalWays.add(
                        numberOfWaysToWin(posAfterThrow, scoreAfterThrow, pos2, score2, rolled = possibleTotal, false)
                                .multiply(findWays(throws = 3, possibleTotal))
                )
            }
            memo[key] = totalWays
        } else {
            var posAfterThrow = (pos2 + rolled) % 10
            if (posAfterThrow == 0) posAfterThrow = 10
            val scoreAfterThrow = score2 + posAfterThrow

            if (scoreAfterThrow >= 21) return Pair(0, 1)

            // 27 total possibilities to obtain a total between 3 and 9 rolling 3 times a 3 faced dice
            var totalWays = Pair(0L, 0L)
            for (possibleTotal in 3..9) {
                totalWays = totalWays.add(
                        numberOfWaysToWin(pos1, score1, posAfterThrow, scoreAfterThrow, rolled = possibleTotal, true)
                                .multiply(findWays(throws = 3, possibleTotal))
                )
            }
            memo[key] = totalWays
        }
    }
    return memo[key]!!
}

fun part2(lines: List<String>): Long {
    val players = listOf(lines.first().toPlayer(), lines.last().toPlayer())
    val pos1 = players.first().pos
    val pos2 = players.last().pos

    var total = Pair(0L, 0L)
    for (possibleTotal in 3..9) {
        total = total.add(
                numberOfWaysToWin(pos1, 0, pos2, 0, rolled = possibleTotal, true) // Player1 starts
                        .multiply(findWays(throws = 3, possibleTotal))
        )
    }
    return maxOf(total.first, total.second)
}

fun main() {
    val testInput = readInput("day21/test")
    println("part1(testInput) => " + part1(testInput))
    println("part2(testInput) => " + part2(testInput))

    val input = readInput("day21/input")
    println("part1(input) => " + part1(input))
    println("part2(input) => " + part2(input))
}
