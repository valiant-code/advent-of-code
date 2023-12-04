package aoc2023

import util.*
import kotlin.math.pow

private fun main() {
    //pt 1 answer: 27454.0
    //Pt 1 Time Taken: 0.043
    //pt 2 answer: 6857330
    //Pt 2 Time Taken: 0.008
    TimeUtil.startClock(1, ::partOne)
    TimeUtil.startClock(2, ::partTwo)
}

private fun partOne(pt: Int = 1) {
    // winning numbers | your numbers
    //Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
    //Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
    val input = InputUtil.readFileAsStringList("2023/day4input.txt");

    val cards = input.map { line ->
        val cardParts = line.split(" | ")
        val winners = cardParts[0].findAllNumbers().drop(1);
        val numbers = cardParts[1].findAllNumbers()
        winners to numbers
    }
    val worth = cards.map {
        val matches = it.first.intersect(it.second.toSet())
        //The first match makes the card worth one point &
        // each match after the first doubles the point value of that card
        if (matches.isNotEmpty()) (2.0.pow(matches.size) / 2)
        else 0.0
    }

    //How many points are they worth in total?
    val answer = worth.sum()
    println("pt $pt answer: ${answer colorize ConsoleColor.BLUE_BRIGHT}")
}


private fun partTwo(pt: Int = 2) {
    val input = InputUtil.readFileAsStringList("2023/day4input.txt");

    // start off with a map of all { cardNums to 1 }
    val cardCountMap = input.indices.associateWith { 1 }.toMutableMap();

    val matchesList = input.map { line ->
        val cardParts = line.split(" | ")
        val winners = cardParts[0].findAllNumbers().drop(1);
        val numbers = cardParts[1].findAllNumbers()
        winners.intersect(numbers.toSet()).size
    }

    //you win copies of the scratchcards below the winning card equal to the number of matches
    matchesList.forEachIndexed { currIndex, matchCount ->
        //for the currentCard, get the count of how many we have
        val currentCardCount = cardCountMap[currIndex]!!
        for (i in 1 .. matchCount) {
            //for x matches, add the currentCardCount to the x scratchcards below the current one
            cardCountMap.merge(currIndex + i, currentCardCount, Int::plus)
        }
    }

    //how many total scratchcards do you end up with?
    val answer = cardCountMap.values.sum();
    println("pt $pt answer: ${answer colorize ConsoleColor.BLUE_BRIGHT}")
}