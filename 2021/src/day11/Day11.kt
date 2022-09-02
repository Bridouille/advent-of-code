package day11

import readInput

fun flash(list: Array<IntArray>, x: Int, y: Int) {
    if (x < 0 || y < 0 || y >= list.size || x >= list[y].size) {
        return
    }
    val current = list[y][x]
    list[y][x] += 1

    if (current == 9) {
        for (i in -1..1) {
            for (j in -1..1) {
                if (!(i == 0 && j == 0)) {
                    flash(list, x + i, y + j)
                }
            }
        }
    }
}

fun part1(lines: List<String>) : Int {
    val octopuses = Array(lines.size) { intArrayOf() }
    for (i in lines.indices) octopuses[i] = lines[i].chunked(1).map{ it.toInt() }.toIntArray()

    val numberOfSteps = 100
    var flashes = 0

    for (i in 1..numberOfSteps) {
        for (y in octopuses.indices) {
            for (x in octopuses.indices) {
                flash(octopuses, x, y)
            }
        }
        // reset the > 9 by 0's
        for (y in octopuses.indices) {
            for (x in octopuses.indices) {
                if (octopuses[y][x] > 9) {
                    flashes++
                    octopuses[y][x] = 0
                }
            }
        }
    }
    return flashes
}

fun part2(lines: List<String>) : Long {
    val octopuses = Array(lines.size) { intArrayOf() }
    for (i in lines.indices) octopuses[i] = lines[i].chunked(1).map{ it.toInt() }.toIntArray()

    var steps = 0L
    while (true) {
        for (y in octopuses.indices) {
            for (x in octopuses.indices) {
                flash(octopuses, x, y)
            }
        }
        // reset the > 9 by 0's
        var allFlashing = true
        for (y in octopuses.indices) {
            for (x in octopuses.indices) {
                if (octopuses[y][x] > 9) {
                    octopuses[y][x] = 0
                } else {
                    allFlashing = false
                }
            }
        }

        if (allFlashing) return ++steps
        steps++
    }
    return -1
}

fun main() {
    val simpleInput = readInput("day11/simple")
    println("part1(simpleInput) => " + part1(simpleInput))
    println("part2(simpleInput) => " + part2(simpleInput))

    val testInput = readInput("day11/test")
    println("part1(testInput) => " + part1(testInput))
    println("part2(testInput) => " + part2(testInput))

    val input = readInput("day11/input")
    println("part1(input) => " + part1(input))
    println("part1(input) => " + part2(input))
}
