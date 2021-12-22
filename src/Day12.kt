import java.util.ArrayDeque
import java.util.Collections

fun main() {
    fun parseLine(caveMap:MutableMap<String, MutableList<String>>, input: String) {
        var it = input.split('-')
        if (caveMap.contains(it[0])) {
            caveMap[it[0]]?.add(it[1])
        }
        else {
            caveMap.set(it[0], mutableListOf(it[1]))
        }

        if (caveMap.contains(it[1])) {
            caveMap[it[1]]?.add(it[0])
        }
        else {
            caveMap.set(it[1], mutableListOf(it[0]))
        }
    }

    fun removeSmallConnectionsAlreadyVisited(connections: MutableList<String>, visited: List<String>) {
        visited.forEach() {
            if (connections.contains(it)) {
                if (it[0] >= 'a' && it[0] <= 'z') {
                    connections.remove(it)
                }
            }
        }
    }

    fun findPath(caveMap:MutableMap<String, MutableList<String>>, currPath: ArrayDeque<String>,
                  start: String, end: String, completedPaths: MutableList<List<String>>): List<String> {
        currPath.push(start)
        if (start == end)
            return currPath.toList()

        var connections = caveMap[start]?.toMutableList()
        if (connections != null) {
            removeSmallConnectionsAlreadyVisited(connections, currPath.toList())

            if (connections.isNotEmpty()) {
                connections.forEach() {
                    var path = findPath(caveMap, currPath, it, end, completedPaths)
                    if (path.isNotEmpty())
                        completedPaths.add(path)
                    currPath.pop()
                }
            }
        }
        return emptyList()
    }

    fun findPath2(caveMap:MutableMap<String, MutableList<String>>, currPath: ArrayDeque<String>,
                 start: String, end: String, completedPaths: MutableList<List<String>>): List<String> {
        currPath.push(start)

        if (start == end)
            return currPath.toList()

        var visitedTwice = false
        for (cave in currPath.distinct()) {
            visitedTwice = (Collections.frequency(currPath, cave) == 2 && cave[0] >= 'a' && cave[0] <= 'z')
            if (visitedTwice)
                break
        }

        var connections = caveMap[start]?.toMutableList()

        if (connections != null) {
            if (connections.contains("start"))
                connections.remove("start")
            if (visitedTwice)
                removeSmallConnectionsAlreadyVisited(connections, currPath.toList())

            if (connections.isNotEmpty()) {
                connections.forEach() {
                    var path = findPath2(caveMap, currPath, it, end, completedPaths)
                    if (path.isNotEmpty())
                        completedPaths.add(path)
                    currPath.pop()
                }
            }
        }
        return emptyList()
    }

    fun part1(input: List<String>): Int {

        var caveMap = mutableMapOf<String, MutableList<String>>()
        input.forEach() {
            parseLine(caveMap, it)
        }
        println(caveMap)

        var completedPaths = mutableListOf<List<String>>()
        findPath(caveMap, ArrayDeque<String>(), "start", "end", completedPaths)

        return completedPaths.size
    }

    fun part2(input: List<String>): Int {
        var caveMap = mutableMapOf<String, MutableList<String>>()
        input.forEach() {
            parseLine(caveMap, it)
        }
        println(caveMap)

        var completedPaths = mutableListOf<List<String>>()
        findPath2(caveMap, ArrayDeque<String>(), "start", "end", completedPaths)
        println(completedPaths)
        return completedPaths.size
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
