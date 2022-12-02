import util.ConsoleColor
import util.InputUtil
import util.TimeUtil
import util.colorize

private fun main() {
    //pt 1 - 707784
    //pt 2 - 157595953724471
    TimeUtil.startClock(1, ::partOne)
    TimeUtil.startClock(2, ::partTwo)
}

private class DeterministicDice() {
    private var nextValue = 1;
    var rollCounter = 0;

    fun roll(): Int {
        if (nextValue >= 101) {
            nextValue = 1
        }
        rollCounter++
        return nextValue++;
    }

    fun roll3Times(): Int {
        return roll() + roll() + roll()

    }
}
// list of possible sums from rolling a dirac dice 3 times (each roll producing 1, 2, or 3)
val diracDicePossibilities = (1..3).flatMap { i -> (1..3)
        .flatMap { j -> (1..3)
            .map { k -> i + j + k } } }

private data class DiracDicePlayer(var position: Int, val goal: Int = 1000, var points: Int = 0) {
    companion object {
        fun getNewPosition(currPos: Int, numToMove: Int): Int {
            val newSpace = (currPos + numToMove) % 10
            return if (newSpace == 0) 10 else newSpace
        }
    }

    fun moveSpaces(num: Int, addPoints: Boolean = true): Int {
        position = getNewPosition(position, num)
        points += position
        return position
    }

    fun hasWon() = points >= goal
}

private fun partOne(pt: Int = 1) {
    val input = InputUtil.readFileAsStringList("2021/day21/input.txt")
    var player1 = DiracDicePlayer(input[0].substringAfter(": ").toInt())
    var player2 = DiracDicePlayer(input[1].substringAfter(": ").toInt())
    val players = listOf(player1, player2);
    val dice = DeterministicDice();

    var turnPlayer = player1;

    while (players.none { it.hasWon() }) {
        turnPlayer.moveSpaces(dice.roll3Times())
        turnPlayer = if (turnPlayer == player1) player2 else player1
    }

    val loserPoints = players.filterNot { it.hasWon() }.map { it.points }[0]
    val answer = dice.rollCounter * players.filterNot { it.hasWon() }.map { it.points }[0]
    println("pt $pt answer $loserPoints * ${dice.rollCounter}: ${answer colorize ConsoleColor.CYAN_BOLD}")
}

private data class GameResult(var player1Wins: Long = 0, var player2Wins: Long = 0) {
    fun addRes(result: GameResult) {
        player1Wins += result.player1Wins
        player2Wins += result.player2Wins
    }
}

val nextTurnMap = mapOf(1 to 2, 2 to 1)

private val resultsCache = mutableMapOf<String, GameResult>()
private fun playGame(p1Score: Long, p2Score: Long, p1Position: Int, p2Position: Int, turn: Int): GameResult {
    val result = resultsCache.computeIfAbsent("$p1Score,$p2Score,$p1Position,$p2Position,$turn") {
        val isP1Turn = turn == 1;
        if (p1Score >= 21) GameResult(1, 0)
        else if (p2Score >= 21) GameResult(0, 1)
        else {
            val res = GameResult();
            for (i in diracDicePossibilities) {
                if (isP1Turn) {
                    val newPos = DiracDicePlayer.getNewPosition(p1Position, i);
                    res.addRes(playGame(p1Score + newPos, p2Score, newPos, p2Position, nextTurnMap[turn]!!))
                } else {
                    val newPos = DiracDicePlayer.getNewPosition(p2Position, i);
                    res.addRes(playGame(p1Score, p2Score + newPos, p1Position, newPos, nextTurnMap[turn]!!))
                }
            }
            res;
        }
    }
    return result;
}

private fun partTwo(pt: Int = 2) {
    val input = InputUtil.readFileAsStringList("2021/day21/input.txt")
    var player1 = DiracDicePlayer(input[0].substringAfter(": ").toInt(), 21)
    var player2 = DiracDicePlayer(input[1].substringAfter(": ").toInt(), 21)

    val finalResult = playGame(0, 0, player1.position, player2.position, 1)

    //ex: 444356092776315 vs 341960390180808
    val answer = maxOf(finalResult.player1Wins, finalResult.player2Wins)
    println("pt $pt answer : ${answer colorize ConsoleColor.CYAN_BOLD}")
}
