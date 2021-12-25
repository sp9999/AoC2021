fun main() {

    class Cuboid(xVal: Pair<Int, Int>, yVal: Pair<Int, Int>, zVal: Pair<Int, Int>) {
        var x: Pair<Int, Int>
        var y: Pair<Int, Int>
        var z: Pair<Int, Int>
        var isValid: Boolean

        fun count(bound: Boolean = false): Long {
            var dx: Long
            var dy: Long
            var dz: Long
            if (bound) {
                dx = Math.abs(Math.max(x.second, -50) - Math.min(x.first, 50)) + 1L
                dy = Math.abs(Math.max(y.second, -50) - Math.min(y.first, 50)) + 1L
                dz = Math.abs(Math.max(z.second, -50) - Math.min(z.first, 50)) + 1L

            } else {
                dx = Math.abs(x.second - x.first) + 1L
                dy = Math.abs(y.second - y.first) + 1L
                dz = Math.abs(z.second - z.first) + 1L
            }

            return dx * dy * dz
        }

        fun print() {
            var c = count()
            var dx = Math.abs(x.second - x.first) + 1
            var dy = Math.abs(y.second - y.first) + 1
            var dz = Math.abs(z.second - z.first) + 1
            println("X: $x, Y: $y, Z: $z  | ($dx,$dy,$dz) |  Volume: $c")
        }

        init {
            x = Pair(xVal.first, xVal.second)
            y = Pair(yVal.first, yVal.second)
            z = Pair(zVal.first, zVal.second)
            isValid = !(x.second < x.first || y.second < y.first || z.second < z.first)
        }

        constructor(input: String) : this(Pair(0,0), Pair(0,0), Pair(0,0)) {
            val xStr = input.substring(input.indexOf("x=")+2, input.indexOf(",y"))
            val yStr = input.substring(input.indexOf("y=")+2, input.indexOf(",z"))
            val zStr = input.substring(input.indexOf("z=")+2)
            val xVal = xStr.split("..").map { it.toInt() }
            val yVal = yStr.split("..").map { it.toInt() }
            val zVal = zStr.split("..").map { it.toInt() }

            x = Pair(xVal[0], xVal[1])
            y = Pair(yVal[0], yVal[1])
            z = Pair(zVal[0], zVal[1])
            isValid = true
        }

        fun checkOverlap(c2: Cuboid): Cuboid? {
            if (x.first < c2.x.second && x.second > c2.x.first) {
                if (y.first < c2.y.second && y.second > c2.y.first){
                    if (z.first < c2.z.second && z.second > c2.z.first) {
                        var xi = Pair(Math.max(x.first, c2.x.first), Math.min(x.second, c2.x.second))
                        var yi = Pair(Math.max(y.first, c2.y.first), Math.min(y.second, c2.y.second))
                        var zi = Pair(Math.max(z.first, c2.z.first), Math.min(z.second, c2.z.second))
                        var ci = Cuboid(xi, yi, zi)
                        return ci
                    }
                }
            }
            return null
        }

        /*fun splitCube(c2: Cuboid): MutableList<Cuboid> {
            var cList = mutableListOf<Cuboid>()
            //corners
            var bul = Cuboid(Pair(x.first, c2.x.first - 1), Pair(y.first, c2.y.first - 1), Pair(z.first, c2.z.first - 1))
            if (bul.isValid) cList.add(bul)
            var ful = Cuboid(Pair(x.first, c2.x.first - 1), Pair(y.first, c2.y.first - 1), Pair(c2.z.second + 1, z.second))
            if (ful.isValid) cList.add(ful)
            var bbl = Cuboid(Pair(x.first, c2.x.first - 1),  Pair(c2.y.second + 1, y.second), Pair(z.first, c2.z.first - 1))
            if (bbl.isValid) cList.add(bbl)
            var fbl = Cuboid(Pair(x.first, c2.x.first - 1),  Pair(c2.y.second + 1, y.second), Pair(c2.z.second + 1, z.second))
            if (fbl.isValid) cList.add(fbl)
            var bur = Cuboid(Pair(c2.x.second + 1, x.second), Pair(y.first, c2.y.first - 1), Pair(z.first, c2.z.first - 1))
            if (bur.isValid) cList.add(bur)
            var fur = Cuboid(Pair(c2.x.second + 1, x.second), Pair(y.first, c2.y.first - 1), Pair(c2.z.second - 1, z.second))
            if (fur.isValid) cList.add(fur)
            var bbr = Cuboid(Pair(c2.x.second + 1, x.second), Pair(c2.y.second + 1, y.second), Pair(z.first, c2.z.first - 1))
            if (bbr.isValid) cList.add(bbr)
            var fbr = Cuboid(Pair(c2.x.second + 1, x.second), Pair(c2.y.second + 1, y.second), Pair(c2.z.second + 1, z.second))
            if (fbr.isValid) cList.add(fbr)

            // centers
            var tc = Cuboid(Pair(Math.max(c2.x.first, x.first), Math.min(c2.x.second - 1, x.second)), Pair(y.first, c2.y.first - 1), Pair(Math.max(c2.z.first, z.first), Math.min(c2.z.second - 1, z.second)))
            if (tc.isValid) cList.add(tc)
            var lc = Cuboid(Pair(x.first, c2.x.first - 1), Pair(Math.max(c2.y.first + 1, y.first), Math.min(c2.y.second - 1, y.second)), Pair(Math.max(c2.z.first + 1, z.first), Math.min(c2.z.second - 1, z.second)))
            if (lc.isValid) cList.add(lc)
            var rc = Cuboid(Pair(c2.x.second + 1, x.second), Pair(Math.max(c2.y.first + 1, y.first), Math.min(c2.y.second - 1, y.second)), Pair(Math.max(c2.z.first + 1, z.first), Math.min(c2.z.second - 1, z.second)))
            if (rc.isValid) cList.add(rc)
            var bac = Cuboid(Pair(Math.max(c2.x.first + 1, x.first), Math.min(c2.x.second - 1, x.second)), Pair(Math.max(c2.y.first + 1, y.first), Math.min(c2.y.second - 1, y.second)), Pair(z.first, c2.z.first - 1))
            if (bac.isValid) cList.add(bac)
            var fc = Cuboid(Pair(Math.max(c2.x.first + 1, x.first), Math.min(c2.x.second - 1, x.second)), Pair(Math.max(c2.y.first + 1, y.first), Math.min(c2.y.second - 1, y.second)), Pair(c2.z.second + 1, z.second))
            if (fc.isValid) cList.add(fc)
            var bc = Cuboid(Pair(Math.max(c2.x.first + 1, x.first), Math.min(c2.x.second - 1, x.second)), Pair(c2.y.second + 1, y.second), Pair(Math.max(c2.z.first + 1, z.first), Math.min(c2.z.second - 1, z.second)))
            if (bc.isValid) cList.add(bc)

            // edges
            var tb = Cuboid(Pair(Math.max(c2.x.first + 1, x.first), Math.min(c2.x.second - 1, x.second)), Pair(y.first, c2.y.first - 1), Pair(z.first, c2.z.first - 1))
            if (tb.isValid) cList.add(tb)
            var tl = Cuboid(Pair(x.first, c2.x.first - 1), Pair(y.first, c2.y.first - 1), Pair(Math.max(c2.z.first + 1, z.first), Math.min(c2.z.second - 1, z.second)))
            if (tl.isValid) cList.add(tl)
            var tr = Cuboid(Pair(c2.x.second + 1, x.second), Pair(y.first, c2.y.first - 1), Pair(Math.max(c2.z.first + 1, z.first), Math.min(c2.z.second - 1, z.second)))
            if (tr.isValid) cList.add(tr)
            var tf = Cuboid(Pair(Math.max(c2.x.first + 1, x.first), Math.min(c2.x.second - 1, x.second)), Pair(y.first, c2.y.first - 1), Pair(c2.z.second + 1, z.second))
            if (tf.isValid) cList.add(tf)

            var bb = Cuboid(Pair(Math.max(c2.x.first + 1, x.first), Math.min(c2.x.second - 1, x.second)), Pair(c2.y.second + 1, y.second), Pair(z.first, c2.z.first - 1))
            if (bb.isValid) cList.add(bb)
            var bl = Cuboid(Pair(x.first, c2.x.first - 1), Pair(c2.y.second + 1, y.second), Pair(Math.max(c2.z.first + 1, z.first), Math.min(c2.z.second - 1, z.second)))
            if (bl.isValid) cList.add(bl)
            var br = Cuboid(Pair(c2.x.second + 1, x.second), Pair(c2.y.second + 1, y.second), Pair(Math.max(c2.z.first + 1, z.first), Math.min(c2.z.second - 1, z.second)))
            if (br.isValid) cList.add(br)
            var bf = Cuboid(Pair(Math.max(c2.x.first + 1, x.first), Math.min(c2.x.second - 1, x.second)), Pair(c2.y.second + 1, y.second), Pair(c2.z.second + 1, z.second))
            if (bf.isValid) cList.add(bf)

            var mbl = Cuboid(Pair(x.first, c2.x.first - 1), Pair(Math.max(y.first, c2.y.first + 1), Math.min(y.second, c2.y.second - 1)), Pair(z.first, c2.z.first - 1))
            if (mbl.isValid) cList.add(mbl)
            var mbr = Cuboid(Pair(c2.x.second + 1, x.second), Pair(Math.max(y.first, c2.y.first + 1), Math.min(y.second, c2.y.second - 1)), Pair(z.first, c2.z.first - 1))
            if (mbr.isValid) cList.add(mbr)
            var mfl = Cuboid(Pair(x.first, c2.x.first - 1), Pair(Math.max(y.first, c2.y.first + 1), Math.min(y.second, c2.y.second - 1)), Pair(c2.z.second + 1, z.second))
            if (mfl.isValid) cList.add(mfl)
            var mfr = Cuboid(Pair(c2.x.second + 1, x.second), Pair(Math.max(y.first, c2.y.first + 1), Math.min(y.second, c2.y.second - 1)), Pair(c2.z.second + 1, z.second))
            if (mfr.isValid) cList.add(mfr)

            return cList
        }*/
    }



    fun turnCuboid(cuboidSet: MutableSet<Vec4i>, x: Pair<Int, Int>, y: Pair<Int, Int>, z: Pair<Int, Int>,
                   turnOn: Boolean, bounds: Boolean = true,
                   xBound: Pair<Int, Int> = Pair(-50, 50), yBound: Pair<Int, Int> = Pair(-50, 50), zBound: Pair<Int, Int> = Pair(-50, 50)) {
        for (xVal in x.first .. x.second) {
            if (bounds && (xVal < xBound.first || xVal > xBound.second))
                continue
            for (yVal in y.first .. y.second) {
                if (bounds && (yVal < yBound.first || yVal > yBound.second))
                    continue
                for (zVal in z.first .. z.second) {
                    if (bounds && (zVal < zBound.first || zVal > zBound.second))
                        continue
                    if (turnOn) {
                        cuboidSet.add(Vec4i(xVal, yVal, zVal,1))
                    } else {
                        val newPt = Vec4i(xVal, yVal, zVal,1)
                        if (cuboidSet.contains(newPt)) {
                            cuboidSet.remove(newPt)
                        }
                    }
                }
            }
        }
    }

    fun parseLine(input: String, cuboidSet: MutableSet<Vec4i>, bounds: Boolean = true) {
        val xStr = input.substring(input.indexOf("x=")+2, input.indexOf(",y"))
        val yStr = input.substring(input.indexOf("y=")+2, input.indexOf(",z"))
        val zStr = input.substring(input.indexOf("z=")+2)

        val xVal = xStr.split("..").map { it.toInt() }
        val yVal = yStr.split("..").map { it.toInt() }
        val zVal = zStr.split("..").map { it.toInt() }

        val turnOn = input[1] == 'n'

        turnCuboid(cuboidSet, Pair(xVal[0], xVal[1]), Pair(yVal[0], yVal[1]), Pair(zVal[0], zVal[1]), turnOn, bounds)
    }

    fun part1(input: List<String>): Int {
        var cuboidSet = mutableSetOf<Vec4i>()
        for (i in 0 until input.size) {
            parseLine(input[i], cuboidSet)
        }
        return cuboidSet.size
    }

    fun part2(input: List<String>): Long {
        var positiveCuboidList = mutableListOf<Cuboid>()
        var negativeCuboidList = mutableListOf<Cuboid>()

        input.forEach() {
            var newC = Cuboid(it)

            var tempPList = mutableListOf<Cuboid>()
            var tempNList = mutableListOf<Cuboid>()
            positiveCuboidList.forEach() {
                var intersect = it.checkOverlap(newC)
                if (intersect != null) {
                    tempNList.add(intersect)
                }
            }
            negativeCuboidList.forEach() {
                var intersect = it.checkOverlap(newC)
                if (intersect != null) {
                    tempPList.add(intersect)
                }
            }
            if (it[1] == 'n') {
                positiveCuboidList.add(newC)
            }
            negativeCuboidList.addAll(tempNList)
            positiveCuboidList.addAll(tempPList)
        }
        var sumPos = positiveCuboidList.sumOf { it.count() }
        var sumNeg = negativeCuboidList.sumOf { it.count() }
        return sumPos - sumNeg
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Input/input_test")
    //println(testInput)
    //println("Test P1: " + part1(testInput))
    println()
    println("Test P2: " + part2(testInput))
    println()

    val input = readInput("Input/input")
    //val part1Ans = part1(input)
    //println("Part1: ${part1Ans}")
    println()

    val part2Ans = part2(input)
    println("Part2: ${part2Ans}")
    println()
}
