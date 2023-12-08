package day08

import GREEN
import LCM
import RESET
import printTimeMillis
import readInput

private fun List<String>.toMap(): Map<String, Pair<String, String>> = drop(1).let {
    buildMap {
        for (line in it) {
            val key = line.split(" = ")[0]
            val (left, right) = line.split(" = ")[1].drop(1).dropLast(1)
                .split(", ")

            put(key, Pair(left, right))
        }
    }
}

private fun solve(
    map: Map<String, Pair<String, String>>,
    instructions: String,
    start: String,
    possibleEnds: Set<String>
): Int {
    var current = start
    var i = 0

    while (!possibleEnds.contains(current)) {
        val inst = instructions[i % instructions.length]
        current = if (inst == 'L') {
            map[current]!!.first
        } else {
            map[current]!!.second
        }
        i += 1
    }
    return i
}

fun part1(input: List<String>): Int {
    val instructions = input.first()
    val map = input.toMap()

    return solve(map, instructions, "AAA", setOf("ZZZ"))
}

fun findLCM(numbers: List<Long>): Long {
    var result = numbers[0]
    for (i in 1 until numbers.size) {
        result = result.LCM(numbers[i])
    }
    return result
}

fun part2(input: List<String>): Long {
    val instructions = input.first()
    val map = input.toMap()

    val starts = map.keys.filter { it.endsWith('A') }
    val ends = map.keys.filter { it.endsWith('Z') }.toSet()

    val startToEnd = mutableListOf<Long>()
    for (start in starts) {
        for (end in ends) {
            val steps = solve(map, instructions, start, ends)
            startToEnd.add(steps.toLong())
        }
    }
    return findLCM(startToEnd)
}

fun main() {
    val testInput = readInput("day08_example.txt")
    val testInput2 = readInput("day08_example2.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput2) + RESET) }

    val input = readInput("day08.txt")
    printTimeMillis { print("part1 input = $GREEN" + part1(input) + RESET) }
    printTimeMillis { print("part2 input = $GREEN" + part2(input) + RESET) }
}
