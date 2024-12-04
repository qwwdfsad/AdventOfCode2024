fun main() {
    val input = readInput("day04.txt")

    val maxX = input.size
    val maxY = input[0].length

    var result = 0
    for (needle in listOf("XMAS", "SAMX")) {
        for (i in 0 until maxX) {
            for (j in 0 until maxY) {
                // Right
                var matched = 4
                for (k in needle.indices) {
                    if (j + k >= maxX || input[i][j + k] != needle[k]) {
                        matched -= 1
                        break
                    }
                }
                // Down
                for (k in needle.indices) {
                    if (i + k >= maxY || input[i + k][j] != needle[k]) {
                        matched -= 1
                        break
                    }
                }
                // Down-right
                for (k in needle.indices) {
                    if (i + k >= maxY || j + k >= maxX || input[i + k][j + k] != needle[k]) {
                        matched -= 1
                        break
                    }
                }
                // Down-left
                for (k in needle.indices) {
                    if (i + k >= maxY || j - k < 0 || input[i + k][j - k] != needle[k]) {
                        matched -= 1
                        break
                    }
                }

                result += matched
            }
        }
    }

    println(result)

    // Part 2
    result = 0
    val a = input
    val n = setOf('M', 'S')
    for (i in 1 until maxX - 1) {
        for (j in 1 until maxY - 1) {
            if (a[i][j] != 'A') {
                continue
            }

            val lu = a[i - 1][j - 1]
            val ll = a[i + 1][j - 1]
            val ru = a[i - 1][j + 1]
            val rl = a[i + 1][j + 1]

            if (setOf(lu, rl) != n || setOf(ru, ll) != n) {
                continue
            }

            result += 1
        }
    }
    println(result)
}