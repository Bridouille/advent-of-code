package day02

import GREEN
import RESET
import printTimeMillis
import readInput

fun part1(input: List<String>): Long {
    val ranges = input.first().split(",").map {
        LongRange(it.split("-").first().toLong(), it.split("-").last().toLong())
    }
    var total = 0L
    for (range in ranges) {
        for (nb in range) {
            if (isInvalid1(nb)) {
                total += nb
            }
        }
    }
    return total
}

private fun isInvalid1(nb: Long): Boolean {
    val str = nb.toString()
    if (str.length % 2 != 0) return false
    return str.take(str.length / 2) == str.takeLast(str.length / 2)
}

fun part2(input: List<String>): Long {
    val ranges = input.first().split(",").map {
        LongRange(it.split("-").first().toLong(), it.split("-").last().toLong())
    }
    var total = 0L
    for (range in ranges) {
        for (nb in range) {
            if (isInvalid2(nb)) {
                total += nb
            }
        }
    }
    return total
}

private fun isInvalid2(nb: Long): Boolean {
    val str = nb.toString()

    for (i in str.length / 2 downTo 1) {
        if (str.length % i != 0) continue
        val substr = str.windowed(i, i)
        if (substr.toSet().size == 1) return true
    }
    return false
}

fun main() {
    val testInput = readInput("day02_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput) + RESET) }

    val input = readInput("day02.txt")
    printTimeMillis { print("part1 input = $GREEN" + part1(input) + RESET) }
    printTimeMillis { print("part2 input = $GREEN" + part2(input) + RESET) }
}
