package day03

import readInput

fun part1(lines: List<String>) : Int {
    val counts = MutableList(lines[0].length) { 0 }

    lines.forEach { line ->
        counts.forEachIndexed { idx, _ ->
            counts[idx] = if (line[idx] == '1') {
                counts[idx] + 1
            } else {
                counts[idx] - 1
            }
        }
    }
    var gamma = 0
    var epsilon = 0

    counts.map {
        val gammaBit = if (it > 0) { 1 } else { 0 }
        val epsilonBit = if (it > 0) { 0 } else { 1 }

        gamma = gamma.shl(1) or gammaBit
        epsilon = epsilon.shl(1) or epsilonBit
    }
    return gamma * epsilon
}

enum class Type {
    OXYGEN_GENERATOR_RATING,
    CO2_SCRUBBER_RATING
}

fun find_rating(lines: List<String>, type: Type, pos: Int = 0) : String {
    if (lines.size == 1) {
        return lines[0]
    }
    var count = 0
    lines.forEach { line -> if (line[pos] == '1') { count++ } else { count-- } }

    val keepOnes = if (type == Type.OXYGEN_GENERATOR_RATING) {
        count >= 0 // If 0 and 1 are equally common, keep values with a 1 in the position being considered
    } else {
        count < 0 // If 0 and 1 are equally common, keep values with a 0 in the position being considered.
    }
    return find_rating(lines.filter {
        it[pos] == if (keepOnes) { '1' } else { '0' }
    }, type, pos + 1)
}

fun part2(lines: List<String>) : Int {
    val ogr = find_rating(lines, Type.OXYGEN_GENERATOR_RATING)
    val csr = find_rating(lines, Type.CO2_SCRUBBER_RATING)
    return ogr.toInt(2) * csr.toInt(2)
}

fun main() {
    val testInput = readInput("day03/test")
    println("part1(testInput) => " + part1(testInput))
    println("part2(testInput) => " + part2(testInput))

    val input = readInput("day03/input")
    println("part1(input) => " + part1(input))
    println("part1(input) => " + part2(input))
}
