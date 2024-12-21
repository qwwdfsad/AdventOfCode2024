import kotlin.io.path.Path
import kotlin.math.abs

fun main() {
    val input = """780A
539A
341A
189A
682A""".split("\n")

    input.sumOf {
        solve(it)
    }.also { println(it) }
}

private fun solve(code: String): Long {
    val keypad = buildMap {
        for (i in 0..2) {
            for (j in 1..3) {
                put((i * 3 + j).digitToChar(), Point(j - 1, 2 - i))
            }
        }
        put('#', Point(0, 3))
        put('0', Point(1, 3))
        put('A', Point(2, 3))
    }

    val dkeypad = mapOf(
        '#' to Point(0, 0),
        '^' to Point(1, 0),
        'A' to Point(2, 0),
        '<' to Point(0, 1),
        'v' to Point(1, 1),
        '>' to Point(2, 1),
    )

    // First layer
    val result = solve(keypad, code)
    var r = result
    repeat(2) {
        r = solve(dkeypad, r)
    }

    println(r.length.toString() + "*" + code.substringBefore("A").toLong())
    return r.length * code.substringBefore("A").toLong()
}

// Part 1
private fun solve(
    keypad: Map<Char, Point>,
    code: String, ): String {
    var current = keypad.getValue('A')
    val result = StringBuilder()
    for (ch in code) {
        val dest = keypad.getValue(ch)
        val dx = dest.x - current.x
        val dy = dest.y - current.y
        // For the first keypad: for downwards movement we have dx precedence, otherwise dy
        val horizontal = (if (dx > 0) ">" else "<").repeat(abs(dx))
        val vertical = (if (dy > 0) "v" else "^").repeat(abs(dy))
        val brick = keypad.getValue('#')

        val hBrick = brick == Point(dest.x, current.y);
        val vBrick = brick == Point(current.x, dest.y);
        if (hBrick) {
            result.append(vertical + horizontal)
        } else if (vBrick) {
            result.append(horizontal + vertical)
        } else {
            // Precedence affects the final score
            if (dx < 0) {
                result.append(horizontal + vertical)
            } else {
                result.append(vertical + horizontal)
            }

        }
        result.append('A')
        current = dest
    }
    return result.toString()
}
