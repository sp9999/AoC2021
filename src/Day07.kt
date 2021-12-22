import java.lang.Integer.MAX_VALUE
import java.lang.Integer.min
import java.lang.Math.max
import kotlin.math.abs

fun main() {

    fun parseLine(input: String): Pair<MutableMap<Int, Int>, Int> {
        var intList = parseLineAsListOfInts(input)
        var groupedList: MutableMap<Int, Int> = mutableMapOf()

        var max = 0
        intList.forEach() {
            if (groupedList.containsKey(it)) {
                groupedList.set(it, groupedList.get(it)?.plus(1) ?: 1)
            } else {
                groupedList.set(it, 1)
            }
            if (it > max)
                max = it
        }

        return Pair(groupedList, max)
    }

    fun calculateFuel(map: MutableMap<Int, Int>, stepTo: Int) : Int {
        var fuel = 0
        map.forEach() {
            fuel += abs(stepTo - it.key) * it.value
        }

        return fuel
    }

    fun cf1(list: MutableList<Int>) : Int {
        var max = list.maxByOrNull{it} ?: 0
        var min = list.minByOrNull{it} ?: 0
        return (min..max).map { pos ->
            list.sumOf {
                abs(it - pos)
            }
        }.minOrNull() ?: Int.MAX_VALUE
    }

    fun calculateFuel2(map: MutableMap<Int, Int>, stepTo: Int) : Int {
        var fuel = 0
        map.forEach() {
            var stepCount = abs(stepTo - it.key)
            var stepCost = 0
            for(i in 1 .. stepCount) {
                stepCost += i
            }
            fuel += (stepCount * (stepCount + 1) / 2) * it.value
        }
        return fuel
    }

    fun part1(input: List<String>): Int {
        var (groupedList, max) = parseLine(input[0])
        var minFuel = MAX_VALUE
        var choice = MAX_VALUE
        for (i in 0 .. max) {
            var fuel = calculateFuel(groupedList, i)
            if (fuel < minFuel) {
                minFuel = fuel
                choice = i
            }
        }

        var intList = parseLineAsListOfInts(input[0])
        intList.sort()
        var median = if (intList.size % 2 == 0) intList[intList.size / 2] else (intList[intList.size / 2] + intList[intList.size / 2 + 1]) / 2
        println("Best choice is ${choice}")
        println("Median is ${median}")
        return minFuel
    }

    fun part2(input: List<String>): Int {
        var (groupedList, max) = parseLine(input[0])
        var minFuel = MAX_VALUE
        var choice = MAX_VALUE
        for (i in 0 .. max) {
            var fuel = calculateFuel2(groupedList, i)
            if (fuel < minFuel) {
                minFuel = fuel
                choice = i
            }
        }

        var mean1 = groupedList.map{it.value * it.key}.sum() / groupedList.map{it.value}.sum()
        var mean2 = (groupedList.map{it.value * it.key}.sum() + .5)/ groupedList.map{it.value}.sum()
        println("Best choice is ${choice}")
        println("Mean is ${mean1} or ${mean2}")
        return minFuel
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Input/input_test")
    //println(testInput)
    println(part1(testInput))
    println()
    println(part2(testInput))
    println()

    val input = readInput("Input/input")
    val part1Ans = part1(input)
    println("Part1: ${part1Ans}")
    println()

    val part2Ans = part2(input)
    println("Part2: ${part2Ans}")
    println()
}
