package util

import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

object InputUtil {
    // sample filepath: 2021/day1/input.txt
    fun readFileAsString(filepath: String): String {
        val file = getFileFromResources(filepath)
        return readFile(file.path)
    }

    fun readFileAsStringList(filepath: String, delimiter: String = "\n"): List<String> {
        val file = getFileFromResources(filepath)
        return readFile(file.path).split(delimiter);
    }

    fun readFileAsIntList(filepath: String, delimiter: String = "\n"): List<Int> {
        return readFileAsStringList(filepath, delimiter).map { s: String -> s.toInt() }
    }

    fun readFileAsLongList(filepath: String, delimiter: String = "\n"): List<Long> {
        return readFileAsStringList(filepath, delimiter).map { s: String -> s.toLong() }
    }

    private fun readFile(path: String): String {
        val encoded = Files.readAllBytes(Paths.get(path))
        return String(encoded, StandardCharsets.UTF_8)
    }

    private fun getFileFromResources(fileName: String): File {
        val resource = this::class.java.getResource("../$fileName")
        return if (resource == null) {
            throw IllegalArgumentException("file is not found! : $fileName")
        } else {
            File(resource.file)
        }
    }
}