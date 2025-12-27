package day11

import GREEN
import RESET
import printTimeMillis
import readInput

private fun toMap(input: List<String>): Map<String, List<String>> {
    return buildMap {
        input.forEach {
            val key = it.substringBefore(":")
            val values = it.substringAfter(":").trim().split(" ")

            put(key, values)
        }
        put("out", listOf())
    }
}

private fun nbPaths(
    map: Map<String, List<String>>,
    cache : MutableMap<Pair<String, String>, Long>,
    start: String,
    end: String
): Long {
    if (start == end) return 1
    if (cache.contains(Pair(start, end))) {
        return cache[Pair(start, end)]!!
    }

    var total = 0L
    val toVisit = map[start]?.toMutableList() ?: return -1

    for (node in toVisit) {
        total += nbPaths(map, cache, node, end)
    }

    cache.put(Pair(start, end), total)
    return total
}

fun part1(input: List<String>) = nbPaths(toMap(input), mutableMapOf(),"you", "out")

fun part2(input: List<String>): Long {
    val map = toMap(input)

    return nbPaths(map, mutableMapOf(),"svr", "fft") *
            nbPaths(map, mutableMapOf(),"fft", "dac") *
            nbPaths(map, mutableMapOf(),"dac", "out")
}

fun main() {
    val testInput = readInput("day11_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput) + RESET) }

    val input = readInput("day11.txt")
    printTimeMillis { print("part1 input = $GREEN" + part1(input) + RESET) }
    printTimeMillis { print("part2 input = $GREEN" + part2(input) + RESET) }
}
