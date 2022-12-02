import util.ConsoleColor
import util.InputUtil
import util.TimeUtil
import util.colorize
import kotlin.math.abs

private fun main() {
    //pt 1 - 11781
    //pt 2 - 4531
    TimeUtil.startClock(1, ::partOne)
    TimeUtil.startClock(2, ::partTwo)
}

private class Probe(var xVelocity: Int, var yVelocity: Int, var position: Pair<Int, Int> = 0 to 0) {
    val initialVelocity = xVelocity to yVelocity

    fun stepForward(): Pair<Int, Int> {
        increasePosition()
        drag()
        gravity()
        return position;
    }

    fun increasePosition() {
        this.position = (position.first + xVelocity) to (position.second + yVelocity)
    }

    fun drag() {
        if (xVelocity > 0) xVelocity -= 1 else if (xVelocity < 0) xVelocity += 1
    }

    fun gravity() {
        yVelocity -= 1
    }

    override fun toString(): String {
        return "Probe(position=$position, initialVelocity=$initialVelocity)"
    }
}

private fun fireProbeAtTarget(probe: Probe, rangeX: IntRange, rangeY: IntRange): Int {
    var maxY = Int.MIN_VALUE;
    do {
        maxY = maxOf(probe.position.second, maxY)
        if (probe.position.first in rangeX && probe.position.second in rangeY)
            break
        if (probe.xVelocity == 0 && probe.position.first !in rangeX) {
            maxY = Int.MIN_VALUE;
            break
        }
        if (probe.position.second < (rangeY.minOrNull() ?: -400)) {
            maxY = Int.MIN_VALUE;
            break
        }
        probe.stepForward()
    } while (true)
    return maxY;
}

private fun partOne(pt: Int = 1) {
    val input = InputUtil.readFileAsString("2021/day17/input.txt")
    //target area: x=20..30, y=-10..-5
    val numbers = "-?\\d+".toRegex().findAll(input).toList()
    val targetMinX = numbers[0].value.toInt()
    val targetMaxX = numbers[1].value.toInt()
    val targetMinY = numbers[2].value.toInt()
    val targetMaxY = numbers[3].value.toInt()
    var maxY = Int.MIN_VALUE;
    val checkYRange = 1..abs(targetMinY)
    var checkXRange = 1..targetMaxX;

    for (x in checkXRange) {
        for (y in checkYRange) {
            maxY = maxOf(maxY, fireProbeAtTarget(Probe(x, y), targetMinX..targetMaxX, targetMinY..targetMaxY))
        }
    }


    val answer = maxY
    println("pt $pt answer: ${answer colorize ConsoleColor.CYAN_BOLD}")
}

private fun partTwo(pt: Int = 2) {
    val input = InputUtil.readFileAsString("2021/day17/input.txt")
    //target area: x=20..30, y=-10..-5
    val numbers = "-?\\d+".toRegex().findAll(input).toList()
    val targetMinX = numbers[0].value.toInt()
    val targetMaxX = numbers[1].value.toInt()
    val targetMinY = numbers[2].value.toInt()
    val targetMaxY = numbers[3].value.toInt()
    val checkYRange = targetMinY..abs(targetMinY)
    var checkXRange = 1..targetMaxX;

    var shotCounter = 0;
    for (x in checkXRange) {
        for (y in checkYRange) {
            val probeShot = fireProbeAtTarget(Probe(x, y), targetMinX..targetMaxX, targetMinY..targetMaxY)
            if (probeShot > Int.MIN_VALUE)
                shotCounter++
        }
    }


    val answer = shotCounter
    println("pt $pt answer: ${answer colorize ConsoleColor.CYAN_BOLD}")
}
