import util.ConsoleColor
import util.InputUtil
import util.TimeUtil
import util.colorize

private fun main() {
    //pt 1 - 1649
    //pt 2 - 256
    TimeUtil.startClock(1, ::partOne)
    TimeUtil.startClock(2, ::partTwo)
}

fun getAdjacentAndDiagonalPoints(point: Pair<Int, Int>): List<Pair<Int, Int>> {
    return setOf(
        point.first + 1 to point.second,
        point.first - 1 to point.second,
        point.first to point.second + 1,
        point.first to point.second - 1,
        //diagonals
        point.first + 1 to point.second + 1,
        point.first + 1 to point.second - 1,
        point.first - 1 to point.second + 1,
        point.first - 1 to point.second - 1,
    ).toList()
}

class Octopus(val coordinate: Pair<Int, Int>, var energyLevel: Int) {
    var lastFlashedStep = -1;
    var flashCounter = 0;
    fun readyToFlash(step: Int) = step > lastFlashedStep && energyLevel > 9;
    fun incrementEnergy(step: Int) {
        if (step > lastFlashedStep) {
            ++energyLevel
        }
    }

    fun flash(step: Int): Boolean {
        if (readyToFlash(step)) {
            energyLevel = 0;
            lastFlashedStep = step;
            flashCounter++

            return true;
        }
        return false;
    }

    override fun toString(): String {
        return "Octo(energyLevel=$energyLevel)"
    }

}

private fun partOne(pt: Int = 1) {
    val dummyPoint = Octopus(Pair(-999, -999), -999)
    val input = InputUtil.readFileAsStringList("2021/day11/input.txt")
    val map = input.flatMapIndexed { row, line ->
        line.mapIndexed { col, char -> Pair(row, col) to Octopus(Pair(row, col), char.digitToInt()) }
    }.toMap()

    //map.entries.groupBy{it.key.first}.map{it.value.map { it.value.energyLevel }}.joinToString("\n")
    for (currentStep in 1..100) {
        //First, the energy level of each octopus increases by 1.
        map.values.forEach { it.incrementEnergy(currentStep) }

        while (map.values.any { it.readyToFlash(currentStep) }) {
            map.values.filter { it.readyToFlash(currentStep) }
                .forEach {
                    if (it.flash(currentStep)) {
                        getAdjacentAndDiagonalPoints(it.coordinate).forEach { point ->
                            map.getOrDefault(point, dummyPoint).incrementEnergy(currentStep)
                        }
                    }
                }
        }
    }

    val answer = map.values.map { it.flashCounter }.sum()
    println("pt $pt answer: ${answer colorize ConsoleColor.CYAN_BOLD}")
}

private fun partTwo(pt: Int = 2) {
    val dummyPoint = Octopus(Pair(-999, -999), -999)

    val input = InputUtil.readFileAsStringList("2021/day11/input.txt")
    val map = input.flatMapIndexed { row, line ->
        line.mapIndexed { col, char -> Pair(row, col) to Octopus(Pair(row, col), char.digitToInt()) }
    }.toMap()

    var answer = -1;
    for (step in 1..Int.MAX_VALUE) {
        //First, the energy level of each octopus increases by 1.
        map.values.forEach { it.incrementEnergy(step) }

        //same flashing logic as pt 1
        while (map.values.any { it.readyToFlash(step) }) {
            map.values.filter { it.readyToFlash(step) }
                .forEach {
                    if (it.flash(step)) {
                        getAdjacentAndDiagonalPoints(it.coordinate).forEach { point ->
                            map.getOrDefault(point, dummyPoint)?.incrementEnergy(step)
                        }
                    }
                }
        }
        //check to see if they all flashed on the current step, before going to the next one
        if (map.values.all { it.lastFlashedStep == step }) {
            answer = step;
            break;
        }
    }

//    val answer =
    println("pt $pt answer: ${answer colorize ConsoleColor.CYAN_BOLD}")
}


