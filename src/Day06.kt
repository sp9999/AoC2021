import java.lang.Integer.min
import java.lang.Math.max
import kotlin.math.abs

fun main() {
    fun parseLine(input: String) : MutableList<Long> {
        return input.split(',').map{it.toLong()}.toMutableList()
    }

    fun iterateDay(list: MutableList<Long>) {
        var addFish: Long = 0
        for (i in 0 until list.size) {
            if (list[i] == 0L) {
                list[i] = 6
                addFish += 1
            }
            else {
                list[i] -= 1L
            }
        }
        for (i in 0 until addFish) {
            list.add(8)
        }
    }

    fun iterateDay2(map: MutableMap<Int, Long>) {
        var newFish: Long = 0
        for (i in 0..8) {
            if (i == 0) {
                newFish = map.get(i)!!
            } else {
                map[i - 1] = map.get(i)!!
            }
        }
        map.set(6, map.get(6)?.plus(newFish) ?: newFish)
        map.set(8, newFish)
    }

    fun part1(input: List<String>): Int {
        var startList = parseLine(input[0])
        println("Start: ${startList}")
        for (i in 0 until 80) {
            iterateDay(startList)

        }
        println(startList)
        return startList.size
    }

    fun part2(input: List<String>): Long {
        var startList = parseLine(input[0])
        var groupedList: MutableMap<Int, Long> = mutableMapOf()

        for (i in 0 .. 8) {
            groupedList.put(i, startList.count{it == i.toLong()}.toLong())
        }
        println("Start: ${groupedList}")

        //println("Day ${i}")
        for (i in 0 until 256) {
            iterateDay2(groupedList)
        }

        println(groupedList)
        var sum: Long = 0
        groupedList.forEach {sum += it.value.toLong()}
        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Input/Day06_test")
    //println(testInput)
    println(part1(testInput))
    println(part2(testInput))

    val input = readInput("Input/Day06")
    val part1Ans = part1(input)
    println("Part1: ${part1Ans}")
    val part2Ans = part2(input)
    println("Part2: ${part2Ans}")
}
