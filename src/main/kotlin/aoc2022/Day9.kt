package aoc2022

import util.*
import kotlin.math.absoluteValue

private fun main() {
    //pt 1 - 6745
    //pt 2 - 2793
    TimeUtil.startClock(1, ::partOne)
    TimeUtil.startClock(2, ::partTwo)
}

private class RopeKnot(var x: Int, var y: Int, var parent: RopeKnot?) {

    fun moveDir(dir: Char) {
        when (dir) {
            'U' -> {
                y += 1
            }
            'D' -> {
                y -= 1
            }
            'R' -> {
                x += 1
            }
            'L' -> {
                x -= 1
            }
        }
    }

    fun moveIfNecessary() {
        val head = parent!!
        //If the head is ever two steps directly up, down, left, or right from the tail,
        // the tail must also move one step in that direction, so it remains close enough
        val xDiff = (head.x - this.x).absoluteValue >= 2
        val yDiff = (head.y - this.y).absoluteValue >= 2

        // if its diagonal, move in a diagonal as well
        val moveX = xDiff || (yDiff && this.x != head.x)
        val moveY = yDiff || (xDiff && this.y != head.y)

        if (moveX) {
            x += if (head.x > this.x) 1 else -1
        }
        if (moveY) {
            y += if (head.y > this.y) 1 else -1
        }
    }

}


private fun partOne(pt: Int = 1) {
    val input = InputUtil.readFileAsStringList("2022/day9/input.txt")
    val head = RopeKnot(0, 0, null)
    val tail = RopeKnot(0, 0, head)
    val tailSet = mutableSetOf(tail.x to tail.y)


    input.forEach { line ->
        val dir = line.first()
        val amount = line.findAllNumbers().first()

        for (i in 1..amount) {
            head.moveDir(dir)
            tail.moveIfNecessary()
            // Set will enforce no duplicates, so just try every time
            tailSet.add(tail.x to tail.y)
        }
    }

    val answer = tailSet.size
    println("pt $pt answer: ${answer colorize ConsoleColor.PURPLE_BRIGHT}")
}


private fun partTwo(pt: Int = 2) {
    //but what if the rope was 10 knots long
    val input = InputUtil.readFileAsStringList("2022/day9/input.txt")
    val head = RopeKnot(0, 0, null)
    var prevKnot = head
    val knotList = mutableListOf<RopeKnot>()
    for (i in 1 .. 9) {
        val knot = RopeKnot(0, 0, prevKnot)
        knotList.add(knot)
        prevKnot = knot
    }
    val tail = knotList.last()
    val tailSet = mutableSetOf(tail.x to tail.y)


    input.forEach { line ->
        val dir = line.first()
        val amount = line.findAllNumbers().first()

        head.moveDir(dir)

        for (i in 1..amount) {
            knotList.forEach(RopeKnot::moveIfNecessary)
            // Set will enforce no duplicates, so just try every time
            tailSet.add(tail.x to tail.y)
        }
    }

    val answer = tailSet.size
    println("pt $pt answer: ${answer colorize ConsoleColor.PURPLE_BRIGHT}")
}