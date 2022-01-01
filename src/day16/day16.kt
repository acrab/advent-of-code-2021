package day16

import checkResult
import readInput
import readInputLine
import java.math.BigInteger

private data class Packet(val version: Int, val type: Int, val literal: BigInteger?, val subPackets: List<Packet>?) {
    //Part 1: Recursive sum of packet versions
    fun sumVersions(): Int = version + (subPackets?.sumOf { it.sumVersions() } ?: 0)

    //Part 2: Use type to recursively a value from the packet
    private fun sum(): BigInteger? = subPackets?.sumOf { it.value() }
    private fun product(): BigInteger? = subPackets?.fold(BigInteger.ONE) { acc, packet -> acc * packet.value() }
    private fun min(): BigInteger? = subPackets?.minOf { it.value() }
    private fun max(): BigInteger? = subPackets?.maxOf { it.value() }
    private fun greaterThan(): BigInteger? = subPackets?.let {
        if (it[0].value() > it[1].value()) BigInteger.ONE else BigInteger.ZERO
    }

    private fun lessThan(): BigInteger? = subPackets?.let {
        if (it[0].value() < it[1].value()) BigInteger.ONE else BigInteger.ZERO
    }

    private fun equal(): BigInteger? = subPackets?.let {
        if (it[0].value() == it[1].value()) BigInteger.ONE else BigInteger.ZERO
    }

    fun value(): BigInteger = when (type) {
        0 -> sum()
        1 -> product()
        2 -> min()
        3 -> max()
        4 -> literal!!
        5 -> greaterThan()
        6 -> lessThan()
        7 -> equal()
        else -> {
            null
        }
    } ?: throw IllegalStateException()

    //Prints this packet, and all it's contained packets.
    override fun toString(): String =
        buildString {
            addToBuilder("", this)
        }


    private fun addToBuilder(indent: String, stringBuilder: StringBuilder) {
        when (type) {
            0 -> stringBuilder.appendLine("$indent+(${subPackets!!.size})")
            1 -> stringBuilder.appendLine("$indent*(${subPackets!!.size})")
            2 -> stringBuilder.appendLine("${indent}min(${subPackets!!.size})")
            3 -> stringBuilder.appendLine("${indent}max(${subPackets!!.size})")
            4 -> stringBuilder.appendLine("$indent$literal")
            5 -> stringBuilder.appendLine("${indent}>(${subPackets!!.size})")
            6 -> stringBuilder.appendLine("${indent}<(${subPackets!!.size})")
            7 -> stringBuilder.appendLine("${indent}=(${subPackets!!.size})")
        }
        subPackets?.forEach {
            it.addToBuilder("$indent ", stringBuilder)
        }
    }
}

/**
 * Given an iterator over a Char array representing a Packet,
 * consumes sufficient characters to produce a valid packet.
 *
 * Note, there is undefined behaviour if the iterator produces characters other than '0' and '1'
 *
 * @return The packet and the number of characters consumed from the iterator.
 */
private fun Iterator<Char>.parsePackets(): Pair<Packet, Int> {
    var bitsConsumed = 0

    //First three bits are the version
    val version = listOf(next(), next(), next()).joinToString("").toInt(2)
    //Second three bits are the type
    val type = listOf(next(), next(), next()).joinToString("").toInt(2)

    bitsConsumed += 6
    //Type 4: literal value
    if (type == 4) {
        //Parse the literal
        var header: Char
        val value = buildString {
            do {
                header = next()
                repeat(4) {
                    append(next())
                }
                bitsConsumed += 5
            } while (header != '0')
        }
        val literal = value.toBigInteger(2)
        return Packet(version, type, literal, null) to bitsConsumed
    } else {

        val lengthTypeID = next()
        bitsConsumed++

        if (lengthTypeID == '0') {
            //next 15 bits is total bit length of sub-packets
            val bitLength = buildString {
                repeat(15) {
                    append(next())
                }
            }.toInt(2)

            bitsConsumed += 15

            //Parse packets until we've consumed the correct number of bits
            val packets = buildList {
                var packetBits = 0
                while (packetBits < bitLength) {
                    val (packet, bitCount) = this@parsePackets.parsePackets()
                    add(packet)
                    packetBits += bitCount
                }
                bitsConsumed += packetBits
            }
            return Packet(version, type, null, packets) to bitsConsumed
        } else {
            //next 11 bits is number of sub-packets
            val packetCount = buildString {
                repeat(11) {
                    append(next())
                }
            }.toInt(2)
            bitsConsumed += 11

            val packets = buildList {
                repeat(packetCount) {
                    val (packet, bitCount) = this@parsePackets.parsePackets()
                    add(packet)
                    bitsConsumed += bitCount
                }
            }
            return Packet(version, type, null, packets) to bitsConsumed
        }
    }
}

/**
 * Given a string of hexadecimal digits, produce an iterator over the binary representation of those digits, treating each character as 4 bits
 * e.g "1A" would produce an iterator over the set ['0','0','0','1','1','0','1','0']
 */
private fun parseHexString(s: String): Iterator<Char> =
    s.flatMap {
        it.digitToInt(16).toString(2).padStart(4, '0').toCharArray().asIterable()
    }.iterator()

fun main() {

    fun part1(input: String): Int {
        val (packet) = parseHexString(input).parsePackets()
        return packet.sumVersions()
    }

    fun part2(input: String): BigInteger {
        val (packet) = parseHexString(input).parsePackets()
        return packet.value()
    }

    //test packets
    checkResult(parseHexString("D2FE28").parsePackets(), Pair(Packet(6, 4, 2021.toBigInteger(), null), 21))

    checkResult(
        parseHexString("38006F45291200").parsePackets(), Pair(
            Packet(
                1, 6, null, listOf(
                    Packet(6, 4, 10.toBigInteger(), null),
                    Packet(2, 4, 20.toBigInteger(), null),
                )
            ), 49
        )
    )

    checkResult(
        parseHexString("EE00D40C823060").parsePackets(), Packet(
            7, 3, null, listOf(
                Packet(2, 4, 1.toBigInteger(), null),
                Packet(4, 4, 2.toBigInteger(), null),
                Packet(1, 4, 3.toBigInteger(), null),
            )
        ) to 51
    )

    //Test part 1
    readInput("day16/part1.test")
        .map { line -> line.split(",").let { it[0] to it[1].toInt() } }
        .forEach { checkResult(part1(it.first), it.second) }

    //Test part 2
    readInput("day16/part2.test")
        .map { line -> line.split(",").let { it[0] to it[1].toBigInteger() } }
        .forEach { checkResult(part2(it.first), it.second) }

    println("Tests passed")

    val input = readInputLine("day16/input")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
