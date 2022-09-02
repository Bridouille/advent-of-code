package day01

import readInput

fun part1(input: List<String>) = input.windowed(2).map { it[0] < it[1] }.count { it }

const val SLIDING_WINDOW_SIZE = 3

fun part2(input: List<String>): Int {
    val sliding1: MutableList<Int> = mutableListOf<Int>()
    val sliding2: MutableList<Int> = mutableListOf<Int>()

    var increase = 0
    input.forEach {
        if (!sliding1.isEmpty()) {
            sliding2.add(0, it.toInt())
        }

        if (sliding1.size > SLIDING_WINDOW_SIZE) {
            sliding1.removeLast()
        }
        if (sliding2.size > SLIDING_WINDOW_SIZE) {
            sliding2.removeLast()
        }
        if (sliding1.size == SLIDING_WINDOW_SIZE && sliding2.size == SLIDING_WINDOW_SIZE) {
            if (sliding2.sum() > sliding1.sum()) {
                increase++
            }
        }
        sliding1.add(0, it.toInt())
    }
    return increase
}

fun main() {
    val testInput = readInput("day01/test")
    println(part1(testInput))
    println(part2(testInput))

    val input = readInput("day01/input")
    println(part1(input))
    println(part2(input))
}
