package day01

import readInput

fun List<String>.toElvesCalories(): List<Int> {
    val elves = mutableListOf<Int>()

    foldIndexed(0) { idx, acc, value ->
        if (value.isEmpty() || idx == size - 1) {
            0.also { elves.add(acc) }
        } else {
            acc + value.toInt()
        }
    }
    return elves
}

fun part1(input: List<String>) = input.toElvesCalories().sortedDescending().first()

fun part2(input: List<String>) = input.toElvesCalories().sortedDescending().let {
    it[0] + it[1] + it[2]
}

fun main() {
    val testInput = readInput("day01_example.txt", false)
    println("part1 example = " + part1(testInput))
    println("part2 example = " + part2(testInput))

    val input = readInput("day01.txt", false)
    println("part1 input = " + part1(input))
    println("part2 input = " + part2(input))
}
