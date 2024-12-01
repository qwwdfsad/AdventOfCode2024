import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.math.abs


fun main() {
    val lines = readInput("day01.txt")
    val (left, right) = lines.map { line ->
        line.split("""\s+""".toRegex()).let {
            require(it.size == 2) { "Line: $it" }
            it[0].toLong() to it[1].toLong()
        }
    }.unzip()

    val frequencies: Map<Long, Int> = right.groupingBy { it }.eachCount()
    left.sumOf { num ->
        num * frequencies.getOrDefault(num, 0)
    }.also(::println)
}
