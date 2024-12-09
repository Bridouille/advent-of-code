package day09

import GREEN
import RESET
import printTimeMillis
import readInput
import kotlin.math.exp

fun String.expand(): List<Int> {
    val ret = mutableListOf<Int>()
    var ID = 0

    for (i in this.indices) {
        if (i % 2 == 0) {
            ret.addAll(List(this[i].toString().toInt()) { ID })
            ID += 1
        } else {
            ret.addAll(List(this[i].toString().toInt()) { -1 })
        }
    }
    return ret
}

fun IntArray.compact() {
    var start = 0
    var end = lastIndex

    while (end > start) {
        while (this[end] == -1) end -= 1
        while (this[start] != -1) start += 1

        if (start > end) break

        this[start] = this[end]
        this[end] = -1
    }
}

fun part1(input: List<String>): Long {
    val str = input.first()
    val expanded = str.expand().toIntArray()

    expanded.compact()
    return expanded.filter { it >= 0 }.mapIndexed { idx, id -> idx * id.toLong() }.sum()
}

fun IntArray.compact2() {
    val removed = mutableSetOf<Int>()
    val filled = mutableSetOf<Int>()
    var end = lastIndex

    while (end > 0) {
        while (end > 0 && this[end] == -1 || filled.contains(end)) end -= 1
        if (end < 0) break
        val endRange = end
        val digit = this[end]
        while (end > 0 && this[end] == digit) end -= 1
        if (end < 0) break
        val startRange = end + 1
        val rangeSize = endRange - startRange

        for (i in 0..lastIndex - rangeSize) {
            var valid = true
            for (j in 0..rangeSize) {
                if (i + j > startRange || this[i + j] != -1 || removed.contains(i + j)) {
                    valid = false
                }
            }

            if (valid) {
                for (idx in 0..rangeSize) {
                    this[i + idx] = this[startRange + idx]
                    this[startRange + idx] = -1
                    filled.add(i + idx)
                    removed.add(startRange + idx)
                }
                break
            }
        }
    }
}

fun part2(input: List<String>): Long {
    val str = input.first()
    val expanded = str.expand().toIntArray()

    expanded.compact2()
    return expanded.mapIndexed { idx, id ->
        if (id < 0) { 0L } else { idx * id.toLong() }
    }.sum()
}

fun main() {
    val testInput = readInput("day09_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput) + RESET) }

    val input = readInput("day09.txt")
    printTimeMillis { print("part1 input = $GREEN" + part1(input) + RESET) }
    printTimeMillis { print("part2 input = $GREEN" + part2(input) + RESET) }
}
