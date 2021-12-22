import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs
import kotlin.math.sqrt

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt").readLines()
fun readInputAsNum(name: String) = File("src", "$name.txt").readLines().map {
    it.toInt()
}
fun parseLinesAsGridOfInts(input: List<String>) : IntGrid {
    var g = IntGrid(input[0].length, input.size)
    var y = 0
    input.forEach() {
        var x = 0
        it.forEach {
            c ->
            g.setValue(x,y, c.digitToInt())
            x += 1
        }
        y += 1
    }
    return g
}

fun parseLineAsListOfLongs(input: String, delimiter: Char = ',') : MutableList<Long> {
    return input.split(delimiter).map{it.toLong()}.toMutableList()
}
fun parseLineAsListOfInts(input: String, delimiter: Char = ',') : MutableList<Int> {
    return input.split(delimiter).map{it.toInt()}.toMutableList()
}
fun parseLineAsListOfStrings(input: String, delimiter: Char = ',') : MutableList<String> {
    return input.split(delimiter).map{it}.toMutableList()
}

fun parseLineOfIntsIntoMap(input: String) : MutableMap<Int, Int> {
    var intList = parseLineAsListOfInts(input)
    var groupedList: MutableMap<Int, Int> = mutableMapOf()

    intList.forEach() {
        if (groupedList.containsKey(it)) {
            groupedList.set(it, groupedList.get(it)?.plus(1) ?: 1)
        } else {
            groupedList.set(it, 1)
        }
    }
    return groupedList
}

fun parseLineOfCharIntoMap(input: String) : MutableMap<Char, Int> {
    var groupedList: MutableMap<Char, Int> = mutableMapOf()

    input.forEach() {
        if (groupedList.containsKey(it)) {
            groupedList.set(it, groupedList.get(it)?.plus(1) ?: 1)
        } else {
            groupedList.set(it, 1)
        }
    }
    return groupedList
}

fun parseLinesOfIntIntoGrid(input:List<String>): IntGrid {
    var height = input.size
    var width = input[0].length

    var g = IntGrid(width, height)
    for (y in 0 until height) {
        for (x in 0 until width) {
            g.setValue(x,y, input[y][x].digitToInt())
        }
    }

    return g
}

fun sumList(numList: List<Int>): Int {
    var sum = 0
    numList.forEach {
        sum += it
    }
    return sum
}

fun prodList(numList: List<Int>): Int {
    var prod = 1
    numList.forEach {
        prod *= it
    }
    return prod
}

fun andTwoStrings(a: String, b:String) : String {
    return a.replace("[^${b}]".toRegex(), "")
}

fun subtractTwoStrings(a: String, b:String) : String {
    return a.replace("[${b}]".toRegex(), "")
}


data class Point(var x: Int, var y: Int) {
    fun print() {
        println("[$x,$y]")
    }

    fun addX(xStep: Int): Point {
        return Point(x + xStep, y)
    }

    fun addY(yStep: Int): Point {
        return Point(x, y + yStep)
    }

    fun add(p2: Point): Point {
        return Point(x + p2.x, y + p2.y)
    }
}

fun <T> generateRow(size: Int, value: T): MutableList<T> {
    return (0 until size).map { value }.toMutableList()
}

data class IntGrid(var xSize: Int, var ySize: Int, var value: Int = 0) {
    var m: MutableList<MutableList<Int>> = mutableListOf()
    init {
        for(i in 0 until ySize)
            m.add(generateRow(xSize, value))

    }

    fun print(padding: Int = 5) {
        m.forEach { n->
            var row = ""
            n.forEach {
                var padString = ""
                for (i in it.toString().length until padding) {
                    padString += " "
                }
                row += padString + it.toString()
            }
            println(row)
        }
        println()
    }

    fun setValue(x: Int, y: Int, value: Int) {
        m[y][x] = value
    }

    fun setValue(p: Point, value: Int) {
        m[p.y][p.x] = value
    }

    fun incValue(x: Int, y:Int) {
        m[y][x] = m[y][x] + 1
    }

    fun getValue(x: Int, y:Int) : Int {
        return m[y][x]
    }

    fun getValue(p: Point) : Int {
        return m[p.y][p.x]
    }

    fun incAll() {
        for (y in 0 until ySize) {
            for (x in 0 until xSize) {
                incValue(x,y)
            }
        }
    }
}

data class Grid<T>(val xSize: Int, val ySize: Int, val value: T) {
    var m: MutableList<MutableList<T>> = mutableListOf()
    var maxX = 0
    var maxY = 0

    init {
        for(i in 0 until ySize)
            m.add(generateRow(xSize, value))

    }

    fun printToMax(padLength: Int = 5) {
        for (y in 0 .. maxY) {
            var row = ""
            for (x in 0 .. maxX) {
                var padString = ""
                for (i in m[y][x].toString().length until padLength) {
                    padString += " "
                }
                row += padString + m[y][x].toString()
            }
            println(row)
        }
        println()
    }
    fun print(padLength: Int = 5) {
        m.forEach { n->
            var row = ""
            n.forEach {
                var padString = ""
                for (i in it.toString().length until padLength) {
                    padString += " "
                }
                row += padString + it.toString()
            }
            println(row)
        }
        println()
    }

    fun setValue(x: Int, y: Int, value: T) {
        if (x > maxX)
            maxX = x
        if (y > maxY)
            maxY = y
        m[y][x] = value
    }

    fun incValue(x: Int, y:Int) {
        //m[y][x] = Integer(m[y][x] + 1
    }

    fun getValue(x: Int, y:Int) : T {
        if (x < 0 || y < 0 || x >= xSize || y >= ySize)
            return value
        return m[y][x]
    }

    fun incAll() {
        for (y in 0 until ySize) {
            for (x in 0 until xSize) {
                incValue(x,y)
            }
        }
    }

    fun countValue(value: T, useMax: Boolean): Int {
        val limitX = if (useMax) maxX else xSize
        val limitY = if (useMax) maxY else ySize

        var sum = 0
        for (y in 0 .. limitY) {
            for (x in 0 .. limitX) {
                if (m[y][x] == value)
                    sum += 1
            }
        }
        return sum
    }
}

fun countOccurrences(s: String, ch: Char): Int {
    return s.filter { it == ch }.count()
}


fun getFreqMap(chars: String): Map<Char, Long> {
    val freq: MutableMap<Char, Long> = HashMap()
    for (c in chars)
    {
        freq.putIfAbsent(c, 0)
        freq[c] = freq[c]!! + 1L
    }
    return freq
}

data class Point3D(var x:Int = 0, var y:Int = 0, var z: Int = 0) {
    fun isEqual(p2: Point3D): Boolean {
        return x == p2.x && y == p2.y && z == p2.z
    }

    fun minus(p2: Point3D): Point3D {
        return Point3D(x - p2.x, y - p2.y, z - p2.z)
    }

    fun minus(v2: Vector3D): Point3D {
        return Point3D(x - v2.x, y - v2.y, z - v2.z)
    }

    fun add(p2: Vector3D): Point3D {
        return Point3D(x + p2.x, y + p2.y, z + p2.z)
    }

    fun multiply(v: Vector3D): Point3D {
        return Point3D(x * v.x, y * v.y, z * v.z)
    }
}

const val EPISLON: Double = 0.0001
fun Vector3D(p: Point3D) = Vector3D(Point3D(), p)

data class Vector3D(val p1: Point3D, val p2: Point3D) {
    val x: Int = p2.x - p1.x
    val y: Int = p2.y - p1.y
    val z: Int = p2.z - p1.z
    val magnitude: Double = sqrt((x*x + y*y + z*z).toDouble())
    val uX: Double = x.toDouble() / magnitude
    val uY: Double = y.toDouble() / magnitude
    val uZ: Double = z.toDouble() / magnitude
    var rX = 0
    var rY = 0
    var rZ = 0

    fun rot90(v: Vector3D): Vector3D {
        var newV: Vector3D? = null
        for (rx in 0 until v.x) {
            newV = newV?.rotX90() ?: rotX90()
        }
        for (ry in 0 until v.y) {
            newV = newV?.rotY90() ?: rotY90()
        }
        for (rz in 0 until v.z) {
            newV = newV?.rotZ90() ?: rotZ90()
        }
        return newV ?: this
    }
    fun rotX90(): Vector3D {
        var v = Point3D(x,y,z)
        v.y = z
        v.z = -y
        rX = (rX + 1) % 4
        var newV = Vector3D(v)
        newV.rX = rX
        return newV
    }

    fun rotY90(): Vector3D {
        var v = Point3D(x,y,z)
        v.x = -z
        v.z = x
        rY = (rY + 1) % 4
        var newV = Vector3D(v)
        newV.rY = rY
        return newV
    }

    fun rotZ90(): Vector3D {
        var v = Point3D(x,y,z)
        v.x = y
        v.y = -x
        rZ = (rZ + 1) % 4
        var newV = Vector3D(v)
        newV.rZ = rZ
        return newV
    }


    fun isEqual(v2: Vector3D, tryRotating: Boolean = false): Boolean {
        if (abs(magnitude - v2.magnitude) < EPISLON) {
            if (abs(uX - v2.uX) < EPISLON &&
                abs(uY - v2.uY) < EPISLON &&
                abs(uZ - v2.uZ) < EPISLON)
                return true
            else if (tryRotating) {
                var r = v2.rotX90()
                for (i in 0 .. 2) {
                    if (isEqual(r)) {
                        v2.rX = r.rX
                        return true
                    }
                    r = r.rotX90()
                }
                r = v2.rotY90()
                for (i in 0 .. 2) {
                    if (isEqual(r)) {
                        v2.rX = r.rX
                        v2.rY = r.rY
                        return true
                    }
                    r = r.rotY90()
                }
                r = v2.rotZ90()
                for (i in 0 .. 2) {
                    if (isEqual(r)) {
                        v2.rX = r.rX
                        v2.rY = r.rY
                        v2.rZ = r.rZ
                        return true
                    }
                    r = r.rotZ90()
                }

            }
        }
        return false
    }
}

data class Vector3(var x: Double = 0.0, var y: Double = 0.0, var z: Double = 0.0) {
    constructor(v: Double) : this(v, v, v)
    constructor(v: Vector3) : this(v.x, v.y, v.z)

    operator fun unaryMinus() = Vector3(-x, -y, -z)
    operator fun inc() = Vector3(x++, y++, z++)
    operator fun dec() = Vector3(x--, y--, z--)

    inline operator fun plus(v: Double) = Vector3(x + v, y + v, z + v)
    inline operator fun minus(v: Double) = Vector3(x - v, y - v, z - v)
    inline operator fun times(v: Double) = Vector3(x * v, y * v, z * v)
    inline operator fun div(v: Double) = Vector3(x / v, y / v, z / v)

    inline operator fun plus(v: Vector3) = Vector3(x + v.x, y + v.y, z + v.z)
    inline operator fun minus(v: Vector3) = Vector3(x - v.x, y - v.y, z - v.z)
    inline operator fun times(v: Vector3) = Vector3(x * v.x, y * v.y, z * v.z)
    inline operator fun div(v: Vector3) = Vector3(x / v.x, y / v.y, z / v.z)

    fun isEqual(p2: Vector3): Boolean {
        return x == p2.x && y == p2.y && z == p2.z
    }
 }

inline fun abs(v: Vector3) = Vector3(abs(v.x), abs(v.y), abs(v.z))
inline fun length(v: Vector3) = sqrt(v.x * v.x + v.y * v.y + v.z * v.z)
inline fun length2(v: Vector3) = v.x * v.x + v.y * v.y + v.z * v.z
inline fun distance(a: Vector3, b: Vector3) = length(a - b)
inline fun dot(a: Vector3, b: Vector3) = a.x * b.x + a.y * b.y + a.z * b.z
inline fun cross(a: Vector3, b: Vector3): Vector3 {
    return Vector3(a.y * b.z - a.z * b.y, a.z * b.x - a.x * b.z, a.x * b.y - a.y * b.x)
}
inline infix fun Vector3.x(v: Vector3): Vector3 {
    return Vector3(y * v.z - z * v.y, z * v.x - x * v.z, x * v.y - y * v.x)
}
fun normalize(v: Vector3): Vector3 {
    val l = 1.0 / length(v)
    return Vector3(v.x * l, v.y * l, v.z * l)
}

inline fun getNormalVector(a: Vector3, b: Vector3): Vector3 {
    println(a)
    println(b)
    return normalize(cross(a, b))
}


inline fun getTransformationMatrixBetweenTwoVectors(a: Vector3, b:Vector3): Matrix3 {
    var n = getNormalVector(a, b)
    var nxa = n.x(a)
    var nxb = n.x(b)
    var mToA = Matrix3(a, nxa, n)
    var mToB = Matrix3(b, nxb, n)
    var mToBInverse = inverse(mToB)
    return mToBInverse
}

data class Matrix3(
    var x: Vector3 = Vector3(x = 1.0),
    var y: Vector3 = Vector3(y = 1.0),
    var z: Vector3 = Vector3(z = 1.0)) {
    constructor(m: Matrix3) : this(m.x.copy(), m.y.copy(), m.z.copy())

    companion object {
        fun of(vararg a: Double): Matrix3 {
            require(a.size >= 9)
            return Matrix3(
                Vector3(a[0], a[3], a[6]),
                Vector3(a[1], a[4], a[7]),
                Vector3(a[2], a[5], a[8])
            )
        }

        fun identity() = Matrix3()
    }

    operator fun times(m: Matrix3) = Matrix3(
        Vector3(
            x.x * m.x.x + y.x * m.x.y + z.x * m.x.z,
            x.y * m.x.x + y.y * m.x.y + z.y * m.x.z,
            x.z * m.x.x + y.z * m.x.y + z.z * m.x.z,
        ),
        Vector3(
            x.x * m.y.x + y.x * m.y.y + z.x * m.y.z,
            x.y * m.y.x + y.y * m.y.y + z.y * m.y.z,
            x.z * m.y.x + y.z * m.y.y + z.z * m.y.z,
        ),
        Vector3(
            x.x * m.z.x + y.x * m.z.y + z.x * m.z.z,
            x.y * m.z.x + y.y * m.z.y + z.y * m.z.z,
            x.z * m.z.x + y.z * m.z.y + z.z * m.z.z,
        )
    )

    operator fun times(v: Vector3) = Vector3(
        x.x * v.x + y.x * v.y + z.x * v.z,
        x.y * v.x + y.y * v.y + z.y * v.z,
        x.z * v.x + y.z * v.y + z.z * v.z,
    )

    operator fun times(v: Double) = Matrix3(x * v, y * v, z * v)
}

fun inverse(m: Matrix3): Matrix3 {
    val a = m.x.x
    val b = m.x.y
    val c = m.x.z
    val d = m.y.x
    val e = m.y.y
    val f = m.y.z
    val g = m.z.x
    val h = m.z.y
    val i = m.z.z

    val A = e * i - f * h
    val B = f * g - d * i
    val C = d * h - e * g

    val det = a * A + b * B + c * C

    return Matrix3.of(
        A / det, B / det, C / det,
        (c * h - b * i) / det, (a * i - c * g) / det, (b * g - a * h) / det,
        (b * f - c * e) / det, (c * d - a * f) / det, (a * e - b * d) / det
    )
}


/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)
