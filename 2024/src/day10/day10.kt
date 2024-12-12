package day10

import GREEN
import RESET
import printTimeMillis
import readInput

const val VISITED = -3

fun getScore(map: List<MutableList<Int>>, y: Int, x: Int, toFind: Int, distinct: Boolean): Int {
    if (y < 0 || y >= map.size || x < 0 || x >= map[0].size) return 0
    if (map[y][x] != toFind) return 0
    if (toFind == 9) {
        if (distinct) {
            map[y][x] = VISITED
        }
        return 1
    }
    return getScore(map, y - 1, x, toFind + 1, distinct) +
            getScore(map, y + 1, x, toFind + 1, distinct) +
            getScore(map, y, x - 1, toFind + 1, distinct) +
            getScore(map, y, x + 1, toFind + 1, distinct)
}

fun countTrails(input: List<String>, distinct: Boolean): Int {
    val list = input.map { it.toCharArray().map { it.digitToIntOrNull() ?: - 1 }.toMutableList() }
    var total = 0

    for (y in list.indices) {
        for (x in list[y].indices) {
            if (list[y][x] == 0) {
                total += getScore(list, y, x, 0, distinct)

                if (distinct) {
                    // Reset the initial 9s for further trailheads
                    list.forEach { it.replaceAll { if (it == VISITED) 9 else it } }
                }
            }
        }
    }
    return total
}

fun part1(input: List<String>) = countTrails(input, distinct = true)

fun part2(input: List<String>) = countTrails(input, distinct = false)

fun main() {
    val testInput = readInput("day10_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput) + RESET) }

    val input = readInput("day10.txt")
    printTimeMillis { print("part1 input = $GREEN" + part1(input) + RESET) }
    printTimeMillis { print("part2 input = $GREEN" + part2(input) + RESET) }
}
