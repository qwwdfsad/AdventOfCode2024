fun main() {
    part1()
    part2()
}

private fun part2() {
    val input = readInput("day15.txt").joinToString("\n")
    val map = input.substring(0, input.indexOfLast { it == '#' } + 1)
        .split("\n").map { line ->
            line
                .replace("#", "##")
                .replace("O", "[]")
                .replace(".", "..")
                .replace("@", "@.")
                .toCharArray()
        }
    var y = map.indexOfFirst { it.contains('@') }
    var x = map[y].indexOfFirst { it == '@' }
    map[y][x] = '.'
    val moves = input.substringAfterLast("#").trim().split("\n").joinToString("")

    moves@ for (move in moves) {
//        map.print(y, x)
        var (dy, dx) = when (move) {
            '<' -> 0 to -1
            '>' -> 0 to 1
            '^' -> -1 to 0
            'v' -> 1 to 0
            else -> error("")
        }

        val (nextY, nextX) = y + dy to x + dx
        val cell = map[nextY][nextX]
        if (cell == '#') {
            continue
        }
        if (cell == '.') {
            y = nextY
            x = nextX
            continue
        }
        if (dx != 0) {
            if (!moveHorizontal(map, y, nextX, dx)) continue
        } else if (!moveVertical(map, nextY, x, dy)) continue
        y = nextY
        x = nextX
        map[y][x] = '.'
    }

//    map.print(y, x)
    var sum = 0L
    for (i in map.indices) {
        for (j in map[i].indices) {
            if (map[i][j] == '[')
                sum += 100 * i + j
        }
    }
    println(sum)
}

private fun moveHorizontal(map: List<CharArray>, y: Int, x: Int, dx: Int): Boolean {
    if (!canMoveHorizontal(map, y, x, dx)) return false
    if (map[y][x + dx * 2] != '.') moveHorizontal(map, y, x + dx * 2, dx)
    map[y][x + dx * 2] = map[y][x + dx]
    map[y][x + dx] = map[y][x]
    map[y][x] = '.'
    return true
}

private fun canMoveHorizontal(map: List<CharArray>, y: Int, x: Int, dx: Int): Boolean {
    return if (map[y][x + dx * 2] != '.') {
        map[y][x + dx * 2] != '#' && canMoveHorizontal(map, y, x + dx * 2, dx)
    } else {
        true
    }
}

private fun moveVertical(map: List<CharArray>, y: Int, x: Int, dy: Int): Boolean {
    if (!canMoveVertical(map, y, x, dy)) return false
    var x2 = if (map[y][x] == '[') x + 1 else x - 1
    if (map[y + dy][x] != '.') {
        moveVertical(map, y + dy, x, dy)
    }
    if (map[y + dy][x2] != '.') {
        moveVertical(map, y + dy, x2, dy)
    }
    map[y + dy][x] = map[y][x]
    map[y + dy][x2] = map[y][x2]
    map[y][x] = '.'
    map[y][x2] = '.'
    return true
}

private fun canMoveVertical(map: List<CharArray>, y: Int, x: Int, dy: Int): Boolean {
    var x2 = if (map[y][x] == '[') x + 1 else x - 1
    var moved = true
    if (map[y + dy][x] != '.') {
        moved = moved && (map[y + dy][x] != '#' && canMoveVertical(map, y + dy, x, dy))
    }
    if (map[y + dy][x2] != '.') {
        moved = moved && (map[y + dy][x2] != '#' && canMoveVertical(map, y + dy, x2, dy))
    }
    return moved
}


private fun part1() {
    val input = readInput("day15.txt").joinToString("\n")

    val map = input.substring(0, input.indexOfLast { it == '#' } + 1)
        .split("\n").map { line -> line.toCharArray() }
    var y = map.indexOfFirst { it.contains('@') }
    var x = map[y].indexOfFirst { it == '@' }
    map[y][x] = '.'
    val moves = input.substringAfterLast("#").trim().split("\n").joinToString("")
    for (move in moves) {
//        map.print(y, x)

        var (dy, dx) = when (move) {
            '<' -> 0 to -1
            '>' -> 0 to 1
            '^' -> -1 to 0
            'v' -> 1 to 0
            else -> error("")
        }

        val (nextY, nextX) = y + dy to x + dx
        val cell = map[nextY][nextX]
        if (cell == '#') {
            continue
        }
        if (cell == '.') {
            y = nextY
            x = nextX
            continue
        }

        // Cell is O, let's move
        var idx = -1
        var blockIdx = -1
        if (dx > 0) {
            idx = map[y].withIndex().indexOfFirst { (i, char) -> char == '.' && i > nextX }
            blockIdx = map[y].withIndex().indexOfFirst { (i, char) -> char == '#' && i > nextX }
            if (idx == -1 || idx > blockIdx) continue
            map[y][idx] = 'O'
        } else if (dx < 0) {
            idx = map[y].withIndex().indexOfLast { (i, char) -> char == '.' && i < nextX }
            blockIdx = map[y].withIndex().indexOfLast { (i, char) -> char == '#' && i < nextX }
            if (idx == -1 || blockIdx > idx) continue
            map[y][idx] = 'O'
        } else if (dy > 0) {
            for (i in nextY + 1 until map.size) {
                if (map[i][x] == '.') {
                    idx = i
                    break
                }
                if (map[i][x] == '#') {
                    break
                }
            }
            if (idx == -1) continue
            map[idx][x] = 'O'
        } else {
            for (i in nextY - 1 downTo 0) {
                if (map[i][x] == '.') {
                    idx = i
                    break
                }
                if (map[i][x] == '#') {
                    break
                }
            }
            if (idx == -1) continue
            map[idx][x] = 'O'
        }
        y = nextY
        x = nextX
        map[y][x] = '.'
    }

//    map.print(y, x)

    var sum = 0L
    for (i in map.indices) {
        for (j in map[i].indices) {
            if (map[i][j] == 'O')
                sum += 100 * i + j
        }
    }
    println(sum)
}

fun List<CharArray>.print(y: Int, x: Int) {
    withIndex().forEach { (idx, line) ->
        if (idx == y) {
            val l = line.clone()
            l[x] = '@'
            println(l.joinToString(""))
        } else {
            println(line.joinToString(""))
        }
    }
//    println()
}
