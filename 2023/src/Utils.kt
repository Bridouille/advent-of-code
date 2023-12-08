import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

const val GREEN = "\u001B[32m"
const val BLUE = "\u001B[34m"
const val RESET = "\u001B[0m"

/**
 * Reads lines from the given input txt file.
 */
fun readInput(
    name: String,
    removeEmptyLines: Boolean = true
) = File("2023/inputs", "$name")
    .readLines()
    .filter {
        if (removeEmptyLines) {
            !it.isEmpty()
        } else {
            true
        }
    }

/**
 * Executes the given [block] and prints the elapsed time in milliseconds.
 */
inline fun printTimeMillis(block: () -> Unit) {
    val start = System.currentTimeMillis()
    block()
    println(" (took $BLUE${System.currentTimeMillis() - start}ms$RESET)")
}

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)

// Least Common Multiple
fun Long.LCM(other: Long): Long {
    val larger = if (this > other) this else other
    val maxLcm = this * other
    var lcm = larger
    while (lcm <= maxLcm) {
        if (lcm % this == 0L && lcm % other == 0L) {
            return lcm
        }
        lcm += larger
    }
    return maxLcm
}