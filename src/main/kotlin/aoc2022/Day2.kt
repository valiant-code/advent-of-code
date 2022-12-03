package aoc2022

import util.ConsoleColor
import util.InputUtil
import util.TimeUtil
import util.colorize

private fun main() {
    //pt 1 - 12855
    //pt 2 - 13726
    TimeUtil.startClock(1, ::partOne)
    TimeUtil.startClock(2, ::partTwo)
}

private fun Char.pt1Translate(): Char {
    return if (this == 'X') 'A'
    else if (this == 'Y') 'B'
    else if (this == 'Z') 'C'
    else throw Exception("bad code")
}

private fun Char.pt2Translate(opp: Char): Char {
    // Y means you need to end the round in a draw
    if (this == 'Y') {
        return opp
    }

    // X means you need to lose,
    return if (this == 'X') {
        when (opp) {
            'A' -> 'C'
            'B' -> 'A'
            'C' -> 'B'
            else -> throw Exception("bad code")
        }
    } else {
        // Z means you need to win
        when (opp) {
            'A' -> 'B'
            'B' -> 'C'
            'C' -> 'A'
            else -> throw Exception("bad code")
        }
    }
}

private val gameMap = mapOf(
    ('A' to 'A') to 3,
    ('A' to 'B') to 0,
    ('A' to 'C') to 6,

    ('B' to 'A') to 6,
    ('B' to 'B') to 3,
    ('B' to 'C') to 0,

    ('C' to 'A') to 0,
    ('C' to 'B') to 6,
    ('C' to 'C') to 3,
)

private fun playGame(player: Char, opp: Char): Int {
    return gameMap.getOrDefault(player to opp, -989898) + (player.code - 64)
}

private fun partOne(pt: Int = 1) {
    // A,X = Rock, B,Y = Paper, C,Z = Scissors
    // What would your total score be if everything goes exactly according to your strategy guide?
    val input = InputUtil.readFileAsStringList("2022/day2/input.txt", "\n").map {
        it[0] to it[2].pt1Translate()
    }

    val answer = input.sumOf { playGame(it.second, it.first) };
    println("pt $pt answer: ${answer colorize ConsoleColor.PURPLE_BRIGHT}")
}

private fun partTwo(pt: Int = 2) {
    //the second column says how the round needs to end:
    // X means you need to lose, Y means you need to end the round in a draw, and Z means you need to win
    val input = InputUtil.readFileAsStringList("2022/day2/input.txt", "\n").map {
        it[0] to it[2].pt2Translate(it[0])
    }

    val answer = input.sumOf { playGame(it.second, it.first) };
    println("pt $pt answer: ${answer colorize ConsoleColor.PURPLE_BRIGHT}")
}