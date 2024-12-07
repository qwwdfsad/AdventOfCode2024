
// 6392012777720
fun main() {
    val input = readInput("day07.txt")
    var result = 0L
    for (line in input) {
        val value = line.substringBefore(":").toLong()
        val operands = line.substringAfter(":").trim().split(" ").map { it.toLong() }
        if (solvable(value, operands)) {
            result += value
        }
    }
    println(result)

    // Part 2
    result = 0L
    for (line in input) {
        val value = line.substringBefore(":").toLong()
        val operands = line.substringAfter(":").trim().split(" ").map { it.toLong() }
        if (solvable2(value, operands)) {
            result += value
        }
    }
    println(result)
}

fun solvable2(result: Long, operands: List<Long>): Boolean {
    fun trySolve(acc: Long, idx: Int): Boolean {
        if (idx == operands.size) return acc == result
        return trySolve(acc + operands[idx], idx + 1)
                || trySolve(acc * operands[idx], idx + 1)
                || trySolve((acc.toString() + operands[idx]).toLong(), idx + 1)
    }

    return trySolve(operands[0], 1)
}

fun solvable(result: Long, operands: List<Long>): Boolean {
    fun trySolve(acc: Long, idx: Int): Boolean {
        if (idx == operands.size) return acc == result
        return trySolve(acc + operands[idx], idx + 1)
                || trySolve(acc * operands[idx], idx + 1)
    }

    return trySolve(operands[0], 1)
}