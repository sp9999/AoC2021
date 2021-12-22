fun main() {

    fun convertHexToBinary(input: String): String {
        var i = 0
        var binaryNum = ""
        while(i < input.length) {
            when(input[i]) {
                '0'  -> binaryNum += "0000"
                '1'  -> binaryNum += "0001"
                '2'  -> binaryNum += "0010"
                '3'  -> binaryNum += "0011"
                '4'  -> binaryNum += "0100"
                '5'  -> binaryNum += "0101"
                '6'  -> binaryNum += "0110"
                '7'  -> binaryNum += "0111"
                '8'  -> binaryNum += "1000"
                '9'  -> binaryNum += "1001"
                'A', 'a'  -> binaryNum += "1010"
                'B', 'b'  -> binaryNum += "1011"
                'C', 'c'  -> binaryNum += "1100"
                'D', 'd'  -> binaryNum += "1101"
                'E', 'e'  -> binaryNum += "1110"
                'F', 'f'  -> binaryNum += "1111"
            }
            i++
        }
        return binaryNum
    }


    fun typeFourLiteral(binaryString: String): Pair<Long, Int> {
        var binarySub = binaryString.substring(6)

        var finalString = ""
        var iterations = 0
        while (binarySub[0] != '0') {
            finalString += binarySub.substring(1, 5)
            binarySub = binarySub.substring(5)
            iterations += 1
        }
        finalString += binarySub.substring(1, 5)
        iterations += 1

        var literalValue = finalString.toLong(2)

        var nextStep = 6 + iterations*5

        return Pair(literalValue, nextStep)
    }

    data class Packet(val input: String) {
        val version: Int = getPacketVersion()
        val type: Int = getPacketType()
        var literal: Long = -1
        var isOperator = type != 4
        var lengthId = 0
        var subPackets: MutableList<Packet> = mutableListOf()
        var stringLength = 0

        init {
            if (isOperator) {
                lengthId = getPacketLengthId()
                if (lengthId == 0) {
                    var subpacket = getSubpacketStringByLength()
                    while(subpacket.isNotEmpty()) {
                        var newP = Packet(subpacket)
                        subPackets.add(newP)
                        subpacket = subpacket.substring(newP.stringLength)
                    }
                } else {
                    var subpacketCount = getSubpacketLengthInfo()
                    var subpacket = getSubpacketString()
                    while(subpacketCount > 0) {
                        var newP = Packet(subpacket)
                        subPackets.add(newP)
                        subpacket = subpacket.substring(newP.stringLength)
                        subpacketCount -= 1
                    }
                }
                var sum = 0
                subPackets.forEach { sum += it.stringLength }
                stringLength = 6 + 1 + getBitLength() + sum
                literal = calcLiteral()
                //var subpacketList = getSubPacketValues(updatingString, subpacketLength)
            }
            else {
                var (literalvalue, nextStep) = typeFourLiteral(input)
                literal = literalvalue
                stringLength = nextStep
            }

        }


        fun getPacketVersion() : Int {
            var versionBinary = input.substring(0, 3)
            return versionBinary.toInt(2)
        }

        fun getPacketType() : Int {
            var str = input.substring(3, 6)
            return str.toInt(2)
        }

        fun getPacketLengthId() : Int {
            var str = input.substring(6, 7)
            return str.toInt(2)
        }

        fun getSubpacketLengthInfo() : Int {
            var str = input.substring(7, 7 + getBitLength())
            return str.toInt(2)
        }
        fun getBitLength() : Int {
            return if (lengthId == 0) 15 else 11
        }

        fun getSubpacketStringByLength(): String {
            var start = 7 + getBitLength()
            return input.substring(start, start + getSubpacketLengthInfo())
        }

        fun getSubpacketString(): String {
            var start = 7 + getBitLength()
            return input.substring(start)
        }

        fun sumVersions(): Int {
            var sum = version
            subPackets.forEach{
                sum += it.sumVersions()
            }
            return sum
        }

        fun calcLiteral(): Long {
            if (literal != -1L) return literal

            var result = if (type == 1) 1L else 0L
            when (type) {
                0 -> subPackets.forEach { result += it.calcLiteral() }
                1 -> subPackets.forEach { result *= it.calcLiteral() }
                2 -> result = subPackets.minByOrNull { it.calcLiteral() }!!.literal
                3 -> result = subPackets.maxByOrNull { it.calcLiteral() }!!.literal
                4 -> result = literal
                5 -> result = if (subPackets[0].calcLiteral() > subPackets[1].calcLiteral()) 1 else 0
                6 -> result = if (subPackets[0].calcLiteral() < subPackets[1].calcLiteral()) 1 else 0
                7 -> result = if (subPackets[0].calcLiteral() == subPackets[1].calcLiteral()) 1 else 0
            }

            literal = result
            return result
        }

    }

    fun part1(input: List<String>): Int {
        var binaryString = convertHexToBinary(input[0])
        var p = Packet(binaryString)
        return p.sumVersions()
    }

    fun part2(input: List<String>): Long {
        var binaryString = convertHexToBinary(input[0])
        var p = Packet(binaryString)
        return p.calcLiteral()
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
