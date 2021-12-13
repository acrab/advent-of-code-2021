package day10

import checkResult
import readInput
import java.math.BigInteger
import java.util.*

fun main() {
    fun part1(input: List<String>): Int {
        val bracketPairs = mapOf(
            ')' to '(',
            ']' to '[',
            '}' to '{',
            '>' to '<'
        )

        val characterScores = mapOf(
            ')' to 3,
            ']' to 57,
            '}' to 1197,
            '>' to 25137
        )
        return input.sumOf { line ->
            val stack = Stack<Char>()
            line.forEach {
                when (it) {
                    '(', '[', '{', '<' -> stack.push(it)
                    ')', ']', '}', '>' -> {
                        if (stack.peek() == bracketPairs[it]) {
                            stack.pop()
                        } else {
                            return@sumOf characterScores[it]!!
                        }
                    }
                }
            }
            return@sumOf 0
        }
    }

    fun part2(input: List<String>): BigInteger {

        fun calculateScore(stack: Stack<Char>): BigInteger {
            val characterScores = mapOf(
                '(' to 1.toBigInteger(),
                '[' to 2.toBigInteger(),
                '{' to 3.toBigInteger(),
                '<' to 4.toBigInteger()
            )

            val multipler = 5.toBigInteger()

            return stack.foldRight(BigInteger.ZERO) { c, acc ->
                val s = (acc * multipler) + characterScores[c]!!
                s
            }
        }

        val bracketPairs = mapOf(
            ')' to '(',
            ']' to '[',
            '}' to '{',
            '>' to '<'
        )

        val scores = input.map { line ->
            var invalid = false
            val stack = Stack<Char>()
            line.forEach {
                when (it) {
                    '(', '[', '{', '<' -> stack.push(it)
                    ')', ']', '}', '>' -> {
                        if (stack.peek() == bracketPairs[it]) {
                            stack.pop()
                        } else {
                            invalid = true
                        }
                    }
                }
            }

            return@map if (invalid) BigInteger.ZERO else calculateScore(stack)
        }.filter { it != BigInteger.ZERO }.sorted()
        println(scores)
        return scores[scores.size / 2]
    }
    println("Testing")
    val test = readInput("day10/test")
    checkResult(part1(test), 26397)
    checkResult(part2(test), 288957.toBigInteger())
    println("Tests passed")

    val input = readInput("day10/input")
    println("part1: ${part1(input)}")
    println("part2: ${part2(input)}")

}