package day04

import GREEN
import RESET
import printTimeMillis
import readInput
import kotlin.math.pow

data class Card(
    val winning: List<String>,
    val numbers: List<String>
) {
    fun matches() = numbers.toSet().intersect(winning.toSet())

    fun score(): Double {
        val matches = matches()

        val score = 2.0.pow((matches.size - 1).toDouble())
        if (score < 1) return 0.0
        return score
    }
}

private fun List<String>.toCardList(): List<Card> = map {
    it.split(": ")[1].let {
        val winning = it.split("|")[0].split(" ").map { it.trim() }
            .filter { it.isNotBlank() }
        val numbers = it.split("|")[1].split(" ").map { it.trim() }
            .filter { it.isNotBlank() }

        Card(winning = winning, numbers = numbers)
    }
}

fun part1(input: List<String>) = input.toCardList().sumOf {it.score() }.toInt()

fun part2(input: List<String>): Int {
    val cards = input.toCardList()
    val nbCards = IntArray(cards.size) { 1 }

    for (idx in cards.indices) {
        val nbMatches = cards[idx].matches().size

        for (i in 1..nbMatches) {
            nbCards[idx + i] += nbCards[idx]
        }
    }
    return nbCards.sum()
}

fun main() {
    val testInput = readInput("day04_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput) + RESET) }

    val input = readInput("day04.txt")
    printTimeMillis { print("part1 input = $GREEN" + part1(input) + RESET) }
    printTimeMillis { print("part2 input = $GREEN" + part2(input) + RESET) }
}
