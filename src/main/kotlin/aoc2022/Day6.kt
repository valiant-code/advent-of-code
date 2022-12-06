package aoc2022

import util.*

private fun main() {
    //pt 1 - 1892
    //pt 2 - 2313
    TimeUtil.startClock(1, ::partOne)
    TimeUtil.startClock(2, ::partTwo)
}


private fun partOne(pt: Int = 1) {
    val input = InputUtil.readFileAsString("2022/day6/input.txt")
    val answer = findEndIndexOfFirstMarker(input, 4);

    println("pt $pt answer: ${answer colorize ConsoleColor.PURPLE_BRIGHT}")
}

private fun partTwo(pt: Int = 2) {
    val input = InputUtil.readFileAsString("2022/day6/input.txt")
    val answer = findEndIndexOfFirstMarker(input, 14);

    println("pt $pt answer: ${answer colorize ConsoleColor.PURPLE_BRIGHT}")
}

private fun findEndIndexOfFirstMarker(input: String, markerLength: Int): Int {
    val markerStart = input.toCharArray().toList()
        .windowed(markerLength, 1, false)
        .indexOfFirst { window -> window.toSet().size == markerLength }

    return markerStart + markerLength
}