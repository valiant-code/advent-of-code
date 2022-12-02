import util.ConsoleColor
import util.InputUtil
import util.TimeUtil
import util.colorize

private fun main() {
    //pt 1 - 374994
    //pt 2 - 1686252324092
    TimeUtil.startClock(1, ::partOne)
    TimeUtil.startClock(2, ::partTwo)
}

private data class LanternFish(var daysToSpawn: Int = 8) {
    fun spendDay(): LanternFish? {
        daysToSpawn -= 1;
        if (daysToSpawn < 0) {
            daysToSpawn = 6
            return LanternFish()
        } else return null
    }
}

private fun partOne(pt: Int = 1) {
    //How many lanternfish would there be after 80 days?
    val input = InputUtil.readFileAsIntList("2021/day6/input.txt", ",");
    val fishList = input.map { num -> LanternFish(num) }.toMutableList()

    //fish takes 7 days to produce a new fish
    //+ 2 days for first cycle of a new fish

    for (i in 1..80) {
        val newFish = mutableListOf<LanternFish>()
        fishList.forEach { fish -> fish.spendDay()?.let { newFish.add(it) } }
        fishList.addAll(newFish);
    }

    println("pt $pt answer: ${fishList.size colorize ConsoleColor.CYAN_BOLD}")
}

private fun partTwo(pt: Int = 2) {
    //How many lanternfish would there be after 256 days?
    val input = InputUtil.readFileAsIntList("2021/day6/input.txt", ",")
    val fishMap = input.groupBy { it }
        //make sure to make these are Longs because we exceed Int.maxValue otherwise
        .map { it.key.toLong() to it.value.size.toLong() }
        .toMap().toMutableMap()

    // naive solution of modeling the fish won't work at this scale
    // track fish per day with a map
    for (i in 1..256) {
        val spawningFish = fishMap[0] ?: 0
        //each day, move the values up 1 day
        fishMap[0] = fishMap[1] ?: 0
        fishMap[1] = fishMap[2] ?: 0
        fishMap[2] = fishMap[3] ?: 0
        fishMap[3] = fishMap[4] ?: 0
        fishMap[4] = fishMap[5] ?: 0
        fishMap[5] = fishMap[6] ?: 0
        //add the spawning fish to both day6 and day8
        fishMap[6] = (fishMap[7] ?: 0) + spawningFish;
        fishMap[7] = fishMap[8] ?: 0
        fishMap[8] = spawningFish;
    }

    println("pt $pt answer: ${fishMap.values.sumOf { it } colorize ConsoleColor.CYAN_BOLD}")
}


