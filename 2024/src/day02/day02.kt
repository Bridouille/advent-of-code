package day02

import GREEN
import RESET
import printTimeMillis
import readInput

fun part1(input: List<String>): Int {
    return input.map {
        it.split(" ").map { it.toInt() }
    }.count {
        val decrease = it.windowed(2).all {
            val diff = it[0] - it[1]

            it[0] >= it[1] && IntRange(1, 3).contains(diff)
        }
        val increase = it.windowed(2).all {
            val diff = it[1] - it[0]

            it[0] <= it[1] && IntRange(1, 3).contains(diff)
        }
        decrease || increase
    }
}

fun part2(input: List<String>): Int {
    return input.map {
        it.split(" ").map { it.toInt() }
    }.count {
        for (i in 0..it.lastIndex) {
            val newList = it.filterIndexed { index, _ -> index != i }

            val decrease = newList.windowed(2).all {
                val diff = it[0] - it[1]
                it[0] >= it[1] && diff <= 3 && diff >= 1
            }
            val increase = newList.windowed(2).all {
                val diff = it[1] - it[0]
                it[0] <= it[1] && diff <= 3 && diff >= 1
            }
            if (decrease || increase) return@count true
        }
        false
    }
}

fun main() {
    val testInput = readInput("day02_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput) + RESET) }

    val input = readInput("day02.txt")
    printTimeMillis { print("part1 input = $GREEN" + part1(input) + RESET) }
    printTimeMillis { print("part2 input = $GREEN" + part2(input) + RESET) }
}
