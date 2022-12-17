package day16

import GREEN
import RESET
import printTimeMillis
import readInput

data class Valve(
    val name: String,
    val flowRate: Int,
    val links: List<String> = mutableListOf(),
    var isOpened: Boolean = false,
) {
    fun shouldOpen() = flowRate > 0 && !isOpened
    fun open() { isOpened = true }
    fun close() { isOpened = false }
}

fun parseInput(input: List<String>): Map<String, Valve> {
    val ret = mutableMapOf<String, Valve>()

    input.forEach {
        val links = it.split("; ")[1].split(" ").drop(4).map { it.replace(",", "").trim() }
        val valve = it.split("; ")[0].split(" ").let {
            Valve(it[1], it[4].substringAfter('=').toInt(), links)
        }
        ret[valve.name] = valve
    }
    return ret
}

// returns a map of distances between valves {"AA-BB": 1, "DD-HH": 4, "II-CC": 3}
fun fillDistances(graph: Map<String, Valve>): Map<String, Int> {
    val ret = mutableMapOf<String, Int>()

    val keys = graph.keys.sorted()
    for (i in 0 until keys.lastIndex) {
        val valve = graph[keys[i]]!!

        for (j in i+1..keys.lastIndex) {
            val start = valve.name
            val end = graph[keys[j]]!!.name

            val visited = mutableSetOf<String>()
            val toVisit = mutableListOf(Pair(start, 0))
            while (toVisit.isNotEmpty()) {
                val curr = toVisit.removeFirst()
                visited.add(curr.first)

                if (curr.first == end) {
                    ret["$start-$end"] = curr.second
                    ret["$end-$start"] = curr.second
                    break
                }
                val nextOnes = graph[curr.first]!!.links.filter { !visited.contains(it) }
                toVisit.addAll(nextOnes.map { Pair(it, curr.second + 1) })
            }
        }
    }
    return ret
}

fun getMaxPressure(
    graph: Map<String, Valve>,
    distances: Map<String, Int>,
    currentPos: String,
    mnLeft: Int,
    currFlow: Int,
    total: Int,
    debugPath: String
): Pair<Int, String>? {
    if (mnLeft < 0) return null
    if (mnLeft == 0) return Pair(total, debugPath)

    val leftToOpen = graph.values.filter { it.shouldOpen() }
    if (leftToOpen.isEmpty()) return Pair(total + currFlow * mnLeft, debugPath)

    val totals = mutableListOf<Pair<Int, String>>()
    for (left in leftToOpen) {
        val dist = distances["${left.name}-$currentPos"]!!
        val timeToOpenValve = dist + 1

        left.open()
        val tot = getMaxPressure(graph, distances,
            left.name,
            mnLeft - timeToOpenValve,
            currFlow + left.flowRate,
            total + (currFlow * timeToOpenValve),
            "$debugPath -> ${left.name}"
        )
        if (tot != null) totals.add(tot)
        left.close()
    }
    totals.add(Pair(total + currFlow * mnLeft, debugPath)) // The total if we sit here doing nothing
    return totals.maxByOrNull { it.first }
}

fun part1(input: List<String>): Int? {
    val graph = parseInput(input)
    val distances = fillDistances(graph)

    return getMaxPressure(graph, distances, "AA",30, 0, 0, "AA")?.first
}

// https://www.reddit.com/r/adventofcode/comments/znjzjm/2022_day_16_if_a_solution_gives_me_a_star_then/
fun part2(input: List<String>): Int {
    val graph = parseInput(input)
    val distances = fillDistances(graph)

    val me = getMaxPressure(graph, distances, "AA", 26, 0, 0, "AA")!!
    for (name in me.second.split(" -> ")) {
        graph[name]!!.open()
    }
    val elephant = getMaxPressure(graph, distances, "AA", 26, 0, 0, "AA")!!
    return me.first + elephant.first
}

fun main() {
    val testInput = readInput("day16_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput) + RESET) } // 1707

    val input = readInput("day16.txt")
    printTimeMillis { print("part1 input = $GREEN" + part1(input) + RESET) }
    printTimeMillis { print("part2 input = $GREEN" + part2(input) + RESET) }
}
