fun main() {

    data class Box (val xVals: Point, val yVals: Point) {
        val leftBot = Point(minOf(xVals.x, xVals.y), minOf(yVals.x, yVals.y))
        val rightTop = Point(maxOf(xVals.x, xVals.y), maxOf(yVals.x, yVals.y))



        fun isInBox(p: Point) : Boolean {
            return p.x >= leftBot.x && p.x <= rightTop.x && p.y >= leftBot.y && p.y <= rightTop.y
        }

        fun isPastBox(p: Point): Boolean {
            return p.x > rightTop.x || p.y < leftBot.y
        }
    }

    fun parseBoxFromInput(input: String) : Box {
        var ignorePrefixString = input.substring(15)
        var (xString, yString) = ignorePrefixString.split(", y=")
        var xVals = xString.split("..")
        var yVals = yString.split("..")

        return Box(Point(xVals[0].toInt(), xVals[1].toInt()), Point(yVals[0].toInt(), yVals[1].toInt()))
    }

    fun updateVelocity(oVelocity: Point) : Point {
        var xVelDelta = 0
        if (oVelocity.x > 0)
            xVelDelta = -1
        else if (oVelocity.x < 0)
            xVelDelta + 1
        var newXVel = oVelocity.x + xVelDelta
        return Point(newXVel,oVelocity.y - 1)
    }

    fun checkVelocity(currPos: Point, oVelocity: Point, goal: Box, currHighest: Int = 0) : Pair<Boolean, Int> {
        var newPos = currPos.add(oVelocity)
        var newHighest = maxOf(currHighest, newPos.y)
        if (goal.isPastBox(newPos))
            return Pair(false, 0)

        if (goal.isInBox(newPos))
            return Pair(true, newHighest)

        if (oVelocity.x == 0 && (currPos.x < goal.leftBot.x || currPos.x > goal.rightTop.x))
            return Pair(false, 0)

        var newVelocity = updateVelocity(oVelocity)
        return checkVelocity(newPos, newVelocity, goal, newHighest)
    }

    fun calcMinXVel(xRange: Int) : Int {
        var sum = 0
        var it = 1
        while (sum < xRange) {
            sum += it
            it += 1
        }
        return it - 1
    }

    fun getHighestVelocity(goal: Box) : Pair<Int, Int> {
        var start = Point(0, 0)
        var minXVel = calcMinXVel(goal.leftBot.x)
        var currHighest = 0
        var hitCount = 0
        for (dx in minXVel .. goal.rightTop.x) {
            for (dy in goal.leftBot.y .. Math.abs(goal.leftBot.y)) {
                var hit = checkVelocity(start, Point(dx, dy), goal)
                if (hit.first) {
                    if (currHighest < hit.second) {
                        currHighest = hit.second
                    }
                    hitCount += 1
                    //println("${dx}, ${dy}")
                }

            }
        }
        return Pair(currHighest, hitCount)
    }

    fun problem(input: String): Pair<Int, Int> {
        var box = parseBoxFromInput(input)
        return getHighestVelocity(box)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Input/input_test")
    //println(testInput)
    var p1 = problem(testInput[0])
    println("Test P1: " + p1.first)
    println()
    println("Test P2: " + p1.second)
    println()

    val input = readInput("Input/input")
    var p2 = problem(input[0])

    println("Part1: ${p2.first}")
    println()
    println("Part2: ${p2.second}")
    println()
}
