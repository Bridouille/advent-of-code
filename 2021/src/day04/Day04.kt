package day04

import readInput

fun Array<IntArray>.markNumber(nb: Int) {
    for (i in 0 until size) {
        for (j in 0 until this[i].size) {
            if (this[i][j] == nb) {
                this[i][j] = -1
            }
        }
    }
}

fun Array<IntArray>.isWinning() : Boolean {
    for (i in 0 until size) {
        if (isLineWinning(this[i])) { // Check line
            return true
        }

        // Build an array with the column
        val column = IntArray(size)
        for (j in 0 until size) {
            column[j] = this[j][i]
        }

        if (isLineWinning(column)) { // Check column
            return true
        }
    }
    return false
}

fun isLineWinning(line: IntArray) = line.filterNot{ it < 0 }.isEmpty()
fun Array<IntArray>.sumOfUnmarkedNumber() : Int {
    var sum = 0
    for (line in this) {
        for (nb in line) {
            if (nb > 0) {
                sum += nb
            }
        }
    }
    return sum
}

fun part1(lines: List<String>) : Int {
    val numbers = lines[0].split(",").map { it.toInt() }
    val boards = mutableListOf<Array<IntArray>>()

    // Building the boards
    lines.drop(1).filter { it.isNotEmpty() }.forEachIndexed { idx, line ->
        val boardIdx = idx / 5
        val boardLine = idx % 5
        if (boardLine == 0) {
            boards.add(Array<IntArray>(5) { IntArray(1) })
        }
        boards[boardIdx][boardLine] = line.split(" ").map { it.toIntOrNull() }.filterNotNull().toIntArray()
    }

    // Going through the numbers to validate them
    numbers.forEach { drawn ->
        boards.forEach { board ->
            board.markNumber(drawn)
            if (board.isWinning()) {
                return drawn * board.sumOfUnmarkedNumber()
            }
        }
    }
    return -1
}

fun part2(lines: List<String>) : Int {
    val numbers = lines[0].split(",").map { it.toInt() }
    val boards = mutableListOf<Array<IntArray>>()

    // Building the boards
    lines.drop(1).filter { it.isNotEmpty() }.forEachIndexed { idx, line ->
        val boardIdx = idx / 5
        val boardLine = idx % 5
        if (boardLine == 0) {
            boards.add(Array<IntArray>(5) { IntArray(1) })
        }
        boards[boardIdx][boardLine] = line.split(" ").map { it.toIntOrNull() }.filterNotNull().toIntArray()
    }

    // Going through the numbers to validate them
    numbers.forEach { drawn ->
        for (i in boards.indices.reversed()) {
            val board = boards[i]

            board.markNumber(drawn)
            if (board.isWinning()) {
                if (boards.size == 1) { // last board
                    return drawn * board.sumOfUnmarkedNumber()
                } else {
                    // remove the board so we don't check if it's winning / mark the number on already won boards
                    boards.removeAt(i)
                }
            }
        }
    }
    return -1
}

fun main() {
    val testInput = readInput("day04/test")
    println("part1(testInput) => " + part1(testInput))
    println("part2(testInput) => " + part2(testInput))

    val input = readInput("day04/input")
    println("part1(input) => " + part1(input))
    println("part1(input) => " + part2(input))
}
