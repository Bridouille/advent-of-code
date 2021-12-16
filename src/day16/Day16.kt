package day16

import readInput
import java.lang.IllegalStateException

sealed class Packet {
    data class LiteralValue(val v: Int, val number: Long) : Packet() {
        override fun getVersionSum() = v
        override fun getTotal() = number
    }
    data class Operator(val v: Int, val typeId: Int, val subPackets: List<Packet>) : Packet() {
        override fun getVersionSum() = v + subPackets.map { it.getVersionSum() }.sum()
        override fun getTotal() = when (typeId) {
            0 -> subPackets.sumOf { it.getTotal() }
            1 -> subPackets.fold(1L) { acc, p -> acc * p.getTotal() }
            2 -> subPackets.minOfOrNull { it.getTotal() }!!
            3 -> subPackets.maxOfOrNull { it.getTotal() }!!
            5 -> if (subPackets[0].getTotal() > subPackets[1].getTotal()) 1L else 0L
            6 -> if (subPackets[0].getTotal() < subPackets[1].getTotal()) 1L else 0L
            7 -> if (subPackets[0].getTotal() == subPackets[1].getTotal()) 1L else 0L
            else -> throw IllegalStateException("Unknown typeId($typeId)")
        }
    }

    abstract fun getVersionSum() : Int
    abstract fun getTotal() : Long
}

const val LITERAL_VALUE_ID = 4

fun String.hexToBinary(): String {
    val sb = StringBuilder()

    for (c in this) {
        val digit = String.format("%4s", Integer.toBinaryString(c.digitToInt(16))).replace(" ", "0")
        sb.append(digit)
    }
    return sb.toString()
}

fun parseLiteralValue(version: Int, binary: String) : Pair<Packet.LiteralValue, String> {
    val binaryNb = StringBuilder()
    var lastIdx = 0

    for (idx in binary.indices step 5) {
        binaryNb.append(binary.drop(idx + 1).take(4))
        if (binary[idx] != '1') {
            lastIdx = idx
            break
        }
    }
    return Pair(Packet.LiteralValue(version, binaryNb.toString().toLong(2)), binary.drop(lastIdx + 5))
}

fun parseOperatorPacket(version: Int, typeId: Int, binary: String) : Pair<Packet.Operator, String> {
    val subPackets = mutableListOf<Packet>()
    var rest = ""

    when (binary.first()) {
        '1' -> {
            val numberOfSubPackets = binary.drop(1).take(11).toInt(2)
            var res = parsePacket(binary.drop(1 + 11))
            subPackets.add(res.first)

            for (i in 1 until numberOfSubPackets) {
                res = parsePacket(res.second)
                subPackets.add(res.first)
            }
            rest = res.second
        }
        '0' -> {
            val totalLength = binary.drop(1).take(15).toInt(2)
            var res = parsePacket(binary.drop(1 + 15).take(totalLength))
            subPackets.add(res.first)

            while (res.second.isNotEmpty()) {
                res = parsePacket(res.second)
                subPackets.add(res.first)
            }
            rest = binary.drop(1 + 15 + totalLength)
        }
    }
    return Pair(Packet.Operator(version, typeId, subPackets), rest)
}

fun parsePacket(binary: String) : Pair<Packet, String> {
    val version = binary.take(3)
    val typeID = binary.drop(3).take(3)

    return when (val typeIDInt = typeID.toInt(2)) {
        LITERAL_VALUE_ID -> parseLiteralValue(version.toInt(2), binary.drop(6))
        else -> parseOperatorPacket(version.toInt(2), typeIDInt, binary.drop(6))
    }
}

fun part1(lines: List<String>): Int {
    val binary = lines.first().hexToBinary()

    println(binary)
    return parsePacket(binary).first.let {
        it.also { println(it) }
    }.getVersionSum()
}

fun part2(lines: List<String>) = parsePacket(lines.first().hexToBinary()).first.getTotal()

fun main() {
    val testInput = readInput("day16/test")
    println("part1(testInput) => " + part1(testInput))
    println("part2(testInput) => " + part2(testInput))

    val input = readInput("day16/input")
    println("part1(input) => " + part1(input))
    println("part2(input) => " + part2(input))
}
