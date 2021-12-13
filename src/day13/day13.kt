package day13

import MutablePoint
import checkResult
import readInput

private enum class Direction {
    X, Y
}

private data class Instruction(val direction: Direction, val line: Int)

fun main() {

    fun fold(points: Iterable<MutablePoint>, instruction: Instruction) {
        when (instruction.direction) {
            Direction.X -> {
                val pointsToMove = points.filter { it.x > instruction.line }
                pointsToMove.forEach {
                    val distance = it.x - instruction.line
                    it.x -= (distance * 2)
                }
            }
            Direction.Y -> {
                val pointsToMove = points.filter { it.y > instruction.line }
                pointsToMove.forEach {
                    val distance = it.y - instruction.line
                    it.y -= (distance * 2)
                }
            }
        }
    }

    fun parseInput(input: List<String>): Pair<List<MutablePoint>, List<Instruction>> {
        val points = input.takeWhile { it.isNotEmpty() }
            .map {
                val (x, y) = it.split(",").map(String::toInt)
                MutablePoint(x, y)
            }
        val instructions = input.takeLastWhile { it.isNotEmpty() }.map {
            val (direction, line) = it.split("=")
            if (direction == "fold along y") {
                Instruction(Direction.Y, line.toInt())
            } else {
                Instruction(Direction.X, line.toInt())
            }
        }

        return points to instructions
    }

    fun part1(input: List<String>): Int {
        val (points, instructions) = parseInput(input)
        fold(points, instructions[0])
        return points.groupBy { it }.size
    }

    fun part2(input: List<String>): String {
        val (initialPoints, instructions) = parseInput(input)
        var points = initialPoints.toSet()
        instructions.forEach { instruction ->
            fold(points, instruction)
            points = points.toSet()
        }
        val output = mutableListOf<String>()
        //Swap coordinates to get a horizontal print out
        points.forEach { (y, x) ->
            if (output.size <= x) {
                repeat((x-output.size)+1) {
                    output.add("")
                }
            }
            output[x] = output[x].padEnd(y+1, '.').replaceRange(y, y + 1, "#")

        }
        return output.joinToString("\n")

    }

    println("Testing part1")
    val testInput = readInput("day13/test")
    val realInput = readInput("day13/input")

    checkResult(part1(testInput), 17)

    println("Part 1: ${part1(realInput)}")

    println("part 2")
    println(part2(realInput))
}