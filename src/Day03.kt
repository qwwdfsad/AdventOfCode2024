import kotlin.math.abs


fun main() {
    val input = readInput("day03.txt")
    var regex = Regex("mul\\((\\d{1,3}),(\\d{1,3})\\)")
    input.flatMap() {
        regex.findAll(it).map { match ->
            match.groupValues[1].toInt() * match.groupValues[2].toInt()
        }
    }.sum().also(::println)

    // Part 2
    // Group format: [matchedGroup, do, don't, mul, d1, d2]
    regex =  Regex("(do\\(\\))|(don't\\(\\))|(mul\\((\\d{1,3}),(\\d{1,3})\\))")

    var mulEnabled = true
    input.flatMap() {
        regex.findAll(it).map { match ->
            when (match.groupValues[0]) {
                "do()" -> {
                    mulEnabled = true
                    0
                }
                "don't()" -> {
                    mulEnabled = false
                    0
                }
                else -> if (mulEnabled) match.groupValues[4].toInt() * match.groupValues[5].toInt() else 0
            }
        }
    }.sum().also(::println)
}