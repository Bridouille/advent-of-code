package day24

import GREEN
import RESET
import printTimeMillis
import readInput
import java.util.*
import kotlin.math.abs

typealias Iter = (Point, Int) -> Point

data class Point(val x: Int, val y: Int) {
    fun neighbors() = listOf(
        Point(x + 1, y), Point(x, y + 1), Point(x - 1, y), Point(x, y - 1),
    )
}
data class Blizzard(val pos: Point, val wind: Iter) {
    override fun toString() = pos.toString()
}

data class Basin(
    val start: Point,
    val end: Point,
    val maxX: Int,
    val maxY: Int,
    val blizzards: Set<Blizzard>
)  {
    fun getBlizzards(minute: Int) = blizzards.map { it.wind(it.pos, minute) }.toSet()
}

fun toBasin(input: List<String>) = input.drop(1).dropLast(1).map {
    it.substring(1, it.lastIndex)
}.let {
    val maxX = it.first().length - 1
    val maxY = it.size - 1
    val start = Point(0, -1)
    val end = Point(maxX, it.size)

    val blizzards = mutableSetOf<Blizzard>()
    for (y in it.indices) {
        for (x in it[y].indices) {
            when (it[y][x]) {
                'v' -> blizzards.add(Blizzard(Point(x, y)) { p, mn -> Point(p.x, (p.y + mn).mod(maxY + 1)) })
                '^' -> blizzards.add(Blizzard(Point(x, y)) { p, mn -> Point(p.x, (p.y - mn).mod(maxY + 1)) })
                '>' -> blizzards.add(Blizzard(Point(x, y)) { p, mn -> Point((p.x + mn).mod(maxX + 1), p.y) })
                '<' -> blizzards.add(Blizzard(Point(x, y)) { p, mn -> Point((p.x - mn).mod(maxX + 1), p.y) })
            }
        }
    }
    Basin(start, end, maxX, maxY, blizzards)
}

data class Visit(val p: Point, val minute: Int)

fun runBfs(
    start: Point,
    end: Point,
    blizCache: MutableMap<Int, Set<Point>>,
    basin: Basin,
    initialMinute: Int = 0
): Int {
    val queue = mutableListOf<Visit>().also { it.add(Visit(start, initialMinute)) }
    val visited = mutableSetOf<Visit>()

    while (queue.isNotEmpty()) {
        val next = queue.removeFirst()
        if (visited.contains(next)) continue
        visited.add(next)

        if (Point(next.p.x, next.p.y + 1) == end ||
            Point(next.p.x, next.p.y - 1) == end) { // reached the end
            return (next.minute + 1) - initialMinute
        }

        for (i in next.minute..next.minute + 1) {
            if (!blizCache.contains(i)) { // generate and cache the blizzard positions
                blizCache[i] = basin.getBlizzards(i)
            }
        }

        // We waited here but stuck in the blizzard, discard this option
        if (blizCache[next.minute]!!.contains(next.p)) continue

        val possible = next.p.neighbors().filterNot {
            blizCache[next.minute + 1]!!.contains(it)
        }.filterNot {
            it.x < 0 || it.x > basin.maxX || it.y < 0 || it.y > basin.maxY
        }
        queue.add(Visit(next.p, next.minute + 1)) // wait here
        queue.addAll(possible.map { Visit(it, next.minute + 1) })
    }
    return -1
}

fun part1(input: List<String>): Int {
    val basin = toBasin(input)
    val blizCache = mutableMapOf<Int, Set<Point>>()
    return runBfs(basin.start, basin.end, blizCache, basin)
}

fun part2(input: List<String>): Int {
    val basin = toBasin(input)
    val blizCache = mutableMapOf<Int, Set<Point>>()

    val there = runBfs(basin.start, basin.end, blizCache, basin)
    val back = runBfs(basin.end, basin.start, blizCache, basin, there)
    val thereAgain = runBfs(basin.start, basin.end, blizCache, basin, there + back)
    return there + back + thereAgain
}

fun main() {
    val testInput = readInput("day24_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput) + RESET) }

    val input = readInput("day24.txt")
    printTimeMillis { print("part1 input = $GREEN" + part1(input) + RESET) } // 322
    printTimeMillis { print("part2 input = $GREEN" + part2(input) + RESET) }
}
