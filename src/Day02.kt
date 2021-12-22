fun main() {

    fun parseLine(line: String) : Pair<String, Int> {
        val spaceIndex = line.indexOf(' ')
        val dir = line.substring(0, spaceIndex)
        val distance = line.substring((spaceIndex + 1))

        return Pair(dir, distance.toInt())
    }

    fun part1(input: List<String>): Pair<Int, Int> {
        var x = 0
        var y = 0
        input.forEach {
            var parsedLine = parseLine(it)
            when (parsedLine.first) {
              "forward" -> x += parsedLine.second
              "up"  -> y -= parsedLine.second
              "down"  -> y += parsedLine.second
            }
        }
        return Pair(x, y)
    }

    fun part2(input: List<String>): Pair<Int, Int> {
        var x = 0
        var y = 0
        var aim = 0
        input.forEach {
            var parsedLine = parseLine(it)
            when (parsedLine.first) {
                "forward" -> {
                    x += parsedLine.second
                    y += aim * parsedLine.second
                }
                "up"  -> aim -= parsedLine.second
                "down"  -> aim += parsedLine.second
            }
        }
        return Pair(x, y)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Input/Day02_test")
    println(testInput)

    val input = readInput("Input/Day02")
    val part1Ans = part1(input)
    val part2Ans = part2(input)
    println(part1Ans)
    println(part1Ans.first * part1Ans.second)
    println(part2Ans)
    println(part2Ans.first * part2Ans.second)
}
