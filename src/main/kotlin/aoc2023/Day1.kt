package aoc2023

import util.*

private fun main() {
    //pt 1 - 54968
    //pt 2 - 54094
    TimeUtil.startClock(1, ::partOne)
    TimeUtil.startClock(2, ::partTwo)
}

private fun partOne(pt: Int = 1) {
    val input = InputUtil.readFileAsStringList("2023/day1/input.txt");
    val numberLists = input.map { line ->
        line.findAll("\\d")
    }

    //On each line, the calibration value can be found by combining
    // the first digit and the last digit (in that order)
    // to form a single two-digit number.
    val calibrationValues = numberLists.map { numbers ->
        numbers.first() + numbers.last()
    }.map(String::toInt)

    //What is the sum of all of the calibration values?
    val answer = calibrationValues.sum();
    println("pt $pt answer: ${answer colorize ConsoleColor.BLUE_BRIGHT}")
}

fun parseNum(str: String): Int {
    return when (str) {
        "one" -> 1
        "two" -> 2
        "three" -> 3
        "four" -> 4
        "five" -> 5
        "six" -> 6
        "seven" -> 7
        "eight" -> 8
        "nine" -> 9
        else -> str.toInt()
    }
}

private fun partTwo(pt: Int = 2) {
    // Some digits are actually spelled out with letters:
    // one, two, three, four, five, six, seven, eight, and nine also count as valid "digits".
    val numPattern = Regex("\\d|one|two|three|four|five|six|seven|eight|nine");
    val numPatternBackwards = Regex("enin|thgie|neves|xis|evif|ruof|eerht|owt|eno|\\d");
    val input = InputUtil.readFileAsStringList("2023/day1/input.txt");
    val numberLists = input.map { line ->
        listOf(
            parseNum(numPattern.find(line)!!.value),
            parseNum(numPatternBackwards.find(line.reversed())!!.value.reversed())
        )
    }

//    On each line, the calibration value can be found by combining
//     the first digit and the last digit (in that order)
//     to form a single two-digit number.
    val calibrationValues = numberLists.map { numbers ->
        numbers.first().toString() + numbers.last().toString()
    }.map(String::toInt)

    //What is the sum of all of the calibration values?
    val answer = calibrationValues.sum();
    println("pt $pt answer: ${answer colorize ConsoleColor.BLUE_BRIGHT}")
}