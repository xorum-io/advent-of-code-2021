import java.lang.IllegalStateException

sealed class Packet {

    abstract val version: Int
    abstract val type: Int
    abstract val value: Long

    data class Operator(
        override val version: Int,
        override val type: Int,
        val subPackets: List<Packet>
    ) : Packet() {

        override val value: Long
            get() = when (type) {
                0 -> subPackets.sumOf { it.value }
                1 -> subPackets.map { it.value }.reduce { acc, value -> acc * value }
                2 -> subPackets.minOf { it.value }
                3 -> subPackets.maxOf { it.value }
                5 -> if (subPackets.first().value > subPackets.last().value) 1 else 0
                6 -> if (subPackets.first().value < subPackets.last().value) 1 else 0
                7 -> if (subPackets.first().value == subPackets.last().value) 1 else 0
                else -> throw IllegalStateException("Unknown operator")
            }
    }

    data class Literal(
        override val version: Int,
        override val type: Int,
        override val value: Long
    ) : Packet()
}

fun List<String>.parse() = first()
    .map { it.digitToInt(16).toString(2).padStart(4, '0') }
    .joinToString("")
    .parsePackets()

fun String.parsePackets(maxSize: Int = Int.MAX_VALUE): Pair<String, List<Packet>> {
    var binaryString = this
    val packets = mutableListOf<Packet>()

    while (binaryString.any { it == '1' } && packets.size < maxSize) {
        val version = binaryString.substring(0..2).toInt(2)
        val type = binaryString.substring(3..5).toInt(2)

        binaryString = binaryString.substring(startIndex = 6)

        val (binaryStringLeft, packet) = when (type) {
            4 -> binaryString.parseLiteral(version, type)
            else -> binaryString.parseOperator(version, type)
        }

        binaryString = binaryStringLeft
        packets.add(packet)
    }

    return binaryString to packets
}

fun String.parseLiteral(
    version: Int,
    type: Int
): Pair<String, Packet> {
    val chunks = chunked(size = 5)
    val lastChunkIndex = chunks.indexOfFirst { it.first() == '0' }
    val literalChunks = chunks.subList(0, lastChunkIndex + 1)
    val literalBinaryString = literalChunks.joinToString(separator = "") { it.substring(1..4) }
    val literalValue = literalBinaryString.toLong(radix = 2)
    return substring(startIndex = literalChunks.size * 5) to Packet.Literal(version, type, literalValue)
}

fun String.parseOperator(
    version: Int,
    type: Int
): Pair<String, Packet> = if (first() == '0') {
    parseOperatorWithLength(version, type)
} else {
    parseOperatorWithCount(version, type)
}

fun String.parseOperatorWithLength(
    version: Int,
    type: Int
): Pair<String, Packet.Operator> {
    val subPacketsLength = substring(1..15).toInt(radix = 2)
    val (_, subPackets) = substring(16 until 16 + subPacketsLength).parsePackets()
    return substring(startIndex = 16 + subPacketsLength) to Packet.Operator(version, type, subPackets)
}

fun String.parseOperatorWithCount(
    version: Int,
    type: Int
): Pair<String, Packet.Operator> {
    val subPacketsCount = substring(1..11).toInt(radix = 2)
    val (binaryStringLeft, subPackets) = substring(startIndex = 12).parsePackets(maxSize = subPacketsCount)
    return binaryStringLeft to Packet.Operator(version, type, subPackets)
}

fun List<Packet>.versionSum(): Int = sumOf {
    when (it) {
        is Packet.Literal -> it.version
        is Packet.Operator -> it.version + it.subPackets.versionSum()
    }
}

fun main() {

    fun part1(
        input: List<String>
    ) = input.parse().let { (_, packets) -> packets.versionSum() }

    fun part2(
        input: List<String>
    ) = input.parse().let { (_, packets) -> packets.first().value }

    val input = readInput("Day16")
    println(part1(input))
    println(part2(input))
}
