package util

fun <A>queueOf(vararg items: A): ArrayDeque<A> {
    return ArrayDeque(items.toList())
}

fun <T> Iterable<T>.takeWhileInclusive(predicate: (T) -> Boolean): List<T> {
    var shouldContinue = true
    return takeWhile {
        val result = shouldContinue
        shouldContinue = predicate(it)
        result
    }
}


fun LongRange.binarySearch(comparison: (Long) -> Int): Long {
    var low = start
    var high = endInclusive

    while (low <= high) {
        val mid = (low + high).ushr(1) // safe from overflows
        val midVal = ((high - low) / 2) + low
        val cmp = comparison(midVal)

        if (cmp < 0)
            low = mid + 1
        else if (cmp > 0)
            high = mid - 1
        else
            return mid // key found
    }
    return -(low + 1)  // key not found
}

fun CharSequence.positionOf(c: Char): Int {
    return this.indexOf(c) + 1;
}

fun String.findAllNumbers(singleDigit: Boolean = false): List<Int> {
    return Regex("-?\\d${if (singleDigit) "" else "+"}").findAll(this).toList().map { it.value.toInt() }
}
fun String.findAllNumbersAsLong(singleDigit: Boolean = false): List<Long> {
    return Regex("-?\\d${if (singleDigit) "" else "+"}").findAll(this).toList().map { it.value.toLong() }
}

fun String.findAll(regexStr: String): List<String> {
    return findAll(Regex(regexStr))
}

fun String.findAll(regex: Regex): List<String> {
    return regex.findAll(this).toList().map { it.value }
}
