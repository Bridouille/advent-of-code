package day11

import GREEN
import RESET
import printTimeMillis
import readInput
import kotlin.math.abs

data class Point(val x: Int, val y: Int)

private fun calculateDistances(
    galaxies: List<Point>,
    emptyColumns: List<Int>,
    emptyRows: List<Int>,
    stretchFactor: Int
): Long {
    var distance = 0L

    for (i in 0 until galaxies.lastIndex) {
        for (j in i + 1..galaxies.lastIndex) {
            val a = galaxies[i]
            val b = galaxies[j]

            val aXOffset = emptyColumns.count { it < a.x } * (stretchFactor - 1)
            val bXOffset = emptyColumns.count { it < b.x } * (stretchFactor - 1)
            val aYOffset = emptyRows.count { it < a.y } * (stretchFactor - 1)
            val bYOffset = emptyRows.count { it < b.y } * (stretchFactor - 1)

            distance += abs((b.x + bXOffset) - (a.x + aXOffset)) + abs((b.y + bYOffset) - (a.y + aYOffset))
        }
    }
    return distance
}

private fun getEmptyRowsIdx(input: List<String>) = buildList {
    for (y in input.indices) {
        if (input[y].all { it == '.' }) add(y)
    }
}

private fun getEmptyColumnsIdx(input: List<String>) = buildList {
    for (x in input[0].indices) {
        var hasGalaxy = false
        for (y in input.indices) {
            if (input[y][x] == '#') {
                hasGalaxy = true
            }
        }
        if (!hasGalaxy) {
            add(x)
        }
    }
}

private fun getGalaxies(input: List<String>) = buildList {
    for (y in input.indices) {
        for (x in input[y].indices) {
            if (input[y][x] == '#') {
                add(Point(x, y))
            }
        }
    }
}

fun part1(input: List<String>): Long {
    return calculateDistances(
        getGalaxies(input),
        getEmptyColumnsIdx(input),
        getEmptyRowsIdx(input),
        2
    )
}

fun part2(input: List<String>): Long {
    return calculateDistances(
        getGalaxies(input),
        getEmptyColumnsIdx(input),
        getEmptyRowsIdx(input),
        1_000_000
    )
}

fun main() {
    val testInput = readInput("day11_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput) + RESET) }

    val input = readInput("day11.txt")
    printTimeMillis { print("part1 input = $GREEN" + part1(input) + RESET) }
    printTimeMillis { print("part2 input = $GREEN" + part2(input) + RESET) }
}
