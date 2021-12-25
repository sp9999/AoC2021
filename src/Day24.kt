import java.util.ArrayDeque

fun main() {

    data class Variables(val a: Int, val b: Int, val c: Int) {
        override fun toString() : String {
            return "($a, $b, $c)"
        }
    }
    val argsList = listOf(
        Variables(1,13,5),
        Variables(1,15,14),
        Variables(1, 15, 15),
        Variables(1, 11, 16),
        Variables(26, -16, 8),
        Variables(26, -11, 9),
        Variables(26,-6,2),
        Variables(1,11,13),
        Variables(1,10,16),
        Variables(26, -10, 6),
        Variables(26, -8, 6),
        Variables(26, -11, 9),
        Variables(1, 12, 11),
        Variables(26, -15, 5),
    )

    var equationStack = ArrayDeque<Pair<Int, Int>>()
    var maxArray = CharArray(14)
    var minArray = CharArray(14)
    for (i in 0 .. 13) {
        if (argsList[i].a == 1) {
            equationStack.push(Pair(i, argsList[i].c))
        } else {
            var lastEquation = equationStack.pop()
            var c = lastEquation.second + argsList[i].b

            maxArray[lastEquation.first] = if (c >= 0) '9' - c else '9'
            maxArray[i] = maxArray[lastEquation.first] + c
            minArray[lastEquation.first] = if (c >= 0) '1' else  '1' + Math.abs(c)
            minArray[i] = minArray[lastEquation.first] + c
        }
    }

    println("MAX: ${String(maxArray)}")
    println("MAX: ${String(minArray)}")

    /*fun checkDigit(input: Int, args: Variables, regZ: Int, printNoPop: Int = 0): Int {
        var x  = if (regZ % 26 + args.b != input) 1 else 0
        var z = (regZ * ((25 * x) + 1))/args.a + (input + args.c) * x // a either 1 or 26, x 1 or 0
        if (printNoPop != 0) {
            if (args.a == 26) {
                if (x == 1)
                    println("Index: ${printNoPop} args: ${args})")
            }
        }
        return z
    }

    fun solve(): List<String> {
        var zRange = setOf(0)
        var idx = argsList.size - 1

        val constrained = Array<MutableMap<Int, MutableSet<Int>>>(14) { mutableMapOf() }

        argsList.reversed().forEach { args ->
            val validZ = mutableSetOf<Int>()
            for (input in 1 .. 9) {
                for (z in 0 .. 500_000) {
                    if (checkDigit(input, args, z) in zRange) {
                        val set = constrained[idx].getOrPut(input) { mutableSetOf() }
                        set.add(z)

                        validZ.add(z)
                    }
                }
            }
            if (validZ.isEmpty()) {
                println("No valid z for input input[$idx]?")
            }
            idx--
            zRange = validZ
        }

        fun findSerial(index: Int, z: Int): List<String> {
            if (index == 14) return listOf("")

            val opts = constrained[index].entries.filter { z in it.value }
            return opts.flatMap { (digit, _) ->
                val newZ = checkDigit(digit, argsList[index], z)

                findSerial(index + 1, newZ).map {
                    digit.toString() + it
                }
            }
        }

        return findSerial(0,0)
    }*/
}
