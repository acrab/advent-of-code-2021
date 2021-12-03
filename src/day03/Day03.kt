package day03

import readInput
import kotlin.math.pow

fun main() {

    fun part1(input: List<String>): Int {
        val wordSize = input[0].length
        //We will use index 0 as the most significant bit, and move towards the least significant
        val bits = MutableList(size = wordSize) { 0 }
        input.forEach { line ->
            line.forEachIndexed { index, c ->
                if (c == '1') bits[index] = bits[index] + 1
            }
        }

        //Used to work out if "1" is the most common item
        val halfInputCount = input.size / 2

        var gamma = 0.0
        var epsilon = 0.0

        //Now, reverse the bits, so the first item in the array is the least significant bit
        bits.reversed().forEachIndexed { index, i ->
            if (i > halfInputCount) {
                gamma += 2.0.pow(index)
            } else {
                epsilon += 2.0.pow(index)
            }
        }
        return (gamma * epsilon).toInt()
    }

    fun part2(input: List<String>): Int {
        val wordSize = input[0].length
        var oxyValues = input

        for (i in 0 until wordSize) {
            //Find most common value at this position
            val mostCommon = oxyValues.mostCommonCharAtPosition(i)
            println("most common in $i: $mostCommon")
            //Keep only rows with this value at this position
            oxyValues = oxyValues.keepWithCharAtPosition(mostCommon, i)
            println("keeping ${oxyValues.count()} items")
            if(oxyValues.size == 1){
                break
            }
        }

        val oxyRating = oxyValues[0].toInt(2)

        println("OxyRating: $oxyRating")
        var co2Values = input
        for (i in 0 until wordSize) {
            //Find most common value at this position
            val mostCommon = co2Values.leastCommonCharAtPosition(i)
            println("least common in $i: $mostCommon")
            //Keep only rows with this value at this position
            co2Values = co2Values.keepWithCharAtPosition(mostCommon, i)
            println("keeping ${co2Values.count()} items")
            if(co2Values.size == 1){
                break
            }
        }
        val co2Rating = co2Values[0].toInt(2)

        println("CO2 rating: $co2Rating")

        return oxyRating * co2Rating
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day03/test")
    println("Testing")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)
    println("Test passed")

    val input = readInput("day03/input")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

fun List<String>.keepWithCharAtPosition(char: Char, position: Int) =
    filter { it[position] == char }

fun List<String>.countOnesAtPosition(position: Int): Int {
    var countOfOnes = 0
    forEach { line ->
        if (line[position] == '1') {
            countOfOnes++
        }
    }
    return countOfOnes
}

fun List<String>.mostCommonCharAtPosition(position: Int): Char {
    val countOfOnes = countOnesAtPosition(position)

    val halfCount = size.toDouble() / 2

    //If tied, use '1' as default
    return if (countOfOnes < halfCount) '0'
    else '1'
}

fun List<String>.leastCommonCharAtPosition(position: Int): Char {
    val countOfOnes = countOnesAtPosition(position)

    val halfCount = size.toDouble() / 2

    //If tied, use '0' as default
    return if (countOfOnes >= halfCount) '0'
    else '1'
}