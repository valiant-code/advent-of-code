package aoc2022

import util.ConsoleColor
import util.InputUtil
import util.TimeUtil
import util.colorize

private fun main() {
    //pt 1 - 8394
    //pt 2 - 2413
    TimeUtil.startClock(1, ::partOne)
    TimeUtil.startClock(2, ::partTwo)
}

                           // extra + at the start, so we can do indexOf without + 1
private const val alphabet = "+abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

private fun partOne(pt: Int = 1) {
    val input = InputUtil.readFileAsStringList("2022/day3/input.txt", "\n").map {
        it.substring(0, it.length / 2).toCharArray().toSet() to
                it.substring(it.length / 2, it.length).toCharArray().toSet()
    }

    val answer = input.sumOf { l -> alphabet.indexOf(l.first.intersect(l.second).first()) };
    println("pt $pt answer: ${answer colorize ConsoleColor.PURPLE_BRIGHT}")
}

private fun partTwo(pt: Int = 2) {
    val input = InputUtil.readFileAsStringList("2022/day3/input.txt", "\n")
        .map { it.toCharArray().toSet() }
        .windowed(3, 3)

    val answer = input.sumOf { group ->
        alphabet.indexOf(group[0].intersect(group[1].intersect(group[2])).first())
    }
    println("pt $pt answer: ${answer colorize ConsoleColor.PURPLE_BRIGHT}")
}