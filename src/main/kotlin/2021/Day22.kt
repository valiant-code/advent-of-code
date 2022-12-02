import util.ConsoleColor
import util.InputUtil
import util.TimeUtil
import util.colorize

private fun main() {
    //pt 1 - 474140
    //pt 2 -
    TimeUtil.startClock(1, ::partOne)
    TimeUtil.startClock(2, ::partTwo)
}

private data class RebootInstruction(val turnOn: Boolean, val cuboid: Cuboid) {

}

private data class Cuboid(val xRange: IntRange, val yRange: IntRange, val zRange: IntRange, var on: Boolean = false) {

}

fun IntRange.Companion.fromString(s: String): IntRange {
    return IntRange(s.split("..")[0].toInt(), s.split("..")[1].toInt())
}

private fun outsideValidRange(it: RebootInstruction): Boolean {
    val validRange = -50..50;

    return it.cuboid.xRange.first !in validRange || it.cuboid.xRange.last !in validRange ||
            it.cuboid.yRange.first !in validRange || it.cuboid.yRange.last !in validRange ||
            it.cuboid.zRange.first !in validRange || it.cuboid.zRange.last !in validRange
}

private fun partOne(pt: Int = 1) {
    val input = InputUtil.readFileAsStringList("2021/day22/input.txt")
    //on x=11..13,y=11..13,z=11..13
    val instructionList = input.map {
        RebootInstruction(
            it.contains("on"), Cuboid(
                IntRange.fromString(it.split(",")[0].substringAfter("=")),
                IntRange.fromString(it.split(",")[1].substringAfter("=")),
                IntRange.fromString(it.split(",")[2].substringAfter("=")),
            )
        )
    }

    val validInstructions = instructionList.filterNot(::outsideValidRange)

    val reactorMap = mutableMapOf<Triple<Int, Int, Int>, Boolean>()

    for (instruction in validInstructions) {
        for (x in instruction.cuboid.xRange) {
            for (y in instruction.cuboid.yRange) {
                for (z in instruction.cuboid.zRange) {
                    reactorMap[Triple(x, y, z)] = instruction.turnOn
                }
            }
        }
    }

    val answer = reactorMap.values.count { it }
    println("pt $pt answer: ${answer colorize ConsoleColor.CYAN_BOLD}")
}

private fun partTwo(pt: Int = 2) {
//    val input = util.InputUtil.readFileAsStringList("2021/day22/input.txt")
//    //on x=11..13,y=11..13,z=11..13
//    val instructionList = input.map {
//        RebootInstruction(
//            it.contains("on"), Cuboid(
//                IntRange.fromString(it.split(",")[0].substringAfter("=")),
//                IntRange.fromString(it.split(",")[1].substringAfter("=")),
//                IntRange.fromString(it.split(",")[2].substringAfter("=")),
//                it.contains("on")
//            )
//        )
//    }
//        //.filterNot(::outsideValidRange)
//
//    val reactorMap = mutableMapOf<Triple<Int, Int, Int>, Boolean>()
//
//    for (instruction in instructionList) {
//        for (x in instruction.cuboid.xRange) {
//            for (y in instruction.cuboid.yRange) {
//                for (z in instruction.cuboid.zRange) {
//                    reactorMap[Triple(x, y, z)] = instruction.turnOn
//                }
//            }
//        }
//    }
//
//    val answer = reactorMap.values.sumOf { if (it) 1L else 0 }
//    println("pt $pt answer: ${answer util.colorize util.ConsoleColor.CYAN_BOLD}")
}
