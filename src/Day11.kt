import java.util.ArrayDeque

fun main() {

    fun incPointIfNotFlashed(g: IntGrid, p: Point): Boolean {
        if (p.x < 0 || p.x >= g.xSize || p.y < 0 || p.y >= g.ySize)
            return false

        val currVal = g.getValue(p.x, p.y)
        if (currVal != 0 && currVal < 10) {
            g.incValue(p.x, p.y)
            if (g.getValue(p.x, p.y) >= 10)
                return true
        }
        return false
    }
    fun incSurroundingPoints(g: IntGrid, p: Point): MutableList<Point> {
        var newFlashes = mutableListOf<Point>()
        for (y in -1 .. 1) {
            for (x in -1 .. 1) {
                if (y == 0 && x == 0)
                    continue
                val np = Point(p.x + x,p.y + y)
                if (incPointIfNotFlashed(g, np))
                    newFlashes.add(np)
            }
        }
        return newFlashes
    }

    fun flash(grid: IntGrid): Long {
        var flashPoints = ArrayDeque<Point>()
        for (y in 0 until grid.ySize) {
            for (x in 0 until grid.xSize) {
                if (grid.getValue(x,y) >= 10) {
                    flashPoints.add(Point(x,y))
                }
            }
        }

        var flashes = flashPoints.size.toLong()
        while (flashPoints.isNotEmpty()) {
            val currP = flashPoints.pop()
            grid.setValue(currP.x, currP.y, 0)
            var newFlashes = incSurroundingPoints(grid, currP)
            if (newFlashes.isNotEmpty())
                flashes += newFlashes.size.toLong()
                flashPoints.addAll(newFlashes)
        }
        return flashes
    }

    fun step(grid: IntGrid, remainingSteps: Int): Long {
        if (remainingSteps <= 0)
            return 0

        grid.incAll()

        var flashes = flash(grid)
        //grid.print()
        flashes += step(grid, remainingSteps - 1)
        return flashes
    }

    fun stepUntilFullFlash(grid: IntGrid, currStep: Long): Long {
        grid.incAll()

        val flashes = flash(grid)
        //grid.print()
        if (flashes == (grid.xSize * grid.ySize).toLong()) {
            return currStep
        }
        return stepUntilFullFlash(grid, currStep + 1)
    }

    fun part1(input: List<String>): Long {
        var g = parseLinesAsGridOfInts(input)
        var flashes = step(g, 100)
        g.print()
        return flashes
    }

    fun part2(input: List<String>): Long {
        var g = parseLinesAsGridOfInts(input)
        var steps = stepUntilFullFlash(g, 1)
        g.print()
        return steps
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
