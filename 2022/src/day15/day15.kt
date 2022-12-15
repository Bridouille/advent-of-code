package day15

import GREEN
import RESET
import printTimeMillis
import readInput
import kotlin.math.*

data class Point(val x: Long, val y: Long)
data class Area(val center: Point, val dist: Long) {
    fun xRangeForY(yPos: Long): Range? {
        val sizeOfRange = dist - abs(center.y - yPos)
        val minX = center.x - sizeOfRange
        val maxX = center.x + sizeOfRange
        if (minX > maxX) return null // out of range
        return Range(minX, maxX)
    }
}
data class Range(val start: Long, val end: Long) {
    /**
     * 5..15 minus 16..20 = 5..15
     * 5..15 minus 3..4 = 5..15
     * 5..15 minus 10..20 = 5..9
     * 5..15 minus 0..7 = 8..15
     * 5..15 minus 8..10 = 5..7 AND 11..15
     * 5..15 minus 4..16 = EMPTY
     */
    fun minus(other: Range): List<Range>? {
        if (other.start <= start && other.end >= end) return null
        if (other.start > end) return listOf(this)
        if (other.end < start) return listOf(this)

        return listOf(
            Range(start, min(end, other.start) - 1), Range(other.end + 1, end)
        ).filterNot { it.isEmpty() }
    }
    private fun isEmpty() = end - start < 0
    fun hasOneItem() = start == end
}

fun String.toPoints() = split(": ").let {
    val sensor = it[0].split(" ").let {
        Point(it[2].substringAfter('=').dropLast(1).toLong(), it[3].substringAfter('=').toLong())
    }
    val closest = it[1].split(" ").let {
        Point(it[4].substringAfter('=').dropLast(1).toLong(), it[5].substringAfter('=').toLong())
    }
    listOf(sensor, closest)
}

fun part1(input: List<String>, rowToLookFor: Long): Int {
    val uniquesX = mutableSetOf<Long>()
    val beaconX = mutableSetOf<Long>()

    for (line in input) {
        val p = line.toPoints()
        val dist = abs(p[0].x - p[1].x) + abs(p[0].y - p[1].y) // Manhattan dist
        val area = Area(p.first(), dist)
        area.xRangeForY(rowToLookFor)?.let {// We have a hit for this row
            for (x in (it.start..it.end)) uniquesX.add(x)
        }
        // Storing the X positions of beacons at pos.y == y
        if (p[1].y == rowToLookFor) beaconX.add(p[1].x)
    }
    uniquesX.removeAll(beaconX)
    return uniquesX.size
}

fun part2(input: List<String>, maxValue: Long): Long {
    val points = input.map { it.toPoints() }

    for (y in 0L..maxValue) { // For each possible Y pos
        var possibleX = mutableListOf(Range(0, maxValue)) // Start with the whole line being possible

        for (p in points) {
            val dist = abs(p[0].x - p[1].x) + abs(p[0].y - p[1].y) // Manhattan dist
            val area = Area(p.first(), dist)
            val range = area.xRangeForY(y) ?: continue

            val newPossibilities = mutableListOf<Range>()
            for (previousRange in possibleX) {
                previousRange.minus(range)?.let { newRanges -> // Remove the ranges from the possibles ones
                    newPossibilities.addAll(newRanges)
                }
            }
            if (possibleX.isEmpty()) break // We already exhausted this line with the previous sensors
            possibleX = newPossibilities
        }
        possibleX.firstOrNull()?.let { onlyRange ->
            if (onlyRange.hasOneItem()) {
                return onlyRange.start * 4000000L + y
            }
        }
    }
    throw IllegalStateException("Beacon not found :(")
}

fun main() {
    val testInput = readInput("day15_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput, 10L) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput, 20) + RESET) }

    val input = readInput("day15.txt")
    printTimeMillis { print("part1 input = $GREEN" + part1(input, 2000000L) + RESET) }
    printTimeMillis { print("part2 input = $GREEN" + part2(input, 4000000L) + RESET) }
}
