package aoc2022

import util.ConsoleColor
import util.InputUtil
import util.TimeUtil
import util.colorize

private fun main() {
    //pt 1 -
    //pt 2 -
    TimeUtil.startClock(1, ::partOne)
    TimeUtil.startClock(2, ::partTwo)
}

class Assignment(val min: Int, val max: Int);

private fun partOne(pt: Int = 1) {
    //In how many assignment pairs does one range fully contain the other?
    val input = InputUtil.readFileAsStringList("2022/day4/input.txt", "\n")
        .map { line -> line.split(",")
            .map { it.split("-")[0].toInt()..it.split("-")[1].toInt() }
        }.map { it[0] to it[1] }

    val answer = input.count {
        it.first.toSet().containsAll(it.second.toSet())
    }

    println("pt $pt answer: ${answer colorize ConsoleColor.PURPLE_BRIGHT}")
}

private fun partTwo(pt: Int = 2) {
    //In how many assignment pairs does the two ranges intersect at all?
    val input = InputUtil.readFileAsStringList("2022/day4/input.txt", "\n")
        .map { line -> line.split(",")
            .map { it.split("-")[0].toInt()..it.split("-")[1].toInt() }
        }.map { it[0] to it[1] }

    val answer = input.count {
        it.first.intersect(it.second).any()
    }

    println("pt $pt answer: ${answer colorize ConsoleColor.PURPLE_BRIGHT}")
}