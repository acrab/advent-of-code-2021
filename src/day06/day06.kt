package day06

import readInput
import java.math.BigInteger

fun main() {
    fun simulate(input: String, days: Int): BigInteger {
        val fish = input
            .split(",")
            .groupBy { it }
            .map { it.key.toInt() to it.value.count() }
        val adultFish = MutableList(7) { BigInteger.ZERO }
        val childFish = MutableList(7) { BigInteger.ZERO }

        fish.forEach {
            adultFish[it.first] = it.second.toBigInteger()
        }
        println("Initial state: $adultFish")
        repeat(days) {
            val dayOfWeek = it % 7
            println("Day: $dayOfWeek ($it)")
            //birth any new fish
            val newFish = adultFish[dayOfWeek]
            println("Birthed $newFish")
            //add to nursery
            childFish[(dayOfWeek + 2) % 7] = newFish
            //mature any children
            adultFish[dayOfWeek] += childFish[dayOfWeek]
            println("Matured ${childFish[dayOfWeek]}")
            childFish[dayOfWeek] = BigInteger.ZERO
        }

        return adultFish.sumOf { it } + childFish.sumOf { it }
    }

    val testInput = readInput("day06/test")
    val check1a = simulate(testInput[0], 18)
    check(check1a == 26.toBigInteger()) { "Expected 26, got $check1a" }

    val check1b = simulate(testInput[0], 80)
    check(check1b == 5934.toBigInteger()) { "Expected 5934, got $check1b" }

    val input = readInput("day06/input")
    //part1
    println(simulate(input[0], 80))
    //part2
    println(simulate(input[0], 256 ))
}
