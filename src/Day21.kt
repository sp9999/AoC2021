fun main() {

    data class Die(var currRoll: Int = 0) {
        var rollCount = 0
        fun roll(): Int {
            rollCount += 1
            currRoll += 1
            if (currRoll > 100)
                currRoll = 1
            return currRoll
        }
    }
    /*split universe for all possible rolls:
      [1,1,1] 3 [2,1,1] 4 [3,1,1] 5
      [1,1,2] 4 [2,1,2] 5 [3,1,2] 6
      [1,1,3] 5 [2,1,3] 6 [3,1,3] 7
      [1,2,1] 4 [2,2,1] 5 [3,2,1] 6
      [1,2,2] 5 [2,2,2] 6 [3,2,2] 7
      [1,2,3] 6 [2,2,3] 7 [3,2,3] 8
      [1,3,1] 5 [2,3,1] 6 [3,3,1] 7
      [1,3,2] 6 [2,3,2] 7 [3,3,2] 8
      [1,3,3] 7 [2,3,3] 8 [3,3,3] 9

      3, 1
      4, 3
      5, 6
      6, 7
      7, 6
      8, 3
      9, 1
     */
    fun getDiracRolls(): List<Pair<Int, Long>> {
        return mutableListOf(
            Pair(3,1),
            Pair(4,3),
            Pair(5,6),
            Pair(6,7),
            Pair(7,6),
            Pair(8,3),
            Pair(9,1)
            )
    }



    data class Player(var pos: Int, var score: Int = 0) {
        fun move(die: Die) {
            pos = (pos + die.roll() + die.roll() + die.roll()) % 10

            if (pos == 0)
                score += 10
            else
                score += pos
        }
    }

    data class Game(var p1Pos: Int, var p2Pos: Int, var p1Score: Int = 0, var p2Score: Int = 0, var goal: Int = 21, var nextP1Turn: Boolean = true, var count: Long = 1L) {
        var p1 = Player(p1Pos, p1Score)
        var p2 = Player(p2Pos, p2Score)

        fun roll(): Pair<List<Game>, Pair<Long, Long>> {
            var gameList = mutableListOf<Game>()
            val rolls = getDiracRolls()
            var p1Wins = 0L
            var p2Wins = 0L
            rolls.forEach() {
                if (nextP1Turn) {
                    val newPos = (p1.pos + it.first) % 10
                    val newScore = p1.score + (if (newPos == 0) 10 else newPos)
                    if (newScore >= goal)
                        p1Wins += count * it.second
                    else
                        gameList.add(Game(newPos, p2.pos, newScore, p2.score, goal, false, count * it.second))
                } else {
                    val newPos = (p2.pos + it.first) % 10
                    val newScore = p2.score + (if (newPos == 0) 10 else newPos)
                    if (newScore >= goal)
                        p2Wins += count * it.second
                    else
                        gameList.add(Game(p1.pos, newPos, p1.score, newScore, goal, true, count * it.second))
                }
            }
            return Pair(gameList, Pair(p1Wins, p2Wins))
        }
    }

    fun playGame(p1Pos: Int, p2Pos: Int, goalScore: Int = 1000): Int {
        var die = Die()

        var p1 = Player(p1Pos)
        var p2 = Player(p2Pos)
        while (true) {
            p1.move(die)
            if (p1.score >= goalScore)
                break
            p2.move(die)
            if (p2.score >= goalScore)
                break
        }
        return Math.min(p1.score, p2.score) * die.rollCount
    }

/*    fun playGame2(p1Pos: Int, p2Pos: Int, goalScore: Int = 1000): Int {
        var die = DiracDie()

        var p1List = MutableList() Player(p1Pos)
        var p2List = Player(p2Pos)
        while (true) {
            p1.move(die)
            if (p1.score >= goalScore)
                break
            p2.move(die)
            if (p2.score >= goalScore)
                break
        }
        return Math.min(p1.score, p2.score) * die.rollCount
    }*/

    fun part1(input: List<String>): Int {
        var p1Pos = input[0][input[0].length - 1]
        var p2Pos = input[1][input[1].length - 1]

        return playGame(p1Pos.toString().toInt(), p2Pos.toString().toInt())
    }

    fun part2(input: List<String>): Long {
        var p1Pos = input[0][input[0].length - 1].toString().toInt()
        var p2Pos = input[1][input[1].length - 1].toString().toInt()

        var gList = mutableListOf( Game(p1Pos, p2Pos))
        var p1Win = 0L
        var p2Win = 0L
        while(gList.isNotEmpty()) {
            var newList = mutableListOf<Game>()
            gList.forEach() {
                var rollResult = it.roll()
                newList.addAll(rollResult.first)
                p1Win += rollResult.second.first
                p2Win += rollResult.second.second
            }
            gList = newList
        }
        return Math.max(p1Win, p2Win)
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
