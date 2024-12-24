package day12

import GREEN
import RESET
import day10.VISITED
import day10.getScore
import printTimeMillis
import readInput

fun List<MutableList<Int>>.safeGet(y: Int, x: Int): Int? {
    return if (y < 0 || y >= size || x < 0 || x >= this[0].size) {
        null
    } else {
        this[y][x]
    }
}

// Return a Pair<Area, Perimeter>
fun exploreWithPerimeter(map: List<MutableList<Int>>, y: Int, x: Int, toFind: Int): Pair<Int, Int> {
    if (y < 0 || y >= map.size || x < 0 || x >= map[0].size) return Pair(0, 1) // OOB, 0 Area, 1 Perimeter
    if (map[y][x] > 0 && map[y][x] != toFind) return Pair(0, 1) // Getting on another region, 0 Area, 1 Perimeter
    if (map[y][x] < 0) return Pair(0, 0) // We already visited this case, ignore it

    map[y][x] = -1 // At this point map[y][x] == toFind
    val top = exploreWithPerimeter(map, y - 1, x, toFind)
    val left = exploreWithPerimeter(map, y, x - 1, toFind)
    val right = exploreWithPerimeter(map, y, x + 1, toFind)
    val bottom = exploreWithPerimeter(map, y + 1, x, toFind)
    return Pair(
        1 + top.first + left.first + right.first + bottom.first,
        0 + top.second + left.second + right.second + bottom.second,
    )
}


fun isTopLeft(map: List<MutableList<Int>>, y: Int, x: Int, toFind: Int): Int {
    return if ((map.safeGet(y - 1, x) != toFind && map.safeGet(y, x - 1) != toFind) ||
        (map.safeGet(y - 1, x) == toFind && map.safeGet(y, x + 1) == toFind && map.safeGet(y - 1, x + 1) != toFind)) { 1 } else { 0 }
}

fun isTopRight(map: List<MutableList<Int>>, y: Int, x: Int, toFind: Int): Int {
    return if ((map.safeGet(y - 1, x) != toFind && map.safeGet(y, x + 1) != toFind) ||
        (map.safeGet(y - 1, x) == toFind && map.safeGet(y, x - 1) == toFind && map.safeGet(y - 1, x - 1) != toFind)) { 1 } else { 0 }
}

fun isBottomLeft(map: List<MutableList<Int>>, y: Int, x: Int, toFind: Int): Int {
    return if ((map.safeGet(y + 1, x) != toFind && map.safeGet(y, x - 1) != toFind) ||
        (map.safeGet(y + 1, x) == toFind && map.safeGet(y, x - 1) == toFind && map.safeGet(y + 1, x - 1) != toFind)) { 1 } else { 0 }
}

fun isBottomRight(map: List<MutableList<Int>>, y: Int, x: Int, toFind: Int): Int {
    return if ((map.safeGet(y + 1, x) != toFind && map.safeGet(y, x + 1) != toFind) ||
        (map.safeGet(y + 1, x) == toFind && map.safeGet(y, x + 1) == toFind && map.safeGet(y + 1, x + 1) != toFind)) { 1 } else { 0 }
}

fun getCorners(map: List<MutableList<Int>>, y: Int, x: Int, toFind: Int): Int {
    return isTopLeft(map, y, x, toFind) + isTopRight(map, y, x, toFind) + isBottomLeft(map, y, x, toFind) + isBottomRight(map, y, x, toFind)
}

fun explore(input: List<String>, perimeter: Boolean = false): Int {
    val list = input.map { it.toCharArray().map { it.code }.toMutableList() }.toMutableList()
    val visited = mutableSetOf<Pair<Int, Int>>()
    var totalPrice = 0

    for (y in list.indices) {
        for (x in list[y].indices) {
            if (list[y][x] > 0 && !visited.contains(y to x)) {
                val letter = list[y][x]
                val result = exploreWithPerimeter(list, y, x, letter)

                if (perimeter) {
                    totalPrice += result.first * result.second
                }

                val area = mutableSetOf<Pair<Int, Int>>()
                for (i in list.indices) {
                    for (j in list[i].indices) {
                        if (list[i][j] == -1) {
                            area.add(i to j)
                            list[i][j] = letter
                        }
                    }
                }
                visited.addAll(area)

                if (!perimeter) {
                    var corners = 0
                    for (point in area)  {
                        // For each point in the area, get their corners, corners == sides
                        corners += getCorners(list, point.first, point.second, letter)
                    }
                    totalPrice += area.size * corners
                }
            }
        }
    }
    return totalPrice
}

fun part1(input: List<String>) = explore(input, perimeter = true)

fun part2(input: List<String>) = explore(input, perimeter = false)

fun main() {
    val testInput = readInput("day12_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput) + RESET) }

    val input = readInput("day12.txt")
    printTimeMillis { print("part1 input = $GREEN" + part1(input) + RESET) }
    printTimeMillis { print("part2 input = $GREEN" + part2(input) + RESET) }
}
