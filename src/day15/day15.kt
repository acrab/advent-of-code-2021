package day15

import checkResult
import forEachGrid
import orthogonalNeighbours
import readIntegerGrid

fun main() {
    data class Chiton(val risk: Int, val x: Int, val y: Int) {
        var totalRisk: Int = Int.MAX_VALUE
        var route: Chiton? = null
    }

    fun part1(input: List<List<Int>>): Int {
        val chitons =
            input.mapIndexed { x: Int, rows: List<Int> -> rows.mapIndexed { y, risk -> Chiton(risk, x, y) } }
        val size = chitons.size to chitons[0].size
        println("size: $size")
        val start = chitons[0][0]
        start.totalRisk = 0
        val leaves = mutableListOf(start)
        while (leaves.isNotEmpty()) {
            //Find best chiton to add to the path
            val best = leaves.minByOrNull { it.totalRisk }!!
            //Set total risk for each adjacent, and add to list
            chitons.orthogonalNeighbours(best.x, best.y, null).filterNotNull().forEach {
                val potentialRisk = best.totalRisk + it.risk
                //If we've got a better route to this chiton
                if (it.totalRisk > potentialRisk) {
                    //Record the total risk to get to this point
                    it.totalRisk = potentialRisk
                    it.route = best
                    //And add it to the list of things to test
                    if (!leaves.contains(it)) {
                        leaves.add(it)
                    }
                }

                //Early exit if this is the final leaf
                if (it.x == (size.first - 1) && it.y == (size.second - 1)) {
                    return potentialRisk
                }

            }
            //Remove this leaf
            leaves.remove(best)
        }
        return -1
    }

    fun expandGrid(input: List<List<Int>>): List<List<Int>> {
        //Generate part2 grid
        val expandedGrid = MutableList(input.size * 5) {
            MutableList<Int>(input.size * 5) { 0 }
        }
        val width = input[0].size
        val height = input.size
        //Generate first duplicate row
        input.forEachGrid { x, y, value ->
            repeat(5) {
                var modifiedValue = value + it
                if (modifiedValue > 9) {
                    modifiedValue -= 9
                }
                expandedGrid[x][y + (width * it)] = modifiedValue
            }
        }

        //Generate additional duplicate rows
        for (repetition in 1 until 5) {
            for (x in 0 until height) {
                val newX = x + (height * repetition)
                for (y in 0 until expandedGrid[0].size) {
                    val value = expandedGrid[x][y]
                    var modifiedValue = value + repetition
                    if (modifiedValue > 9) {
                        modifiedValue -= 9
                    }
                    expandedGrid[newX][y] = modifiedValue
                }
            }
        }
        return expandedGrid
    }

    fun part2(input: List<List<Int>>): Int {
        val expandedGrid = expandGrid(input)

        println(
            expandedGrid.joinToString("\n") { row ->
                row.joinToString("") { it.toString() }
            }
        )
        //Call part1 route finder, with the expanded grid
        return part1(expandedGrid)
    }

    val test = readIntegerGrid("day15/test")
    checkResult(part1(test), 40)
    println("Part 1 test passed")

    val g = expandGrid(listOf(listOf(8)))
    checkResult(
        g, listOf(
            listOf(8, 9, 1, 2, 3),
            listOf(9, 1, 2, 3, 4),
            listOf(1, 2, 3, 4, 5),
            listOf(2, 3, 4, 5, 6),
            listOf(3, 4, 5, 6, 7)
        )
    )

    checkResult(part2(test), 315)

    println("Test passed")
    val input = readIntegerGrid("day15/input")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")

}