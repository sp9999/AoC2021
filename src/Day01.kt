fun main() {
    fun part1(input: List<Int>): Int {
        var prevVal = input[0]
        var depthCheck = mutableListOf<Int>()
        for (i in input) {
            if (prevVal < i) {
                depthCheck.add(1)
            } else {
                depthCheck.add(0)
            }
            prevVal = i
        }
        return sumList(depthCheck)
    }

    fun part2(input: List<Int>): List<Int> {
        var windowSum = mutableListOf<Int>()

        for (i in 0..input.size - 3) {
            windowSum.add(input[i] + input[i+1] + input[i+2])
        }
        return windowSum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInputAsNum("Input/Day01_test")
    println(part1(testInput))

    val input = readInputAsNum("Input/Day01")
    println(part1(input))
    println(part1(part2(input)))
}
