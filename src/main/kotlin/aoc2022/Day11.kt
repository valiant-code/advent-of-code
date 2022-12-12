package aoc2022

import util.*

private fun main() {
    //pt 1 -
    //pt 2 -
    TimeUtil.startClock(1, ::partOne)
    TimeUtil.startClock(2, ::partTwo)
}

private class MonkeyItems(val number: Int,
                          val items: MutableList<Long>,
                          val operation: (x:Long) -> Long,
                          val test: (x:Long) -> Boolean,
                          val testVal: Long,
                          val ifTrue: Int,
                          val ifFalse: Int,
) {
    var inspectionCount = 0

    fun mapItemsModDivisor(divisor: Long) {
        val mappedWorryLevels = items.map { it % divisor }
        items.clear()
        items.addAll(mappedWorryLevels)
    }

}

private fun partOne(pt: Long = 1) {
    //Monkey 2:
    //  Starting items: 79, 60, 97
    //  Operation: new = old * old
    //  Test: divisible by 13
    //    If true: throw to monkey 1
    //    If false: throw to monkey 3
    val input = InputUtil.readFileAsStringList("2022/day11/input.txt", "\n\n")
        .map {
            val parts = it.split("\n")
            val num = parts[0].findAllNumbers().first()
            val startingItems = parts[1].findAllNumbers().map { n -> n.toLong() }

            val operation: (Long) -> Long = { x ->
                val equation = parts[2].substringAfter("= ")
                val first =
                    if (equation.substringBefore(" ") == "old") x
                    else equation.substringBefore(" ").toLong()
                val second =
                    if (equation.substringAfterLast(" ") == "old") x
                    else equation.substringAfterLast(" ").toLong()
                val isPlus = equation.contains("+")
                if (isPlus) {
                    first + second
                } else {
                    first * second
                }
            }

            val testVal = parts[3].findAllNumbers().first().toLong()
            val test: (Long) -> Boolean = { x ->
                x % parts[3].findAllNumbers().first().toLong() == 0L
            }

            val testTrue = parts[4].findAllNumbers().first()
            val testFalse = parts[5].findAllNumbers().first()

            MonkeyItems(num, startingItems.toMutableList(), operation, test, testVal, testTrue, testFalse)
        }.associateBy { it.number }

    for (i in 1..20) {
        input.values.forEach { monkey ->
            monkey.items.forEach { worryLevel ->
                monkey.inspectionCount++
                val newLevel = monkey.operation(worryLevel) / 3
                val throwTo = if (monkey.test(newLevel)) monkey.ifTrue else monkey.ifFalse
                input[throwTo]!!.items.add(newLevel)
            }
            monkey.items.clear();
        }

    }

    val answer = input.values.map { it.inspectionCount }.sortedDescending().take(2).reduce { acc, i -> acc * i }
    println("pt $pt answer: ${answer colorize ConsoleColor.PURPLE_BRIGHT}")
}


private fun partTwo(pt: Long = 2) {
    //Monkey 2:
    //  Starting items: 79, 60, 97
    //  Operation: new = old * old
    //  Test: divisible by 13
    //    If true: throw to monkey 1
    //    If false: throw to monkey 3
    val input = InputUtil.readFileAsStringList("2022/day11/input.txt", "\n\n")
        .map {
            val parts = it.split("\n")
            val num = parts[0].findAllNumbers().first()
            val startingItems = parts[1].findAllNumbers().map { n -> n.toLong() }

            val operation: (Long) -> Long = { x ->
                val equation = parts[2].substringAfter("= ")
                val first =
                    if (equation.substringBefore(" ") == "old") x
                    else equation.substringBefore(" ").toLong()
                val second =
                    if (equation.substringAfterLast(" ") == "old") x
                    else equation.substringAfterLast(" ").toLong()
                val isPlus = equation.contains("+")
                if (isPlus) {
                    first + second
                } else {
                    first * second
                }
            }

            val testVal = parts[3].findAllNumbers().first().toLong();
            val test: (Long) -> Boolean = { x ->
                x % parts[3].findAllNumbers().first().toLong() == 0L
            }

            val testTrue = parts[4].findAllNumbers().first()
            val testFalse = parts[5].findAllNumbers().first()

            MonkeyItems(num, startingItems.toMutableList(), operation, test, testVal, testTrue, testFalse)
        }.associateBy { it.number }

    val divisorsProduct = input.values.map { it.testVal }.reduce(Long::times)


    for (i in 1..10000) {
        input.values.forEach { monkey ->
            monkey.mapItemsModDivisor(divisorsProduct)
            monkey.items.forEach { worryLevel ->
                monkey.inspectionCount++
                val newLevel = monkey.operation(worryLevel)
                val throwTo =
                    if (monkey.test(newLevel))
                        monkey.ifTrue
                    else
                        monkey.ifFalse
                input[throwTo]!!.items.add(newLevel)
            }
            monkey.items.clear();
        }

    }

    val a1 = input.values.map { it.inspectionCount }.sortedDescending().take(2);
    val answer =  a1.map { it.toLong() }.reduce(Long::times)
    println("pt $pt answer: ${answer colorize ConsoleColor.PURPLE_BRIGHT}")
}