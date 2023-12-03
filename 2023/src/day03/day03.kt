package day03

import GREEN
import RESET
import printTimeMillis
import readInput

private fun isSymbol(input: List<String>, y: Int, x: Int) : Boolean {
    if (y < 0 || y >= input.size) return false
    if (x < 0 || x >= input[y].length) return false

    val c = input[y][x]
    return !c.isDigit() && c != '.'
}

private fun checkValid(input: List<String>, y: Int, x: Int) : Boolean {
    return isSymbol(input, y - 1, x - 1) ||
            isSymbol(input, y - 1, x) ||
            isSymbol(input, y - 1, x + 1) ||
            isSymbol(input, y, x  - 1) ||
            isSymbol(input, y, x + 1) ||
            isSymbol(input, y + 1, x - 1) ||
            isSymbol(input, y + 1, x) ||
            isSymbol(input, y + 1, x + 1)
}

fun part1(input: List<String>): Int {
    var sum = 0

    for (y in input.indices) {
        var number = 0
        var isValid = false

        for (x in input[y].indices) {
            if (input[y][x].isDigit()) {
                number = number * 10 + input[y][x].digitToInt()
                if (!isValid) {
                    isValid = checkValid(input, y, x)
                }
            } else {
                // println("number read = $number - isValid = $isValid")
                if (isValid) {
                    sum += number
                    isValid = false
                }
                number = 0
            }
        }
        if (isValid) sum += number
    }
    return sum
}

//
// Return a number read from position x in the string str
// and a set of the x positions of that number
//
private fun readDigit(str: String, x: Int) : Pair<Int?, Set<Int>> {
    if (!str[x].isDigit()) return Pair(null, emptySet())

    var start = x
    while (start > 0 && str[start - 1].isDigit()) start--

    var number = 0
    val xPos = mutableSetOf<Int>()
    while (start < str.length && str[start].isDigit()) {
        xPos.add(start)
        number = number * 10 + str[start].digitToInt()
        start++
    }
    return Pair(number, xPos)
}

private fun findAdj(input: List<String>, y: Int, x: Int): List<Int> {
    // Pair<Number + Number positions> = '467' -> setOf(0, 1, 2)
    val numbers = mutableSetOf<Pair<Int?, Set<Int>>>()

    // Top
    if (y > 0) {
        numbers.add(readDigit(input[y - 1], x - 1))
        numbers.add(readDigit(input[y - 1], x))
        numbers.add(readDigit(input[y - 1], x + 1))
    }
    // Same line
    numbers.add(readDigit(input[y], x - 1))
    numbers.add(readDigit(input[y], x + 1))
    // Bottom
    if (y + 1 < input.size) {
        numbers.add(readDigit(input[y + 1], x - 1))
        numbers.add(readDigit(input[y + 1], x))
        numbers.add(readDigit(input[y + 1], x + 1))
    }
    return numbers.mapNotNull { it.first }
}

fun part2(input: List<String>): Int {
    var sum = 0

    for (y in input.indices) {
        for (x in input[y].indices) {
            if (input[y][x] == '*') {
                val adj = findAdj(input, y, x)

                if (adj.size >= 2) { // This is a gear
                    sum += adj.fold(1) { a, b -> a * b }
                }
            }
        }
    }
    return sum
}

fun main() {
    val testInput = readInput("day03_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput) + RESET) }

    val input = readInput("day03.txt")
    printTimeMillis { print("part1 input = $GREEN" + part1(input) + RESET) }
    printTimeMillis { print("part2 input = $GREEN" + part2(input) + RESET) }
}
