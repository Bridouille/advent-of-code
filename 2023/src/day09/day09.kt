package day09

import GREEN
import RESET
import printTimeMillis
import readInput

private fun List<Int>.toDiffList(): List<List<Int>> {
    val diffList = mutableListOf<List<Int>>().also { it.add(this) }
    val diffs = toMutableList()

    while (diffs.any { it != 0 }) {
        val newDiffs = buildList {
            for (i in 0 until diffs.lastIndex) {
                add(diffs[i + 1] - diffs[i])
            }
        }
        diffs.clear()
        diffs.addAll(newDiffs)
        if (newDiffs.isNotEmpty())
            diffList.add(newDiffs)
    }
    return diffList
}

fun part1(input: List<String>) = input.map {
    val seq = it.split(" ").map { it.toInt() }

    seq.toDiffList().map { it.last() }.sum()
}.sum()

fun part2(input: List<String>) = input.map {
    val seq = it.split(" ").map { it.toInt() }

    seq.toDiffList().map { it.first() }.reversed().fold(0) { acc, i ->
        i - acc
    }
}.sum()

fun main() {
    val testInput = readInput("day09_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput) + RESET) }

    val input = readInput("day09.txt")
    printTimeMillis { print("part1 input = $GREEN" + part1(input) + RESET) }
    printTimeMillis { print("part2 input = $GREEN" + part2(input) + RESET) }
}
