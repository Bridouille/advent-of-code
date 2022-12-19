package day17

import GREEN
import RESET
import printTimeMillis
import readInput
import java.math.BigInteger

data class Point(val x: Int, val y: Int)
val ROCKS = listOf(
    setOf(Point(0, 0), Point(1, 0), Point(2, 0), Point(3, 0)),
    setOf(Point(1, 0), Point(0, 1), Point(1, 1), Point(2, 1), Point(1, 2)),
    setOf(Point(0, 0), Point(1, 0), Point(2, 0), Point(2, 1), Point(2, 2)),
    setOf(Point(0, 0), Point(0, 1), Point(0, 2), Point(0, 3)),
    setOf(Point(0, 0), Point(1, 0), Point(0, 1), Point(1, 1)),
)

fun Set<Point>.initialize(offsetX: Int, offsetY: Int) = map { Point(it.x + offsetX, it.y + offsetY) }.toSet()
fun Set<Point>.moveLeft(cave: Set<Point>): Set<Point>? { // null if we can't move
    val leftMoved = map { Point(it.x - 1, it.y) }
    if (leftMoved.any { it.x < 0 } || leftMoved.any { cave.contains(it) }) return null
    return leftMoved.toSet()
}
fun Set<Point>.moveRight(cave: Set<Point>): Set<Point>? { // null if we can't move
    val rightMoved = map { Point(it.x + 1, it.y) }
    if (rightMoved.any { it.x >= 7 } || rightMoved.any { cave.contains(it) }) return null
    return rightMoved.toSet()
}
fun Set<Point>.moveDown(cave: Set<Point>): Set<Point>? { // null if we can't move
    val downMoved = map { Point(it.x, it.y - 1) }
    if (downMoved.any { it.y <= 0 } || downMoved.any { cave.contains(it) }) return null
    return downMoved.toSet()
}

fun part1(input: List<String>, times: Int = 1) = input.first().let { inst ->
    var instIdx = 0
    val cave = mutableSetOf<Point>()

    for (i in 0 until times) {
        val height = (if (cave.isEmpty()) 0 else cave.maxBy { it.y }.y)
        val nextShape = ROCKS[i % ROCKS.size]
        var falling = nextShape.initialize(2, height + 4)

        while (true) {
            val jet = instIdx % inst.length
            val rockIdx = i % ROCKS.size

            if (height > 0 && jet == rockIdx) { // 35 iterations == 53 height
                // println("jet==rock==$jet (iteration=$i) (height=${height})")
            }
            when (inst[instIdx++ % inst.length]) {
                '>' -> falling.moveRight(cave)?.let { falling = it }
                '<' -> falling.moveLeft(cave)?.let { falling = it }
            }
            falling.moveDown(cave)?.let { falling = it } ?: break // We can't fall more
        }
        cave.addAll(falling)
    }
    cave.maxBy { it.y }.y
}

fun part2(input: List<String>, isExample: Boolean) = input.first().let { inst ->
    // Example: cycle starts at 9 iterations, has a size of 35 iterations and height of 53
    // Input: cycle starts at 1724 iterations, has a size of 1715 iterations and height of 2613
    val cycleSize = if (isExample) 35L else 1715L
    val cycleHeight = if (isExample) 53L else 2613L
    val cycleOffset = if (isExample) 9L else 1724L

    val heightInCycle = ((1_000_000_000_000L - cycleOffset) / cycleSize) * cycleHeight
    val endCycles = ((1_000_000_000_000L - cycleOffset) % cycleSize)
    val bottom = part1(input, cycleOffset.toInt()) // flat floor to the first cycle
    val middle = heightInCycle // nb of full cycles * cycleHeight
    val end = part1(input, cycleOffset.toInt() + endCycles.toInt()) - part1(input, cycleOffset.toInt()) // part cycle
    bottom + middle + end
}

fun main() {
    val testInput = readInput("day17_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput, 2022) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput, true) + RESET) }

    val input = readInput("day17.txt")
    printTimeMillis { print("part1 input = $GREEN" + part1(input, 2022) + RESET) }
    printTimeMillis { print("part2 input = $GREEN" + part2(input, false) + RESET) }
}
