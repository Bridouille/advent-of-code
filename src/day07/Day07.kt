package day07

import readInput
import kotlin.math.abs

fun part1(lines: List<String>) : Int {
    val crabsPos = lines.first().split(",").map{ it.toInt() }
    val min = crabsPos.minOrNull() ?: return -1
    val max = crabsPos.maxOrNull() ?: return -1

    var minFuel = Integer.MAX_VALUE
    for (i in min..max) {
        var fuel = 0

        for (pos in crabsPos) {
            fuel += abs(i - pos)
        }
        if (fuel <= minFuel) minFuel = fuel
    }
    return minFuel
}

fun calcFuel(steps: Int, memo: MutableMap<Int, Int>) : Int {
    return if (steps <= 1) {
        steps
    } else {
        if (!memo.containsKey(steps)) {
            memo[steps] = steps + calcFuel(steps - 1, memo)
        }
        memo[steps]!!
    }
}

fun part2(lines: List<String>) : Int {
    val crabsPos = lines.first().split(",").map{ it.toInt() }
    val min = crabsPos.minOrNull() ?: return -1
    val max = crabsPos.maxOrNull() ?: return -1
    val memo = mutableMapOf<Int, Int>() // steps => fuel required map

    var minFuel = Integer.MAX_VALUE
    for (i in min..max) {
        var fuel = 0

        for (pos in crabsPos) {
            fuel += calcFuel(abs(i - pos), memo)
        }
        if (fuel <= minFuel) minFuel = fuel
    }
    return minFuel
}

fun main() {
    val testInput = readInput("day07/test")
    println("part1(testInput) => " + part1(testInput))
    println("part2(testInput) => " + part2(testInput))

    val input = readInput("day07/input")
    println("part1(input) => " + part1(input))
    println("part1(input) => " + part2(input))
}
