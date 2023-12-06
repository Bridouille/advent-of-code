package day06

import GREEN
import RESET
import printTimeMillis
import readInput

fun part1(input: List<String>): Int {
    val times = input.first().split(":")[1].split(" ").map { it.trim().toIntOrNull() }.filterNotNull()
    val distances = input[1].split(":")[1].split(" ").map { it.trim().toIntOrNull() }.filterNotNull()

    var sum = 1
    for (i in times.indices) {
        var betterWays = 0
        for (t in 1 until times[i]) {
            if (t * (times[i] - t) > distances[i]) {
                betterWays += 1
            }
        }
        sum *= betterWays
    }
    return sum
}

fun part2(input: List<String>): Int {
    val time = input.first().split(":")[1].replace(" ", "").toLong()
    val dist = input[1].split(":")[1].replace(" ", "").toLong()

    var betterWays = 0
    for (t in 1 until time) {
        if (t * (time - t) > dist) {
            betterWays += 1
        }
    }
    return betterWays
}

fun main() {
    val testInput = readInput("day06_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput) + RESET) }

    val input = readInput("day06.txt")
    printTimeMillis { print("part1 input = $GREEN" + part1(input) + RESET) } // 608902
    printTimeMillis { print("part2 input = $GREEN" + part2(input) + RESET) } // 46173809
}
