package aoc2023

import util.*
import kotlin.math.max

private fun main() {
//pt 1 answer: 2239
//Pt 1 Time Taken: 0.03
//pt 2 answer: 83435
//Pt 2 Time Taken: 0.004
    TimeUtil.startClock(1, ::partOne)
    TimeUtil.startClock(2, ::partTwo)
}

private fun findRequiredDice(line: String, color: String): Int {
    return Regex("\\d+(?= $color)").findAll(line).toList().maxOfOrNull { it.value.toInt() } ?: 0
}


private fun partOne(pt: Int = 1) {
    //Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
    //Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
    val input = InputUtil.readFileAsStringList("2023/day2input.txt");


    val possibleGames = input.mapNotNull { line ->
        //Determine which games would have been possible if the bag had been loaded with only
        // 12 red cubes, 13 green cubes, and 14 blue cubes.
        if (findRequiredDice(line, "red") <= 12 &&
            findRequiredDice(line, "green") <= 13 &&
            findRequiredDice(line, "blue") <= 14
        ) {
            //game is possible, grab the game # which is the first num in the line
            Regex("\\d+").find(line)!!.value.toInt()
        } else {
            null //not possible so use null to drop this game from our mapping
        }
    }

    // What is the sum of the IDs of those games?
    val answer = possibleGames.sum()
    println("pt $pt answer: ${answer colorize ConsoleColor.BLUE_BRIGHT}")
}


private fun partTwo(pt: Int = 2) {
    //Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
    //Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
    val input = InputUtil.readFileAsStringList("2023/day2input.txt");

    //fewest number of cubes of each color
    val powerValues = input.map { line ->
        //The power of a set of cubes is equal to the numbers of red, green, and blue cubes multiplied together
        val redNeeded = findRequiredDice(line, "red")
        val greenNeeded = findRequiredDice(line, "green")
        val blueNeeded = findRequiredDice(line, "blue")
        redNeeded * greenNeeded * blueNeeded
    }

    // What is the sum of the power of these sets?
    val answer = powerValues.sum();
    println("pt $pt answer: ${answer colorize ConsoleColor.BLUE_BRIGHT}")
}