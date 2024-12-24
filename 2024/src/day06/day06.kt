package day06

import GREEN
import RESET
import printTimeMillis
import readInput

data class Point(val x: Int, val y: Int)

enum class Direction(val onMove: (Point) -> Point) {
    UP({ p -> Point(p.x, p.y - 1) }),
    RIGHT({ p -> Point(p.x + 1, p.y) }),
    DOWN({ p -> Point(p.x, p.y + 1) }),
    LEFT({ p -> Point(p.x - 1, p.y) }),
}

fun getInitialPos(input: List<String>): Point {
    for (y in input.indices) {
        for (x in input[y].indices) {
            if (input[y][x] == '^') return Point(x, y)
        }
    }
    throw IllegalStateException("Need a valid start")
}

fun List<CharArray>.isInBound(p: Point): Boolean = p.y >= 0 && p.y < this.size && p.x >= 0 && p.x < this[0].size

fun part1(input: List<String>): Int {
    val map = input.map { it.toCharArray() }
    var directionIdx = 0
    var position = getInitialPos(input)

    map[position.y][position.x] = 'X'
    while (true) {
        val nextPosition = Direction.values()[directionIdx % Direction.values().size].onMove(position)

        if (!map.isInBound(nextPosition)) {
            return map.sumOf { it.count { it == 'X' } } // count all visited
        } else {
            if (map[nextPosition.y][nextPosition.x] == '#') {
                directionIdx += 1
            } else {
                map[nextPosition.y][nextPosition.x] = 'X'
                position = nextPosition
            }
        }
    }
}

fun hasLoop(map: List<CharArray>, startPosition: Point): Boolean {
    var directionIdx = 0
    var position = startPosition
    var cycle = 0

    while (true) {
        val nextPosition = Direction.values()[directionIdx % Direction.values().size].onMove(position)

        if (!map.isInBound(nextPosition)) {
            return false // OOB = no loop
        } else {
            if (map[nextPosition.y][nextPosition.x] == '#') {
                directionIdx += 1
            } else {
                map[nextPosition.y][nextPosition.x] = 'V'
                position = nextPosition
                cycle += 1
            }
        }

        if (cycle > map.size * map[0].size) {
            return true
        }
    }
}

fun part2(input: List<String>): Int {
    val map = input.map { it.toCharArray() }
    val initialPosition = getInitialPos(input)
    var totalLoops = 0

    map[initialPosition.y][initialPosition.x] = 'X'
    for (y in 0 until map.size) {
        for (x in 0 until map[y].size) {
            if (map[y][x] == '.') {
                map[y][x] = '#'

                if (hasLoop(map, initialPosition)) totalLoops += 1

                for (line in map) {
                    for (i in line.indices) {
                        if (line[i] == 'V') {
                            line[i] = '.'
                        }
                    }
                }

                map[y][x] = '.'
            }
        }
    }
    return totalLoops
}

fun main() {
    val testInput = readInput("day06_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput) + RESET) }

    val input = readInput("day06.txt")
    printTimeMillis { print("part1 input = $GREEN" + part1(input) + RESET) }
    printTimeMillis { print("part2 input = $GREEN" + part2(input) + RESET) }
}
