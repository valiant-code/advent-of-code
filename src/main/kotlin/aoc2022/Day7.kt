package aoc2022

import util.*

private fun main() {
    //pt 1 - 1118405
    //pt 2 - 12545514
    TimeUtil.startClock(1, ::partOne)
    TimeUtil.startClock(2, ::partTwo)
}

private class Directory(val name: String, val parent: Directory?) {
    init {
        parent?.folders?.set(name, this)
    }

    val files: MutableMap<String, Long> = mutableMapOf()
    val folders: MutableMap<String, Directory> = mutableMapOf()

    fun calcSize(): Long {
        return files.values.sumOf { it } + folders.values.sumOf { it.calcSize() }
    }

    override fun toString(): String {
        return "Directory(name='$name')"
    }
}

private fun readInput(input: List<String>, allDirsMap: MutableMap<String, Directory>) {
    val slash = Directory("/", null)
    var currentDirectory = slash
    allDirsMap[slash.name] = slash

    input.forEach { line ->
        if (line.startsWith('$')) {
            //ls commands get ignored altogether
            if (line.startsWith("$ cd")) {
                val path = line.substring(5)
                currentDirectory = when (path) {
                    "/" -> slash
                    ".." -> currentDirectory.parent!!
                    else -> currentDirectory.folders[currentDirectory.name + "/" + path]!!
                }
            }
        } else if (line[0].isDigit()) {
            // lines that start with a digit are a file listing
            // destructing variable names out is SO much better than split[1] nonsense
            val (value, name) = line.split(" ")
            currentDirectory.files[name] = value.toLong()
        } else { // must be a "dir ..." listing
            val newDirName = currentDirectory.name + "/" + line.substringAfter("dir ")
            val newDir = Directory(newDirName, currentDirectory)
            allDirsMap[newDirName] = newDir
        }
    }
}

private fun partOne(pt: Int = 1) {
    val input = InputUtil.readFileAsStringList("2022/day7/input.txt")
    val allDirs: MutableMap<String, Directory> = mutableMapOf()

    readInput(input, allDirs)
    val smallDirs = allDirs.values.filter {
        it.calcSize() <= 100000
    }

    val answer = smallDirs.map { it.calcSize() }.sumOf { it }
    println("pt $pt answer: ${answer colorize ConsoleColor.PURPLE_BRIGHT}")
}

private fun partTwo(pt: Int = 2) {
    val input = InputUtil.readFileAsStringList("2022/day7/input.txt")
    val allDirs: MutableMap<String, Directory> = mutableMapOf()

    readInput(input, allDirs)

    val neededSpace = 30000000 - (70000000 - allDirs["/"]!!.calcSize())

    val answer = allDirs.values.map { it.calcSize() }
        .filter { it >= neededSpace }
        .minOrNull()!!

    println("pt $pt answer: ${answer colorize ConsoleColor.PURPLE_BRIGHT}")
}