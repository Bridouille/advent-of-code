package day18

import readInput
import java.util.*

fun List<String>.toListOfTree() : List<Node> = this.map { it.toTree()!! }
fun String.toTree(parent: Node? = null) : Node? {
    if (all { it.isDigit() }) return Node(parent = parent, value = toInt())

    var delimitersCount = 0
    val subStr = this.drop(1).dropLast(1) // Removes the outermost '[' and ']'

    for (idx in subStr.indices) {
        if (subStr[idx] == '[') delimitersCount++
        if (subStr[idx] == ']') delimitersCount--

        if (subStr[idx] == ',' && delimitersCount == 0) {
            return Node(parent = parent).apply {
                left = subStr.take(idx).toTree(this)
                right = subStr.drop(idx + 1).toTree(this)
            }
        }
    }
    return null
}

data class Node(
        val id: String = UUID.randomUUID().toString(),
        var left: Node? = null,
        var right: Node? = null,
        var parent: Node? = null,
        var value: Int? = null
) {
    override fun toString() : String {
        if (left == null && right == null) return "$value"

        return "[${left},${right}]"
    }
    override fun equals(other: Any?) = other is Node && id == other.id

    fun add(toAdd: Node): Node {
        val newRoot = Node(right = toAdd, left = this)

        parent = newRoot
        toAdd.parent = newRoot
        return newRoot
    }

    // leaf => left == null && right == null
    // return a list of leafs with their depth
    private fun getLeafs(depth: Int = 0) : List<Pair<Int, Node>> {
        if (left?.value != null && right?.value != null) {
            return listOf(Pair(depth, this))
        }
        val leftLeaf = left?.getLeafs(depth + 1) ?: emptyList()
        val rightLeaf = right?.getLeafs(depth + 1) ?: emptyList()

        return (leftLeaf + rightLeaf).sortedByDescending { it.first }
    }

    private fun getFirstParentRight() : Node? {
        var child: Node? = this
        var root: Node? = this.parent

        while (root?.parent != null && (root.right == null || root.right == child)) {
            child = root
            root = root.parent
        }
        if (root?.parent == null && root?.right == child) {
            return null
        }
        return root?.right?.getLeftmostNode()
    }

    private fun getFirstParentLeft() : Node? {
        var child: Node? = this
        var root: Node? = this.parent

        while (root?.parent != null && (root.left == null || root.left == child)) {
            child = root
            root = root.parent
        }
        if (root?.parent == null && root?.left == child) {
            return null
        }
        return root?.left?.getRightmostNode()
    }

    private fun getRightmostNode(): Node = right?.let { it.getRightmostNode() } ?: this
    private fun getLeftmostNode(): Node = left?.let { it.getLeftmostNode() } ?: this

    // explode the tree, return true if an explode happened
    private fun explodeAll() : Boolean {
        val deepLeafs = getLeafs().filter { it.first >= 4 } // only get the leafs that are deeper than 4
        if (deepLeafs.isEmpty()) return false

        for (deep in deepLeafs) {
            val leaf = deep.second
            val leftValue = (leaf.left?.value ?: 0)
            val rightValue = (leaf.right?.value ?: 0)
            val parentLeft = leaf.getFirstParentLeft()
            val parentRight = leaf.getFirstParentRight()

            if (parentLeft != null) {
                parentLeft.value = (parentLeft.value ?: 0) + leftValue
            }
            if (parentRight != null) {
                parentRight.value = (parentRight.value ?: 0) + rightValue
            }

            leaf.value = 0
            leaf.left = null
            leaf.right = null
        }
        return true
    }

    // split the tree, left-most node first, return true if a split has happened
    private fun split() : Boolean {
        value?.let {
            if (it >= 10) {
                val leftValue = it / 2
                val rightValue = it / 2 + it % 2

                this.value = null
                this.left = Node(value = leftValue, parent = this)
                this.right = Node(value = rightValue, parent = this)
                return true
            }
        }
        val leftSplit = left?.let { it.split() } ?: false
        // one action at a time, return if we already did a split on the left side
        val rightSplit = if (!leftSplit) {
            right?.let { it.split() } ?: false
        } else false
        return leftSplit || rightSplit
    }

    fun reduce() {
        do {
            explodeAll()
        } while (split())
    }

    fun getMagnitude(): Long {
        if (left == null && right == null) {
            return value?.toLong()!!
        }
        return (left?.getMagnitude() ?: 0) * 3 + (right?.getMagnitude() ?: 0) * 2
    }
}

fun part1(lines: List<String>): Long {
    val trees = lines.toListOfTree()
    var firstTree = trees.first()

    for (tree in trees.drop(1)) {
        firstTree = firstTree.add(tree)
        firstTree.reduce()
    }
    println(firstTree)
    return firstTree.getMagnitude()
}

fun part2(lines: List<String>): Long {
    val trees = lines.toListOfTree().toMutableList()
    var maxMagnitude = 0L

    for (i in 0 until trees.size) {
        for (j in i+1 until trees.size) {
            if (i == j) continue // Do not add with yourself

            val newTree = trees[i].add(trees[j])
            newTree.reduce()
            maxMagnitude = maxOf(newTree.getMagnitude(), maxMagnitude)
            trees[i] = lines.toListOfTree()[i]
            trees[j] = lines.toListOfTree()[j]
        }
    }
    return maxMagnitude
}

fun main() {
    val testInput = readInput("day18/test")
    println("part1(testInput) => " + part1(testInput))
    println("part2(testInput) => " + part2(testInput))

    val input = readInput("day18/input")
    println("part1(input) => " + part1(input))
    println("part2(input) => " + part2(input))
}
