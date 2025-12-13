package day06

import GREEN
import RESET
import printTimeMillis
import readInput

fun part1(input: List<String>): Long {
    val operations = input.last().replace(Regex("\\s+"), " ").trim().split(" ")
    val numbers = input.dropLast(1).map {
        it.replace(Regex("\\s+"), " ").trim().split(" ").map { it.toLong() }
    }
    var result = 0L

    for (x in numbers[0].indices) {
        val column = mutableListOf<Long>()

        for (y in numbers.indices) {
            column.add(numbers[y][x])
        }

        if (operations[x] == "*") {
            result += column.fold(1L) { acc, i -> acc * i }
        } else {
            result += column.fold(0L) { acc, i -> acc + i }
        }
    }
    return result
}

fun part2(input: List<String>): Long {
    var result = 0L
    val numbers = mutableListOf<Long>()

    for (x in input[0].length-1 downTo 0) {
        val nb = StringBuilder()

        for (y in input.indices) {
            nb.append(input[y][x])
        }
        val number = nb.toString().replace("-", "").replace(Regex("\\s+"), "").trim()
        if (number.isBlank()) continue

        if (listOf('*', '+').contains(number.last())) {
            numbers.add(number.dropLast(1).toLong())
        } else {
            numbers.add(number.toLong())
        }
        if (number.last() == '*') {
            result += numbers.fold(1L) { acc, i -> acc * i }
            numbers.clear()
        } else if (number.last() == '+') {
            result += numbers.fold(0L) { acc, i -> acc + i }
            numbers.clear()
        }
    }

    return result
}

fun main() {
    val testInput = readInput("day06_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput) + RESET) }

    val input = readInput("day06.txt")
    printTimeMillis { print("part1 input = $GREEN" + part1(input) + RESET) }
    printTimeMillis { print("part2 input = $GREEN" + part2(input) + RESET) }
}
