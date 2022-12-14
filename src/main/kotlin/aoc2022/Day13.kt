package aoc2022

import util.ConsoleColor
import util.InputUtil
import util.TimeUtil
import util.colorize
import java.util.concurrent.atomic.AtomicInteger

private fun main() {
    //pt 1 - 5852
    //pt 2 - 24190
    TimeUtil.startClock(1, ::partOne)
    TimeUtil.startClock(2, ::partTwo)
}

private class SignalPairItem(val items: MutableList<SignalPairItem> = mutableListOf(), var value: Int? = null) {
    constructor(
        input: String,
        replacementMap: MutableMap<Int, SignalPairItem> = mutableMapOf(),
        counter: AtomicInteger = AtomicInteger(-1)
    ) : this() {
        //sample line [4,4],4,4
        var currLine = input
        var closeIndex = currLine.indexOfFirst { it == ']' }
        //go through the line, parsing any sub lists and replacing them with temporary negative numbers
        while (closeIndex > -1) {
            val sublist = currLine.substringBefore(']').substringAfterLast('[')
            val subItem = SignalPairItem(sublist, replacementMap, counter)
            val subItemId = counter.decrementAndGet()
            replacementMap[subItemId] = subItem
            currLine = currLine.replaceFirst("[$sublist]", subItemId.toString())
            closeIndex = currLine.indexOfFirst { it == ']' }
        }
        //we now have comma separated list of numbers (or null)
        items.addAll(currLine.split(",")
            .map { if (it.isNotEmpty()) it.toInt() else null }
            .mapNotNull {
                //if its positive or null, make a new item with that value
                if (it == null || it >= 0) {
                    SignalPairItem(value = it)
                } else {
                    //negative numbers are sub-lists and have already been parsed into items, retrieve it from the map
                    replacementMap[it]
                }
            })
    }

    fun compareOrder(other: SignalPairItem): Int {
        //shortcut, are they equal?
        if (this.toString() == other.toString())
            return 0
        //If both values are integers, the lower integer should come first.
        val thisIsInteger = this.value != null
        val otherIsInteger = other.value != null
        if (thisIsInteger && otherIsInteger) {
            return this.value!!.compareTo(other.value!!)
        }
        //If exactly one value is an integer,
        // convert the integer to a list which contains that integer as its only value, then retry the comparison.
        if (thisIsInteger || otherIsInteger) {
            when (thisIsInteger) {
                true -> {
                    this.items.add(SignalPairItem(value = this.value!!))
                    this.value = null
                }
                false -> {
                    other.items.add(SignalPairItem(value = other.value!!))
                    other.value = null
                }
            }
        }


        //If both values are lists
        for (i in 0 until items.size) {
            // If the right list runs out of items first, the inputs are in the wrong order.
            if (i >= other.items.size)
                return 1
            // compare the first value of each list, then the second value, and so on.
            val compare = this.items[i].compareOrder(other.items[i])
            if (compare != 0)
                return compare
        }
        // If the left list runs out of items first, the inputs are in the right order.
        if (other.items.size > this.items.size) return -1

        return 0
    }

    override fun toString(): String {
        return "${if (items.size == 0) (value ?: "") else items.toString()}"
    }


}

private fun partOne(pt: Long = 1) {
    val input = InputUtil.readFileAsStringList("2022/day13/input.txt", "\n\n")
        .map { pair ->
            val (p1, p2) = pair.split("\n")
                .map { str -> str.substring(1, str.length - 1) }
                .map {
                    SignalPairItem(it)
                }
            p1 to p2
        }

    //Determine which pairs of packets are already in the right order. What is the sum of the indices of those pairs?
    // INDEX STARTS AT 1
    val answer = input
        .mapIndexed { index, pair -> if (pair.first.compareOrder(pair.second) <= 0) index + 1 else 0 }
        .sum()

    println("pt $pt answer: ${answer colorize ConsoleColor.PURPLE_BRIGHT}")
}

private fun partTwo(pt: Long = 2) {
    val dividerA = SignalPairItem("[2]")
    val dividerB = SignalPairItem("[6]")
    val input = InputUtil.readFileAsStringList("2022/day13/input.txt", "\n")
        .filterNot { it.isEmpty() }
        .map { line ->
            SignalPairItem(line.substring(1, line.length - 1))
        }.toMutableList()
    input.add(dividerA)
    input.add(dividerB)
    val sortedList = input.sortedWith(SignalPairItem::compareOrder)

    //Fine the index of the 2 dividers and multiply them
    // INDEX STARTS AT 1
    val answer = (sortedList.indexOf(dividerA) + 1) * (sortedList.indexOf(dividerB) + 1)

    println("pt $pt answer: ${answer colorize ConsoleColor.PURPLE_BRIGHT}")
}