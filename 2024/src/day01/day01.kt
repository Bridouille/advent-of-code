package day01

import GREEN
import RESET
import printTimeMillis
import readInput
import kotlin.math.abs

fun part1(input: List<String>): Int {
    val a = mutableListOf<Int>()
    val b = mutableListOf<Int>()

    input.forEach {
        val list = it.split(" ").map { it.toIntOrNull() }.filterNotNull()

        a.add(list[0])
        b.add(list[1])
    }

    a.sort()
    b.sort()

    return a.mapIndexed { index, i -> abs(i - b[index]) }.sum()
}

fun part2(input: List<String>): Int {
    val a = mutableListOf<Int>()
    val b = mutableMapOf<Int, Int>()

    input.forEach {
        val pair = it.split(" ").map { it.toIntOrNull() }.filterNotNull()

        a.add(pair[0])
        b[pair[1]] = b.getOrDefault(pair[1], 0) + 1
    }

    return a.map { it * b.getOrDefault(it, 0) }.sum()
}

fun main() {
    val testInput = readInput("day01_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput) + RESET) }

    val input = readInput("day01.txt")
    printTimeMillis { print("part1 input = $GREEN" + part1(input) + RESET) }
    printTimeMillis { print("part2 input = $GREEN" + part2(input) + RESET) }
}
