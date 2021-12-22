fun main() {

    fun get3x3AsValue(grid: Grid<Char>, x: Int, y: Int): Int {
        var binaryVal = ""
        for (j in y - 2..y) {
            for (i in x - 2..x) {
                if (grid.getValue(i,j) == '#')
                    binaryVal += "1"
                else
                    binaryVal += "0"
            }
        }
        return binaryVal.toInt(2)
    }

    fun applyAlgo(algo: String, input: Grid<Char>, defaultValue: Char = '.'): Grid<Char> {
        var newX = input.maxX + 4
        var newY = input.maxY + 4

        var newG = Grid(newX, newY, defaultValue)
        for (j in 0 .. input.maxY + 2) {
            for (i in 0 .. input.maxX + 2) {
                var index = get3x3AsValue(input, i,j)
                if (algo[index] != defaultValue)
                    newG.setValue(i,j, algo[index])
            }
        }
        return newG
    }
    fun applyAlgoTwice(algo: String, input: Grid<Char>, currentBackfill: Char = '.', print: Boolean = false): Grid<Char> {

        var backfillAlgo = if (currentBackfill == '.') 0 else 511
        var newG = applyAlgo(algo, input, (algo[backfillAlgo]))
        if (print) {
            newG.printToMax(2)
            println("Size: [${newG.xSize},${newG.ySize}]   Max: [${newG.maxX},${newG.maxY}]")
        }

        backfillAlgo = if (algo[backfillAlgo] == '.') 0 else 511
        newG = applyAlgo(algo, newG, algo[backfillAlgo])

        if (print) {
            newG.printToMax(2)
            println("Size: [${newG.xSize},${newG.ySize}]   Max: [${newG.maxX},${newG.maxY}]")
        }
        return newG
    }

    fun part1(input: List<String>): Int {
        var algo = input[0]

        var inputImage = Grid(input[2].length, input.size - 2, '.')
        inputImage.maxX = inputImage.xSize
        inputImage.maxY = inputImage.ySize
        for (i in 2 until input.size) {
            inputImage.m[i - 2] = input[i].toMutableList()
        }

        inputImage = applyAlgoTwice(algo, inputImage, print = true)

        return inputImage.countValue('#', true)
    }

    fun part2(input: List<String>): Int {
        var algo = input[0]

        var inputImage = Grid(input[2].length, input.size - 2, '.')
        inputImage.maxX = inputImage.xSize
        inputImage.maxY = inputImage.ySize
        for (i in 2 until input.size) {
            inputImage.m[i - 2] = input[i].toMutableList()
        }

        for (i in 0 until 25) {
            inputImage = applyAlgoTwice(algo, inputImage)
        }

        return inputImage.countValue('#', true)
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
