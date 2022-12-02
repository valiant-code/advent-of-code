import util.ConsoleColor
import util.InputUtil
import util.TimeUtil
import util.colorize
import java.util.*

private fun main() {
    //pt 1 - 986
    //pt 2 - 18234816469452
    TimeUtil.startClock(1, ::partOne)
    TimeUtil.startClock(2, ::partTwo)
}

fun <K, V> Map<K, V>.getKey(value: V) = entries.firstOrNull { it.value == value }?.key
fun <A> Queue<A>.removeX(x: Int): Queue<A> {
    val returnQueue: Queue<A> = LinkedList()
    for (i in 1..x) {
        returnQueue.add(this.remove())
    }
    return returnQueue;
}

private val hexMap = mapOf(
    "0" to "0000",
    "1" to "0001",
    "2" to "0010",
    "3" to "0011",
    "4" to "0100",
    "5" to "0101",
    "6" to "0110",
    "7" to "0111",
    "8" to "1000",
    "9" to "1001",
    "A" to "1010",
    "B" to "1011",
    "C" to "1100",
    "D" to "1101",
    "E" to "1110",
    "F" to "1111",
)

private class Packet(
    val version: Int,
    val typeId: Int,
    val value: Long,
    var subPackets: MutableList<Packet> = mutableListOf(),
) {
    fun getVersionSum(): Int {
        return version + subPackets.sumOf { it.getVersionSum() }
    }

    override fun toString(): String {
        return "Packet(version=$version, typeId=$typeId, value=$value, subPackets=${subPackets.size})"
    }

    fun calcValue(): Long = when (typeId) {
        0 -> subPackets.sumOf { it.calcValue() }
        1 -> subPackets.map { it.calcValue() }.reduce(Long::times)
        2 -> subPackets.minOf { it.calcValue() }
        3 -> subPackets.maxOf { it.calcValue() }
        4 -> value
        5 -> if (subPackets[0].calcValue() > subPackets[1].calcValue()) 1 else 0
        6 -> if (subPackets[0].calcValue() < subPackets[1].calcValue()) 1 else 0
        7 -> if (subPackets[0].calcValue() == subPackets[1].calcValue()) 1 else 0
        else -> -9999

    }
}
//Packets with type ID 0 are sum packets - their value is the sum of the values of their sub-packets. If they only have a single sub-packet, their value is the value of the sub-packet.
//Packets with type ID 1 are product packets - their value is the result of multiplying together the values of their sub-packets. If they only have a single sub-packet, their value is the value of the sub-packet.
//Packets with type ID 2 are minimum packets - their value is the minimum of the values of their sub-packets.
//Packets with type ID 3 are maximum packets - their value is the maximum of the values of their sub-packets.
//Packets with type ID 5 are greater than packets - their value is 1 if the value of the first sub-packet is greater than the value of the second sub-packet; otherwise, their value is 0. These packets always have exactly two sub-packets.
//Packets with type ID 6 are less than packets - their value is 1 if the value of the first sub-packet is less than the value of the second sub-packet; otherwise, their value is 0. These packets always have exactly two sub-packets.
//Packets with type ID 7 are equal to packets - their value is 1 if the value of the first sub-packet is equal to the value of the second sub-packet; otherwise, their value is 0. These packets always have exactly two sub-packets.

private fun processPacket(bits: Queue<Char>): Packet {
    //the first three bits encode the packet version
    //the next three bits encode the packet type ID
    val version = hexMap.getKey("0${bits.remove()}${bits.remove()}${bits.remove()}")!!.toInt()
    val typeId = hexMap.getKey("0${bits.remove()}${bits.remove()}${bits.remove()}")!!.toInt()
    val value: Long

    when (typeId) {
        //Packets with type ID 4 represent a literal value
        4 -> {
            //Literal value packets encode a single binary number.
            // To do this, the binary number is padded with leading zeroes until its length is a multiple of four bits,
            // and then it is broken into groups of four bits. Each group is prefixed by a 1 bit except the last group,
            // which is prefixed by a 0 bit
            val valueBlocks = mutableListOf<Char>()
            var readingValues = true;
            while (readingValues) {
                if (bits.remove() == '0') readingValues = false;
                valueBlocks.addAll(bits.removeX(4))
            }
            value = valueBlocks.joinToString("").toLong(2)
            return Packet(version, typeId, value)
        }
        else -> {
            //Every other type of packet (any packet with a type ID other than 4) represent an operator
            val packet = Packet(version, typeId, -2)
            val lengthTypeId = bits.remove()
            if (lengthTypeId == '0') {
                //If the length type ID is 0, then the next 15 bits are a number that represents
                // the total length in bits of the sub-packets contained by this packet.
                val length = bits.removeX(15).joinToString("").replaceBefore("1", "").toInt(2)
                val targetLength = bits.size - length;
                while (bits.size > targetLength) {
                    val res = processPacket(bits)
                    packet.subPackets.add(res)
                }
            } else {
                //If the length type ID is 1, then the next 11 bits are a number that represents
                // the number of sub-packets immediately contained by this packet.
                val length = bits.removeX(11).joinToString("").replaceBefore("1", "").toInt(2)
                for (i in 1..length) {
                    val res = processPacket(bits)
                    packet.subPackets.add(res)
                }
            }
            return packet
        }
    }

}

private fun partOne(pt: Int = 1) {
    val input = InputUtil.readFileAsStringList("2021/day16/input.txt", "").filterNot { it.isEmpty() }
    val bits = input.flatMap { hexMap[it]!!.toList() }
    val bitsQueue: Queue<Char> = LinkedList(bits)

    val packet = processPacket(bitsQueue)

    val answer = packet.getVersionSum()
    println("pt $pt answer: ${answer colorize ConsoleColor.CYAN_BOLD}")
}

private fun partTwo(pt: Int = 2) {
    val input = InputUtil.readFileAsStringList("2021/day16/input.txt", "").filterNot { it.isEmpty() }
    val bits = input.flatMap { hexMap[it]!!.toList() }
    val bitsQueue: Queue<Char> = LinkedList(bits)

    val packet = processPacket(bitsQueue)

    val answer = packet.calcValue()
    println("pt $pt answer: ${answer colorize ConsoleColor.CYAN_BOLD}")
}
