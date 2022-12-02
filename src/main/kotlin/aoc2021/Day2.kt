import util.InputUtil
import util.TimeUtil

private fun main() {
    //pt 1 - 1989014
    //pt 2 - 2006917119
    TimeUtil.startClock(1, ::partOne)
    TimeUtil.startClock(2, ::partTwo)
}

private class SubmarineInstruction(
    val command: String,
    val value: Int) {
    constructor(instructionString: String) : this(
        instructionString.substringBefore(" "),
        instructionString.substringAfter(" ").toInt());
};
private infix fun Int.greaterThanEquals(that: Int): Boolean = this > that;
private fun Boolean.toInt() = if (this) 1 else 0


private fun partOne() {
    //forward X increases the horizontal position by X units.
    //down X increases the depth by X units.
    //up X decreases the depth by X units.
    var depth = 0;
    var horizontal = 0;
    val input = InputUtil.readFileAsStringList("2021/day2/input.txt")
        .map(::SubmarineInstruction)
        .forEach {
            when (it.command) {
                "forward" -> horizontal += it.value;
                "down" -> depth += it.value;
                "up" -> depth -= it.value;
            }
        }

    println("pt 1 answer: ${horizontal * depth}")
}

private fun partTwo() {
//    down X increases your aim by X units.
//    up X decreases your aim by X units.
//    forward X does two things:
//      It increases your horizontal position by X units.
//      It increases your depth by your aim multiplied by X.
    var depth = 0;
    var horizontal = 0;
    var aim = 0;

    val input = InputUtil.readFileAsStringList("2021/day2/input.txt")
        .map(::SubmarineInstruction)
        .forEach {
            when (it.command) {
                "forward" -> {
                    horizontal += it.value; depth += aim * it.value
                }
                "down" -> aim += it.value;
                "up" -> aim -= it.value;
            }
        }

    println("pt 2 answer: ${horizontal * depth}")
}