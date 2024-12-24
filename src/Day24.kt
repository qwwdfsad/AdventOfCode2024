data class Op(val left: String, val op: String, val right: String, val out: String)


fun main() {
    val input = readInput("day24.txt").joinToString("\n")

    val values = input.substringBefore("\n\n")
        .split('\n')
        .groupBy({ it.substringBefore(":") }, { it.substringAfter(": ").toInt() })
        .mapValues { it.value.single() }
        .toMutableMap()


    val ops = input.substringAfter("\n\n")
        .split('\n')
        .map {
            val split = it.split(" ", limit = 5)
            Op(split[0], split[1], split[2], split[4])
        }

    part1(ops, values)

    val toSwap = HashSet<String>()
    ops.forEach { op ->
        val outZ = op.out.startsWith("z")
        val inX = op.left.startsWith("x") || op.right.startsWith("x")
        val inY = op.left.startsWith("y") || op.right.startsWith("y")

        if (
        // Output to Z is not a XOR
            outZ && op.op != "XOR" && op.out != "z45" ||
        // Complement to the prev: XOR with output not Z, and it's not X XOR Y
            !outZ && op.op == "XOR" && !(inX && inY)
        ) {
            toSwap += op.out
        }
        // Checking the origin of the ops: for I XOR J -> Z, I and K should be origined by OR and XOR
        else if (outZ && op.op == "XOR" && op.out != "z01" && op.out != "z00") {
            fun checkOiring(out: String) {
                val op = ops.first { it.out == out }
                if (op.op !in setOf("XOR", "OR")) {
                    toSwap += out
                }
            }
            checkOiring(op.left)
            checkOiring(op.right)
        // Checking the origin of I OR J
        } else if (op.op == "OR") {
            // Let's check OR: it can only be origined by AND ops
            fun checkOiring(out: String) {
                if (ops.first { it.out == out }.op != "AND") {
                    toSwap += out
                }
            }
            checkOiring(op.left)
            checkOiring(op.right)
        }
    }
    println(toSwap.sorted().joinToString(","))
}

private fun part1(
    ops: List<Op>,
    values: MutableMap<String, Int>,
) {
    var gates = ArrayList<Op>(ops)
    while (gates.isNotEmpty()) {
        var leftovers = ArrayList<Op>()
        for (op in gates) {
            if (op.left in values && op.right in values) {
                values[op.out] = when (op.op) {
                    "AND" -> values.getValue(op.left) and values.getValue(op.right)
                    "OR" -> values.getValue(op.left) or values.getValue(op.right)
                    "XOR" -> values.getValue(op.left) xor values.getValue(op.right)
                    else -> error("")
                }
            } else {
                leftovers.add(op)
            }
        }
        gates = leftovers
    }


    println(
        values.entries.filter { it.key.startsWith("z") }.sortedBy { it.key }.map { it.value.toLong() }.reversed()
            .reduce { acc, v ->
                acc shl 1 or v
            })
}