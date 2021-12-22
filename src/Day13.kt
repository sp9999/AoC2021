fun main() {

    fun doHorizontalLineFold(g: Grid<String>, value: Int) {
        g.maxY = value

        for (i in 1 .. value) {
            for (x in 0 .. g.maxX) {
                var aboveVal = g.getValue(x, value + i)
                if (aboveVal != ".") {
                    g.setValue(x, value - i, aboveVal)
                }
            }
        }
    }

    fun doVerticalLineFold(g: Grid<String>, value: Int) {
        g.maxX = value

        for (i in 1 .. value) {
            for (y in 0 .. g.maxY) {
                var rightVal = g.getValue(value + i, y)
                if (rightVal != ".") {
                    g.setValue(value - i, y, rightVal)
                }
            }
        }
    }

    fun doFold(g: Grid<String>, instruction:String) {
        val (foldType, stringValue) = instruction.split('=')
        val isVerticalLineFold = foldType[foldType.length - 1] == 'x'
        val value = stringValue.toInt()

        if (isVerticalLineFold) doVerticalLineFold(g, value) else doHorizontalLineFold(g, value)
    }

    fun part1(input: List<String>, foldCount: Int = 1, initialX: Int = 10000, initialY: Int = 10000): Int {
        var g = Grid(initialX, initialY, ".")
        var i = 0
        while (input[i].isNotBlank()) {
            val (x, y) = input[i].split(',')
            g.setValue(x.toInt(), y.toInt(), "#")

            i += 1
        }

        i += 1 // skip empty line
        var folds = 0
        while (i < input.size) {
            doFold(g, input[i])
            i += 1
            folds += 1
            if (folds == foldCount) {
                return g.countValue("#", true)
            }
        }
        g.printToMax(1)
        return g.countValue("#", true)
    }

    fun part2(input: List<String>): Int {
        return part1(input, -1)
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
