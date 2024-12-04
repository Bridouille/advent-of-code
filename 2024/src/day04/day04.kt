package day04

import GREEN
import RESET
import printTimeMillis
import readInput

fun List<String>.isWord(x: Int, y: Int, word: String, idx: Int, transform: (Int, Int) -> Pair<Int, Int>): Int {
    if (idx >= word.length) return 1
    if (y < 0 || x < 0 || y >= size || x >= this[y].length) return 0

    return if (this[y][x] == word[idx]) {
        val newXY = transform(x, y)

        isWord(newXY.first, newXY.second, word, idx + 1, transform)
    } else {
        0
    }
}

fun List<String>.getLetter(x: Int, y: Int): Char? {
    if (y < 0 || x < 0 || y >= size || x >= this[y].length) return null
    return this[y][x]
}

fun part1(input: List<String>): Int {
    val XMAS = "XMAS"
    var total = 0

    for (y in 0 until input.size) {
        for (x in 0 until input[y].length) {
            var count = 0

            if (input[y][x] == 'X') {
                // Top, right, bottom, left
                count += input.isWord(x, y - 1, XMAS, 1, { x, y -> Pair(x, y - 1) })
                count += input.isWord(x + 1, y, XMAS, 1, { x, y -> Pair(x + 1, y) })
                count += input.isWord(x, y + 1, XMAS, 1, { x, y -> Pair(x, y + 1) })
                count += input.isWord(x - 1, y, XMAS, 1, { x, y -> Pair(x - 1, y) })

                // Diagonals
                count += input.isWord(x + 1, y - 1, XMAS, 1, { x, y -> Pair(x + 1, y - 1) })
                count += input.isWord(x + 1, y + 1, XMAS, 1, { x, y -> Pair(x + 1, y + 1) })
                count += input.isWord(x - 1, y + 1, XMAS, 1, { x, y -> Pair(x - 1, y + 1) })
                count += input.isWord(x - 1, y - 1, XMAS, 1, { x, y -> Pair(x - 1, y - 1) })
            }
            total += count
        }
    }
    return total
}

fun part2(input: List<String>): Int {
    var total = 0

    for (y in 0 until input.size) {
        for (x in 0 until input[y].length) {
            if (input[y][x] == 'A') {
                val topLeft = input.getLetter(x - 1, y - 1) ?: continue
                val bottomRight = input.getLetter(x + 1, y + 1) ?: continue
                val topRight = input.getLetter(x + 1, y - 1) ?: continue
                val bottomLeft = input.getLetter(x - 1, y + 1) ?: continue

                if (topLeft.code + bottomRight.code == 'M'.code + 'S'.code &&
                    topRight.code + bottomLeft.code == 'M'.code + 'S'.code) {
                    total += 1
                }
            }
        }
    }
    return total
}

fun main() {
    val testInput = readInput("day04_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput) + RESET) }

    val input = readInput("day04.txt")
    printTimeMillis { print("part1 input = $GREEN" + part1(input) + RESET) }
    printTimeMillis { print("part2 input = $GREEN" + part2(input) + RESET) }
}
