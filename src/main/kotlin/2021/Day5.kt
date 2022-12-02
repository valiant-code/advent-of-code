import util.ConsoleColor
import util.InputUtil
import util.TimeUtil
import util.colorize

private fun main() {
    //pt 1 - 7473
    //pt 2 - 24164
    TimeUtil.startClock(1, ::partOne)
    TimeUtil.startClock(2, ::partTwo)
}

data class VentLine(val start: Pair<Int, Int>, val end: Pair<Int, Int>) {
    val isVertical = start.first == end.first;
    val isHorizontal = start.second == end.second;

    fun getPoints(): List<Pair<Int, Int>> {
        val points = mutableListOf(start);
        while(points.last() != end) {
            points.add(points.last().moveTowards(end))
        }
        return points;
    }
}

//easy function to make a pair that is 1 unit closer to another
private fun Pair<Int, Int>.moveTowards(other: Pair<Int, Int>): Pair<Int, Int> {
    val x = when {
        this.first == other.first -> this.first
        this.first > other.first -> this.first - 1
        else -> this.first + 1
    };
    val y = when {
        this.second == other.second -> this.second
        this.second > other.second -> this.second - 1
        else -> this.second + 1
    };
    return x to y;
}

private fun partOne(pt: Int = 1, considerDiagonals: Boolean = false) {
    //0,9 -> 5,9
    //8,0 -> 0,8
    val input = InputUtil.readFileAsStringList("2021/day5/input.txt");
    val lines = input.map {
        val vals = it.split(" -> ")
        VentLine(
            Pair(vals.get(0).split(",")[0].toInt(), vals.get(0).split(",")[1].toInt()),
            Pair(vals.get(1).split(",")[0].toInt(), vals.get(1).split(",")[1].toInt())
        )
    }.filter { considerDiagonals || it.isVertical || it.isHorizontal }

    val ans = lines.flatMap { it.getPoints() }.groupBy { it }.filter { it.value.size > 1 }.count()

    //At how many points do at least two lines overlap?
    println("pt $pt answer: ${ans.toString().colorize(ConsoleColor.CYAN_BOLD)}")
}

//same exact code, without filtering down to only horizontal and vertical lines
private fun partTwo() {
    partOne(2, true)
}


