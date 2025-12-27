package day09

import GREEN
import RESET
import printTimeMillis
import readInput
import kotlin.math.abs

data class Point(val x: Int, val y: Int)

fun part1(input: List<String>): Long {
    val pts = input.map { Point(it.split(',')[0].toInt(), it.split(',')[1].toInt()) }
    var maxArea = 0L

    for (i in 0..pts.size - 1) {
        for (j in i + 1..pts.size - 1) {
            val deltaX = abs(pts[i].x - pts[j].x) + 1
            val deltaY = abs(pts[i].y - pts[j].y) + 1

            val area = deltaX * deltaY.toLong()
            if (area > maxArea) {
                maxArea = area
            }
        }
    }
    return maxArea
}

fun part2(input: List<String>): Int {
    val pts = input.map { Point(it.split(',')[0].toInt(), it.split(',')[1].toInt()) }.toMutableList()
    val visited = mutableSetOf<Point>()
    val toVisit = mutableListOf<Point>()
    val perimeter = mutableListOf<Point>() // TODO

    toVisit.add(pts.first())
    while (toVisit.isNotEmpty()) {
        val pt = toVisit.removeFirst()
        visited.add(pt)

        val vertical = pts.filterNot { visited.contains(it) }.filter { it.x == pt.x }.minByOrNull { abs(pt.y - it.y) }
        // next point is on left or right
        if (vertical == null) {
            val horizontal = pts.filterNot { visited.contains(it) }.filter { it.y == pt.y }.minByOrNull { abs(pt.x - it.x) }
            println("From point $pt next point is horizontal: $horizontal")
            if (horizontal != null) {
                // TODO: last point must be connected here?
                toVisit.add(horizontal)
            }
        } else {
            println("From point $pt next point is vertical: $vertical")
            toVisit.add(vertical)
        }
    }
    return 2
}

fun main() {
    val testInput = readInput("day09_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput) + RESET) }

    val input = readInput("day09.txt")
    // printTimeMillis { print("part1 input = $GREEN" + part1(input) + RESET) }
    // printTimeMillis { print("part2 input = $GREEN" + part2(input) + RESET) }
}
