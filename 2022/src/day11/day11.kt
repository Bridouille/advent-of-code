package day11

import GREEN
import RESET
import printTimeMillis
import readInput

data class Monkey(
    val items: MutableList<Long>,
    val operation: (Long) -> Long,
    val test: (Long) -> Boolean,
    val trueMonkey: Int,
    val falseMonkey: Int,
    val divisor: Long,
    var inspect: Int = 0
) {
    fun add(new: Long) { items.add(new) }
    fun inspect() { inspect += 1 }
}

fun parseMonkeys(input: List<String>) = input.windowed(6, 6) {
    val startingItems = it[1].split(":")[1].trim().split(", ").map { it.toLong() }.toMutableList()
    val operation = it[2].split("=")[1].trim().let {
        when {
            it == "old * old" -> {
                { o: Long -> o * o }
            }
            it.startsWith("old *") -> {
                { o: Long -> o * it.split(" ").last().toLong() }
            }
            it.startsWith("old +") -> {
                { o: Long -> o + it.split(" ").last().toLong() }
            }
            else -> throw IllegalStateException("Wrong Operation")
        }
    }
    val divisor = it[3].split(" ").last().toLong()
    val test = { e: Long -> e.mod(divisor) == 0L }
    Monkey(
        startingItems,
        operation,
        test,
        trueMonkey = it[4].split(" ").last().toInt(),
        falseMonkey = it[5].split(" ").last().toInt(),
        divisor = divisor
    )
}

fun playRounds(
    monkeys: List<Monkey>,
    rounds: Int = 1,
    divideWorry: Boolean = true
) {
    val reduce = monkeys.fold(1L) { acc, v -> acc * v.divisor }
    repeat(rounds) {
        monkeys.forEach {
            while (it.items.isNotEmpty()) {
                val item = it.items.removeFirst()
                val examine = it.operation(item).let {
                    if (divideWorry) it / 3L else it
                }.let {
                    // get the modulo of all divisors multiplied with each others
                    it % reduce
                }
                it.inspect()
                if (it.test(examine)) {
                    monkeys[it.trueMonkey].add(examine)
                } else {
                    monkeys[it.falseMonkey].add(examine)
                }
            }
        }
    }
}

fun part1(input: List<String>): Long {
    val monkeys = parseMonkeys(input)
    playRounds(monkeys, 20)
    return monkeys.map { it.inspect.toLong() }.sortedDescending().let { it[0] * it[1] }
}

fun part2(input: List<String>): Long {
    val monkeys = parseMonkeys(input)
    playRounds(monkeys, 10000, false)
    return monkeys.map { it.inspect.toLong() }.sortedDescending().let { it[0] * it[1] }
}

fun main() {
    val testInput = readInput("day11_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput) + RESET) }

    val input = readInput("day11.txt")
    printTimeMillis { print("part1 input = $GREEN" + part1(input) + RESET) }
    printTimeMillis { print("part2 input = $GREEN" + part2(input) + RESET) }
}
