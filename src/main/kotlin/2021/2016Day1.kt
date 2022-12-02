import objects.Direction
import util.InputUtil
import util.TimeUtil
import kotlin.math.absoluteValue

private fun main() {
    //pt 1 - 307
    //pt 2 - 165
    val runPart1 = true
    val runPart2 = true
    if (runPart1) {
        TimeUtil.startClock(1, ::partOne)
    }
    if (runPart2) {
        TimeUtil.startClock(2, ::partTwo)
    }
}


private val turnMap = mapOf(
    "NL" to 'W', "NR" to 'E',
    "WL" to 'S', "WR" to 'N',
    "SL" to 'E', "SR" to 'W',
    "EL" to 'N', "ER" to 'S',
);

private fun partOne() {
    val input = InputUtil.readFileAsStringList("2016day1/input.txt", ", ")
        .map { str -> Direction(str[0], str.substring(1).toInt()) }

    var position = Pair(0, 0);
    var currentHeading = 'N';

    for (direction in input) {
//        println("checking direction $direction from position $currentHeading $position")
        currentHeading = turnMap[currentHeading.toString() + direction.heading]!!;
        position = when (currentHeading) {
            'N' -> position.copy(second = position.second.plus(direction.amount));
            'S' -> position.copy(second = position.second.minus(direction.amount));
            'E' -> position.copy(first = position.first.plus(direction.amount));
            else -> position.copy(first = position.first.minus(direction.amount));
        }
    }

    println("final position ${position} = ${position.first.absoluteValue + position.second.absoluteValue} blocks away")
}

private fun partTwo() {
    val input = InputUtil.readFileAsStringList("2016day1/input.txt", ", ")
        .map { str -> Direction(str[0], str.substring(1).toInt()) }

    var position = Pair(0, 0);
    val visitSet = mutableSetOf(position)

    var currentHeading = 'N';

    outerLoop@ for (direction in input) {
//        println("checking direction $direction from position $currentHeading $position")
        currentHeading = turnMap[currentHeading.toString() + direction.heading]!!;
        for (i in 1..direction.amount) {
            when (currentHeading) {
                'N' -> position = position.copy(second = position.second.plus(1));
                'S' -> position = position.copy(second = position.second.minus(1));
                'E' -> position = position.copy(first = position.first.plus(1));
                else -> position = position.copy(first = position.first.minus(1));
            }
            if (!visitSet.add(position)) {
                println("found $position")
                break@outerLoop;
            }
        }
    }

    println("final position ${position} = ${position.first.absoluteValue + position.second.absoluteValue} blocks away")
}