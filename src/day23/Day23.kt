package day23

import readInput
import java.text.SimpleDateFormat
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
        const val HALLWAY_Y = 1
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
    private fun getCavePosition(table: List<CharArray>, amph: List<Amphipod>) = when (name) {
        'A' -> getCaveforX(table, 3, 'A')
        'B' -> getCaveforX(table, 5, 'B')
        'C' -> getCaveforX(table, 7, 'C')
        'D' -> getCaveforX(table, 9, 'D')
        else -> throw IllegalStateException("Unknown name $name")
    }.filter { cavePos ->
        val targetX = cavePos.x
        // There is one fish on the way, impossible to move there
        !amph.any { (it.pos.x < maxOf(targetX, pos.x) && it.pos.x > minOf(targetX, pos.x)) && it.pos.y == HALLWAY_Y }
    }
    private fun getCaveforX(table: List<CharArray>, requiredX: Int, requiredLetter: Char): List<Point> {
        for (depth in table.caveDepth() downTo 2) {
            if (table[depth][requiredX] != requiredLetter) {
                return if (table[depth][requiredX] == '.'){
                    listOf(Point(requiredX, depth))
                } else {
                    emptyList()
                }
            }
        }
        return emptyList()
    }

    fun isInPosition(table: List<CharArray>) = when (name) {
        'A' -> isWellPlaced(table, 3, 'A')
        'B' -> isWellPlaced(table, 5, 'B')
        'C' -> isWellPlaced(table, 7, 'C')
        'D' -> isWellPlaced(table, 9, 'D')
        else -> throw IllegalStateException("Unknown name $name")
    }
    private fun isWellPlaced(table: List<CharArray>, requiredX: Int, requiredLetter: Char) : Boolean {
        val isInCave = pos.x == requiredX && pos.y >= 2
        val allDownAreGood = (pos.y..table.caveDepth()).all { table[it][pos.x] == requiredLetter }
        return isInCave && allDownAreGood
    }

    fun getPossiblePositions(table: List<CharArray>, amph: List<Amphipod>) : List<Point> {
        // Already in the end position
        if (isInPosition(table)) return emptyList()

        // Blocked by another fish to get out
        if (pos.y >= 3 && table[pos.y - 1][pos.x] != '.') return emptyList()

        return if (usedEnergy != 0) { // When we got out of a cave, next step is to get into our cave
            getCavePosition(table, amph)
        } else {
            return getCavePosition(table, amph) + WAIT_POSITIONS.filter { waitPos ->
                val targetX = waitPos.x
                // There is one fish on the way, impossible to move there
                !amph.any { (it.pos.x <= maxOf(targetX, pos.x) && it.pos.x >= minOf(targetX, pos.x)) && it.pos.y == waitPos.y }
            }
        }
    }
    fun getEnergyToMove(newPos: Point): Int {
        val energyToGoUp = abs(HALLWAY_Y - pos.y) * energyForStep()
        val energyX = abs(newPos.x - pos.x) * energyForStep()
        val energyToGoDown = abs(HALLWAY_Y - newPos.y) * energyForStep()

        return energyToGoUp + energyX + energyToGoDown
    }
    fun moveToPos(newPos: Point) {
        usedEnergy += getEnergyToMove(newPos)
        pos = newPos
    }
    fun resetToPos(resetPos: Point, resetEnergy: Int) {
        usedEnergy = resetEnergy
        pos = resetPos
    }
}

fun List<Amphipod>.isDone(table: List<CharArray>) = all { it.isInPosition(table) }
fun List<Amphipod>.getEnergySpent() = sumOf { it.usedEnergy }
fun List<Amphipod>.print() {
    getEmptyCave(this.maxOf { it.pos.y } + 1).forEachIndexed { y, s ->
        for (x in s.indices) {
            this.firstOrNull { it.pos == Point(x, y) }?.let {
                print(it.name)
            } ?: print(s[x])
        }
        println()
    }
}
fun List<Amphipod>.getTable(caveDepth: Int): MutableList<CharArray> {
    return getEmptyCave(caveDepth).mapIndexed { y, s ->
        s.mapIndexed { x, c -> this.firstOrNull { it.pos == Point(x, y) }?.name ?: c }.toCharArray()
    }.toMutableList()
}
fun getEmptyCave(depth: Int) : MutableList<String> {
    val base = mutableListOf(
            "#############",
            "#...........#",
            "###.#.#.#.###",
    )
    for (i in 1 until depth - base.size) {
        base.add("  #.#.#.#.#  ")
    }
    base.add("  #########  ")
    return base
}
fun List<CharArray>.caveDepth() = this.size - 2

var minFound: Int = Integer.MAX_VALUE
fun energyToSolve(table: MutableList<CharArray>, fishes: List<Amphipod>): Int? {
    if (fishes.isDone(table)) {
        minFound = minOf(minFound, fishes.getEnergySpent())
        return fishes.getEnergySpent()
    }
    val energies = mutableListOf<Int>()

    // fishes.print()
    for (amph in fishes) {
        val possiblePos = amph.getPossiblePositions(table, fishes)

        for (tentativePos in possiblePos) {
            val potentialAdd = fishes.getEnergySpent() + amph.getEnergyToMove(tentativePos)
            if (minFound <= potentialAdd) continue

            val tmp = amph.copy()

            table[amph.pos.y][amph.pos.x] = '.'
            table[tentativePos.y][tentativePos.x] = amph.name
            amph.moveToPos(tentativePos)

            energyToSolve(table, fishes)?.let { tenta ->
                energies.add(tenta)
            }

            table[tentativePos.y][tentativePos.x] = '.'
            table[tmp.pos.y][tmp.pos.x] = amph.name
            amph.resetToPos(tmp.pos, tmp.usedEnergy)
        }
    }
    return energies.minOrNull()
}

fun <T>measureTime(block: () -> T): T {
    val start = System.currentTimeMillis()
    return block().also {
        println("Solving took ${SimpleDateFormat("mm:ss:SSS").format(Date(System.currentTimeMillis() - start))}")
    }
}

fun resolve(lines: List<String>): Int {
    val fishes = lines.toAmphipods()
    val table = fishes.getTable(lines.size)
    minFound = Integer.MAX_VALUE

    return measureTime {
        energyToSolve(table, fishes) ?: -1
    }
}

fun main() {
    val simple = readInput("day23/test")
    println("part1(test) => " + resolve(simple))
    val input = readInput("day23/input")
    println("part1(input) => " + resolve(input))

    val simpleDeep = readInput("day23/test2")
    println("part2(simpleDeep) => " + resolve(simpleDeep))
    val inputDeep = readInput("day23/input2")
    println("part2(inputDeep) => " + resolve(inputDeep))
}
