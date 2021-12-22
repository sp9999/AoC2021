import java.lang.Integer.min
import java.lang.Math.max
import kotlin.math.abs

fun main() {
    fun parseLine(input: String, grid:IntGrid, allowDiagonals: Boolean = false) {
        val cString = input.split(" -> ")
        val c1String = cString[0].split(',')
        val c2String = cString[1].split(',')
        val c1 = Point(c1String[0].toInt(), c1String[1].toInt())
        val c2 = Point(c2String[0].toInt(), c2String[1].toInt())

        val dx = c2.x - c1.x
        val dy = c2.y - c1.y

        if (abs(dx) > 0 && dy == 0)  {
            for (i in min(c1.x,c2.x) .. max(c1.x,c2.x))
                grid.incValue(i, c1.y)
        }

        else if (abs(dy) > 0 && dx == 0) {
            for (i in min(c1.y,c2.y) .. max(c1.y,c2.y))
                grid.incValue(c1.x, i)
        }

        else if (allowDiagonals) {
            if ( abs(dy.toDouble()/dx.toDouble()) == 1.0 ) {
                var sx = c1.x
                var sy = c1.y
                for (i in 0 .. abs(dx)) {
                    grid.incValue(sx, sy)
                    sx += (c2.x - c1.x) / abs(c2.x - c1.x)
                    sy += (c2.y - c1.y) / abs(c2.y - c1.y)
                }
            }
        }
    }

    fun countGridgt2(grid: IntGrid): Int {
        var count = 0
        grid.m.forEach() {
            n->
            n.forEach() {
                if (it >= 2) {
                    count += 1
                }
            }
        }
        return count
    }
    fun part1(input: List<String>, boardSize: Int): Int {
        var grid = IntGrid(boardSize, boardSize)

        input.forEach {
            parseLine(it, grid)
        }

        grid.print()
        return countGridgt2(grid)
    }

    fun part2(input: List<String>, boardSize: Int): Int {
        var grid = IntGrid(boardSize, boardSize)

        input.forEach {
            parseLine(it, grid, true)
        }

        grid.print()
        return countGridgt2(grid)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Input/Day05_test")
    //println(testInput)
    println(part1(testInput, 10))
    println(part2(testInput, 10))

    val input = readInput("Input/Day05")
    //val part1Ans = part1(input, 1000)
    //println(part1Ans)
    val part2Ans = part2(input, 1000)
    println(part2Ans)
}
