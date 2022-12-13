package day13

import GREEN
import RESET
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import printTimeMillis
import readInput

enum class Result { RIGHT, WRONG, PROCEED }

fun rightOrWrong(leftStr: String, rightStr: String): Result {
    val left = Json.parseToJsonElement(leftStr).jsonArray.toList()
    val right = Json.parseToJsonElement(rightStr).jsonArray.toList()

    val ileft = left.iterator()
    val iright = right.iterator()
    while (ileft.hasNext() && iright.hasNext()) {
        val l = ileft.next()
        val r = iright.next()

        val lDigit = l.toString().toIntOrNull()
        val rDigit = r.toString().toIntOrNull()

        val ret = when {
            lDigit != null && rDigit != null -> when { // both are valid digits
                lDigit > rDigit -> Result.WRONG
                lDigit < rDigit -> Result.RIGHT
                else -> Result.PROCEED
            }
            lDigit != null -> rightOrWrong("[$lDigit]", r.toString())
            rDigit != null -> rightOrWrong(l.toString(), "[$rDigit]")
            else -> rightOrWrong(l.toString(), r.toString()) // both are lists
        }
        if (ret != Result.PROCEED) return ret
    }
    return when {
        !ileft.hasNext() && !iright.hasNext() -> Result.PROCEED  // Both ran out of items, Proceed
        !ileft.hasNext() && iright.hasNext() -> Result.RIGHT // Left ran out of items, Correct order
        else -> Result.WRONG // Right ran out of items, Wrong order
    }
}

fun part1(input: List<String>) = input.windowed(2, 2) {
    when (rightOrWrong(it[0], it[1])) {
        Result.RIGHT -> input.indexOf(it[0]) / 2 + 1
        Result.WRONG -> 0
        else -> throw IllegalStateException("Oh no :(")
    }
}.sum()

fun part2(input: List<String>) = input.sortedWith { o1, o2 ->
    when (rightOrWrong(o1, o2)) {
        Result.RIGHT -> -1
        Result.WRONG -> 1
        else -> throw IllegalStateException("Oh no :(")
    }
}.let { (it.indexOf("[[2]]") + 1) * (it.indexOf("[[6]]") + 1) }

fun main() {
    val testInput = readInput("day13_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput) + RESET) }

    val input = readInput("day13.txt") // 5675
    printTimeMillis { print("part1 input = $GREEN" + part1(input) + RESET) }
    printTimeMillis { print("part2 input = $GREEN" + part2(input) + RESET) }
}
