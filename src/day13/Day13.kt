package day13

import readInput

data class Point(val x: Int, val y: Int)
sealed class Fold {
    data class Up(val y: Int) : Fold()
    data class Left(val x: Int) : Fold()
}

fun List<String>.toPoints() = filter { !it.contains("fold") }.map {
    val x = it.split(",")[0]
    val y = it.split(",")[1]
    Point(x.toInt(), y.toInt())
}
fun List<String>.toFolds() = filter { it.contains("fold") }.map {
    when {
        it.contains("y=") -> Fold.Up(it.substringAfter("y=").toInt())
        it.contains("x=") -> Fold.Left(it.substringAfter("x=").toInt())
        else -> null
    }
}.filterNotNull()

// transform the list of coordonnates to an Array<CharArray> containing '.' or '#'
fun List<String>.toTable(): Array<CharArray>? {
    val points = this.toPoints()
    val maxX = points.maxByOrNull { it.x } ?: return null
    val maxY = points.maxByOrNull { it.y } ?: return null

    val table = Array(maxY.y + 1) { CharArray(maxX.x + 1) { '.' } }
    points.forEach { table[it.y][it.x] = '#' }

    return table
}

fun applyFold(table: Array<CharArray>, fold: Fold) : Array<CharArray> {
    val newMaxY = if (fold is Fold.Up) { fold.y } else { table.size }
    val newMaxX = if (fold is Fold.Left) { fold.x } else { table[0].size }
    val newTable = Array(newMaxY) { CharArray(newMaxX) { '.' } }

    // Apply folding
    for (y in 0 until table.size) {
        for (x in 0 until table[0].size) {

            if (y < newMaxY && x < newMaxX) {
                newTable[y][x] = table[y][x]
            } else if (y > newMaxY || x > newMaxX) {

                // if maxSize == 7, then 8 -> 6, 9 -> 5, 10 -> 4, 14 -> 0
                val foldedY = if (y > newMaxY) {
                    newMaxY - (((y / newMaxY - 1) * newMaxY) + y % newMaxY)
                } else { y }
                val foldedX = if (x > newMaxX) {
                    newMaxX - (((x / newMaxX - 1) * newMaxX) + x % newMaxX)
                } else { x }

                if (table[y][x] == '#') {
                    newTable[foldedY][foldedX] = table[y][x]
                }
            }
        }
    }
    return newTable
}

fun part1(lines: List<String>) : Int {
    val table = lines.toTable() ?: return -1

    return applyFold(table, lines.toFolds().first()).toList().map {
        it.toList().count { it == '#' }
    }.sum()
}

fun part2(lines: List<String>) : Int {
    var table = lines.toTable() ?: return -1

    for (fold in lines.toFolds()) {
        table = applyFold(table, fold)
    }

    for (l in table) println(l) // Just read the output in terminal :)

    return table.toList().map { it.toList().count { it == '#' } }.sum()
}

fun main() {
    val testInput = readInput("day13/test")
    println("part1(testInput) => " + part1(testInput))
    println("part2(testInput) => " + part2(testInput))

    val input = readInput("day13/input")
    println("part1(input) => " + part1(input))
    println("part1(input) => " + part2(input))
}
