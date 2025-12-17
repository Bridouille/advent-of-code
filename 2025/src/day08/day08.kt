package day08

import GREEN
import RESET
import printTimeMillis
import readInput
import kotlin.math.sqrt

data class Point(val x: Int, val y: Int, val z: Int) {
    fun distanceTo(other: Point): Double {
        val dx = (other.x - x).toDouble()
        val dy = (other.y - y).toDouble()
        val dz = (other.z - z).toDouble()
        return sqrt(dx * dx + dy * dy + dz * dz)
    }
}

private fun getShortedConnectedBoxes(boxes: List<Point>): MutableList<Pair<Point, Point>> {
    // Calculate all distances between boxes
    val distances = mutableMapOf<Pair<Point, Point>, Double>()
    for (i in boxes.indices) {
        for (j in i + 1..boxes.size - 1) {
            distances.put(Pair(boxes[i], boxes[j]), boxes[i].distanceTo(boxes[j]))
        }
    }
    // Sort by distance
    val shortestConnectedBoxes = distances.toList()
        .sortedBy { it.second }
        .map { it.first }.toMutableList()

    return shortestConnectedBoxes
}

fun part1(input: List<String>): Long {
    val boxes = input.mapIndexed { idx, item ->
        val coord = item.split(",")
        Point(coord[0].toInt(), coord[1].toInt(), coord[2].toInt())
    }
    val shortestConnectedBoxes = getShortedConnectedBoxes(boxes)
    val circuits = boxes.map { mutableListOf(it) }.toMutableList()

    for (i in 1..1000) {
        if (shortestConnectedBoxes.isEmpty()) break
        val shortest = shortestConnectedBoxes.removeFirst()
        val firstIdx = circuits.indexOfFirst { it.contains(shortest.first) }
        val secondIdx = circuits.indexOfFirst { it.contains(shortest.second) }

        if (firstIdx == secondIdx) continue

        // merge circuits
        circuits[firstIdx].addAll(circuits[secondIdx])
        circuits.removeAt(secondIdx)
    }
    return circuits.sortedByDescending { it.size }.take(3).map { it.size }.fold(1L, { acc, i -> acc * i })
}

fun part2(input: List<String>): Long {
    val boxes = input.mapIndexed { idx, item ->
        val coord = item.split(",")
        Point(coord[0].toInt(), coord[1].toInt(), coord[2].toInt())
    }
    val shortestConnectedBoxes = getShortedConnectedBoxes(boxes)
    val circuits = boxes.map { mutableListOf(it) }.toMutableList()

    while (circuits.size > 1) {
        if (shortestConnectedBoxes.isEmpty()) break
        val shortest = shortestConnectedBoxes.removeFirst()
        val firstIdx = circuits.indexOfFirst { it.contains(shortest.first) }
        val secondIdx = circuits.indexOfFirst { it.contains(shortest.second) }

        if (firstIdx == secondIdx) continue
        if (circuits.size == 2) {
            return shortest.first.x.toLong() * shortest.second.x
        }

        // merge circuits
        circuits[firstIdx].addAll(circuits[secondIdx])
        circuits.removeAt(secondIdx)
    }
    return -1
}

fun main() {
    val testInput = readInput("day08_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput) + RESET) }

    val input = readInput("day08.txt")
    printTimeMillis { print("part1 input = $GREEN" + part1(input) + RESET) }
    printTimeMillis { print("part2 input = $GREEN" + part2(input) + RESET) }
}
