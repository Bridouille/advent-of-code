package day08

import GREEN
import RESET
import printTimeMillis
import readInput

fun part1(input: List<String>): Int {
    return 1
}

fun part2(input: List<String>): Int {
    return 2
}

fun main() {
    val testInput = readInput("day08_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput) + RESET) }

    // val input = readInput("day08.txt")
    // printTimeMillis { print("part1 input = $GREEN" + part1(input) + RESET) }
    // printTimeMillis { print("part2 input = $GREEN" + part2(input) + RESET) }
}
