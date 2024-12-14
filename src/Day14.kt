data class Robot(var position: Point, var velocity: Point)

fun main() {
    val input = readInput("day14.txt")
    val maxY = 103
    val maxX = 101

    val regex = Regex(".*=(\\d+),(\\d+).*=(\\-?[0-9]+),(\\-?[0-9]+)")
    val robots = input.map { line ->
        val (px, py, vx, vy) = regex.matchEntire(line)!!.destructured.toList().map(String::toInt)
        Robot(Point(px, py), Point(vx, vy))
    }

//    repeat(10_000) {
//        robots.forEach { robot ->
//            val start = robot.position
//            val velocity = robot.velocity
//            val end = Point(
//                (start.x + velocity.x).mod(maxX),
//                (start.y + velocity.y).mod(maxY)
//            )
//            robot.position = end
//        }
//
//        // ul, ur, ll, lr
//        val quadrants = intArrayOf(0, 0, 0, 0)
//        robots.forEach { robot ->
//            val p = robot.position
//            if (p.x == maxX / 2 || p.y == maxY / 2) return@forEach
//            var idx = 0
//            if (p.x > maxX / 2) idx += 1
//            if (p.y > maxY / 2) idx += 2
//            quadrants[idx]++
//        }
//
//        val safety = quadrants.reduce(Int::times)
//        if (safety < minSafety) {
//            minSafety = safety
//            iter = it
//        }
//    }

    robots.forEach { robot ->
        val start = robot.position
        val velocity = robot.velocity
        val end = Point(
            (start.x + velocity.x * 6445).mod(maxX),
            (start.y + velocity.y * 6445).mod(maxY)
        )
        robot.position = end
    }

    repeat(1000) {
        robots.forEach { robot ->
            val start = robot.position
            val velocity = robot.velocity
            val end = Point(
                (start.x + velocity.x).mod(maxX),
                (start.y + velocity.y).mod(maxY)
            )
            robot.position = end
        }
        printMap(robots, maxY, maxX)
        println("Iter: $it")
        println("===============")
        Thread.sleep(500)
    }

    printMap(robots, maxY, maxX)
}

private fun printMap(robots: List<Robot>, maxY: Int, maxX: Int) {
    val m = robots.groupBy { it.position.y }.mapValues { it.value.groupingBy { it.position.x }.eachCount() }
    buildString {
        repeat(maxY) { y ->
            repeat(maxX) { x ->
                val sym = m[y]?.get(x).let { if (it != null) '*' else '.' }
                append(sym)
            }
            appendLine()
        }
    }.also(::println)
}