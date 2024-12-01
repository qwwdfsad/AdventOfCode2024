import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.math.abs


fun readInput(filename: String): List<String> = Path("/Users/qwwdfsad/Desktop/aoc/$filename").readLines()

fun main() {
    val lines = readInput("day01.txt")
    val (left, right) = lines.map { line ->
        val first = line.substringBefore(" ").toInt()
        val second = line.substringAfterLast(" ").toInt()
        first to second
    }.unzip()

    left.sorted().zip(right.sorted()).sumOf { (first, second) ->
        abs(first - second)
    }.also(::println)
}
