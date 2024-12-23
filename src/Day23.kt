fun main() {
    val input = readInput("day23.txt")
    val connections = hashMapOf<String, HashSet<String>>()
    input.forEach { line ->
        val (f, s) = line.split('-')
        connections.getOrPut(f) { HashSet() }.add(s)
        connections.getOrPut(s) { HashSet() }.add(f)
    }


    var bestClique = ArrayList<String>()
    var currentClique = ArrayDeque<String>()
    val keys = connections.keys.toList()
    fun findClique(keyIdx: Int) {
        // Update the best clique if relevant
        if (currentClique.size > bestClique.size) {
            bestClique = ArrayList(currentClique)
        }

        for (i in keyIdx..<keys.size) {
            val key = keys[i]
            // Let's try to pick this node and see if it works
            val adj = connections.getValue(key)
            // Not a clique, continue
            if (!currentClique.all { adj.contains(it)  }) {
                continue
            }
            // Take the node
            currentClique.addLast(key)
            // Try find the clique
            findClique(i)
            // Remove the node so the next iteration does not involve it
            currentClique.removeLast()
        }
    }

    findClique(0)
    println(bestClique.sorted().joinToString(","))
}