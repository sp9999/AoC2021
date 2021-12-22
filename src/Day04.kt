import java.util.*

fun main() {

    fun parseNumbers(line: String) : List<Int> {
        var listString = line.split(',')
        return listString.map{it.toInt()}
    }

    data class Cell(var value: Int = 0, var isSet:Boolean = false) {
        override fun toString() : String {
            return "${value}(${if (isSet) "1" else "0"})"
        }
    }

    data class Row(var row: List<Cell>) {
        override fun toString() : String {
            var rowString = ""
            row.forEach{
                rowString += "$it "
            }
            return rowString.trim()
        }

        fun setValue(value: Int) {
            row.forEach {
                if (it.value == value){
                    it.isSet = true
                }
            }
        }

        fun checkBingo(): Boolean {
            return row[0].isSet && row[1].isSet && row[2].isSet && row[3].isSet && row[4].isSet
        }

        fun returnListValues(): List<Int> {
            return listOf(row[0].value, row[1].value, row[2].value, row[3].value, row[4].value)
        }

        fun sumUnmarked(): Int {
            var sum = 0
            row.forEach {
                sum += if (!it.isSet) it.value else 0
            }
            return sum
        }
    }

    fun parseLineForBoard(line: String) : Row {
        var listString = line.trim().replace("  ", " ").split(' ')
        var cellList = mutableListOf<Cell>()
        listString.forEach{
            cellList.add(Cell(it.toInt()))
        }
        return Row(cellList)
    }

    class Board(input: List<String>) {
        var m: MutableList<Row> = mutableListOf()
        var columns: MutableList<Row> = mutableListOf()
        var diagonals: MutableList<Row> = mutableListOf()
        var hasBingo: Boolean = false
        init {
            input.forEach{m.add(parseLineForBoard(it))}
            addColumns()
            //addDiagonals()
        }

        fun addColumns() {
            for (i in 0 .. 4) {
                var column: MutableList<Cell> = mutableListOf()
                for (j in 0..4) {
                    column.add(Cell(m[j].row[i].value))
                }
                columns.add(Row(column))
            }
        }

        fun addDiagonals() {
            var dn: MutableList<Cell> = mutableListOf()
            var dp: MutableList<Cell> = mutableListOf()
            for (i in 0 .. 4) {
                dn.add(Cell(m[i].row[i].value))
                dp.add(Cell(m[4-i].row[i].value))
            }
            diagonals.add(Row(dn))
            diagonals.add(Row(dp))
        }

        fun print() {
            for (it in m) {
                println(it.row.toString())
            }
            println()
        }

        fun setValue(value: Int) {
            m.forEach {
                it.setValue(value)
            }
            columns.forEach {
                it.setValue(value)
            }
            diagonals.forEach {
                it.setValue(value)
            }
        }

        fun checkBingo() : List<Int> {
            m.forEach{
                if (it.checkBingo())
                    return it.returnListValues()
            }
            columns.forEach {
                if (it.checkBingo())
                    return it.returnListValues()
            }
            diagonals.forEach{
                if (it.checkBingo())
                    return it.returnListValues()
            }
            return emptyList()
        }

        fun sumUnmarked() : Int {
            var sum = 0
            m.forEach {
                sum += it.sumUnmarked()
            }
            return sum
        }
    }

    fun playBingo(boards: List<Board>, nums: List<Int>):Int {
        nums.forEach { n ->
            boards.forEach {
                it.setValue(n)
                var result = it.checkBingo()
                if (result.isNotEmpty()) {
                    val sumUnmark = it.sumUnmarked()
                    println("Unmarked Sum:${sumUnmark}")
                    println("Last number: ${n}")
                    it.print()
                    return n * sumUnmark
                }
            }
        }
        return 0
    }

    fun loseBingo(boards: MutableList<Board>, nums: List<Int>):Int {
        nums.forEach { n ->
            var iter = boards.iterator()
            while (iter.hasNext()) {
                val it = iter.next()
                it.setValue(n)
                var result = it.checkBingo()
                if (result.isNotEmpty()) {
                    iter.remove()
                    if (boards.size == 0) {
                        val sumUnmark = it.sumUnmarked()
                        println("Unmarked Sum:${sumUnmark}")
                        println("Last number: ${n}")
                        it.print()
                        return n * sumUnmark
                    }
                }
            }

        }
        return 0
    }

    fun part1(input: List<String>): Int {
        var announceNum = parseNumbers(input[0])
        var boards = mutableListOf<Board>()

        var inputLength = input.size - 2
        var boardNum = inputLength/6

        for (i in 0 .. boardNum) {
            boards.add(Board(input.subList(2 + i * 6, 7 + i * 6)))
        }

        println(announceNum)
        boards.forEach {it.print()}

        var bingo = playBingo(boards, announceNum)
        println(bingo)

        return bingo
    }

    fun part2(input: List<String>): Int {
        var announceNum = parseNumbers(input[0])
        var boards = mutableListOf<Board>()

        var inputLength = input.size - 2
        var boardNum = inputLength/6

        for (i in 0 .. boardNum) {
            boards.add(Board(input.subList(2 + i * 6, 7 + i * 6)))
        }

        println(announceNum)
        boards.forEach {it.print()}

        var bingo = loseBingo(boards, announceNum)
        println(bingo)

        return bingo
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Input/Day04_test")
    //println(testInput)
    println(part2(testInput))

    val input = readInput("Input/Day04")
    //val part1Ans = part1(input)
    //println(part1Ans)
    val part2Ans = part2(input)
    println(part2Ans)
}
