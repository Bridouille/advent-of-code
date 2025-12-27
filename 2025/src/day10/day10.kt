package day10

import GREEN
import RESET
import printTimeMillis
import readInput

data class Factory(
    val lights: List<Boolean>,
    val buttons: List<Button>,
    val joltages: List<Int>,
)

data class Button(val toggleIdx: List<Int>)

private fun toFact(input: List<String>): List<Factory> {
    return buildList {
        for (line in input) {
            val withoutJoltage = line.substringBefore("{")
            val lights = line.substringBefore("]").drop(1).map {
                it == '#'
            }
            val buttons = withoutJoltage.substringAfter("]").trim()
                .split(" ").map {
                    Button(
                        toggleIdx = it.drop(1).dropLast(1).split(",").map { it.toInt() }
                    )
                }
            val joltages = line.substringAfter("{").dropLast(1).split(",").map { it.toInt() }

            val fact = Factory(
                lights = lights,
                buttons = buttons,
                joltages = joltages
            )
            add(fact)
        }
    }
}

private fun idxCombinations(n: Int): List<List<Int>> {
    return buildList {
        for (i in 0 until n) {
            // new combinations formed by appending i to every existing combination
            val withI = map { it + i }
            addAll(withI)

            // also include the single-element combination [i]
            add(listOf(i))
        }
    }
}

fun part1(input: List<String>): Int {
    val factories = toFact(input)
    var total = 0

    for (fact in factories) {
        val sequences = idxCombinations(fact.buttons.size).sortedBy { it.size }

        for (sequence in sequences) {
            val lights = MutableList(fact.lights.size) { false }

            for (btnIdx in sequence) {
                val button = fact.buttons[btnIdx]

                for (idx in button.toggleIdx) {
                    lights[idx] = !lights[idx]
                }
            }

            // If all the lights are as we want
            if (lights == fact.lights) {
                total += sequence.size
                break
            }
        }
    }
    return total
}

fun pressButtons(
    joltages: List<Int>,
    currentJolt: List<Int>,
    buttons: List<Button>,
    presses: Int,
): Int {
    // We found a good combination
    if (currentJolt == joltages) {
        return presses
    }
    for (idx in joltages.indices) {
        // we went too high with the button press
        if (currentJolt[idx] > joltages[idx]) {
            return -1
        }
    }

    val results = mutableListOf<Int>()

    for (btn in buttons) {
        val afterPres = currentJolt.toMutableList()

        for (idx in btn.toggleIdx) {
            afterPres[idx] = afterPres[idx] + 1
        }

        results += pressButtons(joltages, afterPres, buttons, presses + 1)
    }
    return results.filterNot { it == -1 }.minOrNull() ?: -1
}

fun part2(input: List<String>): Int {
    val factories = toFact(input)
    var total = 0

    for (fact in factories) {
        val presses = pressButtons(
            fact.joltages,
            fact.joltages.map { 0 },
            fact.buttons,
            presses = 0
        )
        total += presses
        println("found for ${fact.joltages} -> $presses")
    }
    return total
}

fun main() {
    val testInput = readInput("day10_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput) + RESET) }

    val input = readInput("day10.txt")
    printTimeMillis { print("part1 input = $GREEN" + part1(input) + RESET) }
    printTimeMillis { print("part2 input = $GREEN" + part2(input) + RESET) }
}
