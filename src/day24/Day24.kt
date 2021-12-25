package day24

import readInput
import java.lang.IllegalStateException
import java.text.SimpleDateFormat
import java.util.*

val POSSIBLE_DIGIT = listOf(9L, 8L, 7L, 6L, 5L, 4L, 3L, 2L, 1L)

fun getVarFromStr(str: String, w: Long, x: Long, y: Long, z: Long) : Long {
    return when (str) {
        "z" -> z
        "w" -> w
        "x" -> x
        "y" -> y
        else -> str.toLong()
    }
}

fun add(left: String, right: String, w: Long, x: Long, y: Long, z: Long) : Long {
    return getVarFromStr(left, w, x, y, z) + getVarFromStr(right, w, x, y, z)
}

fun mul(left: String, right: String, w: Long, x: Long, y: Long, z: Long) : Long {
    return getVarFromStr(left, w, x, y, z) * getVarFromStr(right, w, x, y, z)
}

fun div(left: String, right: String, w: Long, x: Long, y: Long, z: Long) : Long? {
    val a = getVarFromStr(left, w, x, y, z)
    val b = getVarFromStr(right, w, x, y, z)
    if (b == 0L) return null
    return a / b
}

fun mod(left: String, right: String, w: Long, x: Long, y: Long, z: Long) : Long? {
    val a = getVarFromStr(left, w, x, y, z)
    val b = getVarFromStr(right, w, x, y, z)
    if (b == 0L) return null
    return a % b
}

fun eql(left: String, right: String, w: Long, x: Long, y: Long, z: Long) : Long {
    return if (getVarFromStr(left, w, x, y, z) == getVarFromStr(right, w, x, y, z)) 1 else 0
}

val memo = mutableMapOf<String, String?>()
fun runInstructions(
        idx: Int = 0,
        startW: Long = 0, startX: Long = 0,
        startY: Long = 0, startZ: Long = 0,
        inp: String = "",
        reversed: Boolean = false
): String? {
    if (inp.length >= 8 && startZ > 100000) return null
    if (inp.length >= 6 && startZ > 1000000) return null
    if (reversed && inp.length > 8 && startZ > 4000) return null

    var (w, x, y, z) = listOf(startW, startX, startY, startZ)
    val key = "$inp/$z"
    if (memo.containsKey(key)) return memo[key]

    for (i in idx until toExec.size) {
        val splits = toExec[i].split(" ")
        val ope = splits[0]
        val left = splits[1]
        val right = splits.getOrNull(2)

        when (ope) {
            "inp" -> {
                val possibleDigits = if (!reversed) POSSIBLE_DIGIT else POSSIBLE_DIGIT.reversed()
                for (d in possibleDigits) {
                    val (newW ,newX, newY, newZ) = when (left) {
                        "w" -> listOf(d, x, y, z)
                        "x" -> listOf(w, d, y, z)
                        "y" -> listOf(w, x, d, z)
                        "z" -> listOf(w, x, y, d)
                        else -> throw IllegalStateException("inp with $left")
                    }
                    val res = runInstructions(i + 1, newW, newX, newY, newZ, inp+d, reversed)
                    if (res != null) {
                        memo[key] = "$d$res"
                        return memo[key]
                    } else {
                        memo[key] = null
                    }
                }
            }
            "add" -> {
                when (left) {
                    "w" -> w = add(left, right!!, w, x, y, z)
                    "x" -> x = add(left, right!!, w, x, y, z)
                    "y" -> y = add(left, right!!, w, x, y, z)
                    "z" -> z = add(left, right!!, w, x, y, z)
                }
            }
            "mul" -> {
                when (left) {
                    "w" -> w = mul(left, right!!, w, x, y, z)
                    "x" -> x = mul(left, right!!, w, x, y, z)
                    "y" -> y = mul(left, right!!, w, x, y, z)
                    "z" -> z = mul(left, right!!, w, x, y, z)
                }
            }
            "div" -> {
                when (left) {
                    "w" -> w = div(left, right!!, w, x, y, z) ?: return null
                    "x" -> x = div(left, right!!, w, x, y, z) ?: return null
                    "y" -> y = div(left, right!!, w, x, y, z) ?: return null
                    "z" -> z = div(left, right!!, w, x, y, z) ?: return null
                }
            }
            "mod" -> {
                when (left) {
                    "w" -> w = mod(left, right!!, w, x, y, z) ?: return null
                    "x" -> x = mod(left, right!!, w, x, y, z) ?: return null
                    "y" -> y = mod(left, right!!, w, x, y, z) ?: return null
                    "z" -> z = mod(left, right!!, w, x, y, z) ?: return null
                }
            }
            "eql" -> {
                when (left) {
                    "w" -> w = eql(left, right!!, w, x, y, z)
                    "x" -> x = eql(left, right!!, w, x, y, z)
                    "y" -> y = eql(left, right!!, w, x, y, z)
                    "z" -> z = eql(left, right!!, w, x, y, z)
                }
            }
        }
    }
    print("\\33[2K\r")
    print("startZ: $startZ inp:$inp - z:$z")
    memo[key] = if (z == 0L) "" else null
    return memo[key]
}

fun <T>measureTime(block: () -> T): T {
    val start = System.currentTimeMillis()
    return block().also {
        println("Solving took ${SimpleDateFormat("mm:ss:SSS").format(Date(System.currentTimeMillis() - start))}")
    }
}

var toExec: List<String> = emptyList()
fun part1(lines: List<String>) = measureTime {
    toExec = lines
    memo.clear()
    runInstructions()?.toLong()
}

fun part2(lines: List<String>) = measureTime {
    toExec = lines
    memo.clear()
    runInstructions(reversed = true)?.toLong()
}

fun main() {
    val input = readInput("day24/input")
    println("part1(input) => " + part1(input)) // 99394899891971
    println("part2(input) => " + part2(input))
}
