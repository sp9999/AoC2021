fun main() {

    class Board(values: List<String>) {
        val matrix = values.map {
            it.trim().split(" ").filterNot { i -> i == "" }.map { s -> s.toInt() }.toMutableList()
        }.toMutableList()

        fun isBingo(): Boolean {
            // check rows
            matrix.forEach {
                if (it.count { v -> v == -1 } == 5) return true
            }
            // check columns
            for (i in 0..4) {
                if (matrix.map { it[i] }.count { v -> v == -1 } == 5) return true
            }
            return false
        }

        fun markValue(i: Int) {
            matrix.forEach {
                it.forEachIndexed { index, s ->
                    if (s == i) it[index] = -1
                }
            }
        }

        fun calcScore(draw: Int): Int {
            return matrix.flatten().filterNot { it == -1 }.sum() * draw
        }

        override fun toString(): String {
            return matrix.joinToString("\n", "{\n", "\n}\n")
        }
    }

    fun getBoardsFromInput(input: List<String>): List<Board> {
        val boards: MutableList<Board> = mutableListOf()
        var boardLines = mutableListOf<String>()
        for (i in 2 until input.size) {
            if (input[i] != "") {
                boardLines.add(input[i])
            } else {
                boards.add(Board(boardLines))
                boardLines.clear()
            }
        }
        return boards
    }

    fun part1(input: List<String>): Int {
        val draws = input[0].split(",").map { it.toInt() }
        val boards = getBoardsFromInput(input)
        draws.forEach { draw ->
            boards.forEach { board ->
                board.markValue(draw)
                if (board.isBingo()) return board.calcScore(draw)
            }
        }
        return -1
    }

    fun part2(input: List<String>): Int {
        val draws = input[0].split(",").map { it.toInt() }
        var boards = getBoardsFromInput(input)
        draws.forEach { draw ->
            boards.forEach { it.markValue(draw) }
            if (boards.size == 1 && boards.first().isBingo()) return boards.first().calcScore(draw)
            // remove bingo'ed boards
            boards = boards.filterNot { it.isBingo() }.toMutableList()
        }
        return -1
    }

    val input = readInput("Input/Day04")
    println(part1(input))
    println(part2(input))
}