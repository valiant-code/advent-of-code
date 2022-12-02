import util.*

private fun main() {
    //pt 1 - 5225
    //pt 2 - 18131
    TimeUtil.startClock(1, ::partOne)
    TimeUtil.startClock(2, ::partTwo)
}

fun getBorderPoints(minX: Int, maxX: Int, minY: Int, maxY: Int): MutableSet<Pair<Int, Int>> {
    val border = mutableSetOf(
        minX - 1 to minY - 1,
        minX - 1 to maxY + 1,
        maxX + 1 to maxY + 1,
        maxX + 1 to minY - 1,
    )

    for (y in minY..maxY) {
        border.add(minX - 1 to y)
        border.add(maxX + 1 to y)
    }

    for (x in minX..maxX) {
        border.add(x to minY - 1)
        border.add(x to maxY + 1)
    }
    return border
}

fun enhance(
    image: Map<Pair<Int, Int>, Char>,
    imageEnhancementString: String,
    infiniteChar: Char
): Map<Pair<Int, Int>, Char> {
    val minX = image.keys.minOf { it.first }
    val maxX = image.keys.maxOf { it.first }
    val minY = image.keys.minOf { it.second }
    val maxY = image.keys.maxOf { it.second }

    val border = getBorderPoints(minX, maxX, minY, maxY)
//    border.addAll(getBorderPoints(minX - 1, maxX + 1, minY - 1, maxY + 1))

    val newImage = setOf(image.entries.map { it.key }, border).flatten().associateWith { coordinate ->
        //example: (5,10)
        val threeByThree = listOf(
            // (4,9), (4,10), (4,11)
            coordinate.copy(first = coordinate.first - 1, second = coordinate.second - 1),
            coordinate.copy(first = coordinate.first - 1),
            coordinate.copy(first = coordinate.first - 1, second = coordinate.second + 1),
            // (5,9), (5,10), (5,11)
            coordinate.copy(second = coordinate.second - 1),
            coordinate,
            coordinate.copy(second = coordinate.second + 1),
            // (6,9), (6,10), and (6,11)
            coordinate.copy(first = coordinate.first + 1, second = coordinate.second - 1),
            coordinate.copy(first = coordinate.first + 1),
            coordinate.copy(first = coordinate.first + 1, second = coordinate.second + 1),
        )

        val newPixel = threeByThree.map { image.getOrDefault(it, infiniteChar) }.joinToString("").toInt(2)
        if (imageEnhancementString[newPixel] == '#') '1' else '0'
    }

    return newImage;
}

fun printd20Map(image: Map<Pair<Int, Int>, Char>, num: Int = 15) {
    val minX = image.keys.minOf { it.first } - 2
    val maxX = image.keys.maxOf { it.first } + 2
    val minY = image.keys.minOf { it.second } - 2
    val maxY = image.keys.maxOf { it.second } + 2
    println("image: ")
    for (i in minX until maxX) {
        println()
        for (j in minY until maxY) {
            print(image.getOrDefault(i to j, '0').colorizeIf(ConsoleColor.YELLOW_BOLD, "0"));
        }
    }
    println()
}

private fun partOne(pt: Int = 1) {
    val input = InputUtil.readFileAsStringList("2021/day20/input.txt", "\n\n")
    val imageEnhancementString = input[0]

    var image = input[1].split("\n").flatMapIndexed { i, line ->
        line.mapIndexed { j, char -> Pair(i, j) to (if (char == '#') '1' else '0') }
    }.toMap()
//    printd20Map(image, 6)
    var infiniteChar = '0'

    for (i in 1..2) {
        image = enhance(image, imageEnhancementString, infiniteChar)
//        printd20Map(image)

        if (infiniteChar == '0' && imageEnhancementString[0] == '#') {
            infiniteChar = '1'
        } else if (infiniteChar == '1' && imageEnhancementString.last() == '.') {
            infiniteChar = '0'
        }
    }

    //5255 too high
    val answer = image.values.count { it == '1' }
    println("pt $pt answer: ${answer colorize ConsoleColor.CYAN_BOLD}")
}

private fun partTwo(pt: Int = 2) {
    val input = InputUtil.readFileAsStringList("2021/day20/input.txt", "\n\n")
    val imageEnhancementString = input[0]
    var image = input[1].split("\n").flatMapIndexed { i, line ->
        line.mapIndexed { j, char -> Pair(i, j) to (if (char == '#') '1' else '0') }
    }.toMap()
    var infiniteChar = '0'

    for (i in 1..50) {
        image = enhance(image, imageEnhancementString, infiniteChar)
        if (infiniteChar == '0' && imageEnhancementString[0] == '#') {
            infiniteChar = '1'
        } else if (infiniteChar == '1' && imageEnhancementString.last() == '.') {
            infiniteChar = '0'
        }
    }

    //19288 too high
    val answer = image.values.count { it == '1' }
    println("pt $pt answer: ${answer colorize ConsoleColor.CYAN_BOLD}")
}
