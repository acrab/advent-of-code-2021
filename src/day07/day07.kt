package day07

import readInput
import kotlin.math.abs

fun main() {

    fun simpleFuelCalculator(groups: Map<Int, Int>, position: Int) =
        groups.map { abs(position - it.key) * it.value }.sum()

    fun complexCalculator(groups: Map<Int, Int>, position: Int) =
        groups.map {
            val distance = abs(position - it.key)
            val cost = (distance * (distance + 1)) / 2
            cost * it.value
        }.sum()

    fun calculate(crabs: List<Int>, calculateFuelCost: (Map<Int, Int>, Int) -> Int): Int {
        val groups = crabs.groupBy { it }
            .mapValues { it.value.count() }
            .toSortedMap()

        val min = groups.firstKey()
        val max = groups.lastKey()
        var minimumFuelCost = Int.MAX_VALUE
        println(buildString {
            for (position in min..max) {
                val current = calculateFuelCost(groups, position)
                if (current < minimumFuelCost) {
                    minimumFuelCost = current
                }
                append("$current,")
            }
        })
        //End point must have been minimum
        return minimumFuelCost
    }

    val test = parseInput("day07/test")

    val part1Test = calculate(test, ::simpleFuelCalculator)
    check(part1Test == 37) { "expected 37, got $part1Test" }

    val part2Test = calculate(test, ::complexCalculator)
    check(part2Test == 168) { "expected 168, got $part2Test" }

    val input = parseInput("day07/input")

    println("Part 1: ${calculate(input, ::simpleFuelCalculator)}")
    println("Part 2: ${calculate(input, ::complexCalculator)}")
}

private fun parseInput(file: String) = readInput(file)[0].split(",").map { it.toInt() }