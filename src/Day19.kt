fun main() {
    class Scanner(val index: Int, var beacons: MutableList<Vec4i> = mutableListOf()) {
        var vectors: Map<Int, Pair<Vec4i, Vec4i>>? = null

        fun generateVectors() {
            vectors =
                beacons.flatMapIndexed { i1, v1 ->
                    beacons.subList(i1+1, beacons.size).mapIndexed { i2, v2 ->
                        v1.distanceSqr(v2) to (v1 to v2)
                    }
                }.groupBy {it.first}.mapValues { (_, v) ->
                    v.also { require(it.size == 1) { "Unexpected dupe distance in beacons" } }.first().second
                }
        }

        fun translateTo(anchor: Scanner): Pair<Scanner, Vec4i>? {
            val distanceOverlap = this.vectors!!.keys intersect anchor.vectors!!.keys

            if (distanceOverlap.size < 12) {
                return null
            }

            val matPair = distanceOverlap.flatMap { dist ->
                val (myB1, myB2) = this.vectors!![dist]!!
                val (anchorB1, anchorB2) = anchor.vectors!![dist]!!

                ROTS.mapNotNull { rot ->
                    // try to rotate the beacons in the distance pair
                    val rotB1 = rot * myB1
                    val rotB2 = rot * myB2

                    // the delta between these pairs should be the same now
                    if (rotB1 - anchorB1 == rotB2 - anchorB2) {
                        val translation = Mat4i.translate(anchorB1 - rotB1)
                        (translation * rot)
                    } else if (rotB2 - anchorB1 == rotB1 - anchorB2) {
                        val translation = Mat4i.translate(anchorB2 - rotB1)
                        (translation * rot)
                    } else null
                }
            }.groupBy { it }.mapValues { (_, c) -> c.size }.maxByOrNull { it.value }

            // discard matrices that didn't yield at least 12 overlapping beacons
            if (matPair == null || matPair.value < 12) {
                return null
            }

            // transform the scanner with our transform matrix and also transform 0,0,0 to get scanner's position relative to
            // the anchor
            var newS = Scanner(index, beacons.map { (matPair.key * it).asPoint }.toMutableList()) to matPair.key * Point3i(0,0,0)
            newS.first.generateVectors()
            return newS
        }
    }

    fun parseScanners(input: List<String>): List<Scanner>{
        var scanners: MutableList<Scanner> = mutableListOf()
        input.forEach() {
            if (it.contains("scanner")) {
                var idString = it.substring(it.indexOf("scanner ") + "scanner ".length)
                var id = idString.substring(0, idString.indexOf(" ")).toInt()
                scanners.add(Scanner(id))
            }
            else if (it.isNotEmpty()) {
                var (x,y,z) = it.split(',').map { coord -> coord.toInt() }
                scanners[scanners.size - 1].beacons.add(Point3i(x,y,z))
            }
        }
        scanners.forEach() {
            it.generateVectors()
        }
        return scanners
    }

    fun solve(input: List<Scanner>): List<Pair<Scanner, Vec4i>> {
        val anchored = mutableListOf(input.first() to Point3i(0, 0, 0))
        val scanners = input.drop(1).toMutableList()

        while (scanners.isNotEmpty()) {
            val beginSz = scanners.size

            // iterate over scanners and try to anchor them
            val iter = scanners.listIterator()
            while (iter.hasNext()) {
                val scanner = iter.next()
                val matches = anchored.firstNotNullOfOrNull { (anchor, _) -> scanner.translateTo(anchor) }
                if (matches != null) {
                    anchored.add(matches)
                    iter.remove()
                }
            }

            require(scanners.size != beginSz) { "Couldn't anchor any scanner!" }
        }

        return anchored
    }
    fun part1(input: List<String>): Int {
        var s = parseScanners(input)
        var listS = solve(s)

        //while(localScanners.isNotEmpty() {
        //}
       // s[1].bringToAbsolutePosition(s[0])
        //s[4].bringToAbsolutePosition(s[1])
/*
        var beaconList: MutableSet<Vector3> = mutableSetOf()
        for (i in 1 until s.size) {
            s[i].absoluteBeacons.map {
                if (s[0].beacons.contains(it))
                    beaconList.add(it)
            }
        }*/
        return listS.flatMap{(scanner, _) -> scanner.beacons }.toSet().size
    }

    fun part2(input: List<String>): Int {
        var s = parseScanners(input)
        var listS = solve(s)

        var maxDist = 0
        for (i in 0 until listS.size) {
            for (j in 0 until listS.size) {
                var d = listS[i].second.distanceManhattan(listS[j].second)
                if (maxDist < d)
                    maxDist = d
            }
        }
        return maxDist
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Input/Day19_test")
    //println(testInput)
    println("Test P1: " + part1(testInput))
    println()
    println("Test P2: " + part2(testInput))
    println()

    val input = readInput("Input/Day19")
    val part1Ans = part1(input)
    println("Part1: ${part1Ans}")
    println()

    val part2Ans = part2(input)
    println("Part2: ${part2Ans}")
    println()
}
