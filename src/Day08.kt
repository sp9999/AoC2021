fun main() {
    val lengths = arrayOf(6, 2, 5, 5, 4, 5, 6, 3, 7, 6)

    fun parseLine(input: String) : Pair<List<String>, List<String>> {
        var inputSplit = input.split(" | ")
        var leftSide = inputSplit[0].split(' ').map{it.trim().toCharArray().sorted().joinToString("")}.toList().sortedBy { it.length }
        var rightSide = inputSplit[1].split(' ').map{it.trim().toCharArray().sorted().joinToString("")}.toList()

        return Pair(leftSide, rightSide)
    }
    fun part1(input: List<String>): Int {
        var total = 0
        input.forEach() {
            var (leftSide, rightSide) = parseLine(it)
            var count = rightSide.count() {
                it.length == lengths[1] || it.length == lengths[4] || it.length == lengths[7] || it.length == lengths[8]
            }
            total += count
        }
        return total
    }

    fun findStringWithOneRemaining(list: List<String>, combine: String): String {
        list.forEach() {
            var result = it.replace("[${combine}]".toRegex(), "")
            if (result.length == 1)
                return it
        }
        return ""
    }

    fun calcOutput(input: List<String>, key: Array<String>): Int {
        var stringResult = ""
        input.forEach() {
            stringResult += key.indexOf(it).toString()
        }

        return stringResult.toInt()
    }

    fun determineSignal(left: MutableList<String>, right: List<String>) : Int {
        /*
                00
               1  2
               1  2
                33
               4  5
               4  5
                66
         */
        val number = arrayOf("-", "-", "-", "-", "-", "-", "-", "-", "-", "-")

        // set 1,4,7,8
        number[1] = left[0]
        number[4] = left[2]
        number[7] = left[1]
        number[8] = left[9]

        var top = ""
        left[1].forEach { if (!left[0].contains(it)) {top = it.toString()} }

        var midbottom = ""
        var topbotleft = ""
        var unique73 = left[3].replace("[${number[7]}]".toRegex(),"")
        var unique74 = left[4].replace("[${number[7]}]".toRegex(),"")
        var unique75 = left[5].replace("[${number[7]}]".toRegex(),"")

        var combine = (unique73+unique74+unique75).toCharArray().sorted().joinToString("")
        var midBottomMap = parseLineOfCharIntoMap(combine)
        midBottomMap.forEach() {
            if (it.value == 3) {
                midbottom += it.key
            } else {
                topbotleft += it.key
            }
        }

        // get 6 by removing 6 length strings with lefts(1,4) top(0) mid(3) and bot(6)
        number[6] = findStringWithOneRemaining(listOf(left[6], left[7], left[8]), topbotleft + midbottom + top)
        // get 9 by removing 6 length strings with right(2,5) top(0) mid(3) and bot(6)
        number[9] = findStringWithOneRemaining(listOf(left[6], left[7], left[8]), number[7] + midbottom )
        // set 1, 4, 6, 7, 8, 9

        var listFor0 = mutableListOf(left[6], left[7], left[8])
        listFor0.remove(number[6])
        listFor0.remove(number[9])

        // set 0, 1, 4, 6, 7, 8, 9
        number[0] = listFor0[0]

        // set 0, 1, 2, 3, 4, 6, 7, 8, 9
        number[2] = findStringWithOneRemaining(listOf(left[3], left[4], left[5]), number[9])
        number[3] = findStringWithOneRemaining(listOf(left[3], left[4], left[5]), number[2])

        var listFor5 = mutableListOf(left[3], left[4], left[5])
        listFor5.remove(number[3])
        listFor5.remove(number[2])
        number[5] = listFor5[0]
        // set 0, 1, 2, 3, 4, 5, 6, 7, 8, 9

        return calcOutput(right, number)
    }

    fun determineSignal2(leftO: MutableList<String>, right: List<String>) : Int {
        // set 1,4,7,8
        val number = arrayOf("-", leftO[0], "-", "-", leftO[2], "-", "-", leftO[1], leftO[9], "-")
        var left = leftO.filterNot { listOf(leftO[9], leftO[2], leftO[1], leftO[0]).contains(it)}.toMutableList()

        // 6 {0, 6, 9} | 5 {2, 3, 5}
        number[9] = left.find{andTwoStrings(it, number[4]).length == 4}.toString()
        left.remove(number[9])

        // 6 {0, 6} | 5 {2, 3, 5}
        number[3] = left.find{subtractTwoStrings(it, number[7]).length == 2}.toString()
        left.remove(number[3])

        // 6 {0, 6} | 5 {2, 5}
        number[5] = left.find{subtractTwoStrings(it, number[4]).length == 2}.toString()
        left.remove(number[5])

        // 6 {0, 6} | 5 {2, }
        number[2] = left.find {it.length == 5}.toString()
        left.remove(number[2])

        // 6 {0, 6}
        number[6] = left.find{subtractTwoStrings(it, number[5]).length == 1}.toString()
        left.remove(number[6])

        number[0] = left[0]
        return calcOutput(right, number)
    }

    fun part2(input: List<String>): Int {
        var total = 0
        input.forEach() {
            var (leftSide, rightSide) = parseLine(it)
            var output = determineSignal2(leftSide.toMutableList(), rightSide)
            total += output
        }
        return total
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Input/input_test")
    //println(testInput)
    println("Test P1: " + part1(testInput))
    println()
    println("Test P2: " + part2(testInput))
    println()

    val input = readInput("Input/input")
    val part1Ans = part1(input)
    println("Part1: ${part1Ans}")
    println()

    val part2Ans = part2(input)
    println("Part2: ${part2Ans}")
    println()
}
