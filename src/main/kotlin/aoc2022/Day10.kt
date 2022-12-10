package aoc2022

import util.*

private fun main() {
    //pt 1 - 16480
    //pt 2 - PLEFULPB
    TimeUtil.startClock(1, ::partOne)
    TimeUtil.startClock(2, ::partTwo)
}

private fun partOne(pt: Int = 1) {
    val input = InputUtil.readFileAsStringList("2022/day10/input.txt")

    var acc = 1L;
    val list = mutableListOf<Long>()

    val queue = queueOf({})
    var cycle = 0;
    while (queue.isNotEmpty()) {
        cycle++;
        queue.removeFirst().invoke()

        if (input.lastIndex <= cycle - 1) {
            // stop adding to queue
        } else if (input[cycle - 1] == "noop") {
            queue.add {}
        } else {
            val num = input[cycle - 1].substringAfter(" ").toLong()
            queue.add {}
            queue.add { acc += num }
        }

        if ((cycle == 20) || ((cycle - 20) % 40) == 0) {
            list.add(acc * cycle)
        }
    }

    val answer = list.sum()
    println("pt $pt answer: ${answer colorize ConsoleColor.PURPLE_BRIGHT}")
}


private fun partTwo(pt: Int = 2) {
    val input = InputUtil.readFileAsStringList("2022/day10/input.txt")

    var acc = 1L;
    var output = "\n";

    val queue: ArrayDeque<() -> Unit> = ArrayDeque()
    queue.add {}
    var cycle = 0;
    while (queue.isNotEmpty()) {
        //If the sprite is positioned such that one of its three pixels is the pixel currently being drawn,
        // the screen produces a lit pixel (#); otherwise, the screen leaves the pixel dark (.).
        cycle++;
        queue.removeFirst().invoke()

        val thisRange = acc - 1.. acc + 1
        //the instructions aren't really clear but we have to use cycle % 40 here
        val thisChar = if (cycle % 40 - 1 in thisRange) "#" else "."
        output += thisChar;
        if (cycle % 40 == 0) {
            output += "\n"
        }

        if (input.lastIndex <= cycle - 1) {
            // stop adding to queue
        } else if (input[cycle - 1] == "noop") {
            queue.add {}
        } else {
            val num = input[cycle - 1].substringAfter(" ").toLong()
            queue.add {}
            queue.add { acc += num }
        }
    }

    val answer = output
    println("pt $pt answer: ${answer colorize ConsoleColor.PURPLE_BRIGHT}")
}