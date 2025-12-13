package day05

import GREEN
import RESET
import printTimeMillis
import readInput
import kotlin.collections.plusAssign

data class Database(
    val freshRanges: List<LongRange>,
    val ingredients: List<Long>,
)

private fun List<String>.toDatabase(): Database {
    val freshRanges = mutableListOf<LongRange>()
    val ingredients = mutableListOf<Long>()

    for (line in this) {
        if (line.isEmpty()) continue
        if (line.contains("-")) {
            freshRanges.add(LongRange(line.split('-')[0].toLong(), line.split('-')[1].toLong()))
        } else {
            ingredients.add(line.toLong())
        }
    }
    return Database(freshRanges, ingredients)
}

fun part1(input: List<String>): Int {
    val db = input.toDatabase()
    var fresh = 0

    for (nb in db.ingredients) {
        if (db.freshRanges.any { it.contains(nb) }) {
            fresh += 1
        }
    }
    return fresh
}

fun part2(input: List<String>): Long {
    val db = input.toDatabase()

    return mergeRanges(db.freshRanges).sumOf { it.last - it.first + 1 }
}

private fun mergeRanges(ranges: List<LongRange>): List<LongRange> {
    val sorted = ranges.sortedBy { it.first }
    val result = mutableListOf<LongRange>()
    var currentStart = sorted[0].first
    var currentEnd = sorted[0].last

    for (i in 1 until sorted.size) {
        val range = sorted[i]

        if (range.first <= currentEnd) {
            if (range.last > currentEnd) {
                currentEnd = range.last
            }
        } else {
            result += currentStart..currentEnd
            currentStart = range.first
            currentEnd = range.last
        }
    }
    result += currentStart..currentEnd
    return result
}

fun main() {
    val testInput = readInput("day05_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput) + RESET) }

    val input = readInput("day05.txt")
    printTimeMillis { print("part1 input = $GREEN" + part1(input) + RESET) }
    printTimeMillis { print("part2 input = $GREEN" + part2(input) + RESET) }
}
