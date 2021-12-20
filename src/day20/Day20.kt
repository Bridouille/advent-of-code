package day20

import readInput

typealias Table = Array<CharArray>

fun Table.print() {
    for (line in this) {
        println(line)
    }
}

fun List<String>.toTable() : Table {
    val ret = Array(size) { CharArray(0) }

    for (idx in indices) ret[idx] = this[idx].toCharArray()
    return ret
}

fun Table.getValue(y: Int, x: Int, default: Char) : Int {
    if (x < 0 || y < 0 || y >= size || x >= this[y].size) return if (default == '#') 1 else 0
    return if (this[y][x] == '#') 1 else 0
}
// return a binary number of the 9 squares values around Y and X
fun Table.getSquareValue(y: Int, x: Int, default: Char) : Int {
    var number = 0

    for (yOffset in -1..1) {
        for (xOffset in -1..1) {
            number = number.shl(1) or getValue(y + yOffset, x + xOffset, default)
        }
    }
    return number
}

fun enhanceImage(table: Table, ref: String, numberOfSteps: Int = 1) : Table {
    var ret = table

    for (i in 1..numberOfSteps) {
        val nextIteration = Array(ret.size + 2) { CharArray(ret[0].size + 2) }
        // In case we're OOB, our default is either '.' (initial) or the first char in the reference
        // meaning all the chars were previously surrounded by '.' and got 0 as a total of 9 cells around
        val default = if (i % 2 == 0) { ref.first() } else { '.' }

        for (y in nextIteration.indices) {
            for (x in nextIteration[y].indices) {
                val number = ret.getSquareValue(y - 1, x - 1, default)

                nextIteration[y][x] = ref[number]
            }
        }
        ret = nextIteration
    }
    return ret
}

fun part1(lines: List<String>): Int {
    val ref = lines.first()
    val table = lines.drop(1).toTable()

    return enhanceImage(table, ref, 2).let{
        it.print()
        it.sumOf { it.fold<Int>(0) { acc, c -> if (c == '#') acc + 1 else acc } }
    }
}

fun part2(lines: List<String>): Int {
    val ref = lines.first()
    val table = lines.drop(1).toTable()

    return enhanceImage(table, ref, 50).let{
        it.print()
        it.sumOf { it.fold<Int>(0) { acc, c -> if (c == '#') acc + 1 else acc } }
    }
}

fun main() {
    val testInput = readInput("day20/test")
    println("part1(testInput) => " + part1(testInput))
    println("part2(testInput) => " + part2(testInput))

    val input = readInput("day20/input")
    println("part1(input) => " + part1(input))
    println("part2(input) => " + part2(input))
}
