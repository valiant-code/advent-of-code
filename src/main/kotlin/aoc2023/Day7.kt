package aoc2023

import util.*

private fun main() {
    //pt 1 answer: 246708697
    //Pt 1 Time Taken: 0.063
    //pt 2 answer: 244848487
    //Pt 2 Time Taken: 0.007
    TimeUtil.startClock(1, ::partOne)
    TimeUtil.startClock(2, ::partTwo)
}

private class CamelCard(val label: Int, val strVal: String) : Comparable<CamelCard> {
    override fun compareTo(other: CamelCard): Int {
        return label.compareTo(other.label)
    }

    override fun toString(): String {
        return strVal
    }
}

private enum class HandStrength(val value: Int) {
    FIVE_OF_KIND(5 * 2),
    FOUR_OF_KIND(4 * 2),
    FULL_HOUSE(3 * 2 + 1),
    THREE_OF_KIND(3 * 2),
    TWO_PAIR(2 * 2 + 1),
    TWO_OF_KIND(2 * 2),
    ONE_OF_KIND(1 * 2),
    EMPTY(0),
}

private class CamelHand(val cards: List<CamelCard>, val bid: Int, val pt: Int = 1) : Comparable<CamelHand> {
    //    va pairs: List<CamelCard>
    var typeRank: HandStrength
    val amountToCountMap: Map<Int, Int> = cards.map { it.label }.groupBy { it }
        .map { it.value.size }.groupBy { it }.map { it.key to it.value.size }.toMap()

    val labelToCountMap = cards.map { it.label }.groupBy { it }
    val amountToCountMapWithoutJokers = cards.map { it.label }.filterNot { it == 1 }.groupBy { it }
        .map { it.value.size }.groupBy { it }.map { it.key to it.value.size }.toMap().toMutableMap();

    init {
        val jokers = labelToCountMap[1]?.size ?: 0
        typeRank = if (pt == 2) {
            calcHandStrength(amountToCountMapWithoutJokers)
        } else {
            calcHandStrength(amountToCountMap)
        }

        if (pt == 2 && jokers > 0) {
            //let's just manually handle the scenarios for adding jokers to make the best hand
            typeRank = if (jokers == 5) HandStrength.FIVE_OF_KIND
            else if (typeRank == HandStrength.FOUR_OF_KIND) HandStrength.FIVE_OF_KIND
            else if (typeRank == HandStrength.THREE_OF_KIND && jokers >= 2) HandStrength.FIVE_OF_KIND
            else if (typeRank == HandStrength.THREE_OF_KIND) HandStrength.FOUR_OF_KIND
            else if (typeRank == HandStrength.TWO_PAIR) HandStrength.FULL_HOUSE
            else if (typeRank == HandStrength.TWO_OF_KIND && jokers == 3) HandStrength.FIVE_OF_KIND
            else if (typeRank == HandStrength.TWO_OF_KIND && jokers == 2) HandStrength.FOUR_OF_KIND
            else if (typeRank == HandStrength.TWO_OF_KIND && jokers == 1) HandStrength.THREE_OF_KIND
            else if (typeRank == HandStrength.ONE_OF_KIND && jokers == 4) HandStrength.FIVE_OF_KIND
            else if (typeRank == HandStrength.ONE_OF_KIND && jokers == 3) HandStrength.FOUR_OF_KIND
            else if (typeRank == HandStrength.ONE_OF_KIND && jokers == 2) HandStrength.THREE_OF_KIND
            else if (typeRank == HandStrength.ONE_OF_KIND && jokers == 1) HandStrength.TWO_OF_KIND
            else HandStrength.EMPTY
        }
    }

    private fun calcHandStrength(amountCounts: Map<Int, Int>) = if (amountCounts.containsKey(5)) HandStrength.FIVE_OF_KIND
    else if (amountCounts.containsKey(4)) HandStrength.FOUR_OF_KIND
    else if (amountCounts.containsKey(3) && amountCounts.containsKey(2)) HandStrength.FULL_HOUSE
    else if (amountCounts.containsKey(3)) HandStrength.THREE_OF_KIND
    else if (amountCounts.containsKey(2) && amountCounts[2]!! > 1) HandStrength.TWO_PAIR
    else if (amountCounts.containsKey(2)) HandStrength.TWO_OF_KIND
    else if (amountCounts.containsKey(1)) HandStrength.ONE_OF_KIND
    else HandStrength.EMPTY

    override fun compareTo(other: CamelHand): Int {
        val initialCheck = typeRank.value.compareTo(other.typeRank.value)
        return if (initialCheck != 0) initialCheck
        else cards.mapIndexed { index, card -> card.compareTo(other.cards[index]) }.filterNot { it == 0 }.first()
    }

    override fun toString(): String {
        return "Rank=$typeRank CamelHand(cards=$cards)"
    }
}

private fun mapValue(card: String): Int {
    return when (card) {
        "J" -> 1
        "T" -> 10
        "Q" -> 12
        "K" -> 13
        "A" -> 14
        else -> card.toInt()
    }

}

private fun partOne(pt: Int = 1) {
    //32T3K 765
    //T55J5 684
    //A, K, Q, J, T, 9, 8, 7, 6, 5, 4, 3, or 2
    val input = InputUtil.readFileAsStringList("2023/day7input.txt");
    val hands = input.map { line ->
        val (cardsStr, bid) = line.split(" ")
        val cards = cardsStr.map { CamelCard(mapValue(it.toString()), it.toString()) }
        CamelHand(cards, bid.toInt())
    }
    val sortedHands = hands.sorted()


    val answer = sortedHands.mapIndexed { index, hand -> hand.bid * (index + 1) }.sum()
    println("pt $pt answer: ${answer colorize ConsoleColor.BLUE_BRIGHT}")
}


private fun partTwo(pt: Int = 2) {
    //32T3K 765
    //T55J5 684
    //A, K, Q, J, T, 9, 8, 7, 6, 5, 4, 3, or 2
    val input = InputUtil.readFileAsStringList("2023/day7input.txt");
    val hands = input.map { line ->
        val (cardsStr, bid) = line.split(" ")
        val cards = cardsStr.map { CamelCard(mapValue(it.toString()), it.toString()) }
        CamelHand(cards, bid.toInt(), pt = 2)
    }
    val sortedHands = hands.sorted()


    val answer = sortedHands.mapIndexed { index, hand -> hand.bid * (index + 1) }.sum()
    println("pt $pt answer: ${answer colorize ConsoleColor.BLUE_BRIGHT}")
}