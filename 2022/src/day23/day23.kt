package day23

import GREEN
import RESET
import printTimeMillis
import readInput

data class Elf(val x: Int, val y: Int) {

    fun nextPos(elves: Set<Elf>, round: Int): Elf {
        val POS = listOf(north(), south(), west(), east())
        val CHOSEN = listOf(
            Elf(x, y - 1), // up
            Elf(x, y + 1), // down
            Elf(x - 1, y), // left
            Elf(x + 1, y)  // right
        )

        var elfAround = false
        for (neighbors in POS.flatten()) {
            if (elves.contains(neighbors)) {
                elfAround = true
                break
            }
        }
        if (!elfAround) return this // Nothing around, don't move

        for (i in 0 until 4) {
            var elfInDirection = false
            for (neighbor in POS[(round + i) % POS.size]) {
                if (elves.contains(neighbor)) {
                    elfInDirection = true
                    break
                }
            }
            // Nothing in that direction, go in that direction
            if (!elfInDirection) return CHOSEN[(round + i) % POS.size]
        }
        return this // Cannot go on any direction but there is another elf in the 8 pos around
    }
    private fun north() = setOf(Elf(x - 1, y - 1), Elf(x, y - 1), Elf(x + 1, y - 1))
    private fun south() = setOf(Elf(x - 1, y + 1), Elf(x, y + 1), Elf(x + 1, y + 1))
    private fun west() = setOf(Elf(x - 1, y - 1), Elf(x - 1, y), Elf(x - 1, y + 1))
    private fun east() = setOf(Elf(x + 1, y - 1 ), Elf(x + 1, y), Elf(x + 1, y + 1))
}

fun List<String>.toPoints(): Set<Elf> {
    val ret = mutableSetOf<Elf>()

    forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            if (c == '#') ret.add(Elf(x, y))
        }
    }
    return ret
}

fun Set<Elf>.print() {
    val (minX, maxX) = Pair(minBy { it.x }.x - 1, maxBy { it.x }.x + 1)
    val (minY, maxY) = Pair(minBy { it.y }.y - 1, maxBy { it.y }.y + 1)

    println("--------")
    for (y in minY..maxY) {
        for (x in minX..maxX) {
            if (contains(Elf(x, y))) print('#') else print('.')
        }
        println()
    }
    println("--------")
}

fun Set<Elf>.count(): Int {
    val (minX, maxX) = Pair(minBy { it.x }.x, maxBy { it.x }.x)
    val (minY, maxY) = Pair(minBy { it.y }.y, maxBy { it.y }.y)
    var ret = 0

    for (y in minY..maxY) {
        for (x in minX..maxX) {
            if (!contains(Elf(x, y))) ret += 1
        }
    }
    return ret
}

fun executeRound(elves: Set<Elf>, roundNb: Int): Set<Elf> {
    val newPos = mutableMapOf<Elf, MutableList<Elf>>() // map of NewPos -> listOf(OriginalPos)

    for (elf in elves) {
        val nextPos = elf.nextPos(elves, roundNb)
        if (newPos.contains(nextPos)) {
            newPos[nextPos]?.add(elf)
        } else {
            newPos[nextPos] = mutableListOf(elf)
        }
    }

    val newPts = mutableSetOf<Elf>()
    for (elf in newPos.keys) {
        val list = newPos[elf]!!
        if (list.size == 1) {
            newPts.add(elf) // add the moved position
        } else {
            newPts.addAll(list) // add the original elves positions
        }
    }
    return newPts
}

fun part1(input: List<String>, rounds: Int = 1): Int {
    var pts = input.toPoints()

    for (round in 0 until rounds) {
        pts = executeRound(pts, round)
    }
    return pts.count()
}

fun part2(input: List<String>): Int {
    var pts = input.toPoints()
    var roundNb = 0

    while (true) {
        val newPts = executeRound(pts, roundNb)

        if (pts == newPts) return roundNb + 1
        pts = newPts
        roundNb += 1
    }
}

fun main() {
    val smallTest = readInput("day23_small_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(smallTest, 4) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(smallTest) + RESET) }

    val testInput = readInput("day23_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput, 10) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput) + RESET) }

    val input = readInput("day23.txt")
    printTimeMillis { print("part1 input = $GREEN" + part1(input, 10) + RESET) }
    printTimeMillis { print("part2 input = $GREEN" + part2(input) + RESET) }
}
