import util.InputUtil
import util.TimeUtil

private fun main() {
    //pt 1 - 1374
    //pt 2 - 1418
    TimeUtil.startClock(1, ::partOne)
    TimeUtil.startClock(2, ::partTwo)
}

private fun partOne() {
    //interesting 1 line solutions from another user
    //override fun part1() = input.windowed(2).count{it[1]>it[0]}
    //override fun part2() = input.windowed(3).map{ it.sum() }.windowed(2).count{it[1]>it[0]}
    //the windowed function is neat and fit this problem perfectly
    val input = InputUtil.readFileAsIntList("2021/day1/input.txt");
    var count = 0;

    var prevVal = Int.MAX_VALUE;

    input.forEach { num ->
        if (prevVal < num) count++
        prevVal = num;
    }
    println("pt 1 count: $count")
}

private fun partTwo() {
    val input = InputUtil.readFileAsIntList("2021/day1/input.txt");
    var count = 0;
    input.map { it.plus(1) }

    var prevList = input.subList(0, 3);
    for (i in 4..input.size) {
        val nextList = input.subList(i - 3, i);
        if (prevList.sum() < nextList.sum()) count++;
        prevList = nextList;
    }
    println("pt2 count: $count")
}