import java.util.PriorityQueue
import java.util.TreeMap
import kotlin.math.abs

fun main() {
//    val input = """
//        ###############
//        #...#...#.....#
//        #.#.#.#.#.###.#
//        #S#...#.#.#...#
//        #######.#.#.###
//        #######.#.#...#
//        #######.#.###.#
//        ###..E#...#...#
//        ###.#######.###
//        #...###...#...#
//        #.#####.#.###.#
//        #.#...#.#.#...#
//        #.#.#.#.#.#.###
//        #...#...#...###
//        ###############
//    """.trimIndent().split("\n")
    val input = readInput("day20.txt")
    val maxY = input.size
    val maxX = input[0].length

    val start = input.pos('S')
    val end = input.pos('E')
    val scores = hashMapOf<Point, Long>(start to 0L)
    val queue = PriorityQueue<Point> { l, r ->
        (scores[l]!!).compareTo((scores[r]!!))
    }
    val backtracking = hashMapOf<Point, Point>()
    queue.add(start)
    while (queue.isNotEmpty()) {
        val c = queue.poll()
        val score = scores.getValue(c)
        for (adj in listOf(Point(c.x + 1, c.y), Point(c.x - 1, c.y), Point(c.x, c.y + 1), Point(c.x, c.y - 1))) {
            if (adj.x !in 0 until maxX || adj.y !in 0 until maxY) continue
            if (input[adj.y][adj.x] == '#')
                continue
            if (scores.containsKey(adj)) continue
            backtracking[adj] = c
            scores[adj] = score + 1
            queue.add(adj)
        }
    }

    val path = buildList {
        var curr = backtracking[end]
        while (curr != null) {
            add(curr)
            curr = backtracking[curr]
        }
    }.reversed()
    println(path.size)
    // Diff to number
    val baseline = scores[end]!!
    val distribution = TreeMap<Long, Int>()
    for (i in path.indices) {

        val from = path[i]
        val fromScore = scores[from]!!

        for (y in input.indices) {
            for (x in input[0].indices) {
                val to = input[y][x]
                // Brick
                if (to == '#') continue
                // Unreachable
                val toScore = scores[Point(x, y)] ?: continue
                val toEnd = abs(scores[end]!! - toScore)
                val md = abs(from.x - x) + abs(from.y - y)
                if (md > 20) continue
                val newScore = toEnd + md + fromScore
                if (baseline - newScore >= 50) {
                    distribution.compute(baseline - newScore) { t, u -> if (u == null) 1 else u + 1 }
                }
            }
        }

//        for (j in i until path.size) {
//            val to = path[j]
//            val toScore = scores[to]!!
//            val toEnd = scores[end]!! - toScore
//            val md = abs(from.x - to.x) + abs(from.y - to.y)
//            if (md > 20) continue
//            val newScore = toEnd + md + fromScore
//            distribution.compute(baseline - newScore) { t, u ->  if (u == null) 1 else u + 1 }
//        }
    }
    println(distribution)
    println(distribution.entries.filter { it.key >= 100 }.sumOf { it.value })

//    println(cnt)
//    println(TimeSource.Monotonic.markNow().minus(ts))
}

// Bruteforced
private fun dijkstraWithSingleCheat(
    start: Point,
    maxX: Int,
    maxY: Int,
    input: List<String>,
    y: Int,
    x: Int,
    end: Point,
): Long {
    fun md(from: Point) = 0 //abs(from.x - end.x) + abs(from.y - end.y)
    val scores = hashMapOf<Point, Long>(start to 0L)
    val queue = PriorityQueue<Point> { l, r ->
        (scores[l]!! + md(l)).compareTo((scores[r]!! + md(r)))
    }
    queue.add(start)
    while (queue.isNotEmpty() && scores[end] == null) {
        val c = queue.poll()
        val score = scores.getValue(c)
        for (adj in listOf(Point(c.x + 1, c.y), Point(c.x - 1, c.y), Point(c.x, c.y + 1), Point(c.x, c.y - 1))) {
            if (adj.x !in 0 until maxX || adj.y !in 0 until maxY) continue
            if (input[adj.y][adj.x] == '#' && !(adj.y == y && adj.x == x)) continue
            if (scores.containsKey(adj)) continue
            scores[adj] = score + 1
            queue.add(adj)
        }
    }
    return scores[end]!!
}

data class Backtracked(val prev: Point, val cheated: Int)


private fun List<String>.pos(ch: Char): Point {
    val startY = indexOfFirst { it.contains(ch) }
    val startX = get(startY).indexOfFirst { it == ch }
    return Point(startX, startY)
}
