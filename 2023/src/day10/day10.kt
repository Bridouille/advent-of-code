package day10

import GREEN
import RESET
import printTimeMillis
import readInput
import java.awt.Point

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

data class Visit(
    val x: Int,
    val y: Int,
    val possibles: Set<Char>,
    val step: Int
)

fun part1(input: List<String>): Int {
    val table = Array(input.size) { idx -> input[idx].toCharArray() }
    val (startX, startY) = findStart(table)
    val stack = mutableListOf<Visit>()
    val visited = mutableSetOf<Point>()

    println("Start = ($startX, $startY)")

    stack.add(Visit(startX, startY - 1, Possibles.TOP.letters, 1))
    stack.add(Visit(startX, startY + 1, Possibles.BOTTOM.letters, 1))
    stack.add(Visit(startX - 1, startY, Possibles.LEFT.letters, 1))
    stack.add(Visit(startX + 1, startY, Possibles.RIGHT.letters, 1))
    while (stack.isNotEmpty()) {
        val visit = stack.removeLast()
        if (visit.y < 0 || visit.y >= table.size) continue
        if (visit.x < 0 || visit.x >= table[visit.y].size) continue

        if (table[visit.y][visit.x] == 'S' && visit.step > 2) {
            println("Found 'S' in ${visit.step} steps")
            return visit.step / 2
        }
        if (!visit.possibles.contains(table[visit.y][visit.x])) continue
        if (visited.contains(Point(visit.x, visit.y))) continue

        val x = visit.x
        val y = visit.y
        val tmp = table[y][x]
        val step = visit.step
        visited.add(Point(visit.x, visit.y))
        when (tmp) {
            '-' -> { // explore left + right
                stack.add(Visit(x - 1, y, Possibles.LEFT.letters, step + 1))
                stack.add(Visit(x + 1, y, Possibles.RIGHT.letters, step + 1))
            }
            '|' -> { // explore top + bottom
                stack.add(Visit(x, y - 1, Possibles.TOP.letters, step + 1))
                stack.add(Visit(x, y + 1, Possibles.BOTTOM.letters, step + 1))
            }
            'F' -> { // explore bottom + right
                stack.add(Visit(x, y + 1, Possibles.BOTTOM.letters, step + 1))
                stack.add(Visit(x  + 1, y, Possibles.RIGHT.letters, step + 1))
            }
            '7' -> { // explore left + bottom
                stack.add(Visit(x - 1, y, Possibles.LEFT.letters, step + 1))
                stack.add(Visit( x, y + 1, Possibles.BOTTOM.letters, step + 1))
            }
            'J' -> { // explore top + left
                stack.add(Visit(x, y - 1, Possibles.TOP.letters, step + 1))
                stack.add(Visit(x - 1, y, Possibles.LEFT.letters, step + 1))
            }
            'L' -> { // explore top + right
                stack.add(Visit(x, y - 1, Possibles.TOP.letters, step + 1))
                stack.add(Visit(x + 1, y, Possibles.RIGHT.letters, step + 1))
            }
            else -> throw IllegalStateException("Invalid char -> $tmp")
        }
    }
    throw IllegalStateException("No solution found")
}

fun part2(input: List<String>): Int {

    return 2
}

fun main() {
    val testInput = readInput("day10_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput) + RESET) }

    val input = readInput("day10.txt")
    printTimeMillis { print("part1 input = $GREEN" + part1(input) + RESET) }
    // printTimeMillis { print("part2 input = $GREEN" + part2(input) + RESET) }
}
