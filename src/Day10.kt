import java.util.ArrayDeque

fun main() {

    fun isEndChar(c: Char): Boolean {
        return (c == '>' || c == '}' || c == ')' || c == ']')
    }

    fun calcPoints(c: Char, count: Int): Int {
        when (c) {
            ')' -> return  3 * count
            ']' -> return 57 * count
            '}' -> return 1197 * count
            '>' -> return 25137 * count
        }
        return 0
    }

    fun calcPoints(s: String): Long {
        var sum = 0L
        s.forEach { c ->
            when (c) {
                ')' -> sum = 5 * sum + 1
                ']' -> sum = 5 * sum + 2
                '}' -> sum = 5 * sum + 3
                '>' -> sum = 5 * sum + 4
            }
        }
        return sum
    }


    fun findIllegalCharacter(input: String) : Char? {
        var q = ArrayDeque<Char>()
        input.forEach {
            if (isEndChar(it)) {
                var lastLeft = q.pop()
                when (it) {
                    '>' -> {
                        if (lastLeft != '<')
                            return it
                    }
                    '}' -> {
                        if (lastLeft != '{')
                            return it
                    }
                    ')' -> {
                        if (lastLeft != '(')
                            return it
                    }
                    ']' -> {
                        if (lastLeft != '[')
                            return it
                    }
                }
            } else {
                q.push(it)
            }
        }
        return null
    }

    fun part1(input: List<String>): Int {
        var illegalMap = mutableMapOf<Char, Int> ('}' to 0, ')' to 0, ']' to 0, '>' to 0)
        input.forEach() {
            val illegal = findIllegalCharacter(it)
            if (illegal != null)
                illegalMap.set(illegal, illegalMap[illegal]?.plus(1) ?: 0)
        }
        var sum = 0
        illegalMap.forEach() {
            sum += calcPoints(it.key, it.value)
        }
        return sum
    }

    fun getEndChar(c: Char) : String {
        when (c) {
            '(' -> return  ")"
            '[' -> return "]"
            '{' -> return "}"
            '<' -> return ">"
        }
        return ""
    }

    fun completeString(input: String): String {
        var q = ArrayDeque<Char>()
        input.forEach {
            if (isEndChar(it)) {
                q.pop()
            } else {
                q.push(it)
            }
        }
        var remainingString = ""
        q.forEach() {remainingString += getEndChar(q.pop())}
        return remainingString
    }

    fun part2(input: List<String>): Long {
        var scoreList = mutableListOf<Long>()
        input.forEach() {
            if (findIllegalCharacter(it) == null) {
                var cString = completeString(it)
                scoreList.add(calcPoints(cString))
            }
        }
        scoreList.sort()
        println(scoreList.toString())
        return scoreList.get(scoreList.size/2)

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
