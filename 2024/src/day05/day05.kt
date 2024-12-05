package day05

import GREEN
import RESET
import printTimeMillis
import readInput

fun buildOrders(input: List<String>): MutableMap<Int, MutableList<Int>> {
    val orders = mutableMapOf<Int, MutableList<Int>>()

    for (line in input.filter { it.contains("|") }) {
        val pages = line.split("|").map { it.toInt() }

        if (orders.containsKey(pages[0])) {
            orders[pages[0]]!!.add(pages[1])
        } else {
            orders[pages[0]] = mutableListOf(pages[1])
        }
    }
    return orders
}

fun part1(input: List<String>): Int {
    val orders = buildOrders(input)
    val valids = mutableListOf<List<Int>>()

    for (line in input.filter { it.contains(",") }) {
        val pages = line.split(",").map { it.toInt() }

        for (i in 0 until pages.size - 1) {
            val lookup = pages[i]
            val rest = pages.subList(i + 1, pages.size)

            if (!(orders.containsKey(lookup) && orders[lookup]!!.containsAll(rest))) break
            if (rest.size == 1) valids.add(pages)
        }
    }
    return valids.map { it[it.size / 2] }.sum()
}

fun part2(input: List<String>): Int {
    val orders = buildOrders(input)

    val invalids = mutableListOf<List<Int>>()
    for (line in input.filter { it.contains(",") }) {
        val pages = line.split(",").map { it.toInt() }

        for (i in 0 until pages.size - 1) {
            val lookup = pages[i]
            val rest = pages.subList(i + 1, pages.size)

            if (!(orders.containsKey(lookup) && orders[lookup]!!.containsAll(rest))) {
                invalids.add(pages)
                break
            }
        }
    }

    var total = 0
    for (invalid in invalids) {
        val sorted = invalid.sortedByDescending { page ->
            val listWithoutNb = invalid.filter { it != page }

            orders[page]?.filter { listWithoutNb.contains(it) }?.size ?: -1
        }
        total += sorted[sorted.size / 2]
    }
    return total
}

fun main() {
    val testInput = readInput("day05_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput) + RESET) }

    val input = readInput("day05.txt")
    printTimeMillis { print("part1 input = $GREEN" + part1(input) + RESET) }
    printTimeMillis { print("part2 input = $GREEN" + part2(input) + RESET) }
}
