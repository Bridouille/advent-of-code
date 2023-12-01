package day01

import GREEN
import RESET
import printTimeMillis
import readInput

fun part1(input: List<String>): Int {

    return input.map<String, Int> {
        val one = it.first { it.isDigit() }.digitToInt()
        val two = it.last { it.isDigit() }.digitToInt()

        one * 10 + two
    }.sum()
}

val replaces = mapOf(
    "one" to "1",
    "two" to "2",
    "three" to "3",
    "four" to "4",
    "five" to "5",
    "six" to "6",
    "seven" to "7",
    "eight" to "8",
    "nine" to "9",
)

fun String.substrIdx(str: String, fromEnd: Boolean) : Int? {
    val indexes = if (fromEnd) {
        indices.reversed()
    } else {
        indices
    }

    for (i in indexes) {
        if (length >= i + str.length && substring(i, i + str.length) == str) {
            return i
        }
    }
    return null
}

fun part2(input: List<String>): Int {
    return input.map<String, Int> {
        var firstWrittenDigit = ""
        var lastWrittenDigit = ""
        var minIdx: Int = Int.MAX_VALUE
        var maxIdx: Int = Int.MIN_VALUE

        for (e in replaces) {
            val first = it.substrIdx(e.key, fromEnd = false)
            val last = it.substrIdx(e.key, fromEnd = true)
            if (first != null && first < minIdx) {
                minIdx = first
                firstWrittenDigit = e.value
            }
            if (last != null && last > maxIdx) {
                maxIdx = last
                lastWrittenDigit = e.value
            }
        }
        val firstDigitIdx = it.indexOfFirst { it.isDigit() }
        val lastDigitIdx = it.indexOfLast { it.isDigit() }

        val one = if (firstDigitIdx != -1 && firstDigitIdx < minIdx) {
            it.first { it.isDigit() }.digitToInt()
        } else {
            firstWrittenDigit.toInt()
        }
        val two = if (lastDigitIdx != -1 && lastDigitIdx > maxIdx) {
            it.last { it.isDigit() }.digitToInt()
        } else {
            lastWrittenDigit.toInt()
        }

        one * 10 + two
    }.sum()
}

fun main() {
    val testInput = readInput("day01_example.txt")
    val testInput2 = readInput("day01_example2.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput2) + RESET) }

    val input = readInput("day01.txt")
    printTimeMillis { print("part1 input = $GREEN" + part1(input) + RESET) }
    printTimeMillis { print("part2 input = $GREEN" + part2(input) + RESET) }
}
