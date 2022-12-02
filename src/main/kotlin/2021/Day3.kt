import util.InputUtil
import util.TimeUtil
import kotlin.math.roundToInt

private fun main() {
    //pt 1 - 2583164
    //pt 2 - 2784375
    TimeUtil.startClock(1, ::partOne)
    TimeUtil.startClock(2, ::partTwo)
}

//More complex way that doesn't handle ties - arr.groupBy { it }.mapValues { it.value.size }.maxBy { it.value }?.key
private fun getMostCommonBit(list: List<String>, i: Int): Boolean {
    val countOf1s = list.map { it[i] }.filter { it == '1' }.count();
    val halfTheInput = (list.size / 2.0).roundToInt();
    return countOf1s >= halfTheInput
}

//extension method for the fun of it and to make converting from boolean to 1/0 easier
private fun Boolean.toInt() = if (this) 1 else 0

private fun partOne() {
    //Each bit in the gamma rate can be determined by finding the most common bit in the
    // corresponding position of all numbers in the diagnostic report

    //The epsilon rate is calculated in a similar way
    // the least common bit from each position is used
    val input = InputUtil.readFileAsStringList("2021/day3/input.txt");

    var gamma = "" //most common bits
    var epsilon = "" //least common bits
    for (i in 0 until input[0].length) {
        val mostCommon = getMostCommonBit(input, i).toInt().digitToChar()
        gamma += mostCommon;
        epsilon += if (mostCommon == '1') '0' else '1';
    }
    val gParsed = gamma.toInt(2);
    val eParsed = epsilon.toInt(2);

    //The power consumption can then be found by multiplying the gamma rate by the epsilon rate.
    println("pt 1 answer: ${gParsed * eParsed}")
}

private fun partTwo() {
    val input = InputUtil.readFileAsStringList("2021/day3/input.txt");

    var oxyRatingList = input;
    var carRatingList = input;
    for (currentBitIndex in 0 until input[0].length) {
        if (oxyRatingList.size > 1) {
            val currentMostCommonBit = getMostCommonBit(oxyRatingList, currentBitIndex).toInt();
            // keep only numbers with the MostCommonBit in position[i].
            oxyRatingList = oxyRatingList.filter { it[currentBitIndex].digitToInt() == currentMostCommonBit }
        }
        if (carRatingList.size > 1) {
            val currentLeastCommonBit = (!getMostCommonBit(carRatingList, currentBitIndex)).toInt()
            // keep only numbers with the LeastCommonBit in position[i].
            carRatingList =
                carRatingList.filter { it[currentBitIndex].digitToInt() == currentLeastCommonBit }
        }
        if (carRatingList.size <= 1 && oxyRatingList.size <= 1) {
            //if the lists are both 1 element, we can stop looping through the bits
            break;
        }
    }

    //Finally, to find the life support rating,
    // multiply the oxygen generator rating by the CO2 scrubber rating
    println("pt 2 answer: ${oxyRatingList[0].toInt(2) * carRatingList[0].toInt(2)}")
}


