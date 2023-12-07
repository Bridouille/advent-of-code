package day07

import GREEN
import RESET
import printTimeMillis
import readInput

data class CamelCards(
    val hand: String,
    val occurences: Map<Char, Int>,
    val occurencesWithJoker: Map<Char, Int>,
    val bid: Int
)

private fun typeScore(occurences: Map<Char, Int>): Int {
    if (occurences.values.contains(5)) return 1_000_000 // Five of a kind
    if (occurences.values.contains(4)) return 100_000 // Four of a kind
    if (occurences.values.contains(3)) {
        if (occurences.values.contains(2)) return 10_000 // Full house
        return 1_000 // Three of a kind
    }
    val nbPairs = occurences.values.filter { it == 2 }.size
    if (nbPairs == 2) return 100 // Two pairs
    if (nbPairs == 1) return 10 // One pair
    return 1 // High card
}

private fun List<String>.toCards(): List<CamelCards> = map { str ->
    val occurences = str.split(" ")[0].groupingBy { it }.eachCount()
    val occurencesWithJoker = buildList {
        // Replace each 'J' card with a potential card
        // Calculate the score and sort them to get the best possible score
        listOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2').forEach {
            val replaced = str.split(" ")[0].replace('J', it)

            add(replaced.groupingBy { it }.eachCount())
        }
    }
    .distinct()
    .sortedByDescending { typeScore(it) }
    .first()

    CamelCards(
        str.split(" ")[0],
        occurences,
        occurencesWithJoker,
        str.split(" ")[1].toInt()
    )
}

private fun sortCards(
    cards: List<CamelCards>,
    withJokers: Boolean
): List<CamelCards> {
    val cardScore = if (withJokers) {
        mapOf(
            'A' to 13, 'K' to 12, 'Q' to 11, 'T' to 10,
            '9' to 9, '8' to 8, '7' to 7, '6' to 6, '5' to 5, '4' to 4, '3' to 3, '2' to 2, 'J' to 1,
        )
    } else {
        mapOf(
            'A' to 13, 'K' to 12, 'Q' to 11, 'J' to 10, 'T' to 9,
            '9' to 8, '8' to 7, '7' to 6, '6' to 5, '5' to 4, '4' to 3, '3' to 2, '2' to 1
        )
    }

    val c = Comparator<CamelCards> { a, b ->
        val scoreA = typeScore(if (withJokers) a.occurencesWithJoker else a.occurences)
        val scoreB = typeScore(if (withJokers) b.occurencesWithJoker else b.occurences)

        if (scoreA == scoreB) { // Same type, compare card by card
            for (i in 0 until 5) {
                if (a.hand[i] != b.hand[i]) {
                    return@Comparator cardScore[a.hand[i]]!! - cardScore[b.hand[i]]!!
                }
            }
        }
        scoreA - scoreB
    }
    return cards.sortedWith(c)
}

fun part1(input: List<String>): Int {
    val sorted = sortCards(input.toCards(), withJokers = false)

    return sorted.mapIndexed { index, camelCards -> (index + 1) * camelCards.bid }.sum()
}

fun part2(input: List<String>): Int {
    val sorted = sortCards(input.toCards(), withJokers = true)

    return sorted.mapIndexed { index, camelCards -> (index + 1) * camelCards.bid }.sum()
}

fun main() {
    val testInput = readInput("day07_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput) + RESET) }

    val input = readInput("day07.txt")
    printTimeMillis { print("part1 input = $GREEN" + part1(input) + RESET) }
    printTimeMillis { print("part2 input = $GREEN" + part2(input) + RESET) }
}
