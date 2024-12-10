import kotlin.math.abs

fun main() {
    val input = readInput("day10.txt")
        .map { line -> line.toCharArray().map { if (it == '.') -1 else it.digitToInt() } }

    var sum = 0
    for (y in input.indices) {
        for (x in input[y].indices) {
            val digit = input[y][x]
            if (digit == 0) {
                val heads = mutableSetOf<Point>()
                trails(input, y, x, 0, heads)
                sum += heads.size
            }
        }
    }
    println(sum)

    sum = 0
    for (y in input.indices) {
        for (x in input[y].indices) {
            val digit = input[y][x]
            if (digit == 0) {
                val heads = mutableListOf<Point>()
                trails(input, y, x, 0, heads)
                sum += heads.size
            }
        }
    }
    println(sum)
}

fun trails(input: List<List<Int>>, cy: Int, cx: Int, value: Int, heads: MutableCollection<Point>) {
    if (value == 9) {
        heads.add(Point(cx, cy))
    }

    val b = heads.size
    val xs = listOf(cx, cx - 1, cx + 1)
    val ys = listOf(cy, cy - 1, cy + 1)
    for (x in xs) {
        for (y in ys) {
            if ((x == cx && y == cy) || (abs(cx - x) + abs(cy - y) > 1)) continue
            if (y !in input.indices || x !in input[0].indices) continue
            if (input[y][x] == value + 1) {
                trails(input, y, x, value + 1, heads)
            }
        }
    }
}
