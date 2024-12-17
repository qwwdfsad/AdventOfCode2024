fun main() {
//    emulateForA(23552)

//    return
    /*
     * The program
     * b = a % 8
     * b = b ^ 1
     * c = a >> b
     * a = a >> 3
     * b = b xor 4
     * b = b xor c
     * out = b % 8
     * if a != 0 goto start
     */

    val program = "2,4,1,1,7,5,0,3,1,4,4,5,5,5,3,0".split(",").map { it.toLong() }.toTypedArray()

    fun findAnswer(currentAnswer: Long, outputIndex: Int): Boolean {
        if (outputIndex == -1) {
            println(currentAnswer)
            return true
        }
        val output = program[outputIndex]
        for (group in 0..7) {
            var a = (currentAnswer shl 3) or group.toLong()
            var b = 0L
            var c = 0L
            // Program:
            b = a and 7 // 1
            b = b xor 1 // 2
            c = a shr b.toInt().also { require(b <= 31) } // 3
            a = a shr 3 // 4
            b = b xor 4 // 5
            b = b xor c // 6
            val out = b and 7 // 7, 8th is jump
            if (out == output) {
                val answer = (currentAnswer shl 3) or group.toLong()
                println("Found ${15 - outputIndex}: $answer")
                if (findAnswer(answer, outputIndex - 1)) {
                    return true
                }
            }
        }
        return false
    }

    var currentAnswer = 0L
    findAnswer(currentAnswer, 15)

    println(currentAnswer)

    emulateForA(202322348616234)
}

private fun emulateForA(i: Long) {
    var a = i // input.substringAfter("A: ").substringBefore("\n").toLong()
    var b = 0L
    var c = 0L
    val program = "2,4,1,1,7,5,0,3,1,4,4,5,5,5,3,0".split(",").map { it.toLong() }
    val output = arrayListOf<Long>()
    var ip = 0
    fun combo(): Long {
        return when (val op = program[ip + 1]) {
            in 0..3 -> op
            4L -> a
            5L -> b
            6L -> c
            else -> error("Invalid op $op")
        }
    }

    fun literal(): Int = program[ip + 1].toInt()

    fun dv(): Long {
        if (combo().toInt() > 31) error("Invariant broken")
        return a shr combo().toInt()
    }

    while (ip < program.size) {
        val ins = program[ip]
        when (ins) {
            0L -> a = dv()
            1L -> b = b xor literal().toLong()
            2L -> b = combo() % 8L
            3L -> {
                if (a != 0L) {
                    ip = literal()
                    continue
                }
            }

            4L -> b = b xor c
            5L -> output += combo() % 8L
            6L -> b = dv()
            7L -> c = dv()
        }
        ip += 2
    }

    println(output.joinToString(","))
}