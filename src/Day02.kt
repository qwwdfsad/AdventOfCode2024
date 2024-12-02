import kotlin.math.abs


fun main() {
    val input = readInput("day02.txt")
    input.count { line ->
        val numbers = line.split(" ").map { it.toInt() }
        // Is safe
        isSafe(numbers)
    }.also(::println)


    input.count { line ->
        val numbers = line.split(" ").map { it.toInt() }

        for (i in numbers.indices) {
            val copy = numbers.toMutableList()
            copy.removeAt(i)
            if (isSafe(copy)) {
                return@count true
            }
        }
        false
    }.also(::println)
}

private fun isSafe(numbers: List<Int>): Boolean =
    (numbers.sorted() == numbers || numbers.sortedDescending() == numbers) &&
            numbers.zipWithNext().all { (x, y) -> abs(x - y) in 1..3 }