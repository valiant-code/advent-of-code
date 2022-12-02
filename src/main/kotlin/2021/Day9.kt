import util.ConsoleColor
import util.InputUtil
import util.TimeUtil
import util.colorize
import java.util.*

private fun main() {
    //pt 1 - 465
    //pt 2 - 1269555
    TimeUtil.startClock(1, ::partOne)
    TimeUtil.startClock(2, ::partTwo)
}

//find the low points - the locations that are lower than any of its adjacent locations
private fun isLowPoint(point: Pair<Int, Int>, value: Int, heightMap: Map<Pair<Int, Int>, Int>): Boolean {
    return getAdjacentPoints(point).all { adj -> value < heightMap.getOrDefault(adj, 9) }
}

fun getAdjacentPoints(point: Pair<Int, Int>): List<Pair<Int, Int>> {
    return listOf(
        point.first + 1 to point.second,
        point.first - 1 to point.second,
        point.first to point.second + 1,
        point.first to point.second - 1,
    )
}

private fun getBasin(startPoint: Pair<Int, Int>, heightMap: Map<Pair<Int, Int>, Int>): Set<Pair<Int, Int>> {
    if (heightMap.getOrDefault(startPoint, 9) == 9) return Collections.emptySet()
    val basinPoints = mutableSetOf(startPoint);
    val checkedPoints = mutableSetOf(startPoint);
    val uncheckedPoints = getAdjacentPoints(startPoint).toMutableList();

    while (uncheckedPoints.size > 1) {
        val next = uncheckedPoints.removeFirst();
        checkedPoints.add(next)
        if (heightMap.getOrDefault(next, 9) != 9) {
            basinPoints.add(next);
            uncheckedPoints.addAll(getAdjacentPoints(next).filterNot { checkedPoints.contains(it) })
        }
    }

    return basinPoints;
}

private fun partOne(pt: Int = 1) {
    val heightMap = InputUtil.readFileAsStringList("2021/day9/input.txt")
        .flatMapIndexed { row, line ->
            line.mapIndexed { col, char -> Pair(row, col) to char.digitToInt() }
        }
        .toMap()

    val answer = heightMap.entries.filter { isLowPoint(it.key, it.value, heightMap) }
        .map { it.value + 1 } //risk level of a low point is 1 plus its height
        .sum()
    println("pt $pt answer: ${answer colorize ConsoleColor.CYAN_BOLD}")
}

private fun partTwo(pt: Int = 2) {
    val heightMap = InputUtil.readFileAsStringList("2021/day9/input.txt")
        .flatMapIndexed { row, line ->
            line.mapIndexed { col, char -> Pair(row, col) to char.digitToInt() }
        }
        .toMap()

    val basins: MutableSet<Set<Pair<Int, Int>>> = mutableSetOf();

    for (point in heightMap.keys) {
        //if we already have this point in any of our basins, continue
        if (basins.any { it.contains(point) }) continue
        //otherwise, go look get its basin from the BFS method
        else basins.add(getBasin(point, heightMap))
    }

    //What do you get if you multiply together the sizes of the three largest basins?
    val answer = basins.sortedByDescending { it.size }.take(3).map { it.size }.reduce(Int::times);
    println("pt $pt answer: ${answer colorize ConsoleColor.CYAN_BOLD}")
}


