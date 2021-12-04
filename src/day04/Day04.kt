package day04

import readInput
import java.lang.Exception

fun main() {

    data class Entry(var value: Int, var marked: Boolean) {
        override fun toString() =
            if (marked) " [${value.toString().padStart(2)}] "
            else "  ${value.toString().padStart(2)}  "
    }

    class Card(data: List<List<Int>>) {

        val rows: List<List<Entry>> = List(5) { List(5) { Entry(0, false) } }
        val columns = MutableList(5) { mutableListOf<Entry>() }

        init {
            data.forEachIndexed { x, row ->
                row.forEachIndexed { y, item ->
                    rows[x][y].value = item
                    columns[y].add(x, rows[x][y])
                }
            }
        }

        fun markNumber(number: Int) {
            rows.forEach { row ->
                row.forEach { item ->
                    if (item.value == number) {
                        item.marked = true
                    }
                }
            }
        }

        fun isWinning(): Boolean {
            val winningRow = rows.firstOrNull { r -> r.all { it.marked } }
            if (winningRow != null) {
                println("Win found on row $winningRow")
                return true
            }

            val winningColumn = columns.firstOrNull { c -> c.all { it.marked } }
            if (winningColumn != null) {
                println("Win found on column $winningColumn")
                return true
            }

            return false
        }

        fun sumUnmarked() = rows.sumOf { row -> row.filterNot { it.marked }.sumOf { it.value } }

        override fun toString() =
            buildString {
                rows.forEach { row ->
                    row.forEach { entry ->
                        append(entry)
                    }
                    append("\n")
                }
            }

    }

    fun findWinningScore(drawList: List<Int>, cardList: List<Card>): Int {
        for (draw in drawList) {
            println("Draw: $draw")
            for (card in cardList) {
                card.markNumber(draw)
                if (card.isWinning()) {
                    return card.sumUnmarked() * draw
                }
            }
        }
        throw Exception("No winning cards found!")
    }

    fun findLosingScore(drawList: List<Int>, cardList: List<Card>): Int {
        var nonwinningCards = cardList
        for (draw in drawList) {
            println("Draw: $draw")
            for (card in nonwinningCards) {
                card.markNumber(draw)
            }
            if(nonwinningCards.size == 1){
                if(nonwinningCards[0].isWinning())
                return nonwinningCards[0].sumUnmarked() * draw
            }else {
                nonwinningCards = nonwinningCards.filterNot { it.isWinning() }
            }
        }
        throw Exception("No losing cards found (or two cards lost at the same time)!")
    }

    fun parseCardList(input: List<String>): List<Card> {
        val cardList = input.subList(2, input.size)
            .filterNot { it.isEmpty() }
            .chunked(5)
            .map {
                it.map { row -> row.split(" ").filterNot { it.isEmpty() }.map { entry -> entry.toInt() } }
            }
            .map { Card(it) }
        return cardList
    }

    fun parseDrawList(input: List<String>) = input[0].split(",").map { it.toInt() }

    fun part1(input: List<String>): Int {

        val drawList = parseDrawList(input)

        val cardList = parseCardList(input)

        return findWinningScore(drawList, cardList)
    }

    fun part2(input: List<String>): Int {

        val drawList = parseDrawList(input)

        val cardList = parseCardList(input)

        return findLosingScore(drawList, cardList)
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("day04/test")
    println("Testing")
    val test1 = part1(testInput)
    check(test1 == 4512) { "Expected 4512, got $test1" }
    println("Part 1 passed")
    check(part2(testInput) == 1924)
    println("Test passed")

    val input = readInput("day04/input")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}