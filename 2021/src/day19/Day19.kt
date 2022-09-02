package day19

import readInput
import java.lang.Math.abs
import java.util.*
import kotlin.math.pow

typealias BeaconPairList = List<Pair<Pair<Point, Point>, Pair<Point, Point>>>

data class Point(val x: Int, val y: Int, val z: Int) {

    override fun toString() = "($x,$y,$z)"

    // https://stackoverflow.com/questions/16452383/how-to-get-all-24-rotations-of-a-3-dimensional-array
    private fun roll(p: Point) = Point(p.x, p.z, -p.y)
    private fun turn(p: Point) = Point(-p.y, p.x, p.z)
    fun permutations(): List<Point> {
        val res = mutableListOf<Point>()
        var p = this

        for (cycle in 1..2) {
            for (step in 1..3) {
                p = roll(p)
                res.add(p)

                for (i in 1..3) {
                    p = turn(p)
                    res.add(p)
                }
            }
            p = roll(turn(roll(p)))
        }
        return res
    }

    fun dist(other: Point) : Long {
        return ((other.x - x).toFloat().pow(2) + (other.y - y).toFloat().pow(2) + (other.z - z).toFloat().pow(2)).toLong()
    }

    fun plus(other: Point) = Point(x + other.x, y + other.y, z + other.z)
    fun minus(other: Point) = Point(x - other.x, y - other.y, z - other.z)
    fun absolut() = Point(abs(x), abs(y), abs(z))
}
data class Scanner(val id: Int, var scans: List<Point>, var pos: Point? = null, var rotationIdx: Int? = null) {
    fun manhattan(other: Scanner) = pos?.let {
        other.pos?.let { otherPos ->
            (it.x - otherPos.x) + (it.y - otherPos.y) + (it.z - otherPos.z)
        } ?: -1
    } ?: -1
}

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

/**
 * Gets a list of fingerprints of all pairs of points in the given list
 */
fun getListOfFingerprints(beacons: List<Point>) : Map<Long, Pair<Point, Point>> {
    val distances = mutableMapOf<Long, Pair<Point, Point>>()

    for (i in beacons.indices) {
        for (j in i+1 until beacons.size) {
            val p1 = beacons[i]
            val p2 = beacons[j]
            val fingerprint = p1.absolut().dist(p2.absolut())

            distances[fingerprint] = Pair(p1, p2)
        }
    }
    return distances
}

/**
 * From a list of fingerprints, return a list of pairs of pairs of points which have the same fingerprint
 */
fun getCommonBeacons(fingerprintsFromS1: Map<Long, Pair<Point, Point>>, s2Beacons: List<Point>): BeaconPairList {
    val pairOfPoints = mutableListOf<Pair<Pair<Point, Point>, Pair<Point, Point>>>()

    for (i in s2Beacons.indices) {
        for (j in i+1 until s2Beacons.size) {
            val p1 = s2Beacons[i]
            val p2 = s2Beacons[j]
            val fingerprint = p1.absolut().dist(p2.absolut())

            if (fingerprintsFromS1.containsKey(fingerprint)) {
                pairOfPoints.add(Pair(fingerprintsFromS1[fingerprint]!!, Pair(p1, p2)))
            }
        }
    }
    return pairOfPoints
}
fun findScannerPosition(commonBeacon: BeaconPairList, rotationIdx: Int): Point {
    val listOfDiffs = mutableListOf<Point>()

    for (pair in commonBeacon) {
        val pair1 = pair.first
        val pair2 = pair.second

        val pair2Rotated = Pair(pair2.first.permutations()[rotationIdx], pair2.second.permutations()[rotationIdx])
        val vecA = pair1.first.plus(pair1.second)
        val vecB = pair2Rotated.first.plus(pair2Rotated.second)
        listOfDiffs.add(vecA.minus(vecB))
    }

    return listOfDiffs.groupingBy { it }.eachCount().toList().sortedByDescending { it.second }.map {
        Point(it.first.x / 2, it.first.y / 2, it.first.z / 2)
    }.first()
}

/**
 * For each pair, tries to rotate the second pair (point1 & point2)
 * make a vector from p1 -> p2 and compare the distance
 */
fun findRotationIdx(commonBeacon: BeaconPairList): Int {
    val aligned = mutableMapOf<Int, MutableList<Long>>()

    for (i in 0 until 24) {
        for (pair in commonBeacon) {
            val firstPair = pair.first
            val secondPair = pair.second
            val p1 = secondPair.first.permutations()[i]
            val p2 = secondPair.second.permutations()[i]
            val vec1 = p1.plus(p2)
            val vec2 = firstPair.first.plus(firstPair.second)
            val dist = vec1.dist(vec2)

            if (!aligned.containsKey(i)) {
                aligned[i] = mutableListOf()
            }
            aligned[i]?.add(dist)
        }
    }
    return aligned.toList().map {
        it.first to it.second.groupingBy { it }.eachCount().toList().maxByOrNull { it.second }!!.second
    }.maxByOrNull { it.second }!!.first
}

data class ScanOverlap(val commonBeacons: BeaconPairList, val scanner: Scanner)
fun findCommonBeacons(scanners: List<Scanner>): Set<Point> {
    // Always process the scanners that have the most overlaps
    val overlaps = PriorityQueue<ScanOverlap> { a, b ->
        b.commonBeacons.size - a.commonBeacons.size
    }
    scanners.first().pos = Point(0, 0, 0)
    val points = mutableSetOf<Point>().apply { addAll(scanners.first().scans) }
    val matched = mutableListOf<Int>().apply { add(scanners.first().id) }

    while (matched.size < scanners.size) {
        for (scanner in scanners) {
            if (matched.contains(scanner.id)) continue
            val fingerPrints = getListOfFingerprints(points.toList())
            val commonBeacons = getCommonBeacons(fingerPrints, scanner.scans)

            overlaps.add(ScanOverlap(commonBeacons, scanner))
        }

        val mostOverlap = overlaps.remove()
        val rotationIdx = findRotationIdx(mostOverlap.commonBeacons)
        val scannerPos = findScannerPosition(mostOverlap.commonBeacons, rotationIdx)

        // Translate the points of the scanner in our initial referencial
        scanners.find { it.id == mostOverlap.scanner.id }!!.scans = mostOverlap.scanner.scans.map {
            it.permutations()[rotationIdx].plus(scannerPos)
        }
        scanners.find { it.id == mostOverlap.scanner.id }!!.pos = scannerPos
        points.addAll(mostOverlap.scanner.scans) // add the translated points into our global point pool
        matched.add(mostOverlap.scanner.id)
        overlaps.clear()
    }
    return points
}

fun part1(lines: List<String>) = findCommonBeacons(lines.toScanners()).size

fun part2(lines: List<String>) : Int {
    val scanners = lines.toScanners()
    findCommonBeacons(scanners)

    var maxDist = Integer.MIN_VALUE
    for (i in scanners.indices) {
        for (j in i+1 until scanners.size) {
            maxDist = maxOf(maxDist, scanners[i].manhattan(scanners[j]))
        }
    }
    return maxDist
}

fun main() {
    val testInput = readInput("day19/test")
    println("part1(testInput) => " + part1(testInput))
    println("part2(testInput) => " + part2(testInput))

    val input = readInput("day19/input")
    println("part1(input) => " + part1(input))
    println("part2(input) => " + part2(input))
}
