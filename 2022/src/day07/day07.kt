package day07

import GREEN
import RESET
import printTimeMillis
import readInput

data class File(
    val name: String,
    val isDir: Boolean = false,
    val size: Long = 0
)

fun List<String>.toPath() = "/" + drop(1).joinToString("/")

// construct a map of "/some/file" -> List<File>
fun parseFileSystem(input: List<String>): MutableMap<String, MutableList<File>> {
    val fs = mutableMapOf<String, MutableList<File>>()
    val nav = mutableListOf<String>()

    input.forEach {
        if (it.startsWith("$")) {
            if (it.startsWith("$ cd ")) {
                when(val nextDir = it.split(" ").last()) {
                    ".." -> nav.removeLast()
                    else -> nav.add(nextDir)
                }
            }
        } else { // result of "ls"
            val currPath = nav.toPath()
            if (!fs.containsKey(currPath)) {
                fs[currPath] = mutableListOf()
            }
            if (it.startsWith("dir")) {
                fs[currPath]?.add(File(it.split(" ")[1], isDir = true))
            } else {
                fs[currPath]?.add(it.split(" ").let { File(it.last(), size = it.first().toLong()) })
            }
        }
    }
    return fs
}

// fills the "sums" with their sizes by traversing the graph
// sums: map of "/some/dir/on/the/filesystem" -> sumOfContained
fun traverseAndCount(
    fs: Map<String, MutableList<File>>,
    sums: MutableMap<String, Long>,
    nav: List<String>
): Long {
    val files = fs[nav.toPath()] ?: return 0
    val sum = files.map {
        if (it.isDir) {
            traverseAndCount(fs, sums, nav + it.name)
        } else {
            it.size
        }
    }.sum()
    sums[nav.toPath()] = sum
    return sum
}

fun part1(input: List<String>): Long {
    val fs = parseFileSystem(input)
    val sums = mutableMapOf<String, Long>()
    traverseAndCount(fs, sums, listOf("/"))

    return sums.filter { it.value <= 100000 }.map { it.value }.sum()
}

fun part2(input: List<String>): Long {
    val fs = parseFileSystem(input)
    val sums = mutableMapOf<String, Long>()
    traverseAndCount(fs, sums, listOf("/"))

    val neededSpace = 30000000 - (70000000 - sums["/"]!!)
    return sums.filter {
        it.value >= neededSpace
    }.map { it.value }.sorted().first()
}

fun main() {
    val testInput = readInput("day07_example.txt")
    printTimeMillis { print("part1 example = $GREEN" + part1(testInput) + RESET) }
    printTimeMillis { print("part2 example = $GREEN" + part2(testInput) + RESET) }

    val input = readInput("day07.txt")
    printTimeMillis { print("part1 input = $GREEN" + part1(input) + RESET) }
    printTimeMillis { print("part2 input = $GREEN" + part2(input) + RESET) }
}
