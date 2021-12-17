package day17

import readInput

data class Point(val x: Int, val y: Int)
data class Area(val start: Int, val end: Int) {
    fun contains(pos: Int) = this.start <= pos && this.end >= pos
}

fun List<String>.toAreas() : List<Area> {
    val area = first().substringAfter("target area: ").split(" ")
    val areaX = area.first().substringAfter("x=").dropLast(1).let {
        Area(it.split("..")[0].toInt(), it.split("..")[1].toInt())
    }
    val areaY = area.last().substringAfter("y=").let {
        Area(it.split("..")[0].toInt(), it.split("..")[1].toInt())
    }
    return listOf(areaX, areaY)
}

val memoX = mutableMapOf<String, Int>() // key = "xVelocity/x/steps"
val memoY = mutableMapOf<String, Int>() // key = "yVelocity/y/steps"

fun calcX(x: Int, xVelocity: Int, steps: Int): Int {
    if (xVelocity <= 0 || steps <= 0) return x

    val key = "$xVelocity/$x/$steps"
    if (!memoX.containsKey(key)) {
        memoX[key] = x + xVelocity + calcX(x,xVelocity - 1, steps - 1)
    }
    return memoX[key]!!
}

fun calcY(y: Int, yVelocity: Int, steps: Int): Int {
    if (steps <= 0) return y

    val key = "$yVelocity/$y/$steps"
    if (!memoY.containsKey(key)) {
        memoY[key] = y + yVelocity + calcY(y, yVelocity - 1, steps - 1)
    }
    return memoY[key]!!
}

fun posAfterSteps(xVelocity: Int, yVelocity: Int, steps: Int) = Point(calcX(0, xVelocity, steps), calcY(0, yVelocity, steps))

// result = null means we never hit the target with the provided xVelocity and yVelocity
fun getMaxYForVelocity(xVelocity: Int, yVelocity: Int, areaX: Area, areaY: Area): Int? {
    var step = 1
    var currentPos = Point(0, 0) // Start in 0,0
    var maxY = 0
    var hitTargetArea = false

    while (currentPos.x <= areaX.end && currentPos.y >= areaY.start) {
        if (areaX.contains(currentPos.x) && areaY.contains(currentPos.y)) { // we found a hit
            hitTargetArea = true
        }
        currentPos = posAfterSteps(xVelocity, yVelocity, step)
        maxY = maxOf(maxY, currentPos.y)
        step++
    }
    return if (hitTargetArea) maxY else null
}

fun part1(lines: List<String>): Int {
    val (areaX, areaY) = lines.toAreas()

    var maxY = 0 // starting in 0,0 the maxY is always 0
    for (xVelocity in areaX.end downTo 0) {
        for (yVelocity in areaY.start..areaX.start) {
            getMaxYForVelocity(xVelocity, yVelocity, areaX, areaY)?.let {
                maxY = maxOf(it, maxY)
            }
        }
    }
    return maxY
}

fun part2(lines: List<String>): Int {
    val (areaX, areaY) = lines.toAreas()

    var numberOfHits = 0
    for (xVelocity in areaX.end downTo 0) {
        for (yVelocity in areaY.start..areaX.start) {
            getMaxYForVelocity(xVelocity, yVelocity, areaX, areaY)?.let {
                numberOfHits++
            }
        }
    }
    return numberOfHits
}

fun main() {
    val testInput = readInput("day17/test")
    println("part1(testInput) => " + part1(testInput))
    println("part2(testInput) => " + part2(testInput))

    val input = readInput("day17/input")
    println("part1(input) => " + part1(input))
    println("part2(input) => " + part2(input))
}
