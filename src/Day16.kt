import java.util.PriorityQueue
import kotlin.math.abs

enum class Direction(val dx: Int, val dy: Int) {
    UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0)
}

data class Vertex(val x: Int, val y: Int, val dir: Direction)

fun main() {
//    val input = """
//#################
//#...#...#...#..E#
//#.#.#.#.#.#.#.#.#
//#.#.#.#...#...#.#
//#.#.#.#.###.#.#.#
//#...#.#.#.....#.#
//#.#.#.#.#.#####.#
//#.#...#.#.#.....#
//#.#.#####.#.###.#
//#.#.#.......#...#
//#.#.###.#####.###
//#.#.#...#.....#.#
//#.#.#.#####.###.#
//#.#.#.........#.#
//#.#.#.#########.#
//#S#.............#
//#################
//    """.trimIndent().split("\n")
    val input = readInput("day16.txt")
//    part1(input)

    val startY = input.indexOfFirst { it.contains('S') }
    val startX = input[startY].indexOfFirst() { it == 'S' }
    val start = Vertex(startX, startY, Direction.RIGHT)
    val endY = input.indexOfFirst { it.contains('E') }
    val endX = input[endY].indexOfFirst() { it == 'E' }


    val scores = hashMapOf<Vertex, Long>()
    val backtracking: HashMap<Vertex, MutableSet<Vertex>> = hashMapOf()

    val queue = PriorityQueue<Vertex> { l, r ->
        scores.getValue(l).compareTo(scores.getValue(r))
    }

    queue.add(start)
    scores[start] = 0
    while (queue.isNotEmpty()) {
        val c = queue.poll()
        val adjacents = buildList<Vertex> {
            for (d in Direction.entries) {
                if (d != c.dir) {
                    add(Vertex(c.x, c.y, d))
                }
            }
            add(Vertex(c.x + c.dir.dx, c.y + c.dir.dy, c.dir))
        }

        for (adj in adjacents) {
            val cell = input[adj.y][adj.x]
            if (cell == '#') continue

            /* o  --->  o ---> o
             * o        o
             * |        |
             * o        |
             * o ---> o-o
             */
            val prevScore = scores[adj]
            val rotation = adj.x == c.x && adj.y == c.y
            val newScore = scores.getValue(c) + if (rotation) 1000 else 1
            if (prevScore == null) {
                backtracking[adj] = mutableSetOf(c)
                scores[adj] = newScore
                queue.add(adj)
            } else if (prevScore == newScore) {
                backtracking[adj]!!.add(c)
            }
        }
    }

    val minScore = Direction.entries.minOf {
        scores[Vertex(endX, endY, it)]!!
    }

    val result = HashSet<Point>()
    Direction.entries.forEach { d ->
        val end = Vertex(endX, endY, d)
        if (scores[end] == null || scores[end]!! > minScore) return@forEach
        val bt = ArrayDeque<Vertex>()
        bt.add(end)
        while (bt.isNotEmpty()) {
            val vertex = bt.removeFirstOrNull() ?: continue
            result.add(Point(vertex.x, vertex.y))
            val prev = backtracking[vertex] ?: continue
            bt.addAll(prev)
        }
    }

    println(result.size)
}

private fun part1(input: List<String>) {
    val startY = input.indexOfFirst { it.contains('S') }
    val startX = input[startY].indexOfFirst() { it == 'S' }
    val start = Point(startX, startY)
    val endY = input.indexOfFirst { it.contains('E') }
    val endX = input[endY].indexOfFirst() { it == 'E' }
    val end = Point(endX, endY)
    val scores = hashMapOf<Point, Long>()
    val backtracking = hashMapOf(start to Point(startX - 1, startY))
    val queue = PriorityQueue<Point> { l, r ->
        scores.getValue(l).compareTo(scores.getValue(r))
    }

    queue.add(start)
    scores[start] = 0
    while (queue.isNotEmpty()) { // || !scores.containsKey(end)) {
        val c = queue.poll()
        val prev = backtracking[c]!!
        for (adj in listOf(Point(c.x + 1, c.y), Point(c.x - 1, c.y), Point(c.x, c.y + 1), Point(c.x, c.y - 1))) {
            val cell = input[adj.y][adj.x]
            if (cell == '#') continue
            if (scores.containsKey(adj)) continue
            val dx = prev.x - adj.x
            val dy = prev.y - adj.y
            val prevScore = scores.getValue(c)
            scores[adj] = prevScore + if (dx != 0 && dy != 0) 1001 else 1
            backtracking[adj] = c
            queue.add(adj)
        }
    }
    println(scores[end])

    var prev = backtracking[end]
    var result = -1
    while (prev != null) {
        ++result
        prev = backtracking[prev]
    }
    println(result)
}