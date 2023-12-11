package day10

import GREEN
import RESET
import printTimeMillis
import readInput

private fun findStart(table: Array<CharArray>): List<Int> {
    for (y in table.indices) {
        for (x in table[y].indices) {
            if (table[y][x] == 'S') return listOf(x, y)
        }
    }
    throw IllegalStateException("No S found")
}

fun printTable(table: Array<CharArray>) {
    println("=".repeat(table[0].size * 2))
    for (line in table) {
        print("[")
        for (c in line) {
            print("$c,")
        }
        println("]")
    }
}

enum class Possibles(val letters: Set<Char>) {
    TOP(setOf('|', '7', 'F')),
    BOTTOM(setOf('|', 'L', 'J')),
    LEFT(setOf('-', 'L', 'F')),
    RIGHT(setOf('-', '7', 'J'))
}

data class Point(val x: Int, val y: Int)
data class Visit(
    val x: Int,
    val y: Int,
    val possibles: Set<Char>
)

// Traverses the map in a dfs and return the points contained in the loop
private fun dfs(
    table: Array<CharArray>,
    startX: Int,
    startY: Int
): Set<Point> {
    val stack = mutableListOf<Visit>()
    val visited = mutableSetOf<Point>()

    println("Start = ($startX, $startY)")

    stack.add(Visit(startX, startY - 1, Possibles.TOP.letters))
    stack.add(Visit(startX, startY + 1, Possibles.BOTTOM.letters))
    stack.add(Visit(startX - 1, startY, Possibles.LEFT.letters))
    stack.add(Visit(startX + 1, startY, Possibles.RIGHT.letters))
    while (stack.isNotEmpty()) {
        val visit = stack.removeLast()
        if (visit.y < 0 || visit.y >= table.size) continue
        if (visit.x < 0 || visit.x >= table[visit.y].size) continue

        if (table[visit.y][visit.x] == 'S' && visited.size > 2) {
            println("Found 'S' in ${visited.size + 1} steps")
            visited.add(Point(visit.x, visit.y))
            return visited
        }
        if (!visit.possibles.contains(table[visit.y][visit.x])) continue
        if (visited.contains(Point(visit.x, visit.y))) continue

        val x = visit.x
        val y = visit.y
        val tmp = table[y][x]
        visited.add(Point(visit.x, visit.y))
        when (tmp) {
            '-' -> { // explore left + right
                stack.add(Visit(x - 1, y, Possibles.LEFT.letters))
                stack.add(Visit(x + 1, y, Possibles.RIGHT.letters))
            }
            '|' -> { // explore top + bottom
                stack.add(Visit(x, y - 1, Possibles.TOP.letters))
                stack.add(Visit(x, y + 1, Possibles.BOTTOM.letters))
            }
            'F' -> { // explore bottom + right
                stack.add(Visit(x, y + 1, Possibles.BOTTOM.letters))
                stack.add(Visit(x  + 1, y, Possibles.RIGHT.letters))
            }
            '7' -> { // explore left + bottom
                stack.add(Visit(x - 1, y, Possibles.LEFT.letters))
                stack.add(Visit( x, y + 1, Possibles.BOTTOM.letters))
            }
            'J' -> { // explore top + left
                stack.add(Visit(x, y - 1, Possibles.TOP.letters))
                stack.add(Visit(x - 1, y, Possibles.LEFT.letters))
            }
            'L' -> { // explore top + right
                stack.add(Visit(x, y - 1, Possibles.TOP.letters))
                stack.add(Visit(x + 1, y, Possibles.RIGHT.letters))
            }
            else -> throw IllegalStateException("Invalid char -> $tmp")
        }
    }
    throw IllegalStateException("No solution found")
}

fun part1(input: List<String>): Int {
    val table = Array(input.size) { idx -> input[idx].toCharArray() }
    val (startX, startY) = findStart(table)

    return dfs(table, startX, startY).size / 2
}

fun part2(input: List<String>): Int {
    val table = Array(input.size) { idx -> input[idx].toCharArray() }
    val (startX, startY) = findStart(table)
    val loopPoints = dfs(table, startX, startY)

    // Clean the map from pipes not in the loop
    for (y in table.indices) {
        for (x in table[y].indices) {
            if (!loopPoints.contains(Point(x, y))) {
                table[y][x] = '.'
            }
        }
    }

    // Go through the map from left to right counting the number of points that are inside
    // https://en.wikipedia.org/wiki/Point_in_polygon
    var insideCount = 0
    for (y in table.indices) {
        var inside = false
        for (x in table[y].indices) {
            if (setOf('|', 'L', 'J').contains(table[y][x])) {
                inside = !inside
            } else if (table[y][x] == '.' && inside) {
                insideCount += 1
            }
        }
    }
    return insideCount
}

fun main() {
    val testInput = readInput("day10_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput) + RESET) }

    val input = readInput("day10.txt")
    printTimeMillis { print("part1 input = $GREEN" + part1(input) + RESET) }
    printTimeMillis { print("part2 input = $GREEN" + part2(input) + RESET) }
}
