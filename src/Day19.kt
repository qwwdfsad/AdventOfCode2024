fun main() {
//    val input = """
//        r, wr, b, g, bwu, rb, gb, br
//
//        brwrr
//        bggr
//        gbbr
//        rrbgbr
//        ubwu
//        bwurrg
//        brgr
//        bbrgwb
//    """.trimIndent().split("\n")
    val input = readInput("day19.txt")

    val bases = input[0].split(", ")
    val options = input.drop(2)

    var result = 0L
    for (option in options) {
        val prefixes = mutableMapOf<String, Long>()
        result += solveCount(option, bases, prefixes, "")
    }
    println(result)
}

// Part 2
fun solveCount(golden: String, options: List<String>, prefixes: MutableMap<String, Long>, current: String): Long {
    var cnt = 0L
    for (option in options) {
        val next = current + option
        if (next == golden) {
            cnt++
            continue
        }
        if (golden.startsWith(next)) {
            val result = prefixes[next] ?: solveCount(golden, options, prefixes, next)
            prefixes[next] = result
            cnt += prefixes[next] ?: solveCount(golden, options, prefixes, next)
        }
    }
    return cnt
}

// Part 1
fun solve(golden: String, options: List<String>, prefixes: HashSet<String>, current: String): Boolean {
    for (option in options) {
        val next = current + option
        if (next == golden) return true
        if (golden.startsWith(next) && prefixes.add(next) && solve(golden, options, prefixes, next)) {
            return true
        }
    }
    return false
}

