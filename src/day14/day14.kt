package day14

import checkResult
import readInput

fun main() {

    fun parseInput(input: List<String>): Pair<String, Map<String, String>> {
        val template = input[0]
        val rules = input.slice(2 until input.size).map {
            val (rule, replacement) = it.split(" -> ")
            rule to replacement
        }
        return template to rules.toMap()
    }

    fun maxMinusMin(polymer: String): Int {
        val counts = polymer.groupBy { it }.values.map { it.size }
        return counts.maxOf { it } - counts.minOf { it }
    }

    fun part1(template: String, rules: Map<String, String>): Int {
        var polymer = template
        repeat(10) {
            polymer = buildString {
                polymer.windowed(2, partialWindows = true).forEach { pair ->
                    append(pair[0])
                    //generate new string
                    rules[pair]?.let(::append)
                }
            }
            println(polymer.length)
        }
        return maxMinusMin(polymer)
    }

    fun <T> MutableMap<T, Long>.setOrAdd(key: T, value: Long = 1) {
        val c = this[key] ?: 0
        this[key] = c + value
    }

    fun part2(template: String, rules: Map<String, String>, iter: Int): Long {
        var pairs = mutableMapOf<String, Long>()

        template.windowed(2).forEach {
            pairs.setOrAdd(it)
        }

        repeat(iter) {
            val newPairs = mutableMapOf<String, Long>()
            pairs.forEach { (pair, count) ->
                if (rules.containsKey(pair)) {
                    val mid = rules[pair]!!
                    val left = "${pair[0]}$mid"
                    val right = "$mid${pair[1]}"
                    newPairs.setOrAdd(left, count)
                    newPairs.setOrAdd(right, count)
                } else {
                    newPairs[pair] = count
                }
            }
            pairs = newPairs
        }

        val chars = mutableMapOf<Char, Long>()
        pairs.forEach { (pair, count) ->
            //Only count the first character in each pair. The second will form the first character of another pair
            chars.setOrAdd(pair[0], count)
        }

        //Add the final character in the string, since it won't be the first character of any pair
        chars.setOrAdd(template.last(), 1)

        val max = chars.maxOf { it.value }
        val min = chars.minOf { it.value }

        return max - min
    }

    val (testPoly, testRules) = parseInput(readInput("day14/test"))
    check(part1(testPoly, testRules) == 1588)

    checkResult(part2(testPoly, testRules, 10), 1588)
    checkResult(part2(testPoly, testRules, 40), 2188189693529)
    val (polymer, rules) = parseInput(readInput("day14/input"))
    println("Part1: ${part1(polymer, rules)}")
    println("Part2: ${part2(polymer, rules, 40)}")


}