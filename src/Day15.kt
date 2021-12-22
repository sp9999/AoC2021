import java.lang.Integer.MAX_VALUE

fun main() {

    fun returnPath(g: IntGrid, pathMap: Map<Point, Point>, current: Point) : List<Pair<Point,Int>> {
        var completePath = mutableListOf<Pair<Point,Int>>()
        var itPoint = current
        completePath.add(Pair(itPoint, g.getValue(itPoint)))

        while (pathMap.containsKey(itPoint)) {
            itPoint = pathMap[itPoint]!!
            completePath.add(Pair(itPoint, g.getValue(itPoint)))
        }
        return completePath
    }

    fun heuristic(start: Point, goal: Point): Int {
        return Math.abs(goal.x - start.x) + Math.abs(goal.y - start.y)
    }

    fun A_star(g:IntGrid, start: Point, goal: Point) : List<Pair<Point,Int>> {
        var path = mutableSetOf(start)
        var currPath = mutableMapOf<Point, Point>()

        var scoreMap = mutableMapOf<Point, Int>().withDefault { MAX_VALUE }
        scoreMap.putIfAbsent(start, 0)

        var estScoreMap = mutableMapOf<Point, Int>().withDefault { MAX_VALUE }
        estScoreMap.putIfAbsent(start, scoreMap.get(start)!! + heuristic(start, goal))

        while (path.isNotEmpty()) {
            var lowestPoint = path.minByOrNull { estScoreMap[it]!! }
            if (lowestPoint!!.x == goal.x && lowestPoint.y == goal.y)
                return returnPath(g, currPath, goal)

            path.remove(lowestPoint)
            var neighbors = mutableListOf(lowestPoint.addX(1), lowestPoint.addY(1), lowestPoint.addX(-1), lowestPoint.addY(-1))
            neighbors.forEach() { n ->
                if (n.x >= 0 && n.x < g.xSize && n.y >= 0 && n.y < g.ySize) {
                    var tentativeScore = scoreMap.getOrDefault(lowestPoint, MAX_VALUE) + g.getValue(n)
                    if (tentativeScore < scoreMap.getOrDefault(n, MAX_VALUE)) {
                        currPath[n] = lowestPoint
                        scoreMap[n] = tentativeScore
                        estScoreMap[n] = tentativeScore + heuristic(n, goal)
                        if (!path.contains(n)) {
                            path.add(n)
                        }
                    }
                }
            }
        }
        return emptyList()
    }

    fun part1(input: List<String>): Int {
        var g = parseLinesAsGridOfInts(input)

        g.print(2)

        var pathList = A_star(g, Point(0,0), Point(g.xSize-1, g.ySize - 1))

        var cost = 0
        pathList.forEach( ) {
            cost += it.second
        }
        return cost - g.getValue(0,0)
    }

    fun expandColumns(oList: MutableList<Int>, steps: Int) : MutableList<Int> {
        var newList = mutableListOf<Int>()

        for (i in 0 until steps) {
            oList.forEach() {
                var newValue = it + i
                if (newValue > 9)
                    newValue -= 9
                newList.add(newValue)
            }
        }
        return newList
    }

    fun IncrementRow(oList: MutableList<Int>): MutableList<Int> {
        var newList = mutableListOf<Int>()
        oList.forEach() {
            var newValue = it + 1
            if (newValue > 9)
                newValue -= 9
            newList.add(newValue)
        }
        return newList
    }

    fun expandGrid(g: IntGrid, steps: Int) : IntGrid {
        var newGrid = IntGrid(xSize = 0, ySize = 0)
        newGrid.xSize = g.xSize * steps
        newGrid.ySize = g.ySize * steps

        for (i in 0 until g.ySize) {
            newGrid.m.add(expandColumns(g.m[i], steps))
        }
        for (j in 0 until ((steps - 1) * g.ySize)) {
            newGrid.m.add(IncrementRow(newGrid.m[j]))
        }

        return newGrid
    }

    fun part2(input: List<String>, steps: Int = 5): Int {
        var g = parseLinesAsGridOfInts(input)
        g = expandGrid(g, steps)

        var pathList = A_star(g, Point(0,0), Point(g.xSize - 1, g.ySize - 1))

        var cost = 0
        pathList.forEach( ) {
            cost += it.second
        }
        return cost - g.getValue(0,0)
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
