package day06

import readInput

fun part1(lines: List<String>) : Int {
    val fishes = lines.first().split(",").map{ it.toInt() }.toMutableList()
    val iterations = 80

    for (i in 1..iterations) {
        var numberAdded = 0

        for (idx in fishes.indices) {
            when (fishes[idx]) {
                0 -> {
                    fishes[idx] = 6
                    numberAdded++
                }
                else -> fishes[idx]--
            }
        }

        fishes.addAll(List(numberAdded) { 8 })
    }
    return fishes.size
}

fun part2(lines: List<String>) : Long {
    val initial = lines.first().split(",").map{ it.toInt() }.toMutableList()
    val fishes = LongArray(9) { 0 } // from 0 to 8 included
    val iterations = 256

    for (fish in initial) fishes[8 - fish]++

    for (i in 1..iterations) {
        val babies = fishes[8]

        for (j in 8 downTo 1) {
            fishes[j] = fishes[j - 1]
        }
        fishes[0] = babies
        fishes[8 - 6] += babies
    }
    return fishes.sum()
}

fun main() {
    val testInput = readInput("day06/test")
    println("part1(testInput) => " + part1(testInput))
    println("part2(testInput) => " + part2(testInput))

    val input = readInput("day06/input")
    println("part1(input) => " + part1(input))
    println("part1(input) => " + part2(input))
}
