package day03

import GREEN
import RESET
import printTimeMillis
import readInput

fun part1(input: List<String>): Long {
    return input.sumOf { maxVoltage(it, 2) }
}

fun part2(input: List<String>): Long {
    return input.sumOf { maxVoltage(it, 12) }
}

private fun maxVoltage(batteries: String, nbBatteries: Int): Long {
    val digits = "987654321"
    var currentBatteries = batteries
    var voltage = ""

    while (voltage.length < nbBatteries) {
        val remainingToFind = nbBatteries - voltage.length

        for (digit in digits) {
            val idx = currentBatteries.indexOfFirst { it == digit }
            // digit is not found or we don't have space for the next batteries to turn on
            if (idx < 0 || idx + remainingToFind > currentBatteries.length) {
                continue
            }

            voltage += digit
            currentBatteries = currentBatteries.substring(idx + 1)
            break
        }
    }
    return voltage.toLong()
}

fun main() {
    val testInput = readInput("day03_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput) + RESET) }

    val input = readInput("day03.txt")
    printTimeMillis { print("part1 input = $GREEN" + part1(input) + RESET) }
    printTimeMillis { print("part2 input = $GREEN" + part2(input) + RESET) }
}
