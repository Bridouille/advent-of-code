package day08

import GREEN
import RESET
import printTimeMillis
import readInput
import kotlin.math.max

fun isTreeVisible(input: List<String>, posY: Int, posX: Int) : Boolean {
    val left = input[posY].take(posX).reversed().all { it < input[posY][posX] }
    val right = input[posY].drop(posX + 1).all { it < input[posY][posX] }
    val top = input.take(posY).map { it[posX] }.joinToString("").reversed().all { it < input[posY][posX] }
    val bottom = input.drop(posY + 1).map { it[posX] }.joinToString("").all { it < input[posY][posX] }
    return left || right || top || bottom
}

fun part1(input: List<String>): Int {
    var sum = 0
    for (y in input.indices) {
        for (x in 0 until input[y].length) {
            if (isTreeVisible(input, y, x)) {
                sum += 1
            }
        }
    }
    return sum
}

fun calculateScenicScore(input: List<String>, posY: Int, posX: Int): Int {
    val left = input[posY].take(posX).reversed()
    val leftScore = left.takeWhile { it < input[posY][posX] }.let {
        if (left.length > it.length) { it.length + 1 }
        else { it.length }
    }

    val right = input[posY].drop(posX + 1)
    val rightScore = right.takeWhile { it < input[posY][posX] }.let {
        if (right.length > it.length) { it.length + 1 }
        else { it.length }
    }

    val top = input.take(posY).map { it[posX] }.joinToString("").reversed()
    val topScore = top.takeWhile { it < input[posY][posX] }.let {
        if (top.length > it.length) { it.length + 1 }
        else { it.length }
    }

    val bottom = input.drop(posY + 1).map { it[posX] }.joinToString("")
    val bottomScore = bottom.takeWhile { it < input[posY][posX] }.let {
        if (bottom.length > it.length) { it.length + 1 }
        else { it.length }
    }

    return leftScore * rightScore * topScore * bottomScore
}

fun part2(input: List<String>): Int {
    var maxScenicScore = 0
    for (y in input.indices) {
        for (x in 0 until input[y].length) {
            maxScenicScore = max(maxScenicScore, calculateScenicScore(input, y, x))
        }
    }
    return maxScenicScore
}

fun main() {
    val testInput = readInput("day08_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput) + RESET) }

    val input = readInput("day08.txt")
    printTimeMillis { print("part1 input = $GREEN" + part1(input) + RESET) }
    printTimeMillis { print("part2 input = $GREEN" + part2(input) + RESET) }
}
