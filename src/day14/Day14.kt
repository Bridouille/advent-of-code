package day14

import readInput

fun List<String>.toPairInsertions() : Map<String, String> {
    val map = mutableMapOf<String, String>()
    filter { it.contains("->") }.forEach {
        map[it.substringBefore("->").trim()] = it.substringAfter("->").trim()
    }
    return map
}

fun applyStep(start: String, pairs: Map<String, String>) : String {
    val newStr = StringBuilder()

    for (i in start.indices) {
        newStr.append(start[i])
        if (i + 1 < start.length) {
            val key = "${start[i]}${start[i + 1]}"
            if (pairs.contains(key)) {
                newStr.append(pairs[key])
            }
        }
    }
    return newStr.toString()
}

fun part1(lines: List<String>) : Int {
    var polymerTemplate = lines.first()
    val pairInsertions = lines.toPairInsertions()
    val numberOfSteps = 10

    for (i in 0 until numberOfSteps) {
        polymerTemplate = applyStep(polymerTemplate, pairInsertions)
    }

    val lettersToOccurrences = polymerTemplate.toList().groupingBy { it }.eachCount()
    val maxLetter = lettersToOccurrences.maxOfOrNull { it.value } ?: return -1
    val minLetter = lettersToOccurrences.minOfOrNull { it.value } ?: return -1
    return maxLetter - minLetter
}

fun part2(lines: List<String>) : Long {
    val polymerTemplate = lines.first()
    val pairInsertions = lines.toPairInsertions()
    val numberOfSteps = 40
    val pairs = mutableMapOf<String, Long>()

    for (i in polymerTemplate.indices) { // NNCB => {NN=1, NC=1, CB=1}
        if (i + 1 < polymerTemplate.length) {
            val key = "${polymerTemplate[i]}${polymerTemplate[i + 1]}"
            pairs[key] = pairs.getOrDefault(key, 0) + 1
        }
    }

    // Applying the steps by pairs => [NN] becomes [NC] & [CN]
    for (i in 0 until numberOfSteps) {
        val tmp = mutableMapOf<String, Long>()
        for ((key, value) in pairs) {
            pairInsertions[key]?.let { replacement ->
                val firstKey = ""+key.first() + replacement.first()
                val secondKey = ""+replacement.first() + key.last()

                tmp[firstKey] = tmp.getOrDefault(firstKey, 0) + value
                tmp[secondKey] = tmp.getOrDefault(secondKey, 0) + value
            }
        }
        pairs.clear()
        pairs.putAll(tmp)
    }

    // Calculate the totals
    val totals = mutableMapOf<Char, Long>()
    totals[polymerTemplate.first()] = 1
    for ((k, v) in pairs) {
        totals[k.last()] = totals.getOrDefault(k.last(), 0) + v
    }

    val sorted = totals.toList().map { it.second }.sortedDescending()
    return sorted.first() - sorted.last()
}

fun main() {
    val testInput = readInput("day14/test")
    println("part1(testInput) => " + part1(testInput))
    println("part2(testInput) => " + part2(testInput))

    val input = readInput("day14/input")
    println("part1(input) => " + part1(input))
    println("part2(input) => " + part2(input))
}
