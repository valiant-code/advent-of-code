package aoc2022

import util.*

private fun main() {
    //pt 1 - WCZTHTMPS
    //pt 2 - BLSGJSDTS
    TimeUtil.startClock(1, ::partOne)
    TimeUtil.startClock(2, ::partTwo)
}

//    [D]     [J]
//[N] [C]     [Y]
//[Z] [M] [P] [T]
// 1   2   3   4
//
//move 1 from 2 to 1
//move 3 from 1 to 3

private fun partOne(pt: Int = 1) {
    val input = InputUtil.readFileAsStringList("2022/day5/input.txt", "\n\n")
    val stackInput = ArrayDeque(input[0].split("\n"))
    val instructions = input[1].split("\n")
    val numberOfStacks = stackInput.removeLast().split(Regex("\\W+")).last().toInt()
    val stacksMap: MutableMap<Int, ArrayDeque<Char>> = mutableMapOf()

    for (i in 1..numberOfStacks) {
        stacksMap[i] = ArrayDeque()
    }

    // parse the current map into ArrayDeque's
    stackInput.forEach { line ->
        var cursor = 1
        var counter = 0
        while (cursor < line.length) {
            counter++
            val nextChar = line[cursor]
            if (!nextChar.isWhitespace()) {
                stacksMap[counter]!!.addLast(nextChar)
            }
            cursor += 4
        }
    }

    instructions.map(String::findAllNumbers).forEach { instructionList ->
        val amount = instructionList[0]
        val from = stacksMap[instructionList[1]]!!
        val to = stacksMap[instructionList[2]]!!
        for (i in 1..amount) {
            to.addFirst(from.removeFirst())
        }
    }

    val answer = stacksMap.values.map { it.first() }.joinToString("")

    println("pt $pt answer: ${answer colorize ConsoleColor.PURPLE_BRIGHT}")
}

private fun partTwo(pt: Int = 2) {
    val input = InputUtil.readFileAsStringList("2022/day5/input.txt", "\n\n")
    val stackInput = ArrayDeque(input[0].split("\n"))
    val instructions = input[1].split("\n")
    val numberOfStacks = stackInput.removeLast().split(Regex("\\W+")).last().toInt()
    val stacksMap: MutableMap<Int, ArrayDeque<Char>> = mutableMapOf()

    for (i in 1..numberOfStacks) {
        stacksMap[i] = ArrayDeque()
    }

    // parse the current map into ArrayDeque's
    stackInput.forEach { line ->
        var cursor = 1
        var counter = 0
        while (cursor < line.length) {
            counter++
            val nextChar = line[cursor]
            if (!nextChar.isWhitespace()) {
                stacksMap[counter]!!.addLast(nextChar)
            }
            cursor += 4
        }
    }

    instructions.map(String::findAllNumbers).forEach { instructionList ->
        val amount = instructionList[0]
        val from = stacksMap[instructionList[1]]!!
        val to = stacksMap[instructionList[2]]!!
        for (i in amount downTo 1) {
            //remove them in reverse from the bottom of our stack up
            // So they retain the same order when put in the new stack
            to.addFirst(from.removeAt(i - 1))
        }
    }

    val answer = stacksMap.values.map { it.first() }.joinToString("")

    println("pt $pt answer: ${answer colorize ConsoleColor.PURPLE_BRIGHT}")
}