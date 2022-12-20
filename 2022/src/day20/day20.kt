package day20

import GREEN
import RESET
import printTimeMillis
import readInput

// List of Pair(idx, number)
fun mixString(
    mapOfIdx: MutableList<Pair<Int, Long>>,
    mixTimes: Int = 1,
    decryptionKey: Long = 1
): Long {

    for (i in 0 until mixTimes) {
        for (idx in mapOfIdx.indices) {
            val currentIdx = mapOfIdx.indexOfFirst { it.first == idx }
            val number = mapOfIdx[currentIdx]
            // .mod() The result is either zero or has the same sign as the divisor and has the absolute value less than the absolute value of the divisor
            val newIdx = (currentIdx + number.second * decryptionKey).mod(mapOfIdx.lastIndex)

            mapOfIdx.removeAt(currentIdx)
            mapOfIdx.add(newIdx, number) // we preserve the initial number & initial index
        }
    }
    val zeroIdx = mapOfIdx.indexOfFirst { it.second == 0L }
    return listOf(1000, 2000, 3000).map { decryptionKey * mapOfIdx[(zeroIdx + it) % mapOfIdx.size].second }.sum()
}

fun part1(input: List<String>) = input.let {
    val mapOfIdx = it.mapIndexed { index, s -> Pair(index, s.toLong()) }.toMutableList()
    mixString(mapOfIdx)
}

fun part2(input: List<String>) = input.let {
    val mapOfIdx = it.mapIndexed { index, s -> Pair(index, s.toLong()) }.toMutableList()
    mixString(mapOfIdx,10, decryptionKey = 811589153)
}

fun main() {
    val testInput = readInput("day20_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput) + RESET) }

    val input = readInput("day20.txt")
    printTimeMillis { print("part1 input = $GREEN" + part1(input) + RESET) } // -6188367291625 wrong
    printTimeMillis { print("part2 input = $GREEN" + part2(input) + RESET) } // -9549157974198 wrong, 19079649397877 too high
}
