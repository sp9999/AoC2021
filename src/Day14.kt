import java.util.HashMap

fun main() {
    fun populateRules(rules: MutableMap<String, String>, rule: String) {
        var (key, value) = rule.split(" -> ")
        rules.put(key,value)
    }

    fun iterateRules(pass:String, rules: MutableMap<String, String>): String {
        var newPass = pass

        var insertList = mutableListOf<Pair<String, Int>>()
        for (i in 0 until pass.length - 1) {
            var key = pass.substring(i, i + 2)
            if (rules.containsKey(key)) {
                insertList.add(Pair(rules.get(key), i + 1) as Pair<String, Int>)
            }
        }

        for (i in 0 until insertList.size) {
            newPass = newPass.substring(0,insertList[i].second + i) + insertList[i].first + newPass.substring(insertList[i].second + i)
        }
        return newPass
    }

    fun doSteps(pass: String, rules: MutableMap<String, String>, steps: Int): Long {

        var newPass = pass
        for (step in 0 until steps) {
            newPass = iterateRules(newPass, rules)
        }

        var countMap = getFreqMap(newPass)

        var maxCount = countMap.maxByOrNull { it.value }
        var minCount = countMap.minByOrNull { it.value }

        if (maxCount != null) {
            if (minCount != null) {
                return maxCount.value - minCount.value
            }
        }
        return 0
    }

    fun iterateRules2(passMap: MutableMap<String, Long>, rules: MutableMap<String, String>): MutableMap<String, Long> {
        var newPassMap = mutableMapOf<String, Long>()
        passMap.forEach() {
            if (rules.containsKey(it.key)) {
                var key1 = it.key[0] + rules.get(it.key).toString()
                var key2 = rules.get(it.key).toString() + it.key[1]
                newPassMap.putIfAbsent(key1, 0)
                newPassMap.putIfAbsent(key2, 0)
                newPassMap[key1] = newPassMap[key1]!! + it.value
                newPassMap[key2] = newPassMap[key2]!! + it.value
            }
        }
        return newPassMap
    }

    fun getFreqMap2(passMap: MutableMap<String, Long>, passEnds: Pair<Char, Char>): Map<Char, Long> {
        val freq: MutableMap<Char, Long> = HashMap()

        passMap.forEach() {
            for (c in it.key) {
                freq.putIfAbsent(c, 0)
                freq[c] = freq[c]!! + it.value
            }
        }

        freq[passEnds.first] = freq[passEnds.first]!!.plus(1L)
        freq[passEnds.second] = freq[passEnds.second]!!.plus(1L)

        freq.forEach() {
            freq[it.key] = it.value / 2
        }
        return freq
    }

    fun doSteps2(passMap: MutableMap<String, Long>, passEnds: Pair<Char, Char>, rules: MutableMap<String, String>, steps: Int): Long {
        var newPassMap = passMap
        for (step in 0 until steps) {
            newPassMap = iterateRules2(newPassMap, rules)
        }

        var countMap = getFreqMap2(newPassMap, passEnds)


        var maxCount = countMap.maxByOrNull { it.value }
        var minCount = countMap.minByOrNull { it.value }

        if (maxCount != null) {
            if (minCount != null) {
                return maxCount.value - minCount.value
            }
        }
        return 0

    }

    fun part1(input: List<String>, steps: Int = 10): Long {
        var rules = mutableMapOf<String, String>()
        for (i in 2 until input.size) {
            populateRules(rules, input[i])
        }
        return doSteps(input[0], rules, steps)
    }

    fun populatePass(input: String): MutableMap<String, Long> {
        var passMap = mutableMapOf<String, Long>()
        for (i in 0 until input.length - 1) {
            var key = input.substring(i, i+2)
            passMap.putIfAbsent(key, 0)
            passMap[key] = passMap[key]!! + 1L
        }
        return passMap
    }

    fun part2(input: List<String>, steps: Int = 40): Long {
        var rules = mutableMapOf<String, String>()
        for (i in 2 until input.size) {
            populateRules(rules, input[i])
        }
        return doSteps2(populatePass(input[0]), Pair(input[0][0], input[0][input[0].length - 1]), rules, steps)
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
