package aoc2022

import util.*

private fun main() {
    //pt 1 - 1787
    //pt 2 - 440640
    TimeUtil.startClock(1, ::partOne)
    TimeUtil.startClock(2, ::partTwo)
}

//30373
//25512
//65332
//33549
//35390

private class TreeGridPoint(val size: Int, val r: Int, val c: Int) {
    var visible: Boolean? = null

    fun pointsLeft(): List<Pair<Int, Int>> {
        return (c - 1 downTo 0).toList().map { r to it }
    }

    fun pointsRight(maxCol: Int): List<Pair<Int, Int>> {
        return (c + 1..maxCol).toList().map { r to it }
    }

    fun pointsAbove(): List<Pair<Int, Int>> {
        return (r - 1 downTo 0).toList().map { it to c }
    }

    fun pointsBelow(maxRow: Int): List<Pair<Int, Int>> {
        return (r + 1..maxRow).toList().map { it to c }
    }
}

private fun partOne(pt: Int = 1) {
    //A tree is visible if all the other trees between it and an edge of the grid are shorter than it
    val input = InputUtil.readFileAsStringList("2022/day8/input.txt")
        .map { it.toCharArray().map(Char::digitToInt) };
    val gridMap = input.flatMapIndexed { indexR, rows ->
        rows.mapIndexed { indexC, point ->
            (indexR to indexC) to TreeGridPoint(point, indexR, indexC)
        }
    }.toMap()

    val lastRowIndex = input.size - 1
    val lastColIndex = input[0].size - 1

    //how many trees are visible from outside the grid?

    gridMap.values.forEach { point ->
        point.visible = point.pointsAbove().none { other -> gridMap[other]!!.size >= point.size } ||
                point.pointsLeft().none { other -> gridMap[other]!!.size >= point.size } ||
                point.pointsRight(lastColIndex).none { other -> gridMap[other]!!.size >= point.size } ||
                point.pointsBelow(lastRowIndex).none { other -> gridMap[other]!!.size >= point.size }
    }


    val answer = gridMap.values.count { it.visible != null && it.visible!! }
    println("pt $pt answer: ${answer colorize ConsoleColor.PURPLE_BRIGHT}")
}

private fun partTwo(pt: Int = 2) {
    //A tree is visible if all the other trees between it and an edge of the grid are shorter than it
    val input = InputUtil.readFileAsStringList("2022/day8/input.txt")
        .map { it.toCharArray().map(Char::digitToInt) };
    val gridMap = input.flatMapIndexed { indexR, rows ->
        rows.mapIndexed { indexC, point ->
            (indexR to indexC) to TreeGridPoint(point, indexR, indexC)
        }
    }.toMap()

    val lastRowIndex = input.size - 1
    val lastColIndex = input[0].size - 1

    //To measure the viewing distance from a given tree, look up, down, left, and right from that tree;
    // stop if you reach an edge or at the first tree that is the same height or taller
    //A tree's scenic score is found by multiplying together its viewing distance in each of the four directions

    val answer = gridMap.values.map { point ->
        val above = point.pointsAbove()
            .map { gridMap[it]!! }
            .takeWhileInclusive { point.size > it.size }
            .count()

        val below = point.pointsBelow(lastRowIndex)
            .map { gridMap[it]!! }
            .takeWhileInclusive { point.size > it.size }
            .count()

        val left = point.pointsLeft()
            .map { gridMap[it]!! }
            .takeWhileInclusive { point.size > it.size }
            .count()

        val right = point.pointsRight(lastColIndex)
            .map { gridMap[it]!! }
            .takeWhileInclusive { point.size > it.size }
            .count()

        above * below * left * right
    }.maxOrNull()


    println("pt $pt answer: ${answer colorize ConsoleColor.PURPLE_BRIGHT}")
}