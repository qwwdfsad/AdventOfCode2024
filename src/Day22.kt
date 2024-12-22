fun main() {

    val input = readInput("day22.txt").map { it.toLong() }
//    val input = listOf(1L, 2L, 3L, 2024L)

    val groups = input.map {
        generateSequence(it) { c -> next(c) }
            .take(2000)
            .zipWithNext()
            .map {
                it.second % 10 to (it.second % 10 - it.first % 10)
            }.toList().windowed(size = 4, step = 1)
            .groupBy { it.map { it.second } }.mapValues {
                it.value.first().last().first
            }
    }

    val allWindows = groups.flatMap() { it.keys }.toSet()
    val max = allWindows.maxOf { window ->
        groups.sumOf { group -> group[window] ?: 0L }
    }
    println(max)
}

private fun nextN(secret: Long, n: Int): Long {
    var secret = secret
    repeat(n) {
        secret = next(secret)
    }
    return secret
}

private fun next(secret: Long): Long {
    var secret = secret
    secret = (secret xor (secret * 64)).mod(16777216L)
    secret = (secret xor (secret / 32)).mod(16777216L)
    secret = (secret xor (secret * 2048)).mod(16777216L)
    return secret
}