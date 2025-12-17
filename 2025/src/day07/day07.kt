package day07

import GREEN
import RESET
import printTimeMillis
import readInput

data class Point(val x: Int, val y: Int)

private fun getStart(map: Array<CharArray>): Point {
    for (y in map.indices) {
        for (x in map[0].indices) {
            if (map[y][x] == 'S') {
                return Point(x, y)
            }
        }
    }
    throw IllegalStateException("Missing start S")
}

fun part1(input: List<String>): Int {
    val map = input.map { it.toCharArray() }.toTypedArray()
    val start = getStart(map)
    val falling = mutableListOf<Point>()
    var splitting = 0

    falling.add(start)
    while (falling.isNotEmpty()) {
        val first = falling.removeFirst()

        val down = Point(first.x, first.y + 1)
        if (down.y >= map.size) continue

        when (map[down.y][down.x]) {
            '.' -> {
                map[down.y][down.x] = '|'
                falling.add(down)
            }
            '^' -> {
                splitting += 1
                if (down.x - 1 >= 0) {
                    falling.add(Point(down.x - 1, down.y))
                    map[down.y][down.x - 1] = '|'
                }
                if (down.x + 1 < map[0].size) {
                    falling.add(Point(down.x + 1, down.y))
                    map[down.y][down.x + 1] = '|'
                }
            }
        }
    }
    return splitting
}

private val cache = mutableMapOf<Point, Long>()

private fun explore(map: Array<CharArray>, p: Point): Long {
    if (cache.containsKey(p)) return cache[p]!!
    if (p.x < 0 || p.x + 1 >= map[0].size) return 0
    if (p.y >= map.size) return 1

    when (map[p.y][p.x]) {
        '.' -> {
            map[p.y][p.x] = '|'
            val ret = explore(map, Point(p.x, p.y + 1))
            map[p.y][p.x] = '.'
            cache[p] = ret
            return ret
        }
        '^' -> {
            val ret = explore(map, Point(p.x - 1, p.y)) + explore(map, Point(p.x + 1, p.y))
            cache[p] = ret
            return ret
        }
    }
    return 1
}

fun part2(input: List<String>): Long {
    val map = input.map { it.toCharArray() }.toTypedArray()
    val start = getStart(map)

    return explore(map, Point(start.x, start.y + 1)) + 1
}

fun main() {
    val testInput = readInput("day07_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput) + RESET) }

    val input = readInput("day07.txt")
    printTimeMillis { print("part1 input = $GREEN" + part1(input) + RESET) }
    printTimeMillis { print("part2 input = $GREEN" + part2(input) + RESET) }
}
