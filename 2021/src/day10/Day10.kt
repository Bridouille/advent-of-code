package day10

import readInput

fun part1(lines: List<String>) : Int {
    val chars = mapOf('(' to ')', '[' to ']', '{' to '}', '<' to '>')
    val points = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)
    var total = 0

    for (line in lines) {
        val stack = mutableListOf<Char>()

        for (c in line) {
            if (chars.keys.contains(c)) { // opening char
                stack.add(c)
            } else if (chars.values.contains(c)) { // closing char
                if (c != chars[stack[stack.lastIndex]]) { // corrupted line
                    total += points[c]!!
                    break
                } else {
                    stack.removeAt(stack.lastIndex)
                }
            }
        }
    }
    return total
}

fun part2(lines: List<String>) : Long {
    val chars = mapOf('(' to ')', '[' to ']', '{' to '}', '<' to '>')
    val points = mapOf(')' to 1, ']' to 2, '}' to 3, '>' to 4)
    val scores = mutableListOf<Long>()

    for (line in lines) {
        val stack = mutableListOf<Char>()

        for (c in line) {
            if (chars.keys.contains(c)) { // opening char
                stack.add(c)
            } else if (chars.values.contains(c)) { // closing char

                if (c != chars[stack[stack.lastIndex]]) { // corrupted line
                    stack.clear()
                    break
                } else {
                    stack.removeAt(stack.lastIndex)
                }
            }
        }

        var lineScore = 0L
        while (!stack.isEmpty()) { // incomplete line
            lineScore = lineScore * 5 + points[chars[stack[stack.lastIndex]]]!!
            stack.removeAt(stack.lastIndex)
        }
        if (lineScore > 0) scores.add(lineScore)
    }
    scores.sort()
    return scores[scores.size / 2]
}

fun main() {
    val testInput = readInput("day10/test")
    println("part1(testInput) => " + part1(testInput))
    println("part2(testInput) => " + part2(testInput))

    val input = readInput("day10/input")
    println("part1(input) => " + part1(input))
    println("part1(input) => " + part2(input))
}
