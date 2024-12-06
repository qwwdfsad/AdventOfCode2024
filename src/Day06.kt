fun main() {
    val input1 = readInput("day06.txt")
    part01(input1)

    val input = readInput("day06.txt").map { it.toCharArray() }

    var result = 0
    for (y in input.indices) {
        for (x in input[y].indices) {
            val newInput = input.toList().map { it.clone() }
            val prev = newInput[y][x]
            if (prev == '#' || prev == '^') continue
            newInput[y][x] = '#'
            if (isLooped(newInput)) {
                ++result
            }
        }
    }
    println(result)
}

private fun isLooped(input: List<CharArray>, draw: (List<CharArray>, Int, Int) -> Unit = { _, _, _ -> }): Boolean {
    val maxX = input[0].size
    val maxY = input.size
    val visited = mutableMapOf<Pair<Int, Int>, Int>()
    var (x, y) = input.withIndex().firstNotNullOf { (idx, str) ->
        val x = str.indexOfFirst { it == '^' }
        if (x == -1) null else (x to idx)
    }

    fun upd(x: Int, y: Int): Int = visited.compute(x to y) { p, r -> if (r == null) 1 else r + 1 }!!

    upd(x, y)

    var dir = '^'
    while (true) {
        val delta = when (dir) {
            '^' -> 0 to -1
            '>' -> 1 to 0
            '<' -> -1 to 0
            else -> 0 to 1
        }
        var newY = y + delta.second
        var newX = x + delta.first
        if (newX < 0 || newX == maxX || newY < 0 || newY == maxY) {
            return false
        }
        val cell = input[newY][newX]
        if (cell == '#') {
            newX = x
            newY = y
            dir = when (dir) {
                '^' -> '>'
                '>' -> 'v'
                '<' -> '^'
                else -> '<'
            }
        }
        x = newX
        y = newY
        // If we visited the cell from every possible direction (or looped with >>> .. <<<), breakdown
        if (upd(x, y) > 4) {
            return true
        }
        val prev = input[y][x]
        if (prev == dir) { // Short circuit
            return true
        }
        input[y][x] = dir
        draw(input, y, x)
    }
}

fun draw(lines: List<CharArray>, y: Int, x: Int) {
    println("===")
    lines.withIndex().forEach { (idx, line) ->
        if (idx == y) {
            println(line.clone().also { it[x] = 'A' })
        } else {
            println(line)
        }
    }
    lines.forEach { println(it.joinToString("")) }
    println("===")
    Thread.sleep(200)
}

private fun part01(input: List<String>) {
    val visited = mutableSetOf<Pair<Int, Int>>()
    // start pos
    var (x, y) = input.withIndex().firstNotNullOf { (idx, str) ->
        val x = str.indexOfFirst { it == '^' }
        if (x == -1) null else (x to idx)
    }
    val maxX = input[0].length
    val maxY = input.size
    visited.add(x to y)

    var dir = '^'
    while (true) {
        val delta = when (dir) {
            '^' -> 0 to -1
            '>' -> 1 to 0
            '<' -> -1 to 0
            else -> 0 to 1
        }
        var newY = y + delta.second
        var newX = x + delta.first
        if (newX < 0 || newX == maxX || newY < 0 || newY == maxY) {
            break
        }
        val cell = input[newY][newX]
        if (cell == '#') {
            // Rollback the update
            newX = x
            newY = y
            dir = when (dir) {
                '^' -> '>'
                '>' -> 'v'
                '<' -> '^'
                else -> '<'
            }
        }
        x = newX
        y = newY
        visited.add(x to y)
    }
    println(visited.size)
}
