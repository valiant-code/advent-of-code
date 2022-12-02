import util.ConsoleColor
import util.InputUtil
import util.TimeUtil
import util.colorize

private fun main() {
    //pt 1 - 3118
    //pt 2 - 4332887448171
    TimeUtil.startClock(1, ::partOne)
    TimeUtil.startClock(2, ::partTwo)
}


private fun partOne(pt: Int = 1) {
    val input = InputUtil.readFileAsStringList("2021/day14/input.txt", "\n\n")
    var polymer = input[0].toMutableList()
    val pairRules = input[1].split("\n")
        .map { it.substringBefore(" -> ") to it.last() }
        .toMap()

    for (i in 1..10) {
        val first = polymer.first();
        polymer = polymer.windowed(2).map {
            val insertion = it.toMutableList()
            insertion.removeAt(0)
            insertion.add(0, pairRules[it.joinToString("")]!!)
            insertion
        }
            .flatMap { it }
//            .onEach { println(it) }
            .toMutableList()
        polymer.add(0, first)
    }
//    println(polymer.joinToString (""))
    val counts = polymer.groupBy { it }.map { it.value.count() }.sortedDescending();
    val answer = counts.first() - counts.last()
    println("pt $pt answer: ${answer colorize ConsoleColor.CYAN_BOLD}")
}

fun <A, B> List<Pair<A, B>>.toMap(conflictMerger: (B, B) -> B): Map<A, B> {
    return this.groupBy { it.first }
        .map { groupBy -> groupBy.key to groupBy.value.map { it.second }.reduce(conflictMerger) }
        .toMap()
}

private fun partTwo(pt: Int = 2) {
    val input = InputUtil.readFileAsStringList("2021/day14/input.txt", "\n\n")
    //instead of maintaining the polymer string, we will keep a count map of how many times each pair occurs
    var polymerPairs: Map<String, Long> = input[0].toMutableList().windowed(2)
        .map { "${it[0]}${it[1]}" }
        .groupBy { it }
        .map { it.key to it.value.count().toLong() }
        .toMap()

    //the rules map each pair AB, to it's 2 resulting pairs generated by the mapping, AX and XB
    val pairRules = input[1].split("\n")
        .map { it.substringBefore(" -> ") to listOf("${it[0]}${it.last()}", "${it.last()}${it[1]}") }
        .toMap()
    val firstAndLast = listOf(input[0].first(), input[0].last())

    for (i in 1..40) {
        polymerPairs = polymerPairs.keys
            //for each pair X with count Y, map it to the 2 new pairs, A to Y, B to Y
            .flatMap { pair -> pairRules[pair]!!.map { newPair -> newPair to (polymerPairs[pair] ?: 0L) } }
            //transform the pair list into a map, summing any matching keys
            .toMap(Long::plus)
    }
    val finishedMap = polymerPairs.entries
        //transform the Pair-count map, into a Char-count map
        .flatMap { entry -> entry.key.toList().map { it to entry.value } }
        .toMap(Long::plus)
        //all values (except for the very first and last characters) have been double counted, handle that by dividing
        .mapValues { (it.value + if (it.key in firstAndLast) 1 else 0) / 2 }
    val counts = finishedMap.values.sortedDescending();
    val answer = counts.first() - counts.last()
    println("pt $pt answer: ${answer colorize ConsoleColor.CYAN_BOLD}")
}


