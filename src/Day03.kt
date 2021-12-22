import java.util.*

fun main() {

    fun parseLine(line: String) : Pair<String, Int> {
        val spaceIndex = line.indexOf(' ')
        val dir = line.substring(0, spaceIndex)
        val distance = line.substring((spaceIndex + 1))

        return Pair(dir, distance.toInt())
    }

    fun part1(input: List<String>): Int {
        var bitLength = input[0].length
        var commonBit = mutableListOf<String>()
        var uncommonBit = mutableListOf<String>()

        for (i in 0..bitLength - 1) {
            var count1 = 0
            var count0 = 0
            input.forEach() {
                when (it[i]) {
                    '1' -> count1 += 1
                    '0' -> count0 += 1
                }
            }
            println("Count1:" + count1)
            println("Count0:" + count0)
            if (count1 > count0) {
                commonBit.add("1")
                uncommonBit.add("0")
            }
            else {
                commonBit.add("0")
                uncommonBit.add("1")
            }
        }

        var gammaString = ""
        commonBit.forEach() {
            gammaString += it
        }
        var epsilonString = ""
        uncommonBit.forEach() {
            epsilonString += it
        }
        val gammaRate = Integer.parseInt(gammaString, 2)
        val epsilonRate = Integer.parseInt(epsilonString, 2)
        println(gammaString)
        println(epsilonString)
        println(gammaRate)
        println(epsilonRate)
        return gammaRate * epsilonRate
    }

    fun reduceListByBit(input: List<String>, index: Int, useMostCommon: Boolean = true) : String {
        println("Index " + index + " LIST: " + input)
        var bitLength = input[0].length
        if (index >= bitLength || input.size == 1)
            return input.get(0)

        var count1 = 0
        var count0 = 0
        var strings0 = mutableListOf<String>()
        var strings1 = mutableListOf<String>()
        input.forEach() {
            when (it[index]) {
                '1' -> {
                    count1 += 1
                    strings1.add(it)
                }
                '0' -> {
                    count0 += 1
                    strings0.add(it)
                }
            }
        }
        if (useMostCommon) {
            if (count1 >= count0) {
                return reduceListByBit(strings1, index + 1, useMostCommon)
            }
            return reduceListByBit(strings0, index + 1, useMostCommon)
        } else {
            if (count1 < count0) {
                return reduceListByBit(strings1, index + 1, useMostCommon)
            }
            return reduceListByBit(strings0, index + 1, useMostCommon)
        }
    }

    fun part2(input: List<String>): Int {
        var oxyRate = reduceListByBit(input, 0)
        var co2Rate = reduceListByBit(input, 0, false)

        println(oxyRate)
        println(co2Rate)

        val oxyRateDec = Integer.parseInt(oxyRate, 2)
        val co2RateDec = Integer.parseInt(co2Rate, 2)
        println(oxyRateDec)
        println(co2RateDec)
        return oxyRateDec*co2RateDec
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Input/Day03_test")
    //println(testInput)
    println(part2(testInput))

    val input = readInput("Input/Day03")
    val part1Ans = part1(input)
    println(part1Ans)
    val part2Ans = part2(input)
    println(part2Ans)
}
