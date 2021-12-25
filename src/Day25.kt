fun main() {

    data class Board(var g: Grid<Char>, var eastList: MutableList<Point>, var southList: MutableList<Point>) {
        var stepCount = 0
        var newEastList: MutableList<Point> = mutableListOf()

        fun checkStepViable(p: Point, eastStep: Boolean = true) : Point? {
            if (eastStep) {
                val xVal = (p.x + 1) % g.xSize
                val stepSpace = g.getValue(xVal, p.y)
                if (stepSpace == '.')
                    return Point(xVal, p.y)
            } else {
                val yVal = (p.y + 1) % g.ySize
                val stepSpace = g.getValue(p.x, yVal)
                if ((stepSpace == '.' || stepSpace == '>') && !newEastList.contains(Point(p.x, yVal)))
                    return Point(p.x, yVal)
            }
            return null
        }

        fun step() : Boolean {
            newEastList = mutableListOf<Point>()
            var newSouthList = mutableListOf<Point>()
            var newGrid = Grid(g.xSize, g.ySize, '.')
            var stepTaken = false
            eastList.forEach() {
                var stepCheck = checkStepViable(it)
                if (stepCheck != null) {
                    newEastList.add(stepCheck)
                    stepTaken = true
                } else {
                    newEastList.add(it)
                }
            }
            southList.forEach() {
                var stepCheck = checkStepViable(it, false)
                if (stepCheck != null) {
                    newSouthList.add(stepCheck)
                    stepTaken = true
                } else {
                    newSouthList.add(it)
                }
            }

            newEastList.forEach() { newGrid.setValue(it.x, it.y, '>')}
            newSouthList.forEach() { newGrid.setValue(it.x, it.y, 'v')}
            g = newGrid
            eastList = newEastList
            southList = newSouthList


            stepCount += 1
            return stepTaken
        }

        fun print() {
            println("Steps: $stepCount")
            g.print(1)
        }
    }

    fun parseInput(input: List<String>): Board {
        var g = Grid(input[0].length, input.size, '.')
        var eastList = mutableListOf<Point>()
        var southList = mutableListOf<Point>()

        for (y in 0 until input.size) {
            for (x in 0 until input[0].length) {
                var c = input[y][x]
                g.setValue(x,y,c)
                if (c == '>')
                    eastList.add(Point(x,y))
                else if (c == 'v')
                    southList.add(Point(x,y))
            }
        }
        return Board(g, eastList, southList)
    }

    fun part1(input: List<String>): Int {
        var b = parseInput(input)
        b.print()

        while(b.step()) {}

        b.print()
        return b.stepCount
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Input/input_test")
    //println(testInput)
    println("Test P1: " + part1(testInput))
    println()

    val input = readInput("Input/input")
    val part1Ans = part1(input)
    println("Part1: ${part1Ans}")
    println()

}
