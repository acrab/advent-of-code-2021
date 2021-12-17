package day17

import readInputLine
import kotlin.math.abs
import kotlin.math.max

data class Target(val xMin: Int, val xMax: Int, val yMin: Int, val yMax: Int)

fun simulate(xPower: Int, yPower: Int, target: Target): Boolean {
    var currentXPower = xPower
    var currentYPower = yPower
    var x = 0
    var y = 0
    while (x <= target.xMax && y >= target.yMin) {
        x += currentXPower
        y += currentYPower
        currentXPower = max(0, currentXPower - 1)
        currentYPower -= 1
        if (x in target.xMin..target.xMax && y in target.yMin..target.yMax) {
            return true
        }
    }
    return false
}

fun main() {

    fun triangleNumber(n: Int): Int = (n * (n + 1)) / 2

    fun findFirstTriangleInRange(min: Int, max: Int): Int {
        var t = 0
        var step = 1
        while (t <= max) {
            t += step
            if (t in min..max) {
                return step
            }
            step++
        }
        return -1
    }

    fun parseInput(input: String): Target {
        val regex = Regex("""x=(-?\d+)\.\.(-?\d+), y=(-?\d+)\.\.(-?\d+)""")
        val matches = regex.find(input)!!
        val (xMin, xMax, yMin, yMax) = matches.destructured
        return Target(xMin.toInt(), xMax.toInt(), yMin.toInt(), yMax.toInt())
    }

    fun part1(input: Target): Int {
        //Maximum height is one where the last step moves us from 0 (sub height) to bottom of target
        return triangleNumber(abs(input.yMin) - 1)
    }

    fun part2(input: Target): Int {
        var validVelocities = 0
        //Calculate range of possibilities
        //lowest possible x = first triangle less than xMax, as anything else will stop before it gets there
        val minXVel = findFirstTriangleInRange(input.xMin, input.xMax)
        //highest possible x = xMax (one step to reach far side of target)
        val maxXVel = input.xMax

        //lowest possible y = yMin (one step to bottom of target)
        val minYVel = input.yMin
        //highest possible y = part1 (final step moves from submarine level to bottom of target)
        val maxYVel = part1(input)

        println("Testing $minXVel to $maxXVel")

        for (x in minXVel..maxXVel) {
            for (y in minYVel..maxYVel) {
                if (simulate(x, y, input)) {
                    validVelocities++
                }
            }
            println("X: $x, found $validVelocities so far")
        }
        return validVelocities
    }


    val test = parseInput(readInputLine("day17/test"))
    check(part1(test) == 45)
    check(part2(test) == 112)

    val input = parseInput(readInputLine("day17/input"))
    println("Part1: ${part1(input)}")
    println("Part2: ${part2(input)}")


}