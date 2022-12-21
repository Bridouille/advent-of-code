package day21

import GREEN
import RESET
import printTimeMillis
import readInput

sealed class Job {
    data class Number(val nb: Long): Job()
    data class Operation(val left: String, val right: String, val operation: Ope): Job()
}

interface Ope {
    fun doOp(a: Long, b: Long): Long
    fun fromResult(result: Long, a: Long?, b: Long?): Long // either a or b is null, but not both
}

class Mul : Ope {
    override fun doOp(a: Long, b: Long) = a * b
    // 15 * X = 150 -> X = 150/15
    // X * 15 = 150 -> X = 150/15
    override fun fromResult(result: Long, a: Long?, b: Long?) = result / (a ?: b!!)
}

class Plus : Ope {
    override fun doOp(a: Long, b: Long) = a + b
    // 15 + X = 150 -> X = 150 - 15
    // X + -15 = 150 -> X = 150 - -15
    override fun fromResult(result: Long, a: Long?, b: Long?) = result - (a ?: b!!)
}

class Minus : Ope {
    override fun doOp(a: Long, b: Long) = a - b
    // 15 - X = 150 -> X = -150 + 15 = -135 (-result + b)
    // 15 - X = -40 -> X = 40 + 15 = 55 (-result + b)

    // X - 15 = 150 -> X = 150 + 15 = 165 (result + b)
    // X - 15 = -40 -> X = -40 + 15 = -25 (result + b)
    override fun fromResult(result: Long, a: Long?, b: Long?): Long {
        return if (a != null) -result + a else result + b!!
    }
}

class Div : Ope {
    override fun doOp(a: Long, b: Long) = a / b
    // 150 / X = 15 -> X = 150 / 15 = 10
    // X / 15 = 10 -> X = 10 * 15 = 150
    override fun fromResult(result: Long, a: Long?, b: Long?): Long {
        return if (a != null) a / result else result * b!!
    }
}

val OPE = mapOf("*" to Mul(), "+" to Plus(), "-" to Minus(), "/" to Div())
const val END_NODE = "humn"
const val ROOT_NODE = "root"

fun monkeyMap(input: List<String>): MutableMap<String, Job> {
    val ret = mutableMapOf<String, Job>()

    for (l in input) {
        val name = l.split(": ")[0]
        val inst = l.split(": ")[1]
        val job = if (inst.toIntOrNull() != null) {
            Job.Number(inst.toLong())
        } else {
            val ope = inst.split(" ")
            Job.Operation(ope[0], ope[2], OPE[ope[1]]!!)
        }
        ret[name] = job
    }
    return ret
}

fun resolve(monkeys: MutableMap<String, Job>, start: String): Long {
    if (monkeys[start] is Job.Number) return (monkeys[start] as Job.Number).nb
    val ope = monkeys[start] as Job.Operation

    val result = ope.operation.doOp(resolve(monkeys, ope.left), resolve(monkeys, ope.right))
    monkeys[start] = Job.Number(result)
    return result
}

fun part1(input: List<String>) = resolve(monkeyMap(input), ROOT_NODE)

fun hasEndNode(monkeys: MutableMap<String, Job>, start: String): Boolean {
    if (start == END_NODE) return true
    if (monkeys[start] is Job.Number) return false
    val ope = monkeys[start] as Job.Operation
    return hasEndNode(monkeys, ope.left) || hasEndNode(monkeys, ope.right)
}

fun resolveEquation(monkeys: MutableMap<String, Job>, start: String, wanted: Long): Long {
    if (start == END_NODE) return wanted

    val ope = monkeys[start] as Job.Operation
    val unknownIsLeft = hasEndNode(monkeys, ope.left)
    val knownValue = resolve(monkeys, if (unknownIsLeft) ope.right else ope.left)

    // Compute the new number we're looking for from the result + one operand
    val newWanted = if (start == ROOT_NODE) {
        knownValue
    } else {
        ope.operation.fromResult(
            wanted,
            if (unknownIsLeft) null else knownValue,
            if (unknownIsLeft) knownValue else null
        )
    }
    return resolveEquation(monkeys, if (unknownIsLeft) ope.left else ope.right, newWanted)
}

fun part2(input: List<String>) = resolveEquation(monkeyMap(input), ROOT_NODE, -1)

fun main() {
    val testInput = readInput("day21_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput) + RESET) }

    val input = readInput("day21.txt")
    printTimeMillis { print("part1 input = $GREEN" + part1(input) + RESET) }
    printTimeMillis { print("part2 input = $GREEN" + part2(input) + RESET) }
}
