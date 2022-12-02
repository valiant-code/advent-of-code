import util.ConsoleColor
import util.InputUtil
import util.TimeUtil
import util.colorize

private fun main() {
    //pt 1 - 4912
    //pt 2 - 150004
    TimeUtil.startClock(1, ::partOne)
    TimeUtil.startClock(2, ::partTwo)
}
private const val start = "start"
private const val end = "end"
private val upperCase = Regex("[A-Z]+")

private fun getPossibilities(choices: MutableList<String>, nextChoice: String, map: Map<String, Set<String>>): MutableSet<MutableList<String>> {
    choices.add(nextChoice)
    if (nextChoice == end) return mutableSetOf(choices);

    val nextOptions = map.getOrDefault(nextChoice, emptySet())
        .filter { upperCase.matches(it) || !choices.contains(it) }

    val nextChoices: MutableSet<MutableList<String>> = mutableSetOf();
    nextOptions.forEach { choice ->
        nextChoices.addAll(getPossibilities(choices.toMutableList(), choice, map))
    }

    return nextChoices;
}

private fun hasTwoSmallCaves(choices: List<String>): Boolean {
    return choices.filter { !upperCase.matches(it) }.groupBy { it }.any { it.value.size > 1 }
}

private fun getPossibilitiesPt2(choices: MutableList<String>, nextChoice: String, map: Map<String, Set<String>>): MutableSet<MutableList<String>> {
    choices.add(nextChoice)
    if (nextChoice == end) return mutableSetOf(choices);

    val nextOptions = map.getOrDefault(nextChoice, emptySet()).filterNot { it == start }
        .filter { upperCase.matches(it) || !choices.contains(it) || !hasTwoSmallCaves(choices)}

    val nextChoices: MutableSet<MutableList<String>> = mutableSetOf();
    nextOptions.forEach { choice ->
        nextChoices.addAll(getPossibilitiesPt2(choices.toMutableList(), choice, map))
    }

    return nextChoices;
}


private fun partOne(pt: Int = 1) {
    val input = InputUtil.readFileAsStringList("2021/day12/input.txt")
    val connectionsMap: MutableMap<String, MutableSet<String>> = mutableMapOf();

    input.forEach{mapping ->
        val a = mapping.substringBefore("-")
        val b = mapping.substringAfter("-")
        connectionsMap.getOrPut(a, ::mutableSetOf).add(b);
        connectionsMap.getOrPut(b, ::mutableSetOf).add(a);
    }

    val possibilities = getPossibilities(mutableListOf(), start, connectionsMap);

    val answer = possibilities.size;
    println("pt $pt answer: ${answer colorize ConsoleColor.CYAN_BOLD}")
}

private fun partTwo(pt: Int = 2) {
    val input = InputUtil.readFileAsStringList("2021/day12/input.txt")
    val connectionsMap: MutableMap<String, MutableSet<String>> = mutableMapOf();

    input.forEach{mapping ->
        val a = mapping.substringBefore("-")
        val b = mapping.substringAfter("-")
        connectionsMap.getOrPut(a, ::mutableSetOf).add(b);
        connectionsMap.getOrPut(b, ::mutableSetOf).add(a);
    }

    val possibilities = getPossibilitiesPt2(mutableListOf(), start, connectionsMap);

    val answer = possibilities.size;
    println("pt $pt answer: ${answer colorize ConsoleColor.CYAN_BOLD}")
}


