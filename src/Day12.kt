fun main() {
    part1()
    part2()
}

private fun part2() {
    val input = readInput("day12.txt").map { line -> line.toCharArray() }.toTypedArray()
    val uniquePlants = input.map { it.toSet() }.reduce { a, b -> a + b }
    println(uniquePlants)

    // left-up precedense
    data class Side(val start: Point, val end: Point)
    data class Region(
        var area: Int = 0,
        val sides: MutableSet<Side> = mutableSetOf(),
    )

    val queue = ArrayDeque<Point>()
    val visited = input.map { BooleanArray(it.size) }

    val regions = ArrayList<Region>()
    var result = 0L
    for (plant in uniquePlants) {
        for (y in input.indices) {
            for (x in input[y].indices) {
                if (plant != input[y][x]) continue
                if (visited[y][x]) continue
                // Found a starting point, let's go
                queue.add(Point(x, y))
                val currentRegion = Region(0)
                regions.add(currentRegion)
                while (queue.isNotEmpty()) {
                    // Returns -- whether to remove a side
                    fun visit(y: Int, x: Int): Boolean {
                        // true -- sides are around ends
                        if (y !in 0..<input.size || x !in 0..<input[y].size) return false
                        // true -- between plants
                        if (input[y][x] != plant) return false
                        // Neighbour, no border
                        if (!visited[y][x]) queue += Point(x, y)
                        return true
                    }

                    val plot = queue.removeFirst();
                    val pY = plot.y
                    val pX = plot.x
                    if (visited[pY][pX]) {
                        continue
                    }
                    visited[pY][pX] = true
                    // Using minus sign to distringuish the corner case
                    // Upper left-right side
                    val ulr = Side(Point(pX, -pY), Point(pX + 1, -pY))
                    val llr = Side(Point(pX, pY + 1), Point(pX + 1, pY + 1))
                    // Left up-down
                    val lud = Side(Point(-pX, pY), Point(-pX, pY + 1))
                    val rud = Side(Point(pX + 1, pY), Point(pX + 1, pY + 1))
                    val sides = mutableSetOf(ulr, llr, lud, rud)
                    if (visit(pY - 1, pX)) sides -= ulr
                    if (visit(pY + 1, pX)) sides -= llr
                    if (visit(pY, pX - 1)) sides -= lud
                    if (visit(pY, pX + 1)) sides -= rud
                    currentRegion.area += 1
                    currentRegion.sides += sides
                }
                // Now we can compute the result
                val vertical = currentRegion.sides.filter { it.start.y != it.end.y }
                val horizontal = currentRegion.sides.filter { it.start.x != it.end.x }
                var sides = 0
                val p = plant == 'A'
                horizontal.groupBy { it.start.y }.forEach { key, xses ->
                    val snapshpt = sides
                    sides += 1
                    val xses = xses.sortedBy { it.start.x }
                    for (i in 1..<xses.size) {
                        if (xses[i - 1].start.x != xses[i].start.x - 1) sides += 1
                    }
                    val counted = sides - snapshpt
                    if (p) println("Horizontal: ${key}, sides: $counted")
                }
                vertical.groupBy { it.start.x }.forEach { key, yses ->
                    sides += 1
                    val yses = yses.sortedBy { it.start.y }
                    for (i in 1..<yses.size) {
                        if (yses[i - 1].start.y != yses[i].start.y - 1) sides += 1
                    }
                }
                result += sides * currentRegion.area
            }
        }
    }
    println(result)
}

private fun part1() {
    val input = readInput("day12.txt").map { line -> line.toCharArray() }.toTypedArray()
    val uniquePlants = input.map { it.toSet() }.reduce { a, b -> a + b }
    println(uniquePlants)

    data class Region(var area: Int = 0, var perimeter: Int = 0)

    val queue = ArrayDeque<Point>()
    val visited = input.map { BooleanArray(it.size) }

    val regions = ArrayList<Region>()
    for (plant in uniquePlants) {
        for (y in input.indices) {
            for (x in input[y].indices) {
                if (plant != input[y][x]) continue
                if (visited[y][x]) continue
                // Found a starting point, let's go
                queue.add(Point(x, y))
                val currentRegion = Region(0, 0)
                regions.add(currentRegion)
                while (queue.isNotEmpty()) {
                    var perimeter = 4
                    fun neighbour(y: Int, x: Int) {
                        if (y !in 0..<input.size || x !in 0..<input[y].size) return
                        if (input[y][x] != plant) return
                        // Neighbour, nice
                        perimeter -= 1
                        if (!visited[y][x]) queue += Point(x, y)
                    }

                    val plot = queue.removeFirst();
                    val pY = plot.y
                    val pX = plot.x
                    if (visited[pY][pX]) {
                        continue
                    }
                    visited[pY][pX] = true
                    neighbour(pY - 1, pX)
                    neighbour(pY + 1, pX)
                    neighbour(pY, pX - 1)
                    neighbour(pY, pX + 1)
                    currentRegion.perimeter += perimeter
                    currentRegion.area += 1
                }
            }
        }
    }
    println(regions.sumOf { it.perimeter * it.area })
}