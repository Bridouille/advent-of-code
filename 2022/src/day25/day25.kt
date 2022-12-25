package day25

import GREEN
import RESET
import printTimeMillis
import readInput

fun String.toDecimal() = fold(0L) { acc, c ->  acc * 5L + ("=-012".indexOf(c) - 2) }

fun Long.toSNAFU(): String {
    val base = listOf('0', '1', '2', '=', '-')
    val digits = listOf(0, 1, 2, -2, -1)
    val sb = StringBuilder()
    var nb = this

    while (nb != 0L) {
        val rest = (nb % 5).toInt()
        sb.append(base[rest])
        nb = (nb - digits[rest]) / 5L
    }
    return sb.toString().reversed()
}

fun part1(input: List<String>) = input.map { it.toDecimal() }.sum().let {
    println(it)
    it.toSNAFU()
}

fun main() {
    val testInput = readInput("day25_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput) + RESET) }

    val input = readInput("day25.txt")
    printTimeMillis { print("part1 input = $GREEN" + part1(input) + RESET) }
}
