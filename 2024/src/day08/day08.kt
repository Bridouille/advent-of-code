package day08

import GREEN
import RESET
import printTimeMillis
import readInput
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

data class Point(val x: Int, val y: Int)

fun buildFrequenciesMap(input: List<String>): Map<Char, List<Point>> {
    return buildMap {
        for (y in input.indices) {
            for (x in input[y].indices) {
                if (input[y][x] != '.') {
                    val freq = input[y][x]

                    if (contains(freq)) {
                        put(freq, get(freq)!! + listOf(Point(x, y)))
                    } else {
                        put(freq, listOf(Point(x, y)))
                    }
                }
            }
        }
    }
}

fun part1(input: List<String>): Int {
    val freq = buildFrequenciesMap(input)
    val antinodes = mutableMapOf<Char, List<Point>>()

    for (key in freq.keys) {
        val frequencies = freq[key]!!

        for (i in 0 until frequencies.size - 1) {
            val a = frequencies[i]

            for (j in i + 1 until frequencies.size) {
                val b = frequencies[j]

                val yDiff = abs(b.y - a.y)
                val xDiff = abs(b.x - a.x)
                val pts = listOf(
                    Point(if (b.x - a.x > 0) { min(a.x, b.x) - xDiff } else { max(a.x, b.x) + xDiff }, min(a.y, b.y) - yDiff),
                    Point(if (b.x - a.x > 0) { max(a.x, b.x) + xDiff } else { min(a.x, b.x) - xDiff }, max(a.y, b.y) + yDiff)
                )

                val filteredOob = pts.filter {
                    it.y >= 0 && it.y < input.size && it.x >= 0 && it.x < input[0].length
                }
                if (antinodes.contains(key)) {
                    antinodes[key] = antinodes[key]!! + filteredOob
                } else {
                    antinodes[key] = filteredOob
                }
            }
        }
    }
    return antinodes.values.flatten().toSet().size
}

fun part2(input: List<String>): Int {
    val freq = buildFrequenciesMap(input)
    val antinodes = mutableMapOf<Char, List<Point>>()

    for (key in freq.keys) {
        val frequencies = freq[key]!!

        for (i in 0 until frequencies.size - 1) {
            val a = frequencies[i]

            for (j in i + 1 until frequencies.size) {
                val b = frequencies[j]

                val yDiff = abs(b.y - a.y)
                val xDiff = abs(b.x - a.x)
                val pts = buildList{
                    var cycle = 1
                    do {
                        val xOffset = xDiff * cycle
                        val yOffset = yDiff * cycle

                        add(Point(if (b.x - a.x > 0) { min(a.x, b.x) - xOffset } else { max(a.x, b.x) + xOffset }, min(a.y, b.y) - yOffset))
                        add(Point(if (b.x - a.x > 0) { max(a.x, b.x) + xOffset } else { min(a.x, b.x) - xOffset }, max(a.y, b.y) + yOffset))
                        cycle += 1
                    } while (
                        min(a.x, b.x) - xOffset >= 0 || min(a.y, b.y) - yOffset >= 0 ||
                        max(a.x, b.x) + xOffset < input[0].length || max(a.y, b.y) + yOffset < input.size
                    )
                }

                val filteredOob = pts.filter {
                    it.y >= 0 && it.y < input.size && it.x >= 0 && it.x < input[0].length && input[it.y][it.x] == '.'
                }
                if (antinodes.contains(key)) {
                    antinodes[key] = antinodes[key]!! + filteredOob
                } else {
                    antinodes[key] = filteredOob
                }
            }
        }
    }

    return antinodes.values.flatten().toSet().size + freq.filter { it.value.size > 1 }.values.flatten().size
}

fun main() {
    val testInput = readInput("day08_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput) + RESET) }

    val input = readInput("day08.txt")
    printTimeMillis { print("part1 input = $GREEN" + part1(input) + RESET) }
    printTimeMillis { print("part2 input = $GREEN" + part2(input) + RESET) }
}
