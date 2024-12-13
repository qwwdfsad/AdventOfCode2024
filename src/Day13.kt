fun main() {
    val input = readInput("day13.txt").filter { it.isNotBlank() }
//    val input = """
//        Button A: X+94, Y+34
//        Button B: X+22, Y+67
//        Prize: X=8400, Y=5400
//
//        Button A: X+26, Y+66
//        Button B: X+67, Y+21
//        Prize: X=12748, Y=12176
//
//        Button A: X+17, Y+86
//        Button B: X+84, Y+37
//        Prize: X=7870, Y=6450
//
//        Button A: X+69, Y+23
//        Button B: X+27, Y+71
//        Prize: X=18641, Y=10279
//    """.trimIndent().split("\n").filter(String::isNotEmpty)

    var sum = 0L
    for (i in 0 until input.size / 3) {
        val bas = input[i * 3]
        val bab = input[i * 3 + 1]
        val ps = input[i * 3 + 2]

        val x1 = bas.substringAfter("+").substringBefore(",").toLong()
        val y1 = bas.substringAfterLast("+").toLong()
        val x2 = bab.substringAfter("+").substringBefore(",").toLong()
        val y2 = bab.substringAfterLast("+").toLong()

        val c1 = ps.substringAfter("X=").substringBefore(",").toLong()
        val c2 = ps.substringAfter("Y=").toLong()

        val b = (c2 * x1 - c1 * y1) / (x1 * y2 - x2 * y1)
        val a = (c1 - b * x2) / x1
        // Valid input and is integer
        if (a in 0..100 && b in 0..100 &&
            a * x1 + b * x2 == c1 && a * y1 + b * y2 == c2
        ) {
            sum += 3 * a + b
        }
    }
    println(sum)

    sum = 0L
    for (i in 0 until input.size / 3) {
        val bas = input[i * 3]
        val bab = input[i * 3 + 1]
        val ps = input[i * 3 + 2]

        val x1 = bas.substringAfter("+").substringBefore(",").toLong()
        val y1 = bas.substringAfterLast("+").toLong()
        val x2 = bab.substringAfter("+").substringBefore(",").toLong()
        val y2 = bab.substringAfterLast("+").toLong()

        val c1 = ps.substringAfter("X=").substringBefore(",").toLong() + 10000000000000L
        val c2 = ps.substringAfter("Y=").toLong() + 10000000000000L

        val b = (c2 * x1 - c1 * y1) / (x1 * y2 - x2 * y1)
        val a = (c1 - b * x2) / x1
        if (a * x1 + b * x2 == c1 && a * y1 + b * y2 == c2) {
            sum += 3 * a + b
        }
    }
    println(sum)
}