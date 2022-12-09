package day09

import GREEN
import RESET
import printTimeMillis
import readInput
import kotlin.math.abs

data class Point(var x: Int, var y: Int) {
    private fun right() { x += 1 }
    private fun left() { x-= 1 }
    private fun up() { y -= 1 }
    private fun down() { y += 1 }
    fun executeInstruction(inst: String) {
        when (inst) {
            "R" -> right()
            "U" -> up()
            "L" -> left()
            "D" -> down()
            else -> throw IllegalStateException("Wrong move")
        }
    }

    fun follow(other: Point) {
        when {
            other.x == x -> {
                when {
                    other.y > y + 1 -> y += 1
                    other.y < y - 1 -> y -= 1
                }
            }
            other.y == y -> {
                when {
                    other.x > x + 1 -> x += 1
                    other.x < x - 1 -> x -= 1
                }
            }
            else -> { // diagonals
                val diffX = other.x - x
                val diffY = other.y - y
                if (abs(diffX) > 1 || abs(diffY) > 1) {
                    if (diffX > 0) { x += 1
                    } else { x -= 1 }

                    if (diffY > 0) { y += 1
                    } else { y -= 1 }
                }
            }
        }
    }
}

fun part1(input: List<String>, ropeSize: Int = 2): Int {
    val tailPos = mutableSetOf<Pair<Int, Int>>()
    val rope = List(ropeSize) { Point(0, 0) }

    input.forEach {
        val inst = it.split(" ")
        repeat(inst.last().toInt()) {
            rope.first().executeInstruction(inst.first())
            for (i in rope.indices.drop(1)) {
                rope[i].follow(rope[i - 1])
            }
            tailPos.add(Pair(rope.last().x, rope.last().y))
        }
    }
    return tailPos.size
}

fun part2(input: List<String>) = part1(input, 10)

fun main() {
    val testInput = readInput("day09_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput) + RESET) }

    val input = readInput("day09.txt")
    printTimeMillis { print("part1 input = $GREEN" + part1(input) + RESET) }
    printTimeMillis { print("part2 input = $GREEN" + part2(input) + RESET) }
}
