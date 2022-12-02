package day02

import readInput

fun String.shape(startLetter: Char) = (this.first() - startLetter) + 1

// A == "Rock" -- B == "Paper" -- C == "Scissors"
// lose == 0, draw == 3, win == 6
val shapeToScore = mapOf(
    "X" to mapOf("A" to 3, "B" to 0, "C" to 6),
    "Y" to mapOf("A" to 6, "B" to 3, "C" to 0),
    "Z" to mapOf("A" to 0, "B" to 6, "C" to 3),
)

fun part1(input: List<String>) = input.fold(0) { acc, line ->
    val plays = line.split(" ")
    acc + shapeToScore[plays[1]]!![plays[0]]!! + plays[1].shape('X')
}

val outcomeToShape = mapOf(
    "A" to mapOf("Y" to "A", "X" to "C", "Z" to "B"),
    "B" to mapOf("Y" to "B", "X" to "A", "Z" to "C"),
    "C" to mapOf("Y" to "C", "X" to "B", "Z" to "A")
)
val wantedScore = mapOf("X" to 0, "Y" to 3, "Z" to 6)

fun part2(input: List<String>) = input.fold(0) { acc, line ->
    val plays = line.split(" ")
    val wantedScore = wantedScore[plays[1]]!!
    val wantedPlay = outcomeToShape[plays[0]]!![plays[1]]!!
    acc + wantedScore + wantedPlay.shape('A')
}

fun main() {
    val testInput = readInput("day02_example.txt")
    println("part1 example = " + part1(testInput))
    println("part2 example = " + part2(testInput))

    val input = readInput("day02.txt")
    println("part1 input = " + part1(input))
    println("part2 input = " + part2(input))
}
