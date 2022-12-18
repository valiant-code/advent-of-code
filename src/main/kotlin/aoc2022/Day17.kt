package aoc2022

import util.*

private fun main() {
    //pt 1 - 3092
    //pt 2 -
    TimeUtil.startClock(1, ::partOne)
    TimeUtil.startClock(2, ::partTwo)
}

private abstract class OddRock() {
    abstract fun getPoints(): List<RockPoint>
    abstract fun getRightEndPoints(): List<RockPoint>
    abstract fun getLeftEndPoints(): List<RockPoint>
    abstract fun getBottomEndPoints(): List<RockPoint>
    abstract fun getTopEndPoints(): List<RockPoint>

    fun moveLeft(map: MutableMap<Pair<Long, Long>, Boolean>) {
        if (getLeftEndPoints().all { it.canMoveLeft(map) }) {
            getRightEndPoints().forEach { map.remove(it.coord()) }
            getPoints().forEach { it.moveLeft() }
            getLeftEndPoints().forEach { map[it.coord()] = true }
        }
    }

    fun moveRight(map: MutableMap<Pair<Long, Long>, Boolean>) {
        if (getRightEndPoints().all { it.canMoveRight(map) }) {
            getLeftEndPoints().forEach { map.remove(it.coord()) }
            getPoints().forEach { it.moveRight() }
            getRightEndPoints().forEach { map[it.coord()] = true }
        }
    }

    fun moveDown(map: MutableMap<Pair<Long, Long>, Boolean>): Boolean {
        return if (getBottomEndPoints().all { it.canMoveDown(map) }) {
            getTopEndPoints().forEach { map.remove(it.coord()) }
            getPoints().forEach { it.moveDown() }
            getBottomEndPoints().forEach { map[it.coord()] = true }
            true
        } else {
            false;
        }
    }
}

private class RockPoint(var x: Long, var y: Long, val isSolid: Boolean = true) {
    fun coord() = x to y
    fun moveRight() {
        x += 1
    }

    fun moveLeft() {
        x -= 1
    }

    fun moveDown() {
        y -= 1
    }

    fun canMoveLeft(map: Map<Pair<Long, Long>, Boolean>): Boolean {
        return x - 1 >= 0 && !map.getOrDefault(x - 1 to y, false)
    }

    fun canMoveRight(map: Map<Pair<Long, Long>, Boolean>): Boolean {
        return x + 1 <= 6 && !map.getOrDefault(x + 1 to y, false)
    }

    fun canMoveDown(map: Map<Pair<Long, Long>, Boolean>): Boolean {
        return !map.getOrDefault(x to y - 1, false)
    }
}

private class FlatRock(
    var p1: RockPoint,
    var p2: RockPoint,
    var p3: RockPoint,
    var p4: RockPoint
) : OddRock() {
    companion object {
        fun create(map: MutableMap<Pair<Long, Long>, Boolean>, leftX: Long, bottomY: Long): FlatRock {
            val newRock = FlatRock(
                RockPoint(leftX, bottomY),
                RockPoint(leftX + 1, bottomY),
                RockPoint(leftX + 2, bottomY),
                RockPoint(leftX + 3, bottomY)
            )
            newRock.getPoints().forEach { map[it.coord()] = true }
            return newRock
        }
    }

    //|..@@@@.|
    override fun getPoints(): List<RockPoint> {
        return listOf(p1, p2, p3, p4)
    }

    override fun getRightEndPoints(): List<RockPoint> {
        return listOf(p4)
    }

    override fun getLeftEndPoints(): List<RockPoint> {
        return listOf(p1)
    }

    override fun getBottomEndPoints(): List<RockPoint> {
        return getPoints();
    }

    override fun getTopEndPoints(): List<RockPoint> {
        return getPoints();
    }


}

private class PlusRock(var up: RockPoint, var left: RockPoint, var right: RockPoint, var down: RockPoint, var center: RockPoint) : OddRock() {
    companion object {
        fun create(map: MutableMap<Pair<Long, Long>, Boolean>, leftX: Long, bottomY: Long): PlusRock {
            val newRock = PlusRock(
                RockPoint(leftX + 1, bottomY + 2),
                RockPoint(leftX, bottomY + 1),
                RockPoint(leftX + 2, bottomY + 1),
                RockPoint(leftX + 1, bottomY),
                RockPoint(leftX + 1, bottomY + 1)
            )
            newRock.getPoints().forEach { map[it.coord()] = true }
            return newRock
        }
    }

    //.#.
    //#O#
    //.#.
    override fun getPoints(): List<RockPoint> {
        return listOf(up, left, right, down, center)
    }

    override fun getRightEndPoints(): List<RockPoint> {
        return listOf(up, right, down)
    }

    override fun getLeftEndPoints(): List<RockPoint> {
        return listOf(up, left, down)
    }

    override fun getBottomEndPoints(): List<RockPoint> {
        return listOf(left, right, down)
    }

    override fun getTopEndPoints(): List<RockPoint> {
        return listOf(up, left, right)
    }

}

private class CornerRock(
    var x1: RockPoint,
    var x2: RockPoint,
    var xy3: RockPoint,
    var y2: RockPoint,
    var y1: RockPoint
) : OddRock() {
    companion object {
        fun create(map: MutableMap<Pair<Long, Long>, Boolean>, leftX: Long, bottomY: Long): CornerRock {
            val newRock = CornerRock(
                RockPoint(leftX, bottomY),
                RockPoint(leftX + 1, bottomY),
                RockPoint(leftX + 2, bottomY),
                RockPoint(leftX + 2, bottomY + 1),
                RockPoint(leftX + 2, bottomY + 2),
            )
            newRock.getPoints().forEach { map[it.coord()] = true }
            return newRock
        }
    }
    //..#
    //..#
    //###

    override fun getPoints(): List<RockPoint> {
        return listOf(x1, x2, xy3, y2, y1)
    }

    override fun getRightEndPoints(): List<RockPoint> {
        return listOf(xy3, y2, y1)
    }

    override fun getLeftEndPoints(): List<RockPoint> {
        return listOf(x1, y2, y1)
    }

    override fun getBottomEndPoints(): List<RockPoint> {
        return listOf(x1, x2, xy3)
    }

    override fun getTopEndPoints(): List<RockPoint> {
        return listOf(x1, x2, y1)
    }


}

private class PoleRock(var p1: RockPoint, var p2: RockPoint, var p3: RockPoint, var p4: RockPoint) : OddRock() {
    companion object {
        fun create(map: MutableMap<Pair<Long, Long>, Boolean>, leftX: Long, bottomY: Long): PoleRock {
            val newRock = PoleRock(
                RockPoint(leftX, bottomY + 3),
                RockPoint(leftX, bottomY + 2),
                RockPoint(leftX, bottomY + 1),
                RockPoint(leftX, bottomY + 0),
            )
            newRock.getPoints().forEach { map[it.coord()] = true }
            return newRock
        }
    }

    // #
    // #
    // #
    // #
    override fun getPoints(): List<RockPoint> {
        return listOf(p1, p2, p3, p4)
    }

    override fun getRightEndPoints(): List<RockPoint> {
        return getPoints()
    }

    override fun getLeftEndPoints(): List<RockPoint> {
        return getPoints()
    }

    override fun getBottomEndPoints(): List<RockPoint> {
        return listOf(p4)
    }

    override fun getTopEndPoints(): List<RockPoint> {
        return listOf(p1)
    }
}

private class CubeRock(var l1: RockPoint, var l2: RockPoint, var r1: RockPoint, var r2: RockPoint) : OddRock() {
    companion object {
        fun create(map: MutableMap<Pair<Long, Long>, Boolean>, leftX: Long, bottomY: Long): CubeRock {
            val newRock = CubeRock(
                RockPoint(leftX + 0, bottomY + 1),
                RockPoint(leftX + 0, bottomY + 0),
                RockPoint(leftX + 1, bottomY + 1),
                RockPoint(leftX + 1, bottomY + 0),
            )
            newRock.getPoints().forEach { map[it.coord()] = true }
            return newRock
        }
    }

    //
    //
    // ##
    // ##
    override fun getPoints(): List<RockPoint> {
        return listOf(l1, l2, r1, r2)
    }

    override fun getRightEndPoints(): List<RockPoint> {
        return listOf(r1, r2)
    }

    override fun getLeftEndPoints(): List<RockPoint> {
        return listOf(l1, l2)
    }

    override fun getBottomEndPoints(): List<RockPoint> {
        return listOf(l2, r2)
    }

    override fun getTopEndPoints(): List<RockPoint> {
        return listOf(l1, r1)
    }


}


private fun partOne(pt: Long = 1) {
    //>>><<><>>
    val input = InputUtil.readFileAsStringList("2022/day17/input.txt", "")
        .filterNot { it.isEmpty() }.map { it[0] }
    var nextArrow = 0

    //initialize map with the floor
    val map = (0L..6L).map { it to 0L }.associateWith { true }.toMutableMap()
    for (rockNum in 1..2022) {
        //Each rock appears so that its left edge is two units away from the left wall
        // and its bottom edge is three units above the highest rock in the room
        val leftX = 2L
        val bottomY = map.entries.filter { it.value }.map { it.key }.map { it.second }.maxOf { it } + 4L

        val newRock: OddRock = when (rockNum % 5) {
            1 -> FlatRock.create(map, leftX, bottomY)
            2 -> PlusRock.create(map, leftX, bottomY)
            3 -> CornerRock.create(map, leftX, bottomY)
            4 -> PoleRock.create(map, leftX, bottomY)
            0 -> CubeRock.create(map, leftX, bottomY)
            else -> throw Exception("bad code")
        }

        while (true) {
//            debugPrint(map, bottomY + 3, bottomY - 6)
            if (nextArrow == input.size) nextArrow = 0
            val moveDirection = input[nextArrow]

            if (moveDirection  == '<') newRock.moveLeft(map)
            else newRock.moveRight(map)
//            debugPrint(map, bottomY + 3, bottomY - 6)


            nextArrow++
            if (!newRock.moveDown(map)) {
                break
            }
        }

    }
    debugPrint(map, 7)
    val answer = map.entries.filter { it.value }.map { it.key }.map { it.second }.maxOf { it }
    println("pt $pt answer: ${answer colorize ConsoleColor.PURPLE_BRIGHT}")


}

fun debugPrint(map: MutableMap<Pair<Long, Long>, Boolean>, yMax: Long, yMin: Long = 0) {
    println("debug")
    for (i in yMax downTo 0) {
        println()
        print('|')
        for (x in 0L..6L) {
            print(if (map.getOrDefault(x to i, false)) "#" else ".")
        }
        print('|')
    }
    println()
    println("|-------|")
    println()
    println()
}


private fun partTwo(pt: Long = 2) {
    //>>><<><>>
    val input = InputUtil.readFileAsStringList("2022/day17/input.txt", "")
        .filterNot { it.isEmpty() }.map { it[0] }
    var nextArrow = 0

    //initialize map with the floor
    val map = (0L..6L).map { it to 0L }.associateWith { true }.toMutableMap()
    for (rockNum in 1..1000000000000) {
        //Each rock appears so that its left edge is two units away from the left wall
        // and its bottom edge is three units above the highest rock in the room
        val leftX = 2L
        val bottomY = map.entries.filter { it.value }.map { it.key }.map { it.second }.maxOf { it } + 4L

        val newRock: OddRock = when (rockNum % 5) {
            1L -> FlatRock.create(map, leftX, bottomY)
            2L -> PlusRock.create(map, leftX, bottomY)
            3L -> CornerRock.create(map, leftX, bottomY)
            4L -> PoleRock.create(map, leftX, bottomY)
            0L -> CubeRock.create(map, leftX, bottomY)
            else -> throw Exception("bad code")
        }

        while (true) {
//            debugPrint(map, bottomY + 3, bottomY - 6)
            if (nextArrow == input.size) nextArrow = 0
            val moveDirection = input[nextArrow]

            if (moveDirection  == '<') newRock.moveLeft(map)
            else newRock.moveRight(map)
//            debugPrint(map, bottomY + 3, bottomY - 6)


            nextArrow++
            if (!newRock.moveDown(map)) {
                break
            }
        }

    }
//    debugPrint(map, 7)
    val answer = map.entries.filter { it.value }.map { it.key }.map { it.second }.maxOf { it }
    println("pt $pt answer: ${answer colorize ConsoleColor.PURPLE_BRIGHT}")
}