package day04

import GREEN
import RESET
import printTimeMillis
import readInput

private fun isRoll(map: Array<CharArray>, x: Int, y: Int): Boolean {
    if (x < 0 || x >= map[0].size) return false
    if (y < 0 || y >= map.size) return false
    return map[y][x] == '@'
}

private fun hasForklift(map: Array<CharArray>, x: Int, y: Int): Boolean {
    var adjacentRolls = 0
    for (i in -1..1) {
        for (j in -1..1) {
            if (i == 0 && j == 0) continue // don't count ourselves
            if (isRoll(map, x + i, y + j)) {
                adjacentRolls += 1
            }
        }
    }
    return adjacentRolls < 4
}

fun part1(input: List<String>): Int {
    val map = input.map { it.toCharArray() }.toTypedArray()

    var total = 0
    for (y in map.indices) {
        for (x in map[0].indices) {
            if (map[y][x] == '@' && hasForklift(map, x, y)) {
                total += 1
            }
        }
    }
    return total
}

data class Point(val x: Int, val y: Int)

fun part2(input: List<String>): Int {
    val map = input.map { it.toCharArray() }.toTypedArray()
    var totalRemoved = 0

    do {
        val toRemove = mutableSetOf<Point>()
        for (y in map.indices) {
            for (x in map[0].indices) {
                if (map[y][x] == '@' && hasForklift(map, x, y)) {
                    toRemove.add(Point(x, y))
                }
            }
        }
        totalRemoved += toRemove.size
        for (p in toRemove) {
            map[p.y][p.x] = 'X'
        }
    } while (toRemove.isNotEmpty())

    return totalRemoved
}

fun main() {
    val testInput = readInput("day04_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput) + RESET) }

    val input = readInput("day04.txt")
    printTimeMillis { print("part1 input = $GREEN" + part1(input) + RESET) }
    printTimeMillis { print("part2 input = $GREEN" + part2(input) + RESET) }
}
