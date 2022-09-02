package day25

import readInput
import java.lang.IllegalStateException
import java.text.SimpleDateFormat
import java.util.*

typealias Table = Array<CharArray>

fun List<String>.toTable(): Table {
    val ret = Array<CharArray>(this.size) { CharArray(0) }
    for (y in indices) ret[y] = this[y].toCharArray()
    return ret
}
fun Table.print(stepNb: Int) {
    val nb = stepNb.toString()
    println(nb + ")" + "=".repeat(this[0].size - nb.length - 1))
    for (c in this) println(c)
    println("=".repeat(this[0].size))
}
fun Table.getPos(x: Int, y: Int): Char  {
    if (y < 0) return this[lastIndex][x]
    if (x < 0) return this[y][this[y].lastIndex]
    if (y >= size) return this[0][x]
    if (x >= this[y].size) return this[y][0]

    return this[y][x]
}
fun Table.setPos(x: Int, y: Int, c: Char) {
    when {
        y < 0 -> this[lastIndex][x] = c
        x < 0 -> this[y][this[y].lastIndex] = c
        y >= size -> this[0][x] = c
        x >= this[y].size -> this[y][0] = c
        else -> this[y][x] = c
    }
}

fun runStep(table: Table): Boolean {
    // Occupy any '>' that moved with 'E' (new pos)
    // and replace the current pos by 'R' to avoid any '>' to take its place
    for (y in table.indices) {
        for (x in table[y].indices) {
            val current = table[y][x]
            if (current == '>' && table.getPos(x + 1, y) == '.') {
                table.setPos(x + 1, y, 'E')
                table[y][x] = 'R'
            }
        }
    }
    // Occupy any 'v' that moved with 'T' (new pos)
    // and replace the current pos by 'S' to avoid any 'v' to take its place
    for (x in table[0].indices) {
        for (y in table.indices) {
            val current = table[y][x]
            if ((current == '.' || current == 'R') && table.getPos(x, y - 1) == 'v') {
                table.setPos(x, y, 'S')
                table.setPos(x, y - 1, 'T')
            }
        }
    }
    var replaced = false
    for (y in table.indices) {
        for (x in table[y].indices) {
            when (table[y][x]) {
                'E' -> table[y][x] = '>'
                'S' -> table[y][x] = 'v'
                'T', 'R' -> {
                    table[y][x] = '.'
                    replaced = true
                }
            }
        }
    }
    return replaced
}

fun part1(lines: List<String>): Int {
    val table = lines.toTable()

    table.print(0)
    var stepNb = 0
    do {
        val replaced = runStep(table)
        stepNb++
        table.print(stepNb)
    } while (replaced)
    return stepNb
}

fun part2(lines: List<String>): Int {
    return 2
}

fun main() {
    val test = readInput("day25/test")
    println("part1(test) => " + part1(test))
    // println("part2(test) => " + part2(test))

    val input = readInput("day25/input")
    println("part1(input) => " + part1(input))
    // println("part2(input) => " + part2(input))
}
