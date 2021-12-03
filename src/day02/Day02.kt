package day02

import readInput

private const val FORWARD = "forward"
private const val DOWN = "down"
private const val UP = "up"

fun main(){

    fun part1(input: List<String>): Int{
        var position = 0
        var depth = 0
        input.forEach{
            val (command, distanceStr) = it.split(" ")
            val distance = distanceStr.toInt()
            when(command){
                FORWARD -> position += distance
                DOWN -> depth += distance
                UP -> depth -= distance
            }
        }
        println("Position: $position\nDepth: $depth")
        return position * depth
    }

    fun part2(input: List<String>): Int{
        var position = 0
        var depth = 0
        var aim = 0
        input.forEach{
            val (command, distanceStr) = it.split(" ")
            val distance = distanceStr.toInt()
            when(command){
                FORWARD -> {
                    position += distance
                    depth += aim * distance
                }
                DOWN -> aim += distance
                UP -> aim -= distance
            }
        }
        println("Position: $position\nDepth: $depth")
        return position * depth
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02/test")
    println("Testing")
    check(part1(testInput) == 150)
    check(part2(testInput) == 900)
    println("Test passed")

    val input = readInput("Day02/input")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}