import util.ConsoleColor
import util.InputUtil
import util.TimeUtil
import util.colorize

private fun main() {
    //pt 1 - 288291
    //pt 2 - 820045242
    TimeUtil.startClock(1, ::partOne)
    TimeUtil.startClock(2, ::partTwo)
}

private val chunkChars = mapOf('(' to ')', '[' to ']', '{' to '}', '<' to '>')

private val errorPointMap = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)
private val addedPointMap = mapOf(')' to 1, ']' to 2, '}' to 3, '>' to 4)

private fun getInvalidClosers(line: String): Char? {
    val openers = chunkChars.keys;
    val openersStack = mutableListOf<Char>()
    for (char in line) {
        if (char in openers) {
            openersStack.add(char)
        } else {
            val lastOpener = openersStack.removeLast();
            val neededCloser = chunkChars[lastOpener]
            if (neededCloser != char) {
                return char
            }
        }
    }
    return null;
}

private fun partOne(pt: Int = 1) {
    val input = InputUtil.readFileAsStringList("2021/day10/input.txt")
    val answer = input.map(::getInvalidClosers)
        .filterNotNull()
        .map { c-> errorPointMap.getOrDefault(c, 0) }.sum();
    println("pt $pt answer: ${answer colorize ConsoleColor.CYAN_BOLD}")
}

private fun getNeededCharsToComplete(line: String): List<Char> {
    val openers = chunkChars.keys;
    val openersStack = mutableListOf<Char>()
    for (char in line) {
        if (char in openers) {
            openersStack.add(char)
        } else {
            val lastOpener = openersStack.removeLast();
            val neededCloser = chunkChars[lastOpener]
            if (neededCloser != char) {
                return emptyList()
            }
        }
    }
    val neededClosersList = mutableListOf<Char>()
    while (openersStack.isNotEmpty()) {
        neededClosersList.add(chunkChars[openersStack.removeLast()]!!)
    }
    return neededClosersList;
}

private fun partTwo(pt: Int = 2) {
    val input = InputUtil.readFileAsStringList("2021/day10/input.txt")

    val scores = input.map(::getNeededCharsToComplete).filterNot { it.isEmpty() }.map { listOfAddedChars ->
        //Start with a total score of 0. Then, for each character,
        var score = 0L;
        listOfAddedChars.forEach {
            // multiply the total score by 5 and then add the points
            score = score * 5 + addedPointMap.getOrDefault(it, -9999)
        }
        score
    }.sorted()
    //the winner is found by sorting all of the scores and then taking the middle score.
    // (There will always be an odd number of scores to consider.)

    val answer = scores[(scores.size) / 2]
    println("pt $pt answer: ${answer colorize ConsoleColor.CYAN_BOLD}")
}


