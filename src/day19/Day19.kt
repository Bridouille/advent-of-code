package day19

import readInput
import kotlin.math.pow

data class Point(val x: Int, val y: Int, val z: Int) {

    override fun toString(): String {
        return "[$x,$y,$z]"
    }

    fun getPermutations() : Set<Point> {
        val totalList = mutableSetOf<Point>()
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
data class Scanner(val id: Int, val scans: List<Point>)

fun List<String>.toScanners() : List<Scanner> {
    val scanners = mutableListOf<Scanner>()
    val listOfPoints = mutableListOf<Point>()

    for (line in this.drop(1)) {
        if (line.startsWith("--- scanner")) {
            scanners.add(Scanner(scanners.size, mutableListOf<Point>().apply { addAll(listOfPoints) }))
            listOfPoints.clear()
        } else {
            val coord = line.split(",").map { it.toInt() }
            listOfPoints.add(Point(coord[0], coord[1], coord[2]))
        }
    }
    scanners.add(Scanner(scanners.size, listOfPoints))
    return scanners
}

fun Set<Scanner>.findScannerWithCommonBeacons(from: Scanner, inCommon: Int = 12): Scanner {
    val points1 = mutableSetOf<Point>()
    val points2 = mutableSetOf<Point>()
    val distances = mutableMapOf<Long, Pair<Point, Point>>()

    for (i in from.scans.indices) { // compute the distances for the scanner we're looking from
        for (j in i+1 until from.scans.size) {
            val p1 = from.scans[i]
            val p2 = from.scans[j]
            val dist = p1.dist(p2)

            distances[dist] = Pair(p1, p2)
        }
    }

    for (scanner in this) { // do the same for all the other scanner
        for (i in scanner.scans.indices) {
            for (j in i+1 until scanner.scans.size) {
                val p1 = scanner.scans[i]
                val p2 = scanner.scans[j]
                val dist = p1.dist(p2)

                if (distances.contains(dist)) {
                    points2.addAll(listOf(p1, p2))
                    points1.addAll(distances[dist]!!.toList())
                }
            }
        }
        println("${points2.size} - $points2")
        // match the points with each others
        val list2 = points2.toList().mapIndexed { index, point ->
            point to points2.toList().map { it.dist(point) }.sum() // each point -> sum of distances
        }.sortedBy { it.second }
        val list1 = points1.toList().mapIndexed { index, point ->
            point to points1.toList().map { it.dist(point) }.sum() // each point -> sum of distances
        }.sortedBy { it.second }
        println(list1)
        println(list2)

        if (points2.size >= inCommon) return scanner
        points2.clear()
    }
    throw IllegalStateException("Did not find a scanner with [$inCommon] common points")
}

fun part1(lines: List<String>) : Int {
    val scanners = lines.toScanners()
    val poolOfBeacons = mutableSetOf<Point>().apply { addAll(scanners.first().scans) }

    val toFind = mutableSetOf<Scanner>().apply { addAll(scanners.drop(1)) }
    val from = scanners.first()

    val found = toFind.findScannerWithCommonBeacons(from)
    println("Found scanner ${found.id} matching from ${from.id}")

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
