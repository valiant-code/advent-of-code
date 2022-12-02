import util.ConsoleColor
import util.InputUtil
import util.TimeUtil
import util.colorize

private fun main() {
    //pt 1 - 678
    //pt 2 - ECFHLHZF
    TimeUtil.startClock(1, ::partOne)
    TimeUtil.startClock(2, ::partTwo)
}

private fun foldPoints(points: MutableSet<Pair<Int, Int>>, instruction: Pair<Char, Int>) {
    //map<x, y> to Char  (col, row)
    //map<col, row> -
    val fold = instruction.second;
    when (instruction.first) {
        'x' -> { //flip col
            val keysToAdd = points.filter { it.first > fold }.map { keyBeingFolded ->
                keyBeingFolded.copy(first = 2 * fold - keyBeingFolded.first)
            }
            val keysToRemove = points.filter { it.first > fold }
            keysToAdd.forEach { points.add(it) }
            keysToRemove.forEach { points.remove(it) }
        }
        'y' -> { //flip row
            val keysToAdd = points.filter { it.second > fold }.map { keyBeingFolded ->
                keyBeingFolded.copy(second = 2 * fold - keyBeingFolded.second)
            }
            val keysToRemove = points.filter { it.second > fold }
            keysToAdd.forEach { points.add(it) }
            keysToRemove.forEach { points.remove(it) }
        }
    }
}

private fun partOne(pt: Int = 1) {
    val input = InputUtil.readFileAsStringList("2021/day13/input.txt", "\n\n")
    val instructions: List<Pair<Char, Int>> = input[1].split("\n").map {
        val direction = if (it.contains("x")) 'x' else 'y'
        direction to it.substringAfter("=").toInt()
    }

    val points: MutableSet<Pair<Int, Int>> = input[0].split("\n").map {
        val col = it.substringBefore(",").toInt();
        val row = it.substringAfter(",").toInt();
        Pair(col, row)
    }.toMutableSet()

    foldPoints(points, instructions[0])

    val answer = points.count();
    println("pt $pt answer: ${answer colorize ConsoleColor.CYAN_BOLD}")
}

private fun partTwo(pt: Int = 2) {
    val input = InputUtil.readFileAsStringList("2021/day13/input.txt", "\n\n")
    val instructions: List<Pair<Char, Int>> = input[1].split("\n").map {
        val direction = if (it.contains("x")) 'x' else 'y'
        direction to it.substringAfter("=").toInt()
    }
    val points: MutableSet<Pair<Int, Int>> = input[0].split("\n").map {
        val col = it.substringBefore(",").toInt();
        val row = it.substringAfter(",").toInt();
        Pair(col, row)
    }.toMutableSet()

    instructions.forEach { instruction -> foldPoints(points, instruction) }

    printPoints(points)
//    val answer = map.values.count();
//    println("pt $pt answer: ${answer util.colorize util.ConsoleColor.CYAN_BOLD}")
}

private fun printPoints(points: MutableSet<Pair<Int, Int>>) {
    val maxFirst = points.map { it.first }.maxOrNull()!!
    val maxSecond = points.map { it.second }.maxOrNull()!!
    for (j in 0..maxSecond) {
        for (i in 0..maxFirst) {
            print(if (points.any { it == i to j }) { "#" } else { " " } colorize ConsoleColor.BLUE)
        }
        println();
    }
}


