package day15

import GREEN
import RESET
import printTimeMillis
import readInput

private fun hash(str: String): Int {
    var s = 0

    for (c in str) {
        s = ((s + c.code) * 17) % 256
    }
    return s
}

fun part1(input: List<String>): Int {
    return input.first().split(",")
        .map { hash(it) }
        .sum()
}

data class Lense(val label: String, val number: Int)

fun part2(input: List<String>): Int {
    val boxes = mutableMapOf<Int, MutableList<Lense>>()

    input.first().split(",")
        .forEach {
            val isAdding = it.contains("=")
            val (label, number) = if (isAdding) {
                it.split("=")
            } else {
                it.split("-")
            }

            if (isAdding) {
                val boxNb = hash(label)
                if (!boxes.containsKey(boxNb)) {
                    boxes[boxNb] = mutableListOf()
                }
                val currentLenseIdx = boxes[boxNb]!!.indexOfFirst { it.label == label }
                if (currentLenseIdx != -1) {
                    boxes[boxNb]!![currentLenseIdx] = Lense(label, number.toInt())
                } else {
                    boxes[boxNb]!!.add(Lense(label, number.toInt()))
                }
            } else {
                for (k in boxes.keys) {
                    val toRemoveIdx = boxes[k]!!.indexOfFirst { it.label == label }
                    if (toRemoveIdx != -1) {
                        boxes[k]!!.removeAt(toRemoveIdx)
                    }
                }
            }
        }

    return boxes.keys.map { boxNb ->
        boxes[boxNb]!!.mapIndexed { index, lense -> (boxNb + 1) * (index + 1) * lense.number }.sum()
    }.sum()
}

fun main() {
    val testInput = readInput("day15_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput) + RESET) }

    val input = readInput("day15.txt")
    printTimeMillis { print("part1 input = $GREEN" + part1(input) + RESET) }
    printTimeMillis { print("part2 input = $GREEN" + part2(input) + RESET) }
}
