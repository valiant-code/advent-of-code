package util

fun <T> Iterable<T>.takeWhileInclusive(predicate: (T) -> Boolean): List<T> {
    var shouldContinue = true
    return takeWhile {
        val result = shouldContinue
        shouldContinue = predicate(it)
        result
    }
}

fun CharSequence.positionOf(c: Char): Int {
    return this.indexOf(c) + 1;
}

fun String.findAllNumbers(singleDigit: Boolean = false): List<Int> {
    return Regex("\\d${if (singleDigit) "" else "+"}").findAll(this).toList().map { it.value.toInt() }
}

fun String.findAll(regexStr: String): List<String> {
    return findAll(Regex(regexStr))
}

fun String.findAll(regex: Regex): List<String> {
    return regex.findAll(this).toList().map { it.value }
}
