fun main() {

    fun compareAdjacentToSeeIfLow(g: IntGrid, x: Int, y: Int, maxX: Int, maxY: Int) : Boolean {
        val value = g.getValue(x,y)
        // check top
        if (x > 0) {
            if (value >= g.getValue(x-1, y))
                return false
        }
        if (x < maxX - 1) {
            if (value >= g.getValue(x + 1, y))
                return false
        }
        if (y > 0) {
            if (value >= g.getValue(x, y - 1))
                return false
        }
        if (y < maxY - 1) {
            if (value >= g.getValue(x, y + 1))
                return false
        }
        return true
    }

    fun findLows(g: IntGrid, maxX: Int, maxY: Int) : List<Int> {
        var lows: MutableList<Int> = mutableListOf()
        for (y in 0 until maxY) {
            for (x in 0 until maxX) {
                if (compareAdjacentToSeeIfLow(g, x, y, maxX, maxY)) {
                    lows.add(g.getValue(x,y))
                }
            }
        }
        return lows
    }

    fun expandBasin(g: IntGrid, currPoint: Point, prevPoint:Point, maxX: Int, maxY: Int, ongoingSet: MutableSet<Point>) : MutableSet<Point> {
        if (currPoint.x < 0 || currPoint.x >= maxX || currPoint.y < 0 || currPoint.y >= maxY)
            return mutableSetOf()

        if (ongoingSet.contains(currPoint))
            return mutableSetOf()

        val value = g.getValue(currPoint.x, currPoint.y)
        val prevPointValue = if ((prevPoint.x != -1 && prevPoint.y != -1)) g.getValue(prevPoint.x, prevPoint.y) else -1
        if (value == 9 || value < prevPointValue)
            return mutableSetOf()


        ongoingSet.add(currPoint)
        g.setValue(currPoint.x, currPoint.y, 0)
        ongoingSet.addAll(expandBasin(g, Point(currPoint.x - 1, currPoint.y), currPoint, maxX, maxY, ongoingSet))
        ongoingSet.addAll(expandBasin(g, Point(currPoint.x + 1, currPoint.y), currPoint, maxX, maxY, ongoingSet))
        ongoingSet.addAll(expandBasin(g, Point(currPoint.x, currPoint.y - 1), currPoint, maxX, maxY, ongoingSet))
        ongoingSet.addAll(expandBasin(g, Point(currPoint.x, currPoint.y + 1), currPoint, maxX, maxY, ongoingSet))

        return ongoingSet
    }

    fun findBasinSize(g: IntGrid, maxX: Int, maxY: Int) : MutableList<Int> {
        var basin: MutableList<Int> = mutableListOf()
        for (y in 0 until maxY) {
            for (x in 0 until maxX) {
                if (compareAdjacentToSeeIfLow(g, x, y, maxX, maxY)) {
                    var basinList = expandBasin(g, Point(x,y), Point(-1,-1), maxX, maxY, mutableSetOf())
                    println(basinList)
                    basin.add(basinList.size)
                }
            }
        }
        return basin
    }

    fun part1(input: List<String>): Int {
        var g = parseLinesOfIntIntoGrid(input)
        var lows = findLows(g, g.xSize, g.ySize)
        g.print()
        println(lows)
        return lows.sum() + lows.size
    }


    fun part2(input: List<String>): Int {
        var g = parseLinesOfIntIntoGrid(input)
        var basins = findBasinSize(g, g.xSize, g.ySize)
        println(basins)
        basins.sort()
        var top3 = basins.subList(basins.size-3, basins.size)
        println(top3)
        return prodList(top3)
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
