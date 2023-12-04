package aoc2023

import util.*

private fun main() {
    //pt 1 answer: 535078
    //Pt 1 Time Taken: 0.04
    //pt 2 answer: 75312571
    //Pt 2 Time Taken: 0.055
    TimeUtil.startClock(1, ::partOne)
    TimeUtil.startClock(2, ::partTwo)
}

private class PartNumber(val value: Int, val startCoord: Pair<Int, Int>) {
    val border = mutableListOf<Pair<Int, Int>>();
    private val length = value.toString().length;

    init {
        //given a starting coordinate and the length, we can figure out all possible border coordinates
        border.add(startCoord.copy(second = startCoord.second - 1))
        border.add(startCoord.copy(second = startCoord.second + length))
        IntRange(startCoord.second - 1, startCoord.second + length)
            .forEach { x ->
                border.add(startCoord.first - 1 to x)
                border.add(startCoord.first + 1 to x)
            }
    }
}

private fun partOne(pt: Int = 1) {
    val input = InputUtil.readFileAsStringList("2023/day3input.txt");
    val numRegex = Regex("\\d+")
    val symbolsRegex = Regex("[!@#$%^&*()+\\-=_/<>,`~|]")
    val possibleParts = mutableListOf<PartNumber>()
    val symbolsMap = mutableMapOf<Pair<Int, Int>, String>()

    input.forEachIndexed { lineNum, line ->
        possibleParts.addAll(numRegex.findAll(line).map {
            PartNumber(it.value.toInt(), lineNum to it.range.first)
        })
        symbolsRegex.findAll(line).forEach {
            symbolsMap[lineNum to it.range.first] = it.value
        }
    }
    val validParts = possibleParts.filter { part ->
        part.border.any { coord -> symbolsMap.containsKey(coord)}
    }

    //What is the sum of all the part numbers in the engine schematic?
    val answer = validParts.sumOf { it.value }
    println("pt $pt answer: ${answer colorize ConsoleColor.BLUE_BRIGHT}")
}


private fun partTwo(pt: Int = 2) {
    val input = InputUtil.readFileAsStringList("2023/day3input.txt");
    val numRegex = Regex("\\d+")
    //for this part we only need to look for * symbols
    val symbolRegex = Regex("\\*")
    val possibleParts = mutableListOf<PartNumber>()
    val symbolsMap = mutableMapOf<Pair<Int, Int>, String>()
    input.forEachIndexed { lineNum, line ->
        // we will make a list of all the "parts"
        possibleParts.addAll(numRegex.findAll(line).map {
            PartNumber(it.value.toInt(), lineNum to it.range.first)
        })
        // we will also make a map of the symbols
        symbolRegex.findAll(line).forEach {
            symbolsMap[lineNum to it.range.first] = it.value
        }
    }
    val gearRatios = symbolsMap.keys.mapNotNull { asterisk ->
        //A gear is any * symbol that is adjacent to EXACTLY two part numbers.
        val adjacentParts = possibleParts.filter { part -> part.border.any { it == asterisk } }
        if (adjacentParts.size == 2) {
            // Its gear ratio is the result of multiplying those two numbers together.
            adjacentParts[0].value * adjacentParts[1].value
        } else null
    }

    //What is the sum of all the gear ratios in your engine schematic?
    val answer = gearRatios.sum();
    println("pt $pt answer: ${answer colorize ConsoleColor.BLUE_BRIGHT}")
}