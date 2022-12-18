//package aoc2022
//
//import util.*
//import kotlin.math.absoluteValue
//
//private fun main() {
//    //pt 1 - 5870800
//    //pt 2 - 10908230916597
////    TimeUtil.startClock(1, ::partOne)
//    TimeUtil.startClock(2, ::partTwo)
//}
//
//class BeaconSensor(val x: Long, val y: Long, val mDistance: Long) {
//    fun checkInRange(coord: Pair<Long, Long>, plus: Int = 0): Boolean {
//        val mDistanceToCoord = (coord.first - x).absoluteValue + (coord.second - y).absoluteValue
//        return mDistance + plus >= mDistanceToCoord
//    }
//
//    // 1a/\
//    //   \/1b
//    fun getPositiveSlopeEdges(plus: Int = 1): List<Pair<Pair<Long, Long>, Pair<Long, Long>>> {
//        val adjustedDistance = mDistance + plus
//        val up = y - adjustedDistance
//        val down = y + adjustedDistance
//        val left = x - adjustedDistance
//        val right = x + adjustedDistance
//        return listOf(
//            (left to y) to (x to up), // 1a
//            (x to down) to (right to y), // 1b
//        )
//    }
//
//    //   /\2a
//    // 2b\/
//    fun getNegativeSlopeEdges(plus: Int = 1): List<Pair<Pair<Long, Long>, Pair<Long, Long>>> {
//        val adjustedDistance = mDistance + plus
//        val up = y - adjustedDistance
//        val down = y + adjustedDistance
//        val left = x - adjustedDistance
//        val right = x + adjustedDistance
//        return listOf(
//            (x to up) to (right to y), // 2a
//            (left to y) to (x to down), // 2b
//        )
//    }
//
//    override fun toString(): String {
//        return "Sensor(x=$x, y=$y, mDistance=$mDistance)"
//    }
//
//
//}
//
////https://github.com/chrisleow/advent-of-code/blob/main/2022/src/Day15.kt
//fun getCrossingPoint(line1: Pair<Pair<Long, Long>, Pair<Long, Long>>,
//                     line2: Pair<Pair<Long, Long>, Pair<Long, Long>>): Pair<Long, Long>? {
//    /*
//    Diagonal lines for point distances can be expressed either as:
//        x + y = c      or
//        x - y = c
//    Crossing lines have different forms, and so if:
//        x + y = c1     and
//        x - y = c2
//    then:
//        x = (c1 + c2) / 2
//        y = (c1 - c2) / y
//
//    */
//
//    val c1 = run {
//        val line1c1 = line1.first.first + line1.first.second
//        val line1c2 = line1.second.first + line1.second.second
//        val line2c1 = line2.first.first + line2.first.second
//        val line2c2 = line2.second.first + line2.second.second
//        when {
//            line1c1 == line1c2 -> line1c1
//            line2c1 == line2c2 -> line2c1
//            else -> return null
//        }
//    }
//
//    val c2 = run {
//        val line1c1 = line1.first.first - line1.first.second
//        val line1c2 = line1.second.first - line1.second.second
//        val line2c1 = line2.first.first - line2.first.second
//        val line2c2 = line2.second.first - line2.second.second
//        when {
//            line1c1 == line1c2 -> line1c1
//            line2c1 == line2c2 -> line2c1
//            else -> return null
//        }
//    }
//
//    return((c1 + c2) / 2 to (c1 - c2) / 2)
//}
//
//
////I wasted so much time trying on this
////private fun linesIntersectAtPoint(
////    sensor: BeaconSensor,
////    a: Pair<Pair<Long, Long>, Pair<Long, Long>>,
////    b: Pair<Pair<Long, Long>, Pair<Long, Long>>
////): Pair<Long, Long>? {
////    val (a1, a2) = a
////    val (b1, b2) = b
////    val (a1x, a1y) = a1
////    val (a2x, a2y) = a2
////    val (b1x, b1y) = b1
////    val (b2x, b2y) = b2
////    //y = mx + b
////    //m = slope = rise/run
////    val m1 = (a2y - a1y) / (a2x - a1x)
////    val m2 = (b2y - b1y) / (b2x - b1x)
////    //b = y - mx (y intercept)
////    val interceptA = a1y - (m1 * a1y)
////    val interceptB = b1y - (m2 * b1y)
////    //x = (b2 - b1) / (m1 - m2)
////    val intersectionX = (interceptB - interceptA) / (m1 - m2)
////    //y = m1x + b1
////    val intersectionY = m1 * a1x + interceptA
////
////    val possiblePoint = intersectionX to intersectionY
////    return if (sensor.checkInRange(possiblePoint, 1)) possiblePoint else null
////}
//
//private fun partOne(pt: Long = 1) {
//    val noBeaconSet: MutableSet<Pair<Long, Long>> = mutableSetOf()
//    val beaconSet: MutableSet<Pair<Long, Long>> = mutableSetOf()
//    val sensorSet: MutableSet<BeaconSensor> = mutableSetOf()
//    //Sensor at x=9, y=16: closest beacon is at x=10, y=16
//    val input = InputUtil.readFileAsStringList("2022/day15/input.txt", "\n")
//        .map { it.findAllNumbersAsLong() }
//        .map { (sensorCol, sensorRow, closestCol, closestRow) ->
//            beaconSet.add(closestCol to closestRow)
//            val mDistance = (closestCol - sensorCol).absoluteValue + (closestRow - sensorRow).absoluteValue
//            sensorSet.add(BeaconSensor(sensorCol, sensorRow, mDistance))
//        }
//    val chosenRow = 2000000
//
//    sensorSet.filter {
//        val maxRow = it.y + it.mDistance
//        val minRow = it.y - it.mDistance
//        chosenRow in minRow..maxRow
//    }.forEach { sensor ->
//        val mDistanceAtChosenRow = sensor.mDistance - (sensor.y - chosenRow).absoluteValue
//        for (i in 0..mDistanceAtChosenRow) {
//            noBeaconSet.add((sensor.x + i) to chosenRow.toLong())
//            noBeaconSet.add((sensor.x - i) to chosenRow.toLong())
//        }
//    }
//
//    // (There is never a tie where two beacons are the same distance to a sensor.)
//
//    val selectedRow = noBeaconSet
////        .filter { it.second == chosenRow }
//        .filterNot { beaconSet.contains(it) }
//        .toMutableSet()
////        .filterNot { sensorSet.any { sensor -> it.equals(sensor.x to sensor.y) } }
//    //In the example, in the row where y=10, there are 26 positions where a beacon cannot be present.
//    // In the row where y=2000000, how many positions cannot contain a beacon?
//
//    val answer = selectedRow.count()
//    println("pt $pt answer: ${answer colorize ConsoleColor.PURPLE_BRIGHT}")
//}
//
//private fun partTwo(pt: Long = 2) {
//    val beaconSet: MutableSet<Pair<Long, Long>> = mutableSetOf()
//    val sensorSet: MutableSet<BeaconSensor> = mutableSetOf()
//
//    //Sensor at x=9, y=16: closest beacon is at x=10, y=16
//    InputUtil.readFileAsStringList("2022/day15/input.txt", "\n")
//        .map { it.findAllNumbersAsLong() }
//        .forEach { (sensorCol, sensorRow, closestCol, closestRow) ->
//            beaconSet.add(closestCol to closestRow)
//            val mDistance = (sensorCol - closestCol).absoluteValue + (sensorRow - closestRow).absoluteValue
//            sensorSet.add(BeaconSensor(sensorCol, sensorRow, mDistance))
//        }
//
//
//    //In the example above, the search space is smaller: instead, the x and y coordinates can each be at most 20
//    val searchSpace = 0..4000000
//    val positiveEdges = sensorSet.flatMap { it.getPositiveSlopeEdges() }
//    val negativeEdges = sensorSet.flatMap { it.getNegativeSlopeEdges() }
//
//    val intersectionPoints = positiveEdges.flatMap { a ->
//        negativeEdges.mapNotNull { b ->
//            getCrossingPoint(a, b)
//        }
//    }.toSet()
//
//    //Point(x=2727057, y=2916597)
//    val thePoint = intersectionPoints.filter {
//        // but the distress beacon must have x and y coordinates each no lower than 0 and no larger than 4000000
//        it.first in searchSpace && it.second in searchSpace
//    }.single { coord -> sensorSet.none { it.checkInRange(coord) } }
//
//    val answer = thePoint.first * 4000000 + thePoint.second
//    println("pt $pt answer: ${answer colorize ConsoleColor.PURPLE_BRIGHT} at ${thePoint colorize ConsoleColor.PURPLE_BRIGHT}")
//}