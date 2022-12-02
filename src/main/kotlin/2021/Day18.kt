import util.ConsoleColor
import util.InputUtil
import util.TimeUtil
import util.colorize
import kotlin.math.ceil
import kotlin.math.floor

private fun main() {
    //pt 1 - 3675
    //pt 2 - 4650
    TimeUtil.startClock(1, ::partOne)
    TimeUtil.startClock(2, ::partTwo)
}

val simplePairRegex = "\\[(-?\\d+),(-?\\d+)]".toRegex()

private class SnailPair(
    var leftChild: SnailPair? = null,
    var rightChild: SnailPair? = null,
    var parent: SnailPair? = null,
    var value: Int? = null
) {
    init {
        leftChild?.parent = this;
        rightChild?.parent = this;
    }

    companion object {
        fun buildFromString(str: String): SnailPair {
            var line = str;
            val snailNodesMap = mutableMapOf<String, SnailPair>()
            var currentPair: SnailPair;
            var pairId = 0;
            do {
                --pairId //these negative numbers represent a node we have already parsed and stored in the map
                //find the first pair that is a basic number E.G. [1,-2]
                val match = simplePairRegex.find(line)!!
                val firstVal = match.groupValues[1]
                val secondVal = match.groupValues[2]
                //Make a new node for that number, unless we have that number stored in our map (negative numbers only)
                val firstNode = snailNodesMap.getOrElse(firstVal) { SnailPair(value = firstVal.toInt()) }
                val secondNode = snailNodesMap.getOrElse(secondVal) { SnailPair(value = secondVal.toInt()) }
                //make a new pair using the 2 child nodes
                currentPair = SnailPair(firstNode, secondNode)
                //place that pair into our map, associating it with a counter using a negative number to id it
                snailNodesMap[pairId.toString()] = currentPair
                //before looping, replace the pair in the string with the negative number id
                line = simplePairRegex.replaceFirst(line, pairId.toString())
            } while (line.contains('['))
            return currentPair
        }
    }

    operator fun plus(other: SnailPair): SnailPair {
        val newParent = SnailPair(this.deepCopy(), other.deepCopy());
        newParent.reduce()
        return newParent;
    }

    fun deepCopy(): SnailPair {
        return buildFromString(this.toString())
    }

    fun reduce() {
        while (true) {
            val seq = getListOfEndValueNodes().asSequence()
            if (seq.any { it.checkExplosion() })
                continue;
            else if (seq.any { it.checkSplit() })
                continue
            else break;
        }
    }

    fun getListOfEndValueNodes(): MutableList<SnailPair> {
        if (this.value != null) return mutableListOf(this)
        val list = leftChild!!.getListOfEndValueNodes();
        list.addAll(rightChild!!.getListOfEndValueNodes());
        return list;
    }

    fun checkExplosion(): Boolean {
        //If ANY pair is nested inside four pairs, the leftmost such pair explodes.
        if (this.parent?.parent?.parent?.parent?.parent != null) {
            this.parent!!.explode();
            return true
        }
        return false
    }

    fun checkSplit(): Boolean {
        //If any regular number is 10 or greater, the leftmost such regular number splits.
        if (this.value != null && this.value!! >= 10) {
            this.split();
            return true
        }
        return false
    }

    fun explode() {
        //the pair's left value is added to the first regular number to the LEFT of the exploding pair (if any)
        val left = getNodeToTheLeft()?.getRightMostChild();
        // and the pair's right value is added to the first regular number to the RIGHT of the exploding pair (if any)
        val right = getNodeToTheRight()?.getLeftMostChild();
        left?.value = left!!.value!! + this.leftChild!!.value!!
        right?.value = right!!.value!! + this.rightChild!!.value!!

        this.leftChild = null;
        this.rightChild = null;
        this.value = 0;
    }

    fun split() {
        //To split a regular number, replace it with a pair;
        // the left element of the pair should be the regular number divided by two and rounded down,
        val leftVal = floor(value!! / 2.0).toInt()
        // while the right element of the pair should be the regular number divided by two and rounded up.
        val rightVal = ceil(value!! / 2.0).toInt()
        leftChild = SnailPair(value = leftVal, parent = this)
        rightChild = SnailPair(value = rightVal, parent = this)
        this.value = null;
        // For example, 10 becomes [5,5], 11 becomes [5,6], 12 becomes [6,6], and so on.
    }

    fun getNodeToTheRight(): SnailPair? {
        return if (parent == null || parent?.rightChild == null) null
        else if (parent!!.rightChild != this) parent!!.rightChild!!
        else parent!!.getNodeToTheRight()
    }

    fun getNodeToTheLeft(): SnailPair? {
        return if (parent == null || parent?.leftChild == null) null
        else if (parent!!.leftChild != this) parent!!.leftChild!!
        else parent!!.getNodeToTheLeft()
    }

    fun getLeftMostChild(): SnailPair {
        return if (value != null) this
        else if (leftChild!!.value != null) leftChild!!
        else leftChild!!.getLeftMostChild()
    }

    fun getRightMostChild(): SnailPair {
        return if (value != null) this
        else if (rightChild!!.value != null) rightChild!!
        else rightChild!!.getRightMostChild()
    }

    override fun toString() = if (this.value != null) this.value.toString() else "[$leftChild,$rightChild]"

    fun calcMagnitude(): Long {
        //The magnitude of a regular number is just that number
        if (value != null) return value!!.toLong()

        //The magnitude of a pair is 3 times the magnitude of its left element
        // plus 2 times the magnitude of its right
        return (3 * leftChild!!.calcMagnitude()) + (2 * rightChild!!.calcMagnitude())
    }
}

private fun partOne(pt: Int = 1) {
    val input = InputUtil.readFileAsStringList("2021/day18/input.txt")
    val pairList = input.map { SnailPair.buildFromString(it) }

    val finalNumber = pairList.reduce { a, b -> a + b }
    val answer = finalNumber.calcMagnitude()
    println("pt $pt answer: ${answer colorize ConsoleColor.CYAN_BOLD}")
}

private fun partTwo(pt: Int = 2) {
    var max = Long.MIN_VALUE;
    val input = InputUtil.readFileAsStringList("2021/day18/input.txt")
    val pairList = input.map { SnailPair.buildFromString(it) }
    val permutationSet = mutableSetOf<Pair<SnailPair, SnailPair>>()

    pairList.forEach { node ->
        pairList.filterNot { otherNode -> otherNode == node }.forEach { otherNode ->
            permutationSet.add(node to otherNode)
            permutationSet.add(otherNode to node)
        }
    }

    permutationSet.forEach {
        //Because SnailPair.plus() deep copies, the original objects remain intact,
        // and we don't have to worry about re-using them
        max = maxOf(max, (it.first + it.second).calcMagnitude())
    }

    val answer = max;
    println("pt $pt answer: ${answer colorize ConsoleColor.CYAN_BOLD}")
}
