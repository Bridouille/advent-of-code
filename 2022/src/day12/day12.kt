package day12

import GREEN
import RESET
import printTimeMillis
import readInput
import java.util.*

typealias Table = Array<Array<Point>>

data class Point(val value: Char, val minRisk: Int? = null)
data class Visit(val x: Int, val y: Int, val prev: Char, val currentRisk: Int)

fun Table.getValueOrNull(x: Int, y: Int, prev: Char): Char? {
    if (x < 0 || y < 0 || y >= size || x >= this[y].size) return null // oob
    if (this[y][x].value == 'E' && prev != 'z') return null // this is the end, but we didn't come from 'z'
    if (this[y][x].value > prev + 1) return null // can climb to +1 letter or go down
    return this[y][x].value
}

fun runDijkstra(table: Table, start: Pair<Int, Int>): Int? {
    val toVisits: PriorityQueue<Visit> = PriorityQueue<Visit> { a, b ->
        a.currentRisk - b.currentRisk
    }.also { it.add(Visit(start.first, start.second, 'a', 0)) }

    while (toVisits.isNotEmpty()) {
        val curr = toVisits.remove()
        val x = curr.x
        val y = curr.y
        val cumulatedRisk = curr.currentRisk + 1 // each step += 1 risk
        val currMinRisk = table[y][x].minRisk

        // We have visited this path and have already a risk lower than the current exploration
        if (currMinRisk != null && currMinRisk <= cumulatedRisk) continue

        table[y][x] = table[y][x].copy(minRisk = cumulatedRisk) // update the smallest risk for the cell

        val currValue = table[y][x].value
        if (currValue == 'E' && curr.prev == 'z') { // destination reached
            return cumulatedRisk - 1
        } else {
            // add the next valid (non-null) paths to visit in an ordered stack
            table.getValueOrNull(x + 1, y, currValue)?.let { toVisits.add(Visit(x + 1, y, currValue, cumulatedRisk)) } // right
            table.getValueOrNull(x, y + 1, currValue)?.let { toVisits.add(Visit(x, y + 1, currValue, cumulatedRisk)) } // bottom
            table.getValueOrNull(x - 1, y, currValue)?.let { toVisits.add(Visit(x - 1, y, currValue, cumulatedRisk)) } // left
            table.getValueOrNull(x, y - 1, currValue)?.let { toVisits.add(Visit(x, y - 1, currValue, cumulatedRisk)) } // top
        }
    }
    return null // no path found
}

fun part1(input: List<String>): Int? {
    val start = input.indexOfFirst { it.contains("S") }.let {
        Pair(input[it].indexOfFirst { it == 'S' }, it)
    }
    val newInput = input.map { it.map{ Point(it) }.toTypedArray() }.toTypedArray()
    newInput[start.second][start.first] = newInput[start.second][start.first].copy(value = 'a')
    return runDijkstra(newInput, start)
}

fun part2(input: List<String>): Int {
    val start = input.indexOfFirst { it.contains("S") }.let {
        Pair(input[it].indexOfFirst { it == 'S' }, it)
    }
    val newInput = input.map { it.map{ Point(it) }.toTypedArray() }.toTypedArray()
    newInput[start.second][start.first] = newInput[start.second][start.first].copy(value = 'a')
    val starts = newInput.flatMapIndexed { y, line ->
        line.mapIndexed { x, c -> if (c.value == 'a') Pair(x, y) else null }.filterNotNull()
    }
    return starts.mapNotNull { runDijkstra(newInput, it) }.minOf { it }
}

fun main() {
    val testInput = readInput("day12_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput) + RESET) }

    val input = readInput("day12.txt")
    printTimeMillis { print("part1 input = $GREEN" + part1(input) + RESET) }
    printTimeMillis { print("part2 input = $GREEN" + part2(input) + RESET) }
}
