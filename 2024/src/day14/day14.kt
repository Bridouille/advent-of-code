package day14

import GREEN
import RESET
import printTimeMillis
import readInput
import java.lang.Thread.sleep

data class Point(val x: Int, val y: Int)
data class Robot(val initial: Point, val velocity: Point)

fun getRobots(input: List<String>) : List<Robot> {
    return buildList {
        for (robot in input) {
            val init = robot.split(" ")[0].substringAfter("p=").split(",").map { it.toInt() }
            val velocity = robot.split(" ")[1].substringAfter("v=").split(",").map { it.toInt() }

            add(Robot(Point(init[0], init[1]), Point(velocity[0], velocity[1])))
        }
    }
}

fun part1(input: List<String>): Int {
    val robots = getRobots(input)
    val width = 101
    val height = 103
    val seconds = 100
    val finals = mutableListOf<Point>()

    for (robot in robots) {
        var finalX = (robot.initial.x + robot.velocity.x * seconds) % width
        var finalY = (robot.initial.y + robot.velocity.y * seconds) % height
        while (finalX < 0) finalX += width
        while (finalY < 0) finalY += height
        finals.add(Point(finalX, finalY))
    }

    val topLeft = finals.filter { it.x < width / 2 && it.y < height / 2 }
    val topRight = finals.filter { it.x > width / 2 && it.y < height / 2 }
    val bottomLeft = finals.filter { it.x < width / 2 && it.y > height / 2 }
    val bottomRight = finals.filter { it.x > width / 2 && it.y > height / 2 }

    return topLeft.size * topRight.size * bottomLeft.size * bottomRight.size
}

fun part2(input: List<String>): Int {
    val robots = getRobots(input)
    val width = 101
    val height = 103
    val finals = mutableListOf<Point>()
    val map = Array(height) { Array(width) { '.' } }

    for (seconds in 0 until 10000) {
        finals.clear()
        for (robot in robots) {
            var finalX = (robot.initial.x + robot.velocity.x * seconds) % width
            var finalY = (robot.initial.y + robot.velocity.y * seconds) % height
            while (finalX < 0) finalX += width
            while (finalY < 0) finalY += height
            finals.add(Point(finalX, finalY))
        }

        for (y in 0 until height) {
            for (x in 0 until width) {
                map[y][x] = if (finals.contains(Point(x, y))) {
                    '#'
                } else {
                    '.'
                }
            }
        }

        if (map.any { it.joinToString("").contains("##################") }) {
            for (line in map) {
                println(line.joinToString(""))
            }
            println("Found seconds: $seconds")
            return seconds
        }

        for (y in 0 until height) {
            for (x in 0 until width) {
                map[y][x] = '.'
            }
        }
    }
    throw IllegalStateException("Didn't find seconds")
}

fun main() {
    val testInput = readInput("day14_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput) + RESET) }
    // printTimeMillis { print("part2 example = $GREEN" + part2(testInput) + RESET) }

    val input = readInput("day14.txt")
    printTimeMillis { print("part1 input = $GREEN" + part1(input) + RESET) }
    printTimeMillis { print("part2 input = $GREEN" + part2(input) + RESET) }
}
