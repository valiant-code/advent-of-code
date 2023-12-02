package aoc2023

import util.*
import kotlin.math.max

private fun main() {
    //pt 1 -
    //pt 2 -
    TimeUtil.startClock(1, ::partOne)
    TimeUtil.startClock(2, ::partTwo)
}

private fun partOne(pt: Int = 1) {
    //Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
    //Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
    val input = InputUtil.readFileAsStringList("2023/day2/input.txt");
    val games = input.map { line ->
        line.split(Regex(": |; "))
            .filterNot(String::isEmpty)
    }

    val gameList = mutableListOf<String>()
    games.forEach { g ->
        val maxMap = mutableMapOf<String, Int>()
        g.drop(1).forEach {
            it.split(", ").forEach {
                val dice = it.split(" ");
                maxMap.merge(dice[1], dice[0].toInt(), ::max)
            }
        }
        //Determine which games would have been possible if the bag had been loaded with only
        // 12 red cubes, 13 green cubes, and 14 blue cubes.
        if (maxMap.getOrDefault("red", 0) <= 12 &&
            maxMap.getOrDefault("green", 0) <= 13 &&
            maxMap.getOrDefault("blue", 0) <= 14) {
            gameList.add(g[0])
        }
    }

    // What is the sum of the IDs of those games?
    val answer = gameList.flatMap { it.findAllNumbers() }.sum()
    println("pt $pt answer: ${answer colorize ConsoleColor.BLUE_BRIGHT}")
}


private fun partTwo(pt: Int = 2) {
    //Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
    //Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
    val input = InputUtil.readFileAsStringList("2023/day2/input.txt");
    val games = input.map { line ->
        line.split(Regex(": |; "))
            .filterNot(String::isEmpty)
    }

    val powerList = mutableListOf<Double>()
    games.forEach { g ->
        val diceNeededMap = mutableMapOf<String, Double>()
        g.drop(1).forEach {
            it.split(", ").forEach {
                val dice = it.split(" ");
                diceNeededMap.merge(dice[1], dice[0].toDouble(), ::max)
            }
        }
        //The power of a set of cubes is equal to the numbers of red, green, and blue cubes multiplied together.
        val redNeeded = diceNeededMap.getOrDefault("red", 0.0);
        val blueNeeded = diceNeededMap.getOrDefault("blue", 0.0);
        val greenNeeded = diceNeededMap.getOrDefault("green", 0.0);
        powerList.add(redNeeded * blueNeeded * greenNeeded)
    }

    // What is the sum of the power of these sets?
    val answer = powerList.sum();
    println("pt $pt answer: ${answer colorize ConsoleColor.BLUE_BRIGHT}")
}