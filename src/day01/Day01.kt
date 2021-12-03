package day01

import readInput

fun main() {

    fun List<Int>.countIncreases(): Int{
        var count = 0
        var last = -1
        forEach {
            if (last != -1 && it > last) {
                count++
            }
            last = it
        }
        return count
    }

    fun part1(input: List<String>): Int = input.map { it.toInt() }.countIncreases()

    fun part2(input: List<String>): Int {
        val ints = input.map { it.toInt() }
        val count = ints.size
        val firsts = ints.slice(0..count - 3)
        val seconds = ints.slice(1..count - 2)
        val thirds = ints.slice(2..count - 1)
        val partialWindow = firsts.zip(seconds){a, b -> a+b}
        val windows = partialWindow.zip(thirds){a,b -> a+b}

        return windows.countIncreases()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01/test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 5)

    val input = readInput("Day01/input")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
