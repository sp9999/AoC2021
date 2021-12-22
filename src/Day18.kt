import org.json.JSONObject
import kotlinx.serialization.*
import kotlinx.serialization.json.Json

fun main() {

    data class SnailFish(var input: String) {
        constructor(leftVal: Int = 0, rightVal: Int = 0) : this("[${leftVal},${rightVal}]")
        constructor(leftVal: SnailFish, rightVal: SnailFish) : this(0, 0) {
            left = leftVal
            right = rightVal
        }
        constructor(leftVal: Int, rightVal: SnailFish) : this(leftVal, 0) {
            right = rightVal
        }
        constructor(leftVal: SnailFish, rightVal: Int) : this(0, rightVal) {
            left = leftVal
        }

        var left: Any
        var right: Any
        init {
            if (input[1] == '[') {
                left = SnailFish(input.substring(1))
                //input = input.substring(input.indexOf("]") + 1)
                input = (left as SnailFish).input
            } else {
                val commaIndex = input.indexOf(",")
                left = input.substring(1, commaIndex).toInt()
                input = input.substring(commaIndex)
            }

            if (input[1] == '[') {
                right = SnailFish(input.substring(1))
                //input = input.substring(input.indexOf("]") + 1)
                input = (right as SnailFish).input
            } else {
                val rBracketIndex = input.indexOf("]")
                right = input.substring(1, rBracketIndex).toInt()
                input = input.substring(rBracketIndex + 1)
                while (input.isNotEmpty() && input[0] == ']')
                    input = input.substring(1)
            }
        }


        fun tryExplode(currDepth: Int = 0, resetVal: () -> Unit): Pair<Int?, Int?>? {
            if (left is Int && right is Int && currDepth >= 4) {
                resetVal()
                return left as Int to right as Int
            }

            val (tL, tR) = Pair(left, right)
            if (tL is SnailFish) {
                val leftExplode = tL.tryExplode(currDepth + 1) {left = 0}

                if (leftExplode != null) {
                    val (explodeL, explodeR) = leftExplode
                    if (explodeR != null) {
                        var currSnailFish = this
                        var currIt = right
                        while (currIt !is Int) {
                            currSnailFish = currIt as SnailFish
                            currIt = currIt.left
                        }
                        if (currSnailFish == this) {
                            right = currIt + explodeR
                        } else {
                            currSnailFish.left = currIt + explodeR
                        }
                        return explodeL to null
                    }
                    return leftExplode
                }
            }

            if (tR is SnailFish) {
                val rightExplode = tR.tryExplode(currDepth + 1) {right = 0}

                if (rightExplode != null) {
                    val (explodeL, explodeR) = rightExplode
                    if (explodeL != null) {
                        var currSnailFish = this
                        var currIt = left
                        while (currIt !is Int) {
                            currSnailFish = currIt as SnailFish
                            currIt = currIt.right
                        }
                        if (currSnailFish == this) {
                            left = currIt + explodeL
                        } else {
                            currSnailFish.right = currIt + explodeL
                        }
                        return null to explodeR
                    }
                    return rightExplode
                }
            }
            return null
        }

        fun trySplit(): Boolean {
            if (left is SnailFish) {
                if ((left as SnailFish).trySplit())
                    return true
            }
            if (left is Int && left as Int >= 10) {
                var (splitL, splitR) = Pair((left as Int) / 2, ((left as Int) + 1)/ 2)
                left = SnailFish(splitL, splitR)
                return true
            }
            if (right is SnailFish) {
                if ((right as SnailFish).trySplit())
                    return true
            }
            if (right is Int && right as Int >= 10) {
                var (splitL, splitR) = Pair((right as Int) / 2, ((right as Int) + 1)/ 2)
                right = SnailFish(splitL, splitR)
                return true
            }
            return false
        }

        fun reduce() {
            while (true) {
                if (tryExplode() {} != null)
                    continue
                if (trySplit())
                    continue
                break
            }
        }

        operator fun plus(s2: SnailFish): SnailFish {
            var newS = SnailFish(this, s2)
            newS.reduce()
            return newS
        }

        override fun equals(other: Any?): Boolean {
            when (other) {
                is SnailFish -> {
                    return left == other.left && right == other.right
                }
                else -> return false
            }
        }

        fun magnitude(): Int {
            var leftVal: Int
            if (left is Int)
                leftVal = left as Int * 3
            else {
                leftVal = (left as SnailFish).magnitude() * 3
            }

            var rightVal: Int
            if (right is Int)
                rightVal = right as Int * 2
            else {
                rightVal = (right as SnailFish).magnitude() * 2
            }
            return leftVal + rightVal
        }

        fun clone(): SnailFish {
            var newS = SnailFish()
            newS.left = when (left) {
                is SnailFish -> (left as SnailFish).clone()
                is Int -> left
                else -> throw Exception()
            }
            newS.right = when (right) {
                is SnailFish -> (right as SnailFish).clone()
                is Int -> right
                else -> throw Exception()
            }
            return newS
        }

        override fun toString(): String {
            var leftString: String
            if (left is SnailFish)
                leftString = (left as SnailFish).toString()
            else {
                leftString = left.toString()
            }

            var rightString: String
            if (right is SnailFish)
                rightString = (right as SnailFish).toString()
            else {
                rightString = right.toString()
            }

            return "[$leftString,$rightString]"
        }
    }

    fun part1(input: List<String>): Int {
        var sList = input.map { SnailFish(it) }
        var finalS = sList.reduce() {s1, s2 -> s1 + s2}
        println(finalS)
        return finalS.magnitude()
    }


    fun part2(input: List<String>): Int {
        var sList = input.map { SnailFish(it) }
        var maximumMagnitude = 0
        var max1: SnailFish? = null
        var max2: SnailFish? = null
        sList.forEachIndexed() {i, s1 ->
            sList.forEachIndexed() { j, s2 ->
                if (i != j) {
                    var mag = (s1.clone() + s2.clone()).magnitude()
                    if (mag > maximumMagnitude) {
                        maximumMagnitude = mag
                        max1 = s1
                        max2 = s2
                    }
                }
            }
        }
        println(max1!!)
        println(max2!!)
        return maximumMagnitude
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Input/Day18_test")
    //println(testInput)
    println("Test P1: " + part1(testInput))
    println()
    println("Test P2: " + part2(testInput))
    println()

    val input = readInput("Input/Day18")
    val part1Ans = part1(input)
    println("Part1: ${part1Ans}")
    println()

    val part2Ans = part2(input)
    println("Part2: ${part2Ans}")
    println()
}
