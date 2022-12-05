package util

fun CharSequence.positionOf(c: Char): Int {
    return this.indexOf(c) + 1;
}

fun String.findAllNumbers(singleDigit: Boolean = false): List<Int> {
    return Regex("\\d${if (singleDigit) "" else "+"}").findAll(this).toList().map { it.value.toInt() }
}

fun String.findAll(regexStr: String): List<String> {
    return Regex(regexStr).findAll(this).toList().map { it.value }
}

fun String.findAll(regex: Regex): List<String> {
    return regex.findAll(this).toList().map { it.value }
}
