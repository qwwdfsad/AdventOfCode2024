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

val dkeypad = mapOf(
    '#' to Point(0, 0),
    '^' to Point(1, 0),
    'A' to Point(2, 0),
    '<' to Point(0, 1),
    'v' to Point(1, 1),
    '>' to Point(2, 1),
)

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

    return solveRec(keypad, code, 25) * code.substringBefore("A").toLong()
}

val cache = HashMap<Pair<String, Int>, Long>()

private fun solveRec(
    keypad: Map<Char, Point>,
    code: String, depth: Int,
): Long {
    var current = keypad.getValue('A')
    var result = 0L
    for (ch in code) {
        val dest = keypad.getValue(ch)
        val dx = dest.x - current.x
        val dy = dest.y - current.y
        // For the first keypad: for downwards movement we have dx precedence, otherwise dy
        val horizontal = (if (dx > 0) ">" else "<").repeat(abs(dx))
        val vertical = (if (dy > 0) "v" else "^").repeat(abs(dy))
        val brick = keypad.getValue('#')

        var subsequence: String
        val hBrick = brick == Point(dest.x, current.y);
        val vBrick = brick == Point(current.x, dest.y);
        subsequence = if (hBrick) {
            vertical + horizontal
        } else if (vBrick) {
            horizontal + vertical
        } else {
            // Precedence affects the final score
            if (dx < 0) {
                horizontal + vertical
            } else {
                vertical + horizontal
            }
        }
        subsequence += 'A'
        current = dest
        val key = subsequence to depth
        if (key in cache) {
            result += cache.getValue(key)
            continue
        }

        var interim: Long
        if (depth == 0) {
            interim = subsequence.length.toLong()
        } else {
            interim = solveRec(dkeypad, subsequence, depth - 1)
        }
        cache[key] = interim
        result += interim
    }
    return result
}

// Part 1
private fun solve(
    keypad: Map<Char, Point>,
    code: String,
): String {
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
