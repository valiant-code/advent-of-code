//package aoc2022
//
//import util.*
//
//private fun main() {
//    //pt 1 -
//    //pt 2 -
//    TimeUtil.startClock(1, ::partOne)
//    TimeUtil.startClock(2, ::partTwo)
//}
//
//private data class Valve(val id: String, val flowRate: Long, val leadsTo: List<String>) {
//    var open = false
//}
//
//private fun partOne(pt: Long = 1) {
//    //Valve AA has flow rate=0; tunnels lead to valves DD, II, BB
//    val input = InputUtil.readFileAsStringList("2022/day16/input.txt", "\n")
//        .map { line ->
//            val id = line.substringAfter("Valve ").take(2)
//            val flowRate = line.findAllNumbersAsLong().first()
//            val leadsTo = line.substringAfter("valves ").split(", ")
//            Valve(id, flowRate, leadsTo)
//        }
//    var maxPressureReleased = -1
//    var pressureReleased = 0L
//    var currIndex = 0
//    val g = getUniverses(
//        30,
//        input.map { it.copy() },
//        0,
//        maxPressureReleased,
//        pressureReleased,
//        ValveAction.NO_ACTION
//    )
////    for (i in 30 downTo 1) {
////        pressureReleased += input.filter { it.open }.map { it.flowRate }.sum()
////        var nextAction: () -> Unit = { currIndex += 1 }
////    }
//
//    val answer = input
//    println("pt $pt answer: ${answer colorize ConsoleColor.PURPLE_BRIGHT}")
//}
//
//private enum class ValveAction {
//    UP,
//    DOWN,
//    OPEN,
//    NO_ACTION
//}
//
//private fun getUniverses(
//    i: Int,
//    input: List<Valve>,
//    currentPosition: Int,
//    maxPressureReleased: Int,
//    pressureReleased: Long,
//    nextAction: ValveAction
//): Long {
//    if (i == 0)
//        return pressureReleased
//    var position = currentPosition
//    val inputCopy = input.map { it.copy() }
//
//    var released = pressureReleased + inputCopy.filter { it.open }.sumOf { it.flowRate }
//    //== Minute 1 ==
//    //No valves are open.
//    //You move to valve DD.
//    when (nextAction) {
//        ValveAction.UP -> position += 1
//        ValveAction.DOWN -> position -= 1
//        ValveAction.OPEN -> inputCopy[position].open = true
//        ValveAction.NO_ACTION -> {}
//    }
//
//    val nextValve = inputCopy[position];
//    val outcomes = ValveAction.values().filter { action ->
//        when (action) {
//            ValveAction.OPEN -> !inputCopy[position].open && inputCopy[position].flowRate > 0
//            ValveAction.UP -> inputCopy.size > position + 1
//            ValveAction.DOWN -> position - 1 >= 0
//            ValveAction.NO_ACTION -> false
//        }
//    }.map { action ->
//        getUniverses(
//            i - 1,
//            inputCopy,
//            position,
//            maxPressureReleased,
//            released,
//            action
//        )
//    }
//
//    //== Minute 2 ==
//    //No valves are open.
//    //You open valve DD.
//
//    //== Minute 3 ==
//    //Valve DD is open, releasing 20 pressure.
//    //You move to valve CC.
//
//    return outcomes.maxOf { it }
//}
//
//
//private fun partTwo(pt: Long = 2) {
//
//}