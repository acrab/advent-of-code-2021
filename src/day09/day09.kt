package day09

import checkResult
import forEachGrid
import gridSum
import orthogonalNeighbours
import readIntegerGrid

class Group(private val members: MutableList<GridPoint> = mutableListOf()) {
    fun mergeWith(other: Group) {
        other.members.forEach {
            it.group = this
            members.add(it)
        }
    }

    val size: Int
        get() = members.size

    fun add(gridPoint: GridPoint) {
        members.add(gridPoint)
        gridPoint.group = this
    }
}

data class GridPoint(val value: Int, var group: Group? = null)

fun main() {

    fun calculateRisk(input: Int) = input + 1

    fun part1(grid: List<List<Int>>): Int =
        grid.gridSum { x, y, i ->
            if (grid.orthogonalNeighbours(x, y, 10).all { i < it }) {
                calculateRisk(i)
            } else {
                0
            }
        }

    fun part2(grid: List<List<Int>>): Int {
        val points = grid.map { row -> row.map { GridPoint(it) } }
        val groups = mutableListOf<Group>()
        points.forEachGrid { x, y, gridPoint ->
            //Value 9s are the walls between groups
            if (gridPoint.value == 9) return@forEachGrid

            val neighbours = points.orthogonalNeighbours(x, y, null).filterNotNull()
            val existingGroups = neighbours.mapNotNull { it.group }
            if (existingGroups.isEmpty()) {
                //make a new group
                with(Group()) {
                    add(gridPoint)
                    groups.add(this)
                }
            } else if (existingGroups.size == 1) {
                existingGroups[0].add(gridPoint)
            } else {
                //merge groups, use the resulting group
                val mergedGroup = existingGroups.fold(existingGroups[0]) { acc, group ->
                    if (acc == group) acc else acc.apply {
                        mergeWith(group)
                    }
                }
                mergedGroup.add(gridPoint)
            }
        }
        val sortedGroups = groups.sortedByDescending { it.size }
        return sortedGroups[0].size * sortedGroups[1].size * sortedGroups[2].size
    }

    val test = readIntegerGrid("day09/test")

    checkResult(part1(test), 15)
    checkResult(part2(test), 1134)


    val input = readIntegerGrid("day09/input")
    println(part1(input))

    println(part2(input))
}