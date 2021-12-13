import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)

fun <T> checkResult(actual: T, expected: T) = check(actual == expected) { "Expected $expected, but got $actual" }

fun <T> List<List<T>>.forEachGrid(action: (x: Int, y: Int, T) -> Unit) {
    forEachIndexed { x, row -> row.forEachIndexed { y, it -> action(x, y, it) } }
}

fun <T> List<List<T>>.gridSum(action: (x: Int, y: Int, T) -> Int): Int {
    var sum = 0
    forEachIndexed { x, row -> row.forEachIndexed { y, it -> sum += action(x, y, it) } }
    return sum
}

fun <T> List<List<T>>.neighbours(x: Int, y: Int, default: T): List<T> {
    val up = if (x > 0) this[x - 1][y] else default
    val down = if (x < this.size - 1) this[x + 1][y] else default
    val left = if (y > 0) this[x][y - 1] else default
    val right = if (y < this[x].size - 1) this[x][y + 1] else default
    return listOf(up, right, down, left)
}
