import util.ConsoleColor
import util.InputUtil
import util.TimeUtil
import util.colorize
import kotlin.math.abs

private fun main() {
    //pt 1 - 337488
    //pt 2 - 89647695
    TimeUtil.startClock(1, ::partOne)
    TimeUtil.startClock(2, ::partTwo)
}

private fun partOne(pt: Int = 1) {
    //at what position can we make all the numbers meet up, with the least amount of moves
    val input = InputUtil.readFileAsIntList("2021/day7/input.txt", ",").sorted()

    var minMoves = Int.MAX_VALUE;
    var pos = Int.MAX_VALUE;
    positionLoop@ for (position in input[0]..input.last()) {
        var moves = 0;
        for (num in input) {
            moves += abs(position - num)
            if (moves > minMoves) break@positionLoop //because function is linear, we can break loop safely, otherwise continue
        }
        minMoves = moves;
        pos = position;
    }

    println("pt $pt answer: ${minMoves colorize ConsoleColor.CYAN_BOLD} moves to position $pos")
}

fun Long.nthPartialSum(): Long {
    //Triangle Numbers - https://en.wikipedia.org/wiki/1_%2B_2_%2B_3_%2B_4_%2B_%E2%8B%AF
    return (this * (this + 1)) / 2
}

private fun partTwo(pt: Int = 2) {
    //but now the energy it takes for 1 crab to move position by X is  1+2+3..+X
    val input = InputUtil.readFileAsLongList("2021/day7/input.txt", ",").sorted()

    var minMoves = Long.MAX_VALUE;
    var pos = Long.MAX_VALUE;
    //"fun" optimization might be to binary search instead of sequential..
    positionLoop@ for (position in input[0]..input.last()) {
        var moves = 0L;
        for (num in input) {
            moves += (abs(position - num)).nthPartialSum()
            if (moves > minMoves) break@positionLoop //because function is linear, we can break loop safely, otherwise continue
        }
        minMoves = moves;
        pos = position;
    }

    println("pt $pt answer: ${minMoves colorize ConsoleColor.CYAN_BOLD} moves to position $pos")
}


