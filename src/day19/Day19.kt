package day19

import readInput
import kotlin.math.pow

data class Point(val x: Int, val y: Int, val z: Int) {

    override fun toString(): String {
        return "[$x,$y,$z]"
    }

    fun getPermutations() : List<Point> {
        val totalList = mutableListOf<Point>()
        for (rotateX in listOf(-1, 1)) {
            for (rotateY in listOf(-1, 1)) {
                for (rotateZ in listOf(-1 ,1)) {
                    val (newX , newY, newZ) = listOf(x * rotateX, y * rotateY, z * rotateZ)

                    totalList.addAll(listOf(
                            Point(newX, newY, newZ), Point(newY, newZ, newX), Point(newZ, newX, newY),
                            Point(newX, newZ, newY), Point(newY, newX, newZ), Point(newZ, newY, newX)
                    ))
                }
            }
        }
        return totalList
    }

    fun dist(other: Point) : Long {
        return ((other.x - x).toFloat().pow(2) + (other.y - y).toFloat().pow(2) + (other.z - z).toFloat().pow(2)).toLong()
    }
}
data class Scanner(var pos: Point?, val scans: List<Point>)

fun List<String>.toScanners() : List<Scanner> {
    val scanners = mutableListOf<Scanner>()
    val listOfPoints = mutableListOf<Point>()

    for (line in this.drop(1)) {
        if (line.startsWith("--- scanner")) {
            scanners.add(Scanner(null, mutableListOf<Point>().apply { addAll(listOfPoints) }))
            listOfPoints.clear()
        } else {
            val coord = line.split(",").map { it.toInt() }
            listOfPoints.add(Point(coord[0], coord[1], coord[2]))
        }
    }
    scanners.add(Scanner(null, listOfPoints))
    return scanners
}

fun part1(lines: List<String>) : Int {
    val scanners = lines.toScanners()

    val firstScanner = scanners[0]
    val secondScanner = scanners[1]

    val distances = mutableMapOf<Long, List<Point>>()
    for (i in firstScanner.scans.indices) {
        for (j in i+1 until firstScanner.scans.size) {
            val p1 = firstScanner.scans[i]
            val p2 = firstScanner.scans[j]
            val dist = p1.dist(p2)

            distances[dist] = listOf(p1, p2)
        }
    }

    for (i in secondScanner.scans.indices) {
        for (j in i+1 until secondScanner.scans.size) {
            val p1 = secondScanner.scans[i]
            val p2 = secondScanner.scans[j]
            val dist = p1.dist(p2)

            if (distances.contains(dist)) {
                println("${distances[dist]} => ${Pair(p1, p2)}")
            }
        }
    }

    // TODO:
    // 1) common points, associate point in scanner1 to scanner2
    // 2) figure out scanner1 position
    // 3) de-dup points from scannre1 to a general pool of points
    // 4) repeat from scanner1 to other unexplored scanners

    return 2
}

fun part2(lines: List<String>) : Long {
    return 2
}

fun main() {
    val testInput = readInput("day19/test")
    println("part1(testInput) => " + part1(testInput))
    // println("part2(testInput) => " + part2(testInput))

    // val input = readInput("day19/input")
    // println("part1(input) => " + part1(input))
    // println("part2(input) => " + part2(input))
}
