package day22

import readInput
import java.lang.IllegalStateException

enum class InstructionType { ON, OFF }
data class Bound(val min: Int, val max:Int) {
    fun isSmall() = min >= -50 && max <= 50
}
data class Instruction(val type: InstructionType, val xBound: Bound, val yBound: Bound, val zBound: Bound) {
    fun toArea() = Area(xBound, yBound, zBound)
}
data class Point(val x: Int, val y: Int, val z: Int)
data class Area(val xBound: Bound, val yBound: Bound, val zBound: Bound) {
    fun isEmpty() = size() == 0L
    override fun toString() = "([${xBound.min},${xBound.max}], [${yBound.min},${yBound.max}], [${zBound.min},${zBound.max}])"
    fun size(): Long = maxOf((xBound.max - xBound.min) + 1L,0L) * maxOf((yBound.max - yBound.min) + 1L,0L) * maxOf((zBound.max - zBound.min) + 1L,0L)
}

fun List<String>.toInstructions() : List<Instruction> = map {
    val spaces = it.split(" ")
    val type = when  (spaces[0]) {
        "on" -> InstructionType.ON
        "off" -> InstructionType.OFF
        else -> throw IllegalStateException("Unknown instruction: ${spaces[0]}")
    }
    val commas = spaces[1].split(",")
    val xBounds = commas[0].substringAfter("x=")
    val yBounds = commas[1].substringAfter("y=")
    val zBounds = commas[2].substringAfter("z=")
    Instruction(type,
            Bound(xBounds.split("..")[0].toInt(), xBounds.split("..")[1].toInt()),
            Bound(yBounds.split("..")[0].toInt(), yBounds.split("..")[1].toInt()),
            Bound(zBounds.split("..")[0].toInt(), zBounds.split("..")[1].toInt()),
    )
}

fun part1(lines: List<String>): Int {
    val instructions = lines.toInstructions()
    val turnedOn = mutableSetOf<Point>()

    for (inst in instructions) {
        if (!inst.xBound.isSmall() || !inst.yBound.isSmall() || !inst.zBound.isSmall()) {
            continue
        }
        for (x in inst.xBound.min..inst.xBound.max) {
            for (y in inst.yBound.min..inst.yBound.max) {
                for (z in inst.zBound.min..inst.zBound.max) {
                    when (inst.type) {
                        InstructionType.ON -> turnedOn.add(Point(x, y, z))
                        InstructionType.OFF -> turnedOn.remove(Point(x, y, z))
                    }
                }
            }
        }
    }
    return turnedOn.size
}

/**
 * Returns an Area that represents the intersection with the given Area
 * If there are no intersection, Area.isEmpty() will return true
 */
fun Area.intersection(other: Area): Area {
    return Area(
            Bound(maxOf(xBound.min, other.xBound.min), minOf(xBound.max, other.xBound.max)),
            Bound(maxOf(yBound.min, other.yBound.min), minOf(yBound.max, other.yBound.max)),
            Bound(maxOf(zBound.min, other.zBound.min), minOf(zBound.max, other.zBound.max))
    )
}

/**
 * Given a sub-area (other), returns a list of area that represents the current Area *Without*
 * the given sub-area.
 * Thanks to this guy for helping me visualize: https://www.youtube.com/watch?v=OatnI5y6KQw&t=856
 */
fun Area.sub(other: Area): List<Area> {
    // left & right
    val a = Area(Bound(xBound.min, other.xBound.min - 1), yBound, zBound)
    val b = Area(Bound(other.xBound.max + 1, xBound.max), yBound, zBound)

    // top & bottom
    val c = Area(other.xBound, Bound(other.yBound.max + 1, yBound.max), zBound)
    val d = Area(other.xBound, Bound(yBound.min, other.yBound.min - 1), zBound)

    // depth
    val e = Area(other.xBound, other.yBound, Bound(zBound.min, other.zBound.min - 1))
    val f = Area(other.xBound, other.yBound, Bound(other.zBound.max + 1, zBound.max))

    return listOf(a, b, c, d, e, f).filter { !it.isEmpty() } // return areas that have a non empty volume
}

fun part2(lines: List<String>): Long {
    val instructions = lines.toInstructions()
    val areas = mutableListOf<Area>()

    for (ins in instructions) {
        val newArea = ins.toArea()

        val splitedAreas = mutableListOf<Area>()
        if (ins.type == InstructionType.ON) splitedAreas.add(newArea)

        for (a in areas) {
            val inter = a.intersection(newArea)

            // we cut the bigger cube into smaller remaining pieces
            // we remove the original area & add the smaller remaining pieces
            if (!inter.isEmpty()) {
                splitedAreas.addAll(a.sub(inter))
            } else {
                splitedAreas.add(a)
            }
        }
        areas.clear()
        areas.addAll(splitedAreas)
    }
    return areas.sumOf { it.size() }
}

fun main() {
    val simple = readInput("day22/simple")
    println("part1(simple) => " + part1(simple))
    println("part2(simple) => " + part2(simple))

    val larger = readInput("day22/larger")
    println("part1(larger) => " + part1(larger))
    println("part2(larger) => " + part2(larger))

    val input = readInput("day22/input")
    println("part1(input) => " + part1(input))
    println("part2(input) => " + part2(input))
}
