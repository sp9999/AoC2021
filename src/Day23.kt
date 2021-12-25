fun main() {

    fun getHallway(g:Grid<Char>, index: Int) : Char {
        return g.getValue(1+index, 1)
    }

    fun setHallway(g: Grid<Char>, index: Int, value: Char): Pair<Int, Int> {
        val x = 1 + index
        val y = 1
        g.setValue(x, y, value)
        return Pair(x,y)
    }

    fun moveLeftAvailableHallway(g: Grid<Char>, value: Char, startPos: Pair<Int, Int>): Pair<Int, Int>? {
        var availableIndices = listOf(1,2,4,6,8,10,11)
        availableIndices.forEach() {
            if (it < startPos.first) {
                if (getHallway(g, it) == '.')
                    return setHallway(g, it, value)
            }
        }
        return null
    }

    fun moveRightAvailableHallway(g: Grid<Char>, value: Char, startPos: Pair<Int, Int>): Pair<Int, Int>? {
        var availableIndices = listOf(11,10,8,6,4,2,1)
        availableIndices.forEach() {
            if (it > startPos.first) {
                if (getHallway(g, it) == '.')
                    return setHallway(g, it, value)
            }
        }
        return null
    }

    fun getColumn(g: Grid<Char>, index: Int, height: Int) : Char{
        return g.getValue(3 + index * 2, g.ySize - 1 - height)
    }

    fun setColumn(g: Grid<Char>, index: Int, height: Int, value: Char): Pair<Int, Int>{
        val x = 3 + index * 2
        val y = g.ySize - 1 - height
        g.setValue(x, y, value)
        return Pair(x, y)
    }

    fun calcDistanceOfMove(startPos: Pair<Int, Int>, endPos: Pair<Int, Int>) : Int{
        // from column to hallway
        if ((startPos.second > 1 && endPos.second == 1) ||
            (startPos.second == 1 && endPos.second > 1)) {
            return Math.abs(endPos.first - startPos.first) + Math.abs(startPos.second - endPos.second)
        }
        // from column to column
        return Math.abs(endPos.first - startPos.first) + startPos.second + endPos.second - 2
    }

    fun moveToColumn(g: Grid<Char>, value:Char, startPos: Pair<Int, Int>) : Int? {
        var index = 0
        var multiplier = 1
        when (value) {
            'A' -> index = 0
            'B' -> {
                index = 1
                multiplier = 10
            }
            'C' -> {
                index = 2
                multiplier = 100
            }
            'D' -> {
                index = 3
                multiplier = 1000
            }
        }

        for (y in g.ySize - 2 downTo 2) {
            if (getColumn(g, index, y) == value || getColumn(g, index, y) == '.' ) {
                if (getColumn(g, index, y) == '.') {
                    var endPos = setColumn(g, index, y, value)
                    g.setValue(startPos.first, startPos.second, '.')
                    var cost = calcDistanceOfMove(startPos, endPos)
                    return cost * multiplier
                }
            }
            else
                break
        }
        return null
    }

    fun solve(g: Grid<Char>) {

    }

    // Amber one step one energy
    // Bronze 10 energy
    // Copper 100
    // Desert 1000
    fun part1(input: List<String>): Int {
        var p1Grid = Grid(13, 5, '#')
        for (i in 1 until p1Grid.m[1].size - 1)
            p1Grid.m[1][i] = '.'

        var l1Split = input[2].split('#').filter { it.isNotEmpty() && it.isNotBlank() }.map { it[0] }
        var l2Split = input[3].split('#').filter { it.isNotEmpty() && it.isNotBlank() }.map { it[0] }

        for (i in 2 .. 8 step 2) {
            p1Grid.m[2][1 + i] = l1Split[i / 2 - 1]
            p1Grid.m[3][1 + i] = l2Split[i / 2 - 1]
        }
        p1Grid.print()

        return 0
    }

    fun part2(input: List<String>): Int {
        return 0
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
