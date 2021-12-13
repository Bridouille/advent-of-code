package day12

import readInput

fun buildGraph(lines: List<String>) : Map<String, MutableSet<String>> {
    val points = mutableMapOf<String, MutableSet<String>>()

    for (line in lines) {
        val connexion = line.split("-")
        val from = connexion[0]
        val to = connexion[1]

        if (points.contains(from)) {
            points[from]?.add(to)
        } else {
            points.put(from, mutableSetOf(to))
        }

        if (points.contains(to)) {
            points[to]?.add(from)
        } else {
            points.put(to, mutableSetOf(from))
        }
    }
    return points
}

fun String.isAllLowerCase() = !this.any { it.isUpperCase() }

fun visitGraph(
        start: String,
        graph: Map<String, MutableSet<String>>,
        visited: Map<String, Int>, // mapOf("cave" -> numberOfVisits)
        canVisitOneSmallCaveTwice: Boolean,
        cache: MutableMap<String, Int> = mutableMapOf() // mapOf("node+visited" => numberOfPathsTillEnd)
) : Int {
    val toVisit = mutableListOf<String>()
    var nbPath = 0

    for (node in graph[start]!!) {
        when (node) {
            "end" -> nbPath++
            "start" -> { } // Do no re-visit start
            else -> {
                if (visited.getOrDefault(node, 0) < 1) {
                    toVisit.add(node)
                } else if (canVisitOneSmallCaveTwice && !visited.values.contains(2)) {
                    toVisit.add(node)
                }
            }
        }
    }

    while (!toVisit.isEmpty()) {
        val item = toVisit.removeAt(toVisit.lastIndex)

        val nextVisited = visited.toMutableMap()
        if (item.isAllLowerCase()) { // small cave
            nextVisited[item] = visited.getOrDefault(item, 0) + 1 // add one visit to that cave
        }

        val cacheKey = item + nextVisited.toString()
        if (!cache.contains(cacheKey)) {
            cache[cacheKey] = visitGraph(item, graph, nextVisited, canVisitOneSmallCaveTwice, cache)
        }
        nbPath += cache[cacheKey]!!
    }
    return nbPath
}

fun part1(lines: List<String>) : Int {
    val graph = buildGraph(lines)
    println(graph)
    return visitGraph("start", graph, mapOf(), false)
}

fun part2(lines: List<String>) : Int {
    val graph = buildGraph(lines)
    return visitGraph("start", graph, mapOf(), true)
}

fun main() {
    val simpleInput = readInput("day12/simple")
    println("part1(simpleInput) => " + part1(simpleInput))
    println("part2(simpleInput) => " + part2(simpleInput))

    val larger = readInput("day12/larger")
    println("part1(larger) => " + part1(larger))
    println("part2(larger) => " + part2(larger))

    val evenLarger = readInput("day12/even_larger")
    println("part1(evenLarger) => " + part1(evenLarger))
    println("part2(evenLarger) => " + part2(evenLarger))

    val input = readInput("day12/input")
    println("part1(input) => " + part1(input))
    println("part1(input) => " + part2(input))
}
