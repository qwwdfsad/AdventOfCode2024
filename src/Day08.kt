import kotlin.math.abs

data class Point(val x: Int, val y: Int)

fun main() {
    val input = readInput("day08.txt")

    part01(input)
    part02(input)
}

private fun part02(input: List<String>) {
    val validY = 0 until input.size
    val validX = 0 until input[0].length

    val antennas = HashMap<Char, MutableList<Point>>()
    input.forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            if (c == '.' || c == '#') return@forEachIndexed
            antennas.getOrPut(c, { mutableListOf() }).add(Point(x, y))
        }
    }
    // a1: (3, 4), a2: (5, 5)
    // #1 -- (7; 6), #2 -- (1; 3)
    // Sovle for each antenna
    val locations = mutableSetOf<Point>()
    for (positions in antennas.values) {
        for (i in 0 until positions.size) {
            for (j in (i + 1) until positions.size) {
                locations.add(positions[j])
                locations.add(positions[i])
                val a1 = positions[i]
                val a2 = positions[j]
                val dx = a1.x - a2.x // dx diff
                val dy = a1.y - a2.y // dy diff

                var c1 = Point(a1.x + dx, a1.y + dy)
                while (c1.x in validX && c1.y in validY) {
                    locations += c1
                    c1 = Point(c1.x + dx, c1.y + dy)
                }

                var c2 = Point(a2.x - dx, a2.y - dy)
                while (c2.x in validX && c2.y in validY) {
                    locations += c2
                    c2 = Point(c2.x - dx, c2.y - dy)
                }
            }
        }
    }
    println(locations.size)
}

private fun part01(input: List<String>) {
    val validY = 0 until input.size
    val validX = 0 until input[0].length

    val antennas = HashMap<Char, MutableList<Point>>()
    input.forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            if (c == '.' || c == '#') return@forEachIndexed
            antennas.getOrPut(c, { mutableListOf() }).add(Point(x, y))
        }
    }
    // a1: (3, 4), a2: (5, 5)
    // #1 -- (7; 6), #2 -- (1; 3)
    // Sovle for each antenna
    val locations = mutableSetOf<Point>()
    for (positions in antennas.values) {
        for (i in 0 until positions.size) {
            for (j in (i + 1) until positions.size) {
                val a1 = positions[i]
                val a2 = positions[j]
                val dx = a1.x - a2.x // dx diff
                val dy = a1.y - a2.y // dy diff
                val c1 = Point(a1.x + dx, a1.y + dy)
                val c2 = Point(a2.x - dx, a2.y - dy)
                if (c1.x in validX && c1.y in validY) locations += c1
                if (c2.x in validX && c2.y in validY) locations += c2
            }
        }
    }
    println(locations.size)
}
