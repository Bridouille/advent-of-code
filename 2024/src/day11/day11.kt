package day11

import GREEN
import RESET
import printTimeMillis
import readInput

// cache of stone + times to "final stones number"
val MEMO = mutableMapOf<Pair<String, Int>, Long>()

fun blink(stone: String, times: Int = 1): Long {
    if (MEMO.contains(Pair(stone, times))) return MEMO[Pair(stone, times)]!!
    if (times == 0) return 1

    val result = when {
        stone == "0" -> blink("1", times - 1)
        stone.length % 2 == 0 -> {
            val left = stone.substring(0, stone.length / 2).toLong().toString()
            val right = stone.substring(stone.length / 2).toLong().toString()

            blink(left, times - 1) + blink(right, times - 1)
        }
        else -> blink((stone.toLong() * 2024).toString(), times - 1)
    }
    MEMO[Pair(stone, times)] = result
    return result
}

fun part1(input: List<String>): Long {
    val stones = input.first().split(" ")
    var total = 0L

    for (stone in stones) total += blink(stone, 25)
    return total
}

fun part2(input: List<String>): Long {
    val stones = input.first().split(" ")
    var total = 0L

    for (stone in stones) total += blink(stone, 75)
    return total
}

fun main() {
    val testInput = readInput("day11_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput) + RESET) }

    val input = readInput("day11.txt")
    printTimeMillis { print("part1 input = $GREEN" + part1(input) + RESET) }
    printTimeMillis { print("part2 input = $GREEN" + part2(input) + RESET) }
}
