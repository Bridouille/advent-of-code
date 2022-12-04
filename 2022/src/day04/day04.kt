package day04

import GREEN
import RESET
import printTimeMillis
import readInput

data class Bound(val min: Int, val max: Int) {
    fun contains(other: Bound) = min <= other.min && max >= other.max
    fun overlap(other: Bound) = (min >= other.min && min <= other.max) || (max <= other.max && max >= other.min)
}

fun part1(input: List<String>) = input.fold(0) { acc, line ->
    val first = line.split(",")[0].split("-").let { Bound(it[0].toInt(), it[1].toInt()) }
    val second = line.split(",")[1].split("-").let { Bound(it[0].toInt(), it[1].toInt()) }
    val increase = if (first.contains(second) || second.contains(first)) { 1 } else { 0 }
    acc+ increase
}

fun part2(input: List<String>) = input.fold(0) { acc, line ->
    val first = line.split(",")[0].split("-").let { Bound(it[0].toInt(), it[1].toInt()) }
    val second = line.split(",")[1].split("-").let { Bound(it[0].toInt(), it[1].toInt()) }
    val increase = if (first.overlap(second) || second.overlap(first)) { 1 } else { 0 }
    acc+ increase
}

fun main() {
    val testInput = readInput("day04_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput) + RESET) }

    val input = readInput("day04.txt")
    printTimeMillis { print("part1 input = $GREEN" + part1(input) + RESET) }
    printTimeMillis { print("part2 input = $GREEN" + part2(input) + RESET) }
}
