fun main() {
    val input = readInput("day05.txt")

    val rules = input.subList(0, input.indexOfFirst { it == "" })
    val updates = input.subList(input.indexOfFirst { it == "" } + 1, input.size)
        .map { it.split(",").map { it.toInt() } }

    // i.e. for 1|2 + 1|3 it's 1 -> [2, 3]
    val presedences = rules.groupBy { it.substringBefore("|").toInt() }
        .mapValues { it.value.mapTo(HashSet()) { it.substringAfter("|").toInt() } }

    updates.filter { page ->
        page.withIndex().all { (idx, v) ->
            val rule = presedences[v] ?: return@all true
            rule.none { i ->
                page.indexOf(i) != -1 && page.indexOf(i) < idx
            }
        }
    }.sumOf {
        it.get(it.size / 2)
    }.also(::println)

    // Part 2
    updates.filter { page ->
        !page.withIndex().all { (idx, v) ->
            val rule = presedences[v] ?: return@all true
            rule.none { i ->
                page.indexOf(i) != -1 && page.indexOf(i) < idx
            }
        }
    }.map {
        // Fix according to rules
        it.sortedWith { v1, v2 ->
            if (presedences[v1]?.contains(v2) == true) {
                -1
            } else if (presedences[v2]?.contains(v1) == true) {
                1
            } else {
                0
            }
        }
    }.sumOf {
        it.get(it.size / 2)
    }.also(::println)
}