package day16

import readInput

sealed class Packet(val version: Int) {
    data class LiteralValue(val v: Int, val number: Long) : Packet(v) {
        override fun getVersionSum() = v
    }
    data class Operator(val v: Int, val typeId: Int, val subPackets: List<Packet>) : Packet(v) {
        override fun getVersionSum() = v + subPackets.map { it.getVersionSum() }.sum()
    }

    abstract fun getVersionSum() : Int
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
    println("parseLiteralValue($version)")

    var beginIdx = 0
    var nb = ""
    while (beginIdx < binary.length && binary[beginIdx] == '1') {
        nb += binary.drop(beginIdx + 1).take(4)
        beginIdx += 5
    }
    nb += binary.drop(beginIdx + 1).take(4)
    println("number: $nb (decimal: ${nb.toLong(2)})")
    return Pair(Packet.LiteralValue(version, nb.toLong(2)), binary.drop(beginIdx + 5))
}

fun parseOperatorPacket(version: Int, typeId: Int, binary: String) : Pair<Packet.Operator, String> {
    println("parseOperatorPacket($version)")
    val subPackets = mutableListOf<Packet>()
    var rest = ""

    when (binary.first()) {
        '1' -> {
            val numberOfSubPackets = binary.drop(1).take(11).toInt(2)
            println("numberOfSubPackets=$numberOfSubPackets")
            var res = parsePacket(binary.drop(12))
            subPackets.add(res.first)
            for (i in 1 until numberOfSubPackets) {
                res = parsePacket(res.second)
                subPackets.add(res.first)
            }
            rest = res.second
        }
        '0' -> {
            val totalLength = binary.drop(1).take(15).toInt(2)
            println("totalLength=$totalLength")
            var res = parsePacket(binary.drop(16).take(totalLength))
            subPackets.add(res.first)
            while (res.second.isNotEmpty()) {
                res = parsePacket(res.second)
                subPackets.add(res.first)
            }
            rest = binary.drop(16 + totalLength)
        }
    }
    return Pair(Packet.Operator(version, typeId, subPackets), rest)
}

fun parsePacket(binary: String) : Pair<Packet, String> {
    println("parsePacket($binary)")
    val version = binary.take(3)
    val typeID = binary.drop(3).take(3)

    println("version: $version, typeID: ${typeID.toInt(2)}")
    return when (val typeIDInt = typeID.toInt(2)) {
        LITERAL_VALUE_ID -> parseLiteralValue(version.toInt(2), binary.drop(6))
        else -> parseOperatorPacket(version.toInt(2), typeIDInt, binary.drop(6))
    }
}

fun part1(lines: List<String>): Int {
    val binary = lines.first().hexToBinary()

    println(binary)
    val res = parsePacket(binary)
    println(res.first)
    return res.first.getVersionSum()
}

fun part2(lines: List<String>): Int {
    return 2
}

fun main() {
    val testInput = readInput("day16/input")
    println("part1(testInput) => " + part1(testInput))
    println("part2(testInput) => " + part2(testInput))

    // val input = readInput("day15/input")
    // println("part1(input) => " + part1(input))
    // println("part2(input) => " + part2(input))
}
