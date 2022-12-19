package day17

import GREEN
import RESET
import printTimeMillis
import readInput

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

fun Set<Point>.print() {
    println("-------")
    for (y in (maxBy { it.y }.y) downTo 1) {
        for (x in 0..6) if (contains(Point(x, y))) print("#") else print(".")
        println()
    }
    println("-------\n")
}

fun part2(input: List<String>, times: Long = 1L) = input.first().let { inst ->
    2
}

fun main() { // 1_000_000_000_000
    val testInput = readInput("day17_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput, 2022) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput, 2022L) + RESET) }

    val input = readInput("day17.txt")
    printTimeMillis { print("part1 input = $GREEN" + part1(input, 2022) + RESET) }
    // printTimeMillis { print("part2 input = $GREEN" + part2(input) + RESET) }
}
