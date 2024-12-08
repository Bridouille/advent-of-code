package day07

import GREEN
import RESET
import permute
import printTimeMillis
import readInput

fun canCalibrate(toFind: Long, currentNb: Long, nbs: List<Long>, canConcatenate: Boolean = false): Boolean {
    if (toFind == currentNb && nbs.isEmpty()) return true
    if (toFind != currentNb && nbs.isEmpty()) return false

    if (canConcatenate) {
        return canCalibrate(toFind, currentNb + nbs[0], nbs.subList(1, nbs.size), true) ||
                canCalibrate(toFind, currentNb * nbs[0], nbs.subList(1, nbs.size), true) ||
                canCalibrate(toFind, ("$currentNb${nbs[0]}").toLong(), nbs.subList(1, nbs.size), true)
    }
    return canCalibrate(toFind, currentNb + nbs[0], nbs.subList(1, nbs.size)) ||
            canCalibrate(toFind, currentNb * nbs[0], nbs.subList(1, nbs.size))
}

fun part1(input: List<String>): Long {
    return input.mapNotNull {
        val toFind = it.split(":")[0].toLong()
        val nbs = it.split(":")[1].trim().split(" ").map { it.toLong() }

        if (canCalibrate(toFind, nbs[0], nbs.subList(1, nbs.size))) {
            return@mapNotNull toFind
        }
        null
    }.sum()
}

fun part2(input: List<String>): Long {
    return input.mapNotNull {
        val toFind = it.split(":")[0].toLong()
        val nbs = it.split(":")[1].trim().split(" ").map { it.toLong() }

        if (canCalibrate(toFind, nbs[0], nbs.subList(1, nbs.size), canConcatenate = true)) {
            return@mapNotNull toFind
        }
        null
    }.sum()
}

fun main() {
    val testInput = readInput("day07_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput) + RESET) }

    val input = readInput("day07.txt")
    printTimeMillis { print("part1 input = $GREEN" + part1(input) + RESET) }
    printTimeMillis { print("part2 input = $GREEN" + part2(input) + RESET) }
}
