package day05

import readInput
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.max

private data class Vent(val x: Int, val y: Int, var size: Int = 0) {
    fun incrementSize() {
        size += 1
    }

    override fun toString() = "{$x,$y}@$size"
}

private data class Line(val start: Pair<Int, Int>, val end: Pair<Int, Int>) {
    val orthogonal = start.first == end.first || start.second == end.second

    override fun toString(): String = "${start.first},${start.second} -> ${end.first},${end.second}"
}

private fun String.toPoint() = split(",").let { it[0].toInt() to it[1].toInt() }

private fun List<String>.toLines(): List<Line> {
    return map {
        val (start, end) = it.split(" -> ")
        Line(start.toPoint(), end.toPoint())
    }
}

private fun printGrid(vents: Set<Vent>) {
    println(buildString {
        for (y in 0..9) {
            for (x in 0..9) {
                val vent = vents.firstOrNull { it.x == x && it.y == y }
                if(vent != null){
                    append(vent.size)
                }else{
                    append('.')
                }
            }
            append("\n")
        }
    })
}

fun main() {

    fun part1(input: List<String>): Int {

        val lines = input.toLines().filter { it.orthogonal }

        val vents = mutableSetOf<Vent>()

        fun addOrIncrementVent(x: Int, y: Int) {
            var vent = vents.firstOrNull { it.x == x && it.y == y }

            if (vent == null) {
                vent = Vent(x, y)
                vents.add(vent)
            }

            vent.incrementSize()
        }

        lines.forEach {
            //work out which way we're going
            if (it.start.first == it.end.first) {
                val x = it.start.first
                val start = min(it.start.second, it.end.second)
                val end = max(it.start.second, it.end.second)
                for (y in start..end) {
                    addOrIncrementVent(x, y)
                }
            } else {
                val y = it.start.second
                val start = min(it.start.first, it.end.first)
                val end = max(it.start.first, it.end.first)
                for (x in start..end) {
                    addOrIncrementVent(x, y)
                }
            }
        }
        printGrid(vents)

        println("Counting")
        return vents.count {
            it.size > 1
        }
    }

    fun part2(input: List<String>): Int {

        val lines = input.toLines()

        val vents = mutableSetOf<Vent>()

        fun addOrIncrementVent(x: Int, y: Int) {
            var vent = vents.firstOrNull { it.x == x && it.y == y }

            if (vent == null) {
                Vent(x, y).also {
                    vent = it
                    vents.add(it)
                }
            }

            vent?.incrementSize()
        }

        lines.forEach {

            val length = max(
                abs(it.start.first - it.end.first),
                abs(it.start.second - it.end.second)
            ) + 1
            fun calcDelta(start: Int, end: Int) = when {
                start < end -> 1
                start > end -> -1
                else -> 0
            }

            val deltaX = calcDelta(it.start.first, it.end.first)
            val deltaY = calcDelta(it.start.second, it.end.second)

            var x = it.start.first
            var y = it.start.second
            repeat(length) {
                addOrIncrementVent(x, y)
                x += deltaX
                y += deltaY
            }
        }
        println("Counting")
        return vents.count {
            it.size > 1
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day05/test")
    println("Testing")
    val test1 = part1(testInput)
    check(test1 == 5) { "Expected 5, got $test1" }
    println("Part 1 passed")
    val test2 = part2(testInput)
    check(test2 == 12) { "Expected 12, got $test2" }
    println("Test passed")

    val input = readInput("day05/input")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}