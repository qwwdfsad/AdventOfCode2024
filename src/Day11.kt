fun main() {
    val input = "4610211 4 0 59 3907 201586 929 33750"
    var result = input.split(" ").map { it.toLong() }.groupingBy { it }.eachCount().mapValues { it.value.toLong() }
    repeat(75) {
        val newResult = HashMap<Long, Long>()
        for ((stone, count) in result) {
            if (stone == 0L) {
                newResult.compute(1L) { t, u -> if (u == null) count else u + count }
                continue
            }

            var pow10 = 10L
            var digits = 1
            while (stone >= pow10) {
                pow10 *= 10L
                ++digits
            }

            if (digits and 1 == 0) {
                var pow10 = 1
                repeat(digits / 2) { pow10 *= 10 }
                newResult.compute(stone % pow10) { t, u -> if (u == null) count else u + count }
                newResult.compute(stone / pow10) { t, u -> if (u == null) count else u + count }
            } else {
                newResult.compute(stone * 2024) { t, u -> if (u == null) count else u + count }
            }
        }
        result = newResult
    }
    println(result.values.sumOf { it.toLong() })
}