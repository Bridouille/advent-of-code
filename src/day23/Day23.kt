package day23

import readInput
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.util.*
import kotlin.IllegalStateException
import kotlin.math.abs

fun List<String>.toAmphipods() : List<Amphipod> {
    val amphipods = mutableListOf<Amphipod>()
    for (y in this.indices) {
        for (x in this[y].indices) {
            if (listOf('A', 'B', 'C', 'D').contains(this[y][x])) {
                amphipods.add(Amphipod(this[y][x], Point(x, y)))
            }
        }
    }
    return amphipods
}

data class Point(val x: Int, val y:Int)
data class Amphipod(
        val name: Char,
        var pos: Point,
        var usedEnergy: Int = 0
) {
    companion object {
        val HALLWAY_Y = 1
        val WAIT_POSITIONS = listOf(
                Point(1, 1), Point(2, 1), Point(4, 1),
                Point(6, 1),  Point(8, 1), Point(10, 1), Point(11, 1),
        )
    }

    private fun energyForStep() = when (name) {
        'A' -> 1
        'B' -> 10
        'C' -> 100
        'D' -> 1000
        else -> throw IllegalStateException("Unknown name $name")
    }
    private fun getCavePositions(amp: List<Amphipod>) = when (name) {
        'A' -> {
            val possibles = mutableListOf<Point>()
            if (!amp.any { it.pos == Point(3, 2) || it.pos == Point(3, 3) }) {
                possibles.add(Point(3, 3))
            }
            if (amp.any { it.pos == Point(3, 3) && it.name == 'A'} && !amp.any { it.pos == Point(3,2) }) {
                possibles.add(Point(3, 2))
            }
            possibles
        }
        'B' -> {
            val possibles = mutableListOf<Point>()
            if (!amp.any { it.pos == Point(5, 2) || it.pos == Point(5, 3) }) {
                possibles.add(Point(5, 3))
            }
            if (amp.any { it.pos == Point(5, 3) && it.name == 'B'} && !amp.any { it.pos == Point(5,2) }) {
                possibles.add(Point(5, 2))
            }
            possibles
        }
        'C' -> {
            val possibles = mutableListOf<Point>()
            if (!amp.any { it.pos == Point(7, 2) || it.pos == Point(7, 3) }) {
                possibles.add(Point(7, 3))
            }
            if (amp.any { it.pos == Point(7, 3) && it.name == 'C'} && !amp.any { it.pos == Point(7,2) }) {
                possibles.add(Point(7, 2))
            }
            possibles
        }
        'D' -> {
            val possibles = mutableListOf<Point>()
            if (!amp.any { it.pos == Point(9, 2) || it.pos == Point(9, 3) }) {
                possibles.add(Point(9, 3))
            }
            if (amp.any { it.pos == Point(9, 3) && it.name == 'D'} && !amp.any { it.pos == Point(9,2) }) {
                possibles.add(Point(9, 2))
            }
            possibles
        }
        else -> throw IllegalStateException("Unknown name $name")
    }

    fun isInPosition(amph: List<Amphipod>) = when (name) {
        'A' -> pos == Point(3, 3) || (pos == Point(3, 2) && amph.any { it.name == 'A' && it.pos == Point(3, 3) })
        'B' -> pos == Point(5, 3) || (pos == Point(5, 2) && amph.any { it.name == 'B' && it.pos == Point(5, 3) })
        'C' -> pos == Point(7, 3) || (pos == Point(7, 2) && amph.any { it.name == 'C' && it.pos == Point(7, 3) })
        'D' -> pos == Point(9, 3) || (pos == Point(9, 2) && amph.any { it.name == 'D' && it.pos == Point(9, 3) })
        else -> throw IllegalStateException("Unknown name $name")
    }

    fun getPossiblePositions(amph: List<Amphipod>) : List<Point> {
        // Already in the end position
        if (isInPosition(amph)) return emptyList()

        // Blocked by another fish to get out
        if (pos.y == 3 && amph.any { it.pos == Point(pos.x, pos.y - 1) }) return emptyList()

        return if (usedEnergy != 0) { // When we got out of a cave, next step is to get into our cave
            getCavePositions(amph).filter { cavePos ->
                val targetX = cavePos.x
                // There is one fish on the way, impossible to move there
                !amph.any { (it.pos.x < maxOf(targetX, pos.x) && it.pos.x > minOf(targetX, pos.x)) && it.pos.y == HALLWAY_Y }
            }
        } else {
            val directCaves = getCavePositions(amph).filter { cavePos ->
                val targetX = cavePos.x
                // There is one fish on the way, impossible to move there
                !amph.any { (it.pos.x < maxOf(targetX, pos.x) && it.pos.x > minOf(targetX, pos.x)) && it.pos.y == HALLWAY_Y }
            }
            val waitPos = WAIT_POSITIONS.filter { waitPos ->
                val targetX = waitPos.x
                // There is one fish on the way, impossible to move there
                !amph.any { (it.pos.x <= maxOf(targetX, pos.x) && it.pos.x >= minOf(targetX, pos.x)) && it.pos.y == waitPos.y }
            }
            return directCaves + waitPos
        }
    }

    fun moveToPos(newPos: Point) {
        val energyToGoUp = abs(HALLWAY_Y - pos.y) * energyForStep()
        val energyX = abs(newPos.x - pos.x) * energyForStep()
        val energyToGoDown = abs(HALLWAY_Y - newPos.y) * energyForStep()

        this.usedEnergy += energyToGoUp + energyX + energyToGoDown
        this.pos = newPos
    }
    fun resetToPos(pos: Point, energy: Int) {
        this.usedEnergy = energy
        this.pos = pos
    }
}

fun List<Amphipod>.isDone() = all { it.isInPosition(this) }
fun List<Amphipod>.getEnergySpent() = sumOf { it.usedEnergy }
fun List<Amphipod>.print() {
    listOf(
            "#############",
            "#...........#",
            "###.#.#.#.###",
            "  #.#.#.#.#  ",
            "  #########  "
    ).forEachIndexed { y, s ->
        for (x in s.indices) {
            this.firstOrNull { it.pos == Point(x, y) }?.let {
                print(it.name)
            } ?: print(s[x])
        }
        println()
    }
}

fun energyToSolve(fishes: List<Amphipod>): Int? {
    if (fishes.isDone()) {
        // println("Found solution: ${fishes.getEnergySpent()}")
        return fishes.getEnergySpent()
    }
    val energies = mutableListOf<Int>()
    var hasMoved = false

    // fishes.print()
    // Thread.sleep(500)
    for (amph in fishes) {
        val possiblePos = amph.getPossiblePositions(fishes)
        // println("$amph -> $possiblePos")

        for (tentativePos in possiblePos) {
            val tmp = amph.pos.copy()
            val tmpE = amph.usedEnergy
            amph.moveToPos(tentativePos)
            hasMoved = true

            energyToSolve(fishes)?.let { tenta ->
                energies.add(tenta)
            }
            amph.resetToPos(tmp, tmpE)
        }
    }
    if (!hasMoved) return null
    return energies.minOrNull()
}

fun part1(lines: List<String>): Int {
    val fishes = lines.toAmphipods()

    val start = System.currentTimeMillis()
    val result = energyToSolve(fishes) ?: -1

    println("took ${SimpleDateFormat("mm:ss:SSS").format(Date(System.currentTimeMillis() - start))}")
    return result
}

fun part2(lines: List<String>): Long {
    return 2
}

fun main() {

    val simple = readInput("day23/test")
    println("part1(test) => " + part1(simple))
    // println("part2(test) => " + part2(simple))

    // val input = readInput("day23/input")
    // println("part1(input) => " + part1(input)) // // 16157
    // println("part2(input) => " + part2(input))
}
