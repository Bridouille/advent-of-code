package day02

import readInput

fun part1(input: List<String>) : Int {
    var depth = 0
    var horizontal = 0

    input.forEach {
        it.split(" ").let {
            val instruction = it.firstOrNull() ?: return@let
            val nb = it.elementAtOrNull(1)?.toInt() ?: return@let

            if (instruction == "forward") {
                horizontal += nb
            } else if (instruction == "down") {
                depth += nb
            } else if (instruction == "up") {
                depth -= nb
            }
        }
    }
    return depth * horizontal
}

fun part2(input: List<String>): Int {
    var aim = 0
    var depth = 0
    var horizontal = 0

    input.forEach {
        it.split(" ").let {
            val instruction = it.firstOrNull() ?: return@let
            val nb = it.elementAtOrNull(1)?.toInt() ?: return@let

            if (instruction == "forward") {
                horizontal += nb
                depth += aim * nb
            } else if (instruction == "down") {
                aim += nb
            } else if (instruction == "up") {
                aim -= nb
            }
        }
    }
    return depth * horizontal
}

fun main() {
    val testInput = readInput("day02/test")
    println(part1(testInput))
    println(part2(testInput))

    val input = readInput("day02/input")
    println(part1(input))
    println(part2(input))
}
