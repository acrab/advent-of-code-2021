import java.io.File

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt").readLines()

fun readIntegerGrid(name: String) = readInput(name).map { line -> line.toCharArray().map { it.digitToInt() } }

fun <T> checkResult(actual: T, expected: T) = check(actual == expected) { "Expected $expected, but got $actual" }

fun <T> List<List<T>>.forEachGrid(action: (x: Int, y: Int, T) -> Unit) {
    forEachIndexed { x, row -> row.forEachIndexed { y, it -> action(x, y, it) } }
}

fun <T> List<List<T>>.gridSum(action: (x: Int, y: Int, T) -> Int): Int {
    var sum = 0
    forEachIndexed { x, row -> row.forEachIndexed { y, it -> sum += action(x, y, it) } }
    return sum
}

fun <T> List<List<T>>.orthogonalNeighbours(x: Int, y: Int, default: T): List<T> {
    val up = if (x > 0) this[x - 1][y] else default
    val down = if (x < this.size - 1) this[x + 1][y] else default
    val left = if (y > 0) this[x][y - 1] else default
    val right = if (y < this[x].size - 1) this[x][y + 1] else default
    return listOf(up, right, down, left)
}

fun <T> List<List<T>>.allNeighbours(x: Int, y: Int, default: T): List<T> {
    val spaceNorth = x > 0
    val spaceEast = y < this[x].size - 1
    val spaceSouth = x < this.size - 1
    val spaceWest = y > 0

    val north = if (spaceNorth) this[x - 1][y] else default
    val northEast = if (spaceNorth && spaceEast) this[x - 1][y + 1] else default
    val east = if (spaceEast) this[x][y + 1] else default
    val southEast = if (spaceSouth && spaceEast) this[x + 1][y + 1] else default
    val south = if (spaceSouth) this[x + 1][y] else default
    val southWest = if (spaceSouth && spaceWest) this[x + 1][y - 1] else default
    val west = if (spaceWest) this[x][y - 1] else default
    val northWest = if (spaceNorth && spaceWest) this[x - 1][y - 1] else default
    return listOf(north, northEast, east, southEast, south, southWest, west, northWest)
}
