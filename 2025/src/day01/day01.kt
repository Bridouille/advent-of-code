package day01

import GREEN
import RESET
import printTimeMillis
import readInput

fun part1(input: List<String>): Int {
    var rotation = 50
    var count = 0

    for (line in input) {
        val amount = line.drop(1).toInt()

        if (line.first() == 'L') {
            rotation = rotation - amount % 100
            if (rotation < 0) rotation += 100
        } else {
            rotation = (rotation + amount % 100) % 100
        }
        if (rotation == 0) count += 1
    }
    return count
}

fun part2(input: List<String>): Int {
    var rotation = 50
    var count = 0

    for (line in input) {
        val amount = line.drop(1).toInt()

        if (line.first() == 'L') {
            for (i in 0 until amount) {
                rotation -= 1
                if (rotation == 0) count += 1
                if (rotation < 0) rotation += 100
            }
        } else {
            for (i in 0 until amount) {
                rotation += 1
                if (rotation == 100) rotation -= 100
                if (rotation == 0) count += 1
            }
        }
    }
    return count
}

fun main() {
    val testInput = readInput("day01_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput) + RESET) }

    val input = readInput("day01.txt")
    printTimeMillis { print("part1 input = $GREEN" + part1(input) + RESET) }
    printTimeMillis { print("part2 input = $GREEN" + part2(input) + RESET) }
}
