package day05

import GREEN
import RESET
import printTimeMillis
import readInput

const val letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"

fun buildStacks(input: List<String>): MutableList<MutableList<Char>> {
    val stacks = mutableListOf<MutableList<Char>>()
    input.forEach { s ->
        for (i in 1..s.lastIndex step 4) {
            if (letters.contains(s[i])) {
                val stackNb = i / 4
                if (stacks.size <= stackNb) {
                    stacks.add(stackNb, mutableListOf())
                }
                stacks[stackNb].add(s[i])
            }
        }
    }
    return stacks
}

fun executeInstructions(
    instructions: List<String>,
    stacks: MutableList<MutableList<Char>>,
    keepInPlace: Boolean = false
) {
    for (inst in instructions) {
        val (nb, from, to) = inst.split(" ").let {
            listOf(it[1].toInt(), it[3].toInt() - 1, it[5].toInt() - 1)
        }
        if (!keepInPlace) {
            for (i in 0 until nb) {
                stacks[to].add(stacks[from].removeLast())
            }
        } else {
            val toTransfer = stacks[from].takeLast(nb)
            stacks[to].addAll(toTransfer)
            stacks[from] = stacks[from].take(stacks[from].size - toTransfer.size).toMutableList()
        }
    }
}

fun part1(input: List<String>): String {
    val delimiter = input.indexOfFirst { it.isEmpty() }
    val stacks = buildStacks(input.subList(0, delimiter - 1).reversed())
    executeInstructions(input.subList(delimiter + 1, input.size), stacks)
    return stacks.map { it.last() }.joinToString("")
}

fun part2(input: List<String>): String {
    val delimiter = input.indexOfFirst { it.isEmpty() }
    val stacks = buildStacks(input.subList(0, delimiter - 1).reversed())
    executeInstructions(input.subList(delimiter + 1, input.size), stacks, true)
    return stacks.map { it.last() }.joinToString("")
}

fun main() {
    val testInput = readInput("day05_example.txt", false)
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput) + RESET) }

    val input = readInput("day05.txt", false)
    printTimeMillis { print("part1 input = $GREEN" + part1(input) + RESET) }
    printTimeMillis { print("part2 input = $GREEN" + part2(input) + RESET) }
}