fun main() {
    fun part1(input: List<String>): Int {
        var count = 0
        var last = -1
        input.map { it.toInt() }
            .forEach {
                if (last != -1 && it > last) {
                    count++
                }
                last = it
            }
        return count
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 7)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
