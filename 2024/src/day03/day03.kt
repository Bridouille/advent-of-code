package day03

import GREEN
import RESET
import printTimeMillis
import readInput

fun part1(input: List<String>): Int {
    val line = input.joinToString("")
    val regex = Regex("""mul\(\d{1,3},\d{1,3}\)""")

    return regex.findAll(line).sumOf {
        val digits = it.value.substring(4, it.value.length - 1).split(",")

        digits[0].toInt() * digits[1].toInt()
    }
}

fun part2(input: List<String>): Int {
    val line = input.joinToString("")
    val regex = Regex("""mul\(\d{1,3},\d{1,3}\)""")
    val dosAndDonts = Regex("""do\(\)|don't\(\)""").findAll(line).toList()

    return regex.findAll(line).sumOf { match ->
        if (!isEnabled(dosAndDonts, match.range.start)) return@sumOf 0
        val digits = match.value.substring(4, match.value.length - 1).split(",")

        digits[0].toInt() * digits[1].toInt()
    }
}

fun isEnabled(matches: List<MatchResult>, startIdx: Int): Boolean {
    var enabled = true

    for (match in matches) {
        if (match.range.start < startIdx) {
            enabled = match.value == "do()"
        }
    }
    return enabled
}

fun main() {
    val testInput = readInput("day03_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput) + RESET) }

    val input = readInput("day03.txt")
    printTimeMillis { print("part1 input = $GREEN" + part1(input) + RESET) }
    printTimeMillis { print("part2 input = $GREEN" + part2(input) + RESET) }
}
