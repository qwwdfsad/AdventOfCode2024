fun main() {
//    val input = """
//        #####
//        .####
//        .####
//        .####
//        .#.#.
//        .#...
//        .....
//
//        #####
//        ##.##
//        .#.##
//        ...##
//        ...#.
//        ...#.
//        .....
//
//        .....
//        #....
//        #....
//        #...#
//        #.#.#
//        #.###
//        #####
//
//        .....
//        .....
//        #.#..
//        ###..
//        ###.#
//        ###.#
//        #####
//
//        .....
//        .....
//        .....
//        #....
//        #.#..
//        #.#.#
//        #####
//    """.trimIndent().split("\n\n").map { it.split("\n") }
    val input = readInput("day25.txt")
        .joinToString("\n")
        .split("\n\n").map { it.split("\n") }

    val locks = input.filter { it[0].count { it == '#' } == it[0].length }
    val keys = input.filter { it.last().count { it == '#' } == it.last().length }

    var answer = 0
    for (lock in locks) {
        for (key in keys) {
            var allFit = true
            for (column in 0 until key[0].length) {
                val keyIdx = key.indexOfFirst { it[column] == '#' }
                val lockIdx = lock.indexOfLast { it[column] == '#' }
                if (keyIdx <= lockIdx) {
                    allFit = false
                    break
                }
            }
            if (allFit) ++answer
        }
    }
    println(answer)
}