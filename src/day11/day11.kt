package day11

import allNeighbours
import checkResult
import forEachGrid
import gridSum
import readIntegerGrid

fun main() {

    data class Octopus(var level: Int, var flashed: Boolean, var flashCount: Int, var x: Int, var y: Int) {
        fun incrementLevel() {
            level += 1
        }

        fun flash(): Boolean {
            if (flashed) {
                return false
            }
            if (level <= 9) {
                return false
            }
            flashCount += 1
            flashed = true
            return true
        }

        fun endStep() {
            if (flashed) {
                level = 0
                flashed = false
            }
        }
    }

    fun recursiveFlash(levels: List<List<Octopus>>, target: Octopus) {
        if (target.flash()) {
            levels.allNeighbours(target.x, target.y, null).forEach {
                if (it != null) {
                    it.incrementLevel()
                    recursiveFlash(levels, it)
                }
            }
        }
    }

    fun printState(levels: List<List<Octopus>>) {
        println(buildString {
            levels.forEachGrid { _, y, it ->
                if (y == 0) append("\n")
                if (it.flashed) append("*${it.level.toString().padStart(2)}*")
                else append(" ${it.level.toString().padStart(2)} ")
            }
        })

    }

    fun simulateStep(levels: List<List<Octopus>>): Boolean {
        //Increase level
        levels.forEachGrid { _, _, octopus ->
            octopus.incrementLevel()
        }
//        println("Post increment")
//        printState(levels)
        //flash everything
        levels.forEachGrid { _, _, octopus ->
            recursiveFlash(levels, octopus)
        }
//        println("Post flash")
//        printState(levels)
        val allFlashed = levels.all { row -> row.all { it.flashed } }
        //reset
        levels.forEachGrid { _, _, octopus ->
            octopus.endStep()
        }
        return allFlashed
    }

    fun part1(levels: List<List<Int>>, steps: Int): Int {
        val octopuses = levels.mapIndexed { x, row -> row.mapIndexed { y, value -> Octopus(value, false, 0, x, y) } }
        repeat(steps) {
            simulateStep(octopuses)
        }
        return octopuses.gridSum { _, _, octopus -> octopus.flashCount }
    }

    fun part2(levels: List<List<Int>>): Int {
        val octopuses = levels.mapIndexed { x, row -> row.mapIndexed { y, value -> Octopus(value, false, 0, x, y) } }
        var allFlashed = false
        var stepCount = 0
        while(!allFlashed){
            allFlashed = simulateStep(octopuses)
            stepCount++
        }
        return stepCount
    }

    //simple test
    val simpleInput = readIntegerGrid("day11/simple_test")
    checkResult(part1(simpleInput, 1), 9)
    checkResult(part1(simpleInput, 2), 9)
    println("Simple test passed")

    //complex test
    val complexTestInput = readIntegerGrid("day11/complex_test")
    checkResult(part1(complexTestInput, 1), 0)
    println("Complex test, 2 steps")
    checkResult(part1(complexTestInput, 2), 35)
    println("Complex test, 10 steps")
    checkResult(part1(complexTestInput, 10), 204)
    println("Complex test, 100 steps")
    checkResult(part1(complexTestInput, 100), 1656)
    println("Simple test passed")

    val fullInput = readIntegerGrid("day11/input")
    println("Part 1: ${part1(fullInput, 100)}")

    println("Test part 2")
    checkResult(part2(complexTestInput), 195)

    println("Part 2: ${part2(fullInput)}")

}