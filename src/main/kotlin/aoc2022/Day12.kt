package aoc2022

import util.*

private fun main() {
    //pt 1 - 352
    //pt 2 - 345
    TimeUtil.startClock(1, ::partOne)
    TimeUtil.startClock(2, ::partTwo)
}


private class HillStep(val h: Int, val r: Int, val c: Int) {
    var possibleDirections: Set<HillStep> = setOf()
    var reversePossibleDirections: Set<HillStep> = setOf()

    private fun north(map: Map<Pair<Int, Int>, HillStep>) = map[(r + 1) to c]
    private fun south(map: Map<Pair<Int, Int>, HillStep>) = map[(r - 1) to c]
    private fun east(map: Map<Pair<Int, Int>, HillStep>) = map[r to (c + 1)]
    private fun west(map: Map<Pair<Int, Int>, HillStep>) = map[r to (c - 1)]
    private fun canStepTo(other: HillStep) = h + 1 >= other.h
    private fun canStepToReverse(other: HillStep) = h <= other.h + 1
    fun calcPossibleDirections(map: Map<Pair<Int, Int>, HillStep>) {
        possibleDirections =
            listOfNotNull(north(map), south(map), east(map), west(map)).filter { this.canStepTo(it) }.toSet()
        reversePossibleDirections =
            listOfNotNull(north(map), south(map), east(map), west(map)).filter { this.canStepToReverse(it) }.toSet()
    }

    override fun toString(): String {
        return "${alphabet[h]} $r-$c"
    }
}

private const val alphabet = "+abcdefghijklmnopqrstuvwxyz"

private fun partOne(pt: Long = 1) {
    var start: HillStep? = null
    var destination: HillStep? = null
    val input = InputUtil.readFileAsStringList("2022/day12/input.txt", "\n")
        .mapIndexed { r, row ->
            row.toCharArray().mapIndexed { c, char ->
                if (char.isUpperCase()) {
                    if (char == 'S') {
                        start = HillStep(alphabet.indexOf('a'), r, c)
                        start!!
                    } else {
                        destination = HillStep(alphabet.indexOf('z'), r, c)
                        destination!!
                    }
                } else {
                    HillStep(alphabet.indexOf(char), r, c)
                }
            }
        }

    val map = input.flatten().associateBy { (it.r to it.c) }
    map.values.forEach { it.calcPossibleDirections(map) }


    val path = findPath(destination!!) { step -> step == start }
    val answer = path.size - 1
    println("pt $pt answer: ${answer colorize ConsoleColor.PURPLE_BRIGHT}")
}

private fun findPath(destination: HillStep, doneCheck: (HillStep) -> Boolean): List<HillStep> {
    // we'll search from the destination backwards
    val queue = queueOf(listOf(destination))
    var currentPath: List<HillStep>
    var currentStep: HillStep
    val checkedPoints = mutableSetOf<HillStep>()

    while (!queue.isEmpty()) {
        currentPath = queue.removeFirst()
        currentStep = currentPath.last()
        if (doneCheck(currentStep)) {
            return currentPath
        } else {
            // very important for BFS to prevent cycles
            if (checkedPoints.add(currentStep)) {
                queue.addAll(currentStep.reversePossibleDirections.map { next -> currentPath + next })
            }
        }
    }

    return emptyList()
}


private fun partTwo(pt: Long = 2) {
    var destination: HillStep? = null
    val input = InputUtil.readFileAsStringList("2022/day12/input.txt", "\n")
        .mapIndexed { r, row ->
            row.toCharArray()
                .map { if (it == 'S') 'a' else it }
                .mapIndexed { c, char ->
                if (char == 'E') {
                    destination = HillStep(alphabet.indexOf('z'), r, c)
                    destination!!
                } else {
                    HillStep(alphabet.indexOf(char), r, c)
                }
            }
        }

    val map = input.flatten().associateBy { (it.r to it.c) }
    map.values.forEach { it.calcPossibleDirections(map) }


    val path = findPath(destination!!) { step -> step.h == 1 }
    val answer = path.size - 1
    println("pt $pt answer: ${answer colorize ConsoleColor.PURPLE_BRIGHT}")
}