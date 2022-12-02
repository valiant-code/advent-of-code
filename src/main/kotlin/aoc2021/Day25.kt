import util.*

private fun main() {
    //pt 1 - 509
    //pt 2 -
    TimeUtil.startClock(1, ::partOne)
    TimeUtil.startClock(2, ::partTwo)
}

private val downPosMap = mutableMapOf<CucumberPosition, CucumberPosition>()
private val rightPosMap = mutableMapOf<CucumberPosition, CucumberPosition>()

private fun getPosToRight(
    start: CucumberPosition,
    positionMap: Map<Pair<Int, Int>, CucumberPosition>
): CucumberPosition {
    return rightPosMap.computeIfAbsent(start) {
        positionMap.getOrElse(start.position.copy(second = start.position.second + 1)) {
            positionMap[start.position.copy(
                second = 0
            )]!!
        }
    }
}

private fun getPosToDown(
    start: CucumberPosition,
    positionMap: Map<Pair<Int, Int>, CucumberPosition>
): CucumberPosition {
    return downPosMap.computeIfAbsent(start) {
        positionMap.getOrElse(start.position.copy(first = start.position.first + 1)) {
            positionMap[start.position.copy(
                first = 0
            )]!!
        }
    }
}

private class CucumberPosition(val position: Pair<Int, Int>, var arrow: Char) {
    override fun toString(): String {
        return "$arrow"
    }
}

private fun partOne(pt: Int = 1) {
    val input = InputUtil.readFileAsStringList("2021/day25/input.txt")
    val positionMap: Map<Pair<Int, Int>, CucumberPosition> = input.flatMapIndexed { i, line ->
        line.mapIndexed { j, char -> (i to j) to CucumberPosition(i to j, char) }
    }.toMap()
    val cucumberList = positionMap.values;

    var counter = 1;
    while (stepCucumbers(cucumberList, positionMap)) {
//        printFloorMap(positionMap)
        counter++;

        assert(counter < 10000000)
    }
    printFloorMap(positionMap)

    val answer = counter
    println("pt $pt answer: ${answer colorize ConsoleColor.CYAN_BOLD}")

}

private fun stepCucumbers(
    cucumberList: Collection<CucumberPosition>,
    positionMap: Map<Pair<Int, Int>, CucumberPosition>
): Boolean {
    val eastMoved = mutableListOf<CucumberPosition>()
    cucumberList.filter { it.arrow == '>' }
        .filter { getPosToRight(it, positionMap).arrow == '.' }
        .forEach {
            eastMoved.add(getPosToRight(it, positionMap))
            it.arrow = '.'
        }
    eastMoved.forEach { it.arrow = '>' }

    val southMoved = mutableListOf<CucumberPosition>()
    cucumberList.filter { it.arrow == 'v' }
        .filter { getPosToDown(it, positionMap).arrow == '.' }
        .forEach {
            val next = getPosToDown(it, positionMap)
            southMoved.add(next)
            it.arrow = '.'
        }
    southMoved.forEach { it.arrow = 'v' }

    return eastMoved.size > 0 || southMoved.size > 0
}

private fun printFloorMap(image: Map<Pair<Int, Int>, CucumberPosition>, num: Int = 7) {
    val minX = image.keys.minOf { it.first }
    val maxX = image.keys.maxOf { it.first } + 1
    val minY = image.keys.minOf { it.second }
    val maxY = image.keys.maxOf { it.second } + 1
    println("image: ")
    for (i in minX until maxX) {
        println()
        for (j in minY until maxY) {
            print(
                image.getOrDefault(i to j, CucumberPosition(1 to 1, '.')).toString()
                    .colorizeIf(ConsoleColor.YELLOW_BOLD, ">")
                    .colorizeIf(ConsoleColor.CYAN_BOLD, ("v"))
            );
        }
    }
    println()
}

private fun partTwo(pt: Int = 2) {

}
