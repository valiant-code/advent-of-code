package aoc2023

import util.*

private fun main() {
    //pt 1 answer: 781200
    //Pt 1 Time Taken: 0.029
    //pt 2 answer: 49240091
    //Pt 2 Time Taken: 0.0
    TimeUtil.startClock(1, ::partOne)
    TimeUtil.startClock(2, ::partTwo)
}

private class BoatRace(val time: Long, val distance: Long) {
    fun scenarioPossible(holdLength: Long): Boolean {
        return holdLength * (time - holdLength) > distance
    }
    fun findWinCount(): Long {
        val possibilyPairs = (1 until time)

        val lowerIndex = possibilyPairs.binarySearch {
            val firstValid = scenarioPossible(it)
            val secondValid = scenarioPossible(it+1)
            if (!firstValid && secondValid) 0 else if (firstValid) 1 else -1
        }
        val upperIndex = possibilyPairs.binarySearch {
            val firstValid = scenarioPossible(it)
            val secondValid = scenarioPossible(it + 1)
            if (firstValid && !secondValid) 0 else if (secondValid) -1 else 1
        }

        val lowerBound = lowerIndex + 1
        val upperBound = upperIndex
        return upperBound - lowerBound + 1
    }
}

private fun partOne(pt: Int = 1) {
    //Time:      7  15   30
    //Distance:  9  40  200
    val input = InputUtil.readFileAsStringList("2023/day6input.txt");
    val (times, distance) = input.map { it.findAllNumbersAsLong() }
    val races = times.mapIndexed { index, time -> BoatRace(time, distance[index]) }

    val wins = races.map { it.findWinCount() }


    val answer = wins.reduce(Long::times)
    println("pt $pt answer: ${answer colorize ConsoleColor.BLUE_BRIGHT}")
}


private fun partTwo(pt: Int = 2) {
    //Time:      7  15   30
    //Distance:  9  40  200
    val input = InputUtil.readFileAsStringList("2023/day6input.txt")
        .map { it.replace(" ", "") }
    val (times, distance) = input.map { it.findAllNumbersAsLong() }
    val races = times.mapIndexed { index, time -> BoatRace(time, distance[index]) }

    val wins = races.map { it.findWinCount() }


    val answer = wins.reduce(Long::times)
    println("pt $pt answer: ${answer colorize ConsoleColor.BLUE_BRIGHT}")

}