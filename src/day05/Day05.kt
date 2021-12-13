package day05

import readInput
import kotlin.math.abs

data class Point(val x: Int, val y: Int)

fun part1(lines: List<String>) : Int {
    val boardLines = mutableListOf<Point>()

    for (line in lines) {
        val split = line.filter { !it.isWhitespace() }.split("->")
        val fromX = split[0].split(",")[0].toInt()
        val fromY = split[0].split(",")[1].toInt()
        val toX = split[1].split(",")[0].toInt()
        val toY = split[1].split(",")[1].toInt()

        if (fromX == toX) { // horizontal line
            for (i in minOf(fromY, toY)..maxOf(fromY, toY)) {
                boardLines.add(Point(fromX, i))
            }
        } else if (fromY == toY) { // vertical line
            for (i in minOf(fromX, toX)..maxOf(fromX, toX)) {
                boardLines.add(Point(i, fromY))
            }
        }
    }

    val pointsOccurences = boardLines.groupingBy { it }.eachCount().values

    return pointsOccurences.count { it >= 2 }
}

fun part2(lines: List<String>) : Int {
    val boardLines = mutableListOf<Point>()

    for (line in lines) {
        val split = line.filter { !it.isWhitespace() }.split("->")
        val fromX = split[0].split(",")[0].toInt()
        val fromY = split[0].split(",")[1].toInt()
        val toX = split[1].split(",")[0].toInt()
        val toY = split[1].split(",")[1].toInt()

        if (fromX == toX) { // horizontal line
            for (i in minOf(fromY, toY)..maxOf(fromY, toY)) {
                boardLines.add(Point(fromX, i))
            }
        } else if (fromY == toY) { // vertical line
            for (i in minOf(fromX, toX)..maxOf(fromX, toX)) {
                boardLines.add(Point(i, fromY))
            }
        } else if (abs(fromX - toX) == abs(fromY - toY)) { // diagonal lines
            val dist = abs(fromX - toX) // == fromY - toY
            val incrementX = if (toX > fromX) { 1 } else { -1 }
            val incrementY = if (toY > fromY) { 1 } else { -1 }

            for (i in 0..dist) {
                boardLines.add(Point(fromX + (incrementX * i), fromY + (incrementY * i)))
            }
        }
    }

    val pointsOccurences = boardLines.groupingBy { it }.eachCount().values

    return pointsOccurences.count { it >= 2 }
}

fun main() {
    val testInput = readInput("day05/test")
    println("part1(testInput) => " + part1(testInput))
    println("part2(testInput) => " + part2(testInput))

    val input = readInput("day05/input")
    println("part1(input) => " + part1(input))
    println("part1(input) => " + part2(input))
}
