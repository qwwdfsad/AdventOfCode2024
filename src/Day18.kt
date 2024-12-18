import java.util.PriorityQueue

fun main() {
    val input = readInput("day18.txt")

    val maxY = 71
    val maxX = 71

    for (i in input.indices) {
        val blocks = input.take(i + 1).map { it.split(",") }.map {
            it[0].toInt() to it[1].toInt()
        }.groupBy { it.first }.mapValues { it.value.map { it.second }.toSet() }

        val start = Point(0,0)
        val scores = hashMapOf<Point, Long>(start to 0L)
        val queue = PriorityQueue<Point> { l, r -> scores[l]!!.compareTo(scores[r]!!) }
        queue.add(start)
        while (queue.isNotEmpty()) {
            val c = queue.poll()
            val score = scores.getValue(c)
            for (adj in listOf(Point(c.x + 1, c.y), Point(c.x - 1, c.y), Point(c.x, c.y + 1), Point(c.x, c.y - 1))) {
                if (adj.x !in 0 until maxX || adj.y !in 0 until maxY) continue
                if (blocks.getOrDefault(adj.x, emptySet()).contains(adj.y)) continue
                if (scores.containsKey(adj)) continue
                scores[adj] = score + 1
                queue.add(adj)
            }
        }
        if (scores[Point(70, 70)] == null) {
            println(input[i])
            break
        }
    }
}