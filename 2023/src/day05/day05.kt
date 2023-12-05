package day05

import GREEN
import RESET
import printTimeMillis
import readInput

val transforms = listOf(
    "soil",
    "fertilizer",
    "water",
    "light",
    "temperature",
    "humidity",
    "location"
)

// 50 98 2 -> LongRange(98..99) offset = 50-98 = -48
// 52 50 48 -> LongRange(50..97) offset = 52-50 = 2
data class Offset(
    val range: LongRange,
    val offset: Long,
)

data class Garden(
    val seeds: List<Long>,
    val srcToDest: Map<String, List<Offset>>
)

private fun parseInput(input: List<String>): Garden {
    val seeds = input.first().split(": ")[1].split(" ").map { it.toLong() }

    val srcToDest = mutableMapOf<String, MutableList<Offset>>()
    var end = ""
    input.drop(1).forEach {
        if (it.contains("map:")) {
            // start = it.split("-to-")[0] but we only care about the destination
            end = it.split("-to-")[1].split(" ")[0]
        } else {
            val (destRangeStart, sourceRangeStart, rangeSize) = it.split(" ").map { it.toLong() }

            if (!srcToDest.containsKey(end)) {
                srcToDest[end] = mutableListOf()
            }
            val offset = Offset(
                range = LongRange(sourceRangeStart, sourceRangeStart + rangeSize - 1),
                offset = destRangeStart - sourceRangeStart
            )
            srcToDest[end]!!.add(offset)
        }
    }

    return Garden(
        seeds = seeds,
        srcToDest = srcToDest
    )
}

fun traverseGarden(garden: Garden, seed: Long): Long {
    var current = seed

    for (transform in transforms) {
        for (offset in garden.srcToDest[transform]!!) {
            if (offset.range.contains(current)) {
                current += offset.offset
                break
            }
        }
    }
    return current
}

fun part1(input: List<String>): Long {
    val garden = parseInput(input)

    return garden.seeds.map {
        traverseGarden(garden, it)
    }.min()
}

fun part2(input: List<String>): Long {
    val garden = parseInput(input)
    val ranges = garden.seeds.windowed(2, 2) {
        LongRange(it.first(), it.first() + it[1] - 1)
    }

    var min: Long = Long.MAX_VALUE
    for (range in ranges) {
        for (i in range) {
            val score = traverseGarden(garden, i)
            if (score < min) {
                min = score
            }
        }
    }
    return min
}

fun main() {
    val testInput = readInput("day05_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput) + RESET) }

    val input = readInput("day05.txt")
    printTimeMillis { print("part1 input = $GREEN" + part1(input) + RESET) }
    printTimeMillis { print("part2 input = $GREEN" + part2(input) + RESET) }
}
