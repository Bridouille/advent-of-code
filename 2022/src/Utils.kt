import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(
    name: String,
    removeEmptyLines: Boolean = true
) = File("src", "$name.txt")
    .readLines()
    .filter {
        if (removeEmptyLines) {
            !it.isEmpty()
        } else {
            true
        }
    }

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)
