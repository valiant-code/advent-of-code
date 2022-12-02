import util.ConsoleColor
import util.InputUtil
import util.TimeUtil
import util.colorize
import java.util.*

private fun main() {
    //pt 1 - 412
    //pt 2 - 978171
    TimeUtil.startClock(1, ::partOne)
    TimeUtil.startClock(2, ::partTwo)
}
//  0:      1:      2:      3:      4:
// aaaa    ....    aaaa    aaaa    ....
//b    c  .    c  .    c  .    c  b    c
//b    c  .    c  .    c  .    c  b    c
// ....    ....    dddd    dddd    dddd
//e    f  .    f  e    .  .    f  .    f
//e    f  .    f  e    .  .    f  .    f
// gggg    ....    gggg    gggg    ....
//
//  5:      6:      7:      8:      9:
// aaaa    aaaa    aaaa    aaaa    aaaa
//b    .  b    .  .    c  b    c  b    c
//b    .  b    .  .    c  b    c  b    c
// dddd    dddd    ....    dddd    dddd
//.    f  e    f  .    f  e    f  .    f
//.    f  e    f  .    f  e    f  .    f
// gggg    gggg    ....    gggg    gggg


data class SevenSegmentDigit(val segments: SortedSet<Char>, var decoded: Char? = null) {
    init {
        //2 segments = 1
        //3 segments = 7
        //4 segments = 4
        //5 segments = 2,5,3
        //6 segments = 0,6,9
        //7 segments = 8
        when (segments.size) {
            2 -> decoded = '1'
            3 -> decoded = '7'
            4 -> decoded = '4'
            7 -> decoded = '8'
        }
    }

    fun intersectionCount(other: SevenSegmentDigit): Int {
        return segments.intersect(other.segments).size
    }
}

class SegmentEntry(inputLine: String) {
    private val digitPermutations = inputLine.split(" | ")[0].split(" ").map { SevenSegmentDigit(it.toSortedSet()) }
    val outputValues = inputLine.split(" | ")[1].split(" ").map { SevenSegmentDigit(it.toSortedSet()) }

    init {
        decode2And3And5();
        decode0And6And9();
    }

    fun getOutputValue(): Long {
        return outputValues.map { it.decoded }.joinToString("").toLong()
    }

    private fun getDigit(num: Char): SevenSegmentDigit {
        return digitPermutations.find { it.decoded == num }!!
    }

    private fun decode2And3And5() {
        val five = digitPermutations.filter { it.segments.size == 5 }
            .filter { it.intersectionCount(getDigit('4')) == 3 } //5 shares three segments with 4
            .find { it.intersectionCount(getDigit('1')) == 1 }!! //5 shares one segments with 1

        val three = digitPermutations.filter { it.segments.size == 5 }
            .filter { it.intersectionCount(getDigit('4')) == 3 } //3 shares three segments with 4
            .find { it.intersectionCount(getDigit('1')) == 2 }!! //3 shares two segments with 1

        //2 is just the 5 segment value that's not 3 or 5
        val two = digitPermutations.filter { it.segments.size == 5 }.find { it != five && it != three }!!

        five.decoded = '5';
        three.decoded = '3';
        two.decoded = '2'
        outputValues.filter { it.decoded == null }.filter { it.segments == five.segments }.forEach { it.decoded = '5' }
        outputValues.filter { it.decoded == null }.filter { it.segments == three.segments }.forEach { it.decoded = '3' }
        outputValues.filter { it.decoded == null }.filter { it.segments == two.segments }.forEach { it.decoded = '2' }
    }

    private fun decode0And6And9() {
        val six = digitPermutations.filter { it.segments.size == 6 }
            .find { it.intersectionCount(getDigit('1')) == 1 }!! //6 only shares one segments with 1

        val nine = digitPermutations.filter { it.segments.size == 6 }
            .filter { it != six }
            .find { it.intersectionCount(getDigit('5')) == 5 }!! //9 shares five segments with 5 (and isn't 6)

        val zero = digitPermutations.filter { it.segments.size == 6 }
            .find { it != six && it != nine }!! //0 has 6 segments but isn't 6 or 9

        six.decoded = '6';
        nine.decoded = '9';
        zero.decoded = '0'
        outputValues.filter { it.decoded == null }.filter { it.segments == six.segments }.forEach { it.decoded = '6' }
        outputValues.filter { it.decoded == null }.filter { it.segments == nine.segments }.forEach { it.decoded = '9' }
        outputValues.filter { it.decoded == null }.filter { it.segments == zero.segments }.forEach { it.decoded = '0' }
    }
}

private fun partOne(pt: Int = 1) {
    //at what position can we make all the numbers meet up, with the least amount of moves
    val input = InputUtil.readFileAsStringList("2021/day8/input.txt").map(::SegmentEntry)

    //In the output values, how many times do digits 1, 4, 7, or 8 appear?
    val answer = input.flatMap { it.outputValues }.filter { it.decoded in arrayOf('1', '4', '7', '8') }.count()
    println("pt $pt answer: ${answer colorize ConsoleColor.CYAN_BOLD}")
}

private fun partTwo(pt: Int = 2) {
    //at what position can we make all the numbers meet up, with the least amount of moves
    val input = InputUtil.readFileAsStringList("2021/day8/input.txt").map(::SegmentEntry)

    //Decode the 4 digit output values (Done in objects), then sum them up
    val answer = input.map { it.getOutputValue() }.sum();
    println("pt $pt answer: ${answer colorize ConsoleColor.CYAN_BOLD}")
}


