package day10

import GREEN
import RESET
import printTimeMillis
import readInput

// Hey fab
// returns a map of {cycle -> X register}
fun computeInstructions(input: List<String>): Map<Int, Int> {
    var xRegister = 1
    var cycle = 1
    val memory = mutableMapOf(1 to 1)

    input.forEach {
        if (it == "noop") {
            cycle++
            memory[cycle] = xRegister
        } else {
            memory[cycle + 1] = xRegister
            cycle += 2
            xRegister += it.split(" ").last().toInt()
            memory[cycle] = xRegister
        }
    }
    return memory
}

fun part1(input: List<String>): Int {
    val memory = computeInstructions(input)
    return (20..220 step(40)).fold(0) { acc, value ->
        acc + (value * memory[value]!!)
    }
}

// if cycle == xRegister+/-1 = we draw at position cycle on an horizontal line
fun part2(input: List<String>): String {
    val memory = computeInstructions(input)

    memory.keys.forEach { cycle ->
        val xReg = memory[cycle]!!
        val letter = if (setOf(xReg - 1, xReg, xReg + 1).contains((cycle - 1) % 40)) '#' else '.'
        print(letter)
        if (cycle % 40 == 0) println()
    }
    return "Read output :)"
}

fun main() {
    val testInput = readInput("day10_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput) + RESET) }

    val input = readInput("day10.txt")
    printTimeMillis { print("part1 input = $GREEN" + part1(input) + RESET) }
    printTimeMillis { print("part2 input = $GREEN" + part2(input) + RESET) }
}
