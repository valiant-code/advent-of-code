package aoc2022

import util.ConsoleColor
import util.InputUtil
import util.TimeUtil
import util.colorize

private fun main() {
    //pt 1 - 71934
    //pt 2 - 211447
    TimeUtil.startClock(1, ::partOne)
    TimeUtil.startClock(2, ::partTwo)
}

private fun partOne(pt: Int = 1) {
    // Find the Elf carrying the most Calories.
    // How many total Calories is that Elf carrying?
    val input = InputUtil.readFileAsStringList("2022/day1/input.txt", "\n\n")
        .map {
            // each string in the inputList is a set of numbers
            // split each set by newline sum them as ints, resulting in a list of sums
            it.split("\n").sumOf { n -> n.toInt() }
        }

    val answer = input.maxOrNull();
    println("pt $pt answer: ${answer colorize ConsoleColor.PURPLE_BRIGHT}")
}

private fun partTwo(pt: Int = 2) {
    // Find the top three Elves carrying the most Calories.
    // How many Calories are those Elves carrying in total?
    val input = InputUtil.readFileAsStringList("2022/day1/input.txt", "\n\n")
        .map {
            it.split("\n").sumOf { n -> n.toInt() }
        }

    //a loop would be faster than sorting but this is simple
    val answer = input.sortedDescending().subList(0, 3).sum()

    println("pt $pt answer: ${answer colorize ConsoleColor.PURPLE_BRIGHT}")
}