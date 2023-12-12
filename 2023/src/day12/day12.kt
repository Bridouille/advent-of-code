package day12

import GREEN
import RESET
import printTimeMillis
import readInput

data class Arrangement(
    val str: String,
    val records: List<Int>
) {
    fun isValid() : Boolean {
        val chunks = str.split(".")
            .filter { it.contains("#") }
            .map { it.length }

        return chunks == records
    }
}

private fun nbWays(arr: Arrangement): Int {
    val toReplace = arr.str.indexOfFirst { it == '?' }
    if (toReplace == -1) return if (arr.isValid()) 1 else 0

    val withDot = arr.str.toCharArray().also {
        it[toReplace] = '.'
    }
    val withDieze = arr.str.toCharArray().also {
        it[toReplace] = '#'
    }
    return nbWays(Arrangement(withDot.concatToString(), arr.records)) +
            nbWays(Arrangement(withDieze.concatToString(), arr.records))
}

fun part1(input: List<String>): Int {
    val springs = input.map {
        Arrangement(it.split(" ")[0], it.split(" ")[1].split(",").map { it.toInt() })
    }

    return springs.map { nbWays(it) }.sum()
}

fun part2(input: List<String>): Int {
    return 2
}

fun main() {
    val testInput = readInput("day12_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput) + RESET) }

    val input = readInput("day12.txt")
    printTimeMillis { print("part1 input = $GREEN" + part1(input) + RESET) }
    // printTimeMillis { print("part2 input = $GREEN" + part2(input) + RESET) }
}
