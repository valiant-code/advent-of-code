import util.ConsoleColor
import util.InputUtil
import util.TimeUtil
import util.colorize

private fun main() {
    //pt 1 - 87456
    //pt 2 - 15561
    TimeUtil.startClock(1, ::partOne)
    TimeUtil.startClock(2, ::partTwo)
}

val doNothing = Unit;

data class BingoSpace(val row: Int, val col: Int, val num: Int, var marked: Boolean = false) {
    //The problem says to ignore diagonals but afterwards I wanted to figure it out anyway
    val isDiagonalA = row == col;     //0,0, 1,1, 2,2, 3,3, 4,4
    val isDiagonalB = row + col == 4; //4,0, 3,1, 2,2, 1,3, 0,4
    fun markIfMatched(n: Int) = if (n == num) marked = true else doNothing;

    override fun toString(): String {
        return (if (!marked) "$num".padStart(3) else "*$num".padStart(3) colorize ConsoleColor.YELLOW_BOLD);
    }
}

private class BingoBoard(val boardId: Int, boardString: String) {
    //22 13 17 11  0\n
    // 8  2 23  4 24\n
    //21  9 14 16  7\n
    // 6 10  3 18  5\n
    // 1 12 20 15 19\n
    val spaces: List<BingoSpace> = boardString.split("\n")
        .flatMapIndexed { row: Int, line: String ->
            line.trim()
                .split(Regex("\\W+"))
                .mapIndexed { col: Int, num -> BingoSpace(row, col, num.toInt()) }
        }

    fun hasBingo(): Boolean {
        return spaces.filter { it.marked }.groupBy { it.col }.filter { it.value.size >= 5 }.any() ||
                spaces.filter { it.marked }.groupBy { it.row }.filter { it.value.size >= 5 }.any()
//        Diagonal logic is easy
//                || spaces.filter { it.marked && it.isDiagonalA }.count() >= 5 ||
//                spaces.filter { it.marked && it.isDiagonalB }.count() >= 5
    }

    override fun toString(): String {
        return spaces.groupBy { it.row }.values.joinToString("\n") { it.toString() }
    }
}

private fun partOne() {
    val input = InputUtil.readFileAsString("2021/day4/input.txt");
    val numbersToDraw = input.substringBefore("\n").split(",").map { it.toInt() };
    val boards = input.substringAfter("\n\n").split("\n\n").mapIndexed(::BingoBoard)

    var firstBingo: BingoBoard? = null;
    var lastNumberCalled = 0;

    for (num in numbersToDraw) {
        boards.forEach { b -> b.spaces.filterNot(BingoSpace::marked).forEach { it.markIfMatched(num) } }
        firstBingo = boards.firstOrNull { it.hasBingo() };
        if (firstBingo != null) {
            lastNumberCalled = num;
            break;
        }
    }
    // Start by finding the sum of all unmarked numbers on that board;
    val allUnmarkedSum = firstBingo!!.spaces.filter { !it.marked }.sumOf { it.num }


    // Then, multiply that sum by the number that was just called when the board won
    println("pt 1 answer: board #${firstBingo.boardId} ${allUnmarkedSum * lastNumberCalled}")
    println("firstBingo:\n$firstBingo\n");
}

private fun partTwo() {
    val input = InputUtil.readFileAsString("2021/day4/input.txt");
    val numbersToDraw = input.substringBefore("\n").split(",").map { it.toInt() };
    val boards = input.substringAfter("\n\n").split("\n\n").mapIndexed(::BingoBoard).toMutableList()

    var lastBingo: BingoBoard? = null;
    var lastNumberCalled = 0;

    for (num in numbersToDraw) {
        boards.forEach { b -> b.spaces.filterNot(BingoSpace::marked).forEach { it.markIfMatched(num) } }
        val winners = boards.filter { it.hasBingo() };
        if (winners.size >= boards.size) {
            lastBingo = winners.firstOrNull();
            lastNumberCalled = num;
            break;
        } else if (winners.isNotEmpty()) {
            boards.removeAll(winners);
        }
    }

    //I appreciate Kotlin making me use Bingo!!
    val allUnmarkedSum = lastBingo!!.spaces.filter { !it.marked }.sumOf { it.num }

    println("pt 2 answer: board #${lastBingo.boardId} ${allUnmarkedSum * lastNumberCalled}")
    println("lastBingo:\n$lastBingo\n");
}


