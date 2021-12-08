package day08

import day08.Line.Companion.toLine
import readInput

private fun String.sorted(): String = toCharArray().sorted().joinToString(separator = "")

data class Line(val record: List<String>, val output: List<String>) {
    companion object {
        fun String.toLine(): Line {
            val (r, o) = split(" | ")
            return Line(r.split(" "), o.split(" "))
        }
    }

    fun deduceValues(): Map<String, Int> {
        //unique values are straight forward
        val (one, r1) = record.partition { it.length == 2 }
        val (seven, r2) = r1.partition { it.length == 3 }
        val (four, r3) = r2.partition { it.length == 4 }
        val (eight, r4) = r3.partition { it.length == 7 }
        //nine uses 6 segments, and contains all of the segments from four
        val (nine, r5) = r4.partition { it.length == 6 && four[0].all { f -> it.contains(f) } }
        //zero is also 6 segments, and contains all elements from one
        val (zero, r6) = r5.partition { it.length == 6 && one[0].all { f -> it.contains(f) } }
        //six is the only remaining 6 segment item
        val (six, r7) = r6.partition { it.length == 6 }
        //three is the only remaining item using all segments from one
        val (three, r8) = r7.partition { one[0].all { f -> it.contains(f) } }
        //all of five's segments are shared with nine; two is the only remaining value
        val (five, two) = r8.partition { it.all { f -> nine[0].contains(f) } }

        return mapOf(
            zero[0].sorted() to 0,
            one[0].sorted() to 1,
            two[0].sorted() to 2,
            three[0].sorted() to 3,
            four[0].sorted() to 4,
            five[0].sorted() to 5,
            six[0].sorted() to 6,
            seven[0].sorted() to 7,
            eight[0].sorted() to 8,
            nine[0].sorted() to 9
        )
    }

    fun sumOutput(): Int {
        val values = deduceValues()
        return output.fold(0) { acc, i ->
            val v = values[i.sorted()] ?: throw(IllegalStateException("un-deduced value $i in line $this"))
            return@fold (acc * 10) + v
        }
    }

    override fun toString() = "$record | $output"
}

fun main() {
    fun part1(input: List<String>): Int {
        val uniqueLengths = listOf(2, 3, 4, 7)
        return input.map { it.toLine() }.sumOf { line -> line.output.count { it.length in uniqueLengths } }
    }

    fun part2(input: List<String>): Int {
        return input.map { it.toLine() }.sumOf { it.sumOutput() }
    }

    println("Testing")
    val test = readInput("day08/test")
    val part1 = part1(test)
    check(part1 == 26) { "Expected 26, got $part1" }
    println("Test passed")

    val input = readInput("day08/input")
    println("Part 1: ${part1(input)}")


    println("Testing part two")
    val singleTest = readInput("day08/singleLine")[0]
    val testLine = singleTest.toLine()
    println(testLine.deduceValues())
    val singleTestValue = testLine.sumOutput()
    check(singleTestValue == 5353) { "Expected 5353, got $singleTestValue" }

    println()

    val part2 = part2(test)
    check(part2 == 61229) { "Expected 61229, got $part2" }
    println("Test passed")

    println("Part 2: ${part2(input)}")
}