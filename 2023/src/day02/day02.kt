package day02

import GREEN
import RESET
import printTimeMillis
import readInput
import kotlin.math.max

val maxes = mapOf(
    "red" to 12,
    "green" to 13,
    "blue" to 14
)

fun part1(input: List<String>): Int {
    return input.sumOf {
        val gameIdx = it.split(":")[0].split(" ")[1].toInt()
        val reveals = it.split(":")[1].split(";")

        for (reveal in reveals.flatMap { it.split(",").map { it.trim() } }) {
            val color = reveal.split(" ")[1]
            val amount = reveal.split(" ")[0].toInt()

            if (amount > maxes[color]!!) return@sumOf 0
        }
        gameIdx
    }
}

fun part2(input: List<String>): Int {
    return input.sumOf {
        val reveals = it.split(":")[1].split(";")

        val maxCubes = mutableMapOf<String, Int>()

        for (reveal in reveals.flatMap { it.split(",").map { it.trim() } }) {
            val color = reveal.split(" ")[1]
            val amount = reveal.split(" ")[0].toInt()

            maxCubes[color] = max(maxCubes.getOrElse(color) { amount }, amount)
        }
        maxCubes.values.fold(1) { a, b -> a * b }.toInt()
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
