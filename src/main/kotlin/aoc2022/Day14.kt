package aoc2022

import util.ConsoleColor
import util.InputUtil
import util.TimeUtil
import util.colorize
import kotlin.math.max
import kotlin.math.min

private fun main() {
    //pt 1 - 578
    //pt 2 - 24377
    TimeUtil.startClock(1, ::partOne)
    TimeUtil.startClock(2, ::partTwo)
}

private class CaveSediment(val col: Int, val row: Int) {

    fun sandFall(caveMap: Map<Pair<Int, Int>, CaveSediment>): Int {
        return if (caveMap[col - 1 to row] == null) -1
        else if (caveMap[col + 1 to row] == null) +1
        else 0
    }

    fun pathTo(other: CaveSediment): List<CaveSediment> {
        if (col == other.col) {
            return (min(row, other.row)..max(row, other.row)).map { rowIterator ->
                CaveSediment(col, rowIterator)
            }
        } else {
            return (min(col, other.col)..max(col, other.col)).map { colIterator ->
                CaveSediment(colIterator, row)
            }
        }
    }

    fun getCoord() = col to row
    override fun toString(): String {
        return "$col,$row"
    }

}

private fun getNextRock(rockMap: Map<Pair<Int, Int>, CaveSediment>, col: Int, startRow: Int): CaveSediment? {
    return rockMap.values.filter { it.col == col }.filter { it.row > startRow }.minByOrNull { it.row }
}

private fun partOne(pt: Long = 1) {
    //503,4 -> 502,4 -> 502,9 -> 494,9
    val input = InputUtil.readFileAsStringList("2022/day14/input.txt", "\n")
        .map { line ->
            val rocks = line.split(" -> ").map { coordString ->
                val (c1, c2) = coordString.split(",")
                    .map { num -> num.toInt() }
                CaveSediment(c1, c2)
            }.windowed(2, 1, false).map { it[0] to it[1] }
            rocks
        }

    val map: MutableMap<Pair<Int, Int>, CaveSediment> = input.flatten()
        .flatMap { boundary -> boundary.first.pathTo(boundary.second) }
        .associateBy { it.getCoord() }.toMutableMap()

    var counter = 0;
    var nextRock = getNextRock(map, 500, Int.MIN_VALUE)
    first@ while (nextRock != null) {
        var checkLanding: Int = nextRock.sandFall(map)
        while (checkLanding != 0) {
            nextRock = getNextRock(map, nextRock!!.col + checkLanding, nextRock.row)
            if (nextRock == null) {
                break@first
            }
            checkLanding = nextRock.sandFall(map)
        }
        //"down" = a higher row value
        var newRock = CaveSediment(nextRock!!.col, nextRock.row - 1)
        map[newRock.getCoord()] = newRock
        counter++
        nextRock = getNextRock(map, 500, 0)
    }


    val answer = counter
    println("pt $pt answer: ${answer colorize ConsoleColor.PURPLE_BRIGHT}")
}

private fun partTwo(pt: Long = 2) {
    //503,4 -> 502,4 -> 502,9 -> 494,9
    val input = InputUtil.readFileAsStringList("2022/day14/input.txt", "\n")
        .map { line ->
            val rocks = line.split(" -> ").map { coordString ->
                val (c1, c2) = coordString.split(",")
                    .map { num -> num.toInt() }
                CaveSediment(c1, c2)
            }.windowed(2, 1, false).map { it[0] to it[1] }
            rocks
        }

    val map: MutableMap<Pair<Int, Int>, CaveSediment> = input.flatten()
        .flatMap { boundary -> boundary.first.pathTo(boundary.second) }
        .associateBy { it.getCoord() }.toMutableMap()

    //assume the floor is an infinite horizontal line with a y coordinate equal to
    // two plus the highest y coordinate of any point in your scan.
    val floorRow = map.values.map { it.row }.maxOf { it } + 2

    for (col in -200..1500) {
        map[col to floorRow] = CaveSediment(col, floorRow)
    }


    // TODO optimize somehow.. 87s runtime means there must be some better way
    var counter = 0;
    var nextRock = getNextRock(map, 500, 0)
    first@ while (map[500 to 0] == null && nextRock != null)  {
        var checkLanding: Int = nextRock!!.sandFall(map)
        while (checkLanding != 0) {
            nextRock = getNextRock(map, nextRock!!.col + checkLanding, nextRock.row)
            if (nextRock == null) {
                break@first
            }
            checkLanding = nextRock.sandFall(map)
        }
        var newRock = CaveSediment(nextRock!!.col, nextRock.row - 1)
        map[newRock.getCoord()] = newRock
        counter++
        nextRock = getNextRock(map, 500, 0)
    }

    assert(nextRock != null)


    val answer = counter
    println("pt $pt answer: ${answer colorize ConsoleColor.PURPLE_BRIGHT}")
}