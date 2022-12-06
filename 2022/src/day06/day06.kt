package day06

import GREEN
import RESET
import printTimeMillis
import readInput

fun findMarker(str: String, markerSize: Int): Int {
    for (i in 0..str.length - markerSize) {
        if (str.substring(i, i + markerSize).toSet().size == markerSize) {
            return i + markerSize
        }
    }
    throw IllegalStateException("Wrong packet bro")
}

fun part1(input: List<String>) = findMarker(input.first(), 4)

fun part2(input: List<String>) = findMarker(input.first(), 14)

fun part2forDodo(input: List<String>) = input.first().let {
    val markerSize = 5*2+4
    for (i in 0..it.length - markerSize) {
        val one = it[i]
        val two = it[i + 1]
        val three = it[i + 2]
        val four = it[i + 3]
        val five = it[i + 4]
        val six = it[i + 5]
        val seven = it[i + 6]
        val two_times_four = it[i + 7]
        val nine = it[i + 8]
        val two_times_five = it[i + 9]
        val eleven = it[i + 10]
        val twelve = it[i + 11]
        val thirteen = it[i + 12]
        val two_times_seven = it[i + 13]
        if (setOf(
                one, two, three, four, five, six, seven, two_times_four,
                nine, two_times_five, eleven, twelve, thirteen, two_times_seven
            ).size == markerSize) {
            return@let i + markerSize
        }
    }
    throw IllegalStateException("Wrong packet bro")
}

fun main() {
    val testInput = readInput("day06_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput) + RESET) }

    val input = readInput("day06.txt")
    printTimeMillis { print("part1 input = $GREEN" + part1(input) + RESET) }
    printTimeMillis { print("part2 input = $GREEN" + part2(input) + RESET) }
    printTimeMillis { print("part2 for dodo = $GREEN" + part2forDodo(input) + RESET) }
}
