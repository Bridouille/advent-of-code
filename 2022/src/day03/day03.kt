package day03

import GREEN
import RESET
import printTimeMillis
import readInput

fun Char.score() = when (this) {
    in 'a'..'z' -> this - 'a' + 1
    in 'A'..'Z' -> this - 'A' + 27
    else -> throw IllegalStateException("Not a letter")
}

fun part1(input: List<String>) = input.fold(0) { acc, line ->
    val secondHalf = line.takeLast(line.length / 2).groupingBy { it }.eachCount()
    val commonLetter = line.take(line.length / 2).groupingBy { it }.eachCount().filterKeys {
        secondHalf.keys.contains(it)
    }
    acc + commonLetter.entries.first().key.score()
}

fun part2(input: List<String>) = input.windowed(3, 3).map { lists ->
    val result = lists[0].groupingBy { it }.eachCount().filterKeys {
        lists[1].groupingBy { it }.eachCount().contains(it) && lists[2].groupingBy { it }.eachCount().contains(it)
    }
    result.entries.first().key.score()
}.sum()

fun main() {
    val testInput = readInput("day03_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput) + RESET) }

    val input = readInput("day03.txt")
    printTimeMillis { print("part1 input = $GREEN" + part1(input) + RESET) }
    printTimeMillis { print("part2 input = $GREEN" + part2(input) + RESET) }
}
