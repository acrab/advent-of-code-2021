package day12

import checkResult
import readInput

private abstract class Cave(val name: String) {
    abstract fun canVisit(): Boolean
    abstract fun visit()
    abstract fun exit()
    val isEnd: Boolean = name == "end"
    val isStart: Boolean = name == "start"

    val connections = mutableListOf<Cave>()

    fun connect(other: Cave) {
        connections.add(other)
        other.connections.add(this)
    }

}

private class LargeCave(name: String) : Cave(name) {
    //we can enter large caves many times
    override fun canVisit() = true
    override fun visit() {}
    override fun exit() {}
}

private class SmallCave(name: String) : Cave(name) {
    //we can only visit small caves once
    private var visited = false
    override fun canVisit() = isEnd || !visited
    override fun visit() {
        visited = true
    }

    override fun exit() {
        visited = false
    }
}

private class ReVisitableSmallCave(name: String) : Cave(name) {
    //we can visit one small cave twice
    private var visited = false
    override fun canVisit(): Boolean {
        return when {
            //We can always go to the final cave
            isEnd -> true
            //We can never go back to the start
            isStart -> false
            //If we've visited this cave before, we can go again,
            //as long as we haven't already visited another cave twice
            visited -> duplicateCave == null
            //If we haven't visited this cave before, we can visit it
            else -> true
        }
    }

    override fun visit() {
        if (visited && !isEnd) {
            println("Visiting $name again")
            duplicateCave = this
        } else {
            visited = true
        }
    }

    override fun exit() {
        if (duplicateCave == this) {
            duplicateCave = null
        } else {
            visited = false
        }
    }

    companion object {
        var duplicateCave: Cave? = null
    }
}

fun main() {
    fun parseCaves(input: List<String>, revisitSmallCaves: Boolean = false): Map<String, Cave> {
        val allCaves = input
            //Split into start/end
            .flatMap { it.split("-") }
            //find unique elements
            .groupBy { it }
            //convert into caves
            .mapValues { (key, _) ->
                if (key.all { it.isUpperCase() }) {
                    LargeCave(key)
                } else if (revisitSmallCaves) {
                    ReVisitableSmallCave(key)
                } else {
                    SmallCave(key)
                }
            }

        //Make connections
        input.forEach {
            val (start, end) = it.split("-")
            allCaves[start]!!.connect(allCaves[end]!!)
        }
        return allCaves
    }

    fun countPathsFrom(start: Cave, route: String): Int {
        start.visit()
        val path = "$route,${start.name}"
        if (start.isEnd) {
            println(path.removePrefix(","))
            return 1
        }
        val count = start.connections.sumOf {
            if (!it.canVisit()) 0
            else {
                val paths = countPathsFrom(it, path)
                paths
            }
        }
        start.exit()
        return count
    }

    fun findPaths(input: List<String>, allowOneRevisit:Boolean): Int {
        val allCaves = parseCaves(input, allowOneRevisit)
        val startCave = allCaves["start"]!!
        return countPathsFrom(startCave, "")
    }

    val simpleInput = readInput("day12/simple")
    val complexInput = readInput("day12/complex")
    val largestTestInput = readInput("day12/largest")
    val questionInput = readInput("day12/input")

    checkResult(findPaths(simpleInput, allowOneRevisit = false), 10)
    checkResult(findPaths(complexInput, allowOneRevisit = false), 19)
    checkResult(findPaths(largestTestInput, allowOneRevisit = false), 226)

    println("Tests passed")
    println("Part 1: ${findPaths(questionInput, allowOneRevisit = false)}")

    checkResult(findPaths(simpleInput, allowOneRevisit = true), 36)
    checkResult(findPaths(complexInput, allowOneRevisit = true), 103)
    checkResult(findPaths(largestTestInput, allowOneRevisit = true), 3509)

    println("Tests passed")
    println("Part 2: ${findPaths(questionInput, allowOneRevisit = true)}")
}