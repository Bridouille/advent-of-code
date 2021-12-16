package day15

import readInput
import java.util.*

typealias Table = Array<Array<Point>>

data class Point(val value: Int, val minRisk: Int? = null)
data class Visit(val x: Int, val y: Int, val currentRisk: Int)

fun List<String>.toMultipliedTable(multiplyBy: Int = 5): Table {
    val table = Array(size * multiplyBy) { arrayOf<Point>() }
    for (i in 0 until size * multiplyBy) {
        val row = mutableListOf<Point>()
        val downMultiplier = i / size
        repeat(multiplyBy) { repeat ->
            row.addAll(this[i % size].chunked(1).map {
                val newNumber = (it.toInt() + repeat + downMultiplier)
                Point(if (newNumber > 9) newNumber % 9 else newNumber)
            }.toTypedArray())
        }
        table[i] = row.toTypedArray()
    }
    return table
}

fun Table.getValueOrNull(x: Int, y: Int) = if (x < 0 || y < 0 || y >= size || x >= this[y].size) null else this[y][x].value

fun runDijkstra(table: Table) : Int {
    val toVisits: PriorityQueue<Visit> = PriorityQueue<Visit> { a, b ->
        a.currentRisk - b.currentRisk
    }.also { it.add(Visit(0, 0 ,0)) } // start on the top left, with 0 risk

    while (toVisits.isNotEmpty()) {
        val curr = toVisits.remove()
        val x = curr.x
        val y = curr.y
        val cumulatedRisk = curr.currentRisk + table[y][x].value

        val currMinRisk = table[y][x].minRisk
        // We have visited this path and have already a risk lower than the current exploration
        if (currMinRisk != null && currMinRisk <= cumulatedRisk) {
            continue
        }
        table[y][x] = table[y][x].copy(minRisk = cumulatedRisk) // update the smallest risk for the cell

        if (y == table.size - 1 && x == table[y].size - 1) { // destination reached
            println("found final destination ($x, $y) = $cumulatedRisk")
            break
        } else {
            mapOf(
                    Visit(x + 1, y, cumulatedRisk) to table.getValueOrNull(x + 1, y), // right
                    Visit(x, y + 1, cumulatedRisk) to table.getValueOrNull(x, y + 1), // bottom
                    Visit(x - 1, y, cumulatedRisk) to table.getValueOrNull(x - 1, y), // left
                    Visit(x, y - 1, cumulatedRisk) to table.getValueOrNull(x,y - 1), // top
            ).filterValues { it != null } // remove OOB values
            .forEach {
                toVisits.add(it.key) // add the next paths to visit in an ordered stack
            }
        }
    }
    return table.last().last().minRisk?.let { it - table.first().first().value } ?: -1
}

fun part1(lines: List<String>) = runDijkstra(lines.toMultipliedTable(1))

fun part2(lines: List<String>) = runDijkstra(lines.toMultipliedTable(5))

fun main() {
    val testInput = readInput("day15/test")
    println("part1(testInput) => " + part1(testInput)) // 40
    println("part2(testInput) => " + part2(testInput)) // 315

    val input = readInput("day15/input")
    println("part1(input) => " + part1(input)) // 717
    println("part2(input) => " + part2(input)) // 2993
}
