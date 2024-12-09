import kotlin.io.path.Path

data class F(val id: Int, var size: Int, var maybeMoved: Boolean = false)

fun main() {
//    part1()
    part2()

}

private fun part2() {
    val input = readInput("day09.txt")[0]
    val disk = input.withIndex().map { (idx, value) ->
        if (idx % 2 == 0) F(idx / 2, value.toString().toInt())
        else F(-1, value.toString().toInt())
    }.toMutableList()

    disk.printExpanded()

    var notMovedIndex = disk.indexOfLast { !it.maybeMoved && it.id >= 0 }
    while (notMovedIndex != -1) {
        val file = disk[notMovedIndex]
        file.maybeMoved = true
        val holeIndex = disk.indexOfFirst { it.size >= file.size && it.id == -1 }
        if (holeIndex == -1 || holeIndex > notMovedIndex) {
            notMovedIndex = disk.indexOfLast { !it.maybeMoved && it.id >= 0 }
            continue
        }
        val hole = disk[holeIndex]
        if (hole.size == file.size) {
            disk[holeIndex] = file
            disk[notMovedIndex] = F(-1, file.size)
            notMovedIndex = disk.indexOfLast { !it.maybeMoved && it.id >= 0 }
        } else {
            disk[notMovedIndex] = F(-1, file.size)
            disk.add(holeIndex, file)
            hole.size -= file.size
            notMovedIndex = disk.indexOfLast { !it.maybeMoved && it.id >= 0 }
        }
        disk.printExpanded()
    }

    var idx = 0
    var result = 0L
    for (f in disk) {
        if (f.id != -1) {
            repeat(f.size) {
                result += f.id * idx++
            }
        } else {
            idx += f.size
        }
    }
    println(result)
}

private fun part1() {
    val input = readInput("day09.txt")[0]
    val disk = input.withIndex().map { (idx, value) ->
        if (idx % 2 == 0) F(idx / 2, value.toString().toInt())
        else F(-1, value.toString().toInt())
    }.toMutableList()

    disk.printExpanded()

    while (true) {
        val holeIndex = disk.indexOfFirst { it.id == -1 }
        val notMovedIndex = disk.indexOfLast { it.id > 0 }
        if (notMovedIndex < holeIndex) {
            break
        }
        val hole = disk[holeIndex]
        val file = disk[notMovedIndex]
        if (hole.size < file.size) {
            file.size -= hole.size
            disk[holeIndex] = F(file.id, hole.size)
        } else if (hole.size == file.size) {
            disk[holeIndex] = F(file.id, file.size)
            disk[notMovedIndex] = F(-1, -1) // stub
        } else {
            disk[notMovedIndex] = F(-1, -1) // stub
            hole.size -= file.size
            disk.add(holeIndex, file)
        }
        disk.printExpanded()
    }

    var idx = 0
    var result = 0L
    for (f in disk) {
        repeat(f.size) {
            result += f.id * idx++
        }
    }
    println(result)
}

fun List<F>.printExpanded() {
//    val str = buildString {
//        for (file in this@printExpanded) {
//            if (file.id >= 0) {
//                append(file.id.toString().repeat(file.size))
//            } else if (file.size >= 0) {
//                append(".".repeat(file.size))
//            }
//        }
//    }
//    println(str)
}