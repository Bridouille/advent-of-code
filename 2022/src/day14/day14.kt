package day14

import GREEN
import RESET
import printTimeMillis
import readInput
import kotlin.math.*

data class Point(val x: Int, val y: Int) {
    fun below() = copy(x = x, y = y + 1)
    fun leftDiago() = copy(x = x - 1, y = y + 1)
    fun rightDiago() = copy(x = x + 1, y = y + 1)
}

// "498,4 -> 498,6 -> 496,6" => setOf([498, 4], [498, 5], [498, 6], [497, 6], [496, 6]))
fun List<String>.toPoints() = map {
    val ret = mutableSetOf<Point>()
    it.split(" -> ").windowed(2, 1) {
        val a = it[0].split(",").let { Point(it[0].toInt(), it[1].toInt()) }
        val b = it[1].split(",").let { Point(it[0].toInt(), it[1].toInt()) }

        val res = when {
            a.x == b.x -> (min(a.y, b.y)..max(a.y, b.y)).map { Point(a.x, it) }
            a.y == b.y -> (min(a.x, b.x)..max(a.x, b.x)).map { Point(it, a.y) }
            else -> throw IllegalStateException("Not a line")
        }
        ret.addAll(res)
    }
    ret
}.fold(mutableSetOf<Point>()) { set, points ->
    set.also { set.addAll(points) }
}

fun findNextPos(start: Point, points: Set<Point>, abyssesY: Int? = null): Point? {
    if (abyssesY != null && start.y >= abyssesY) return null // We reached the abysses :(
    if (points.contains(start.below())) { // there's an obstacle down
        return if (points.contains(start.leftDiago())) { // there's an obstacle left
            if (points.contains(start.rightDiago())) { // there's an obstacle right, we sit here
                start
            } else {
                findNextPos(start.rightDiago(), points, abyssesY) // continue right
            }
        } else {
            findNextPos(start.leftDiago(), points, abyssesY) // continue left
        }
    }
    return findNextPos(start.below(), points, abyssesY) // continue down
}

fun part1(input: List<String>) = input.toPoints().let { points ->
    val startSize = points.size
    val abysses = points.maxBy { it.y }.y
    val fallOrigin = Point(500, 0)

    var nextPos = findNextPos(fallOrigin, points, abysses)
    while (nextPos != null) {
        points.add(nextPos)
        nextPos = findNextPos(fallOrigin, points, abysses)
    }
    points.size - startSize
}

fun part2(input: List<String>) = input.toPoints().let { points ->
    val start = points.size
    val fallOrigin = Point(500, 0)
    val lowest = points.maxBy { it.y }.y + 2
    val maxY = abs(0 - lowest)
    val bottomLine = ((fallOrigin.x - maxY)..(fallOrigin.x + maxY)).map { Point(it, lowest) }.toSet()
    points.addAll(bottomLine)

    var nextPos = findNextPos(fallOrigin, points)!!
    while (nextPos != fallOrigin) {
        points.add(nextPos)
        nextPos = findNextPos(fallOrigin, points)!!
    }
    (points.size - start - bottomLine.size) + 1 // hop petit +1 pour fix
}

fun main() {
    val testInput = readInput("day14_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput) + RESET) }

    val input = readInput("day14.txt")
    printTimeMillis { print("part1 input = $GREEN" + part1(input) + RESET) }
    printTimeMillis { print("part2 input = $GREEN" + part2(input) + RESET) }
}
