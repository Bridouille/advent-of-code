package day18

import GREEN
import RESET
import printTimeMillis
import readInput
import java.lang.Math.abs

data class Point(val x: Int, val y: Int, val z: Int) {
    fun neighbors() = listOf(
        Point(x - 1, y, z), Point(x + 1, y, z),
        Point(x, y - 1, z), Point(x, y + 1, z),
        Point(x, y,z - 1), Point(x, y,z + 1),
    )
}

fun surfaceArea(points: List<Point>) = points.sumOf {
    it.neighbors().count { it !in points }
}

fun part1(input: List<String>) = input.map {
    it.split(",").map { it.toInt() }.let { Point(it[0], it[1], it[2]) }
}.let { points -> surfaceArea(points) }

fun part2(input: List<String>) = input.map {
    it.split(",").map { it.toInt() }.let { Point(it[0], it[1], it[2]) }
}.let { points ->
    val minX = points.minBy { it.x }.x
    val maxX = points.maxBy { it.x }.x
    val minY = points.minBy { it.y }.y
    val maxY = points.maxBy { it.y }.y
    val minZ = points.minBy { it.z }.z
    val maxZ = points.maxBy { it.z }.z

    // Generating a cubic set of points in the "outside" of the shape
    // Doing a DFS from an edge of the cube tills we reach the other edge / encounter a point in the shape
    val outside = buildSet {
        val stack = mutableListOf(Point(minX, minY, minZ).also { add(it) })
        while (stack.isNotEmpty()) {
            for (neighbor in stack.removeLast().neighbors()) {
                // Within the bound of the cube, but is not in the original points
                if (neighbor.x in minX-1..maxX+1 &&
                    neighbor.y in minY-1..maxY+1 &&
                    neighbor.z in minZ-1..maxZ+1 &&
                    neighbor !in points
                ) {
                    if (add(neighbor)) { // If we haven't visited it, visit it next
                        stack.add(neighbor)
                    }
                }
            }
        }
    }
    outside.map { it.neighbors().count { it in points } }.sum()
}

fun main() {
    val testInput = readInput("day18_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput) + RESET) }

    val input = readInput("day18.txt")
    printTimeMillis { print("part1 input = $GREEN" + part1(input) + RESET) }
    printTimeMillis { print("part2 input = $GREEN" + part2(input) + RESET) }
}
