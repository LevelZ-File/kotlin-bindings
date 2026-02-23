package xyz.calcugames.levelz.parser

import xyz.calcugames.levelz.Block
import xyz.calcugames.levelz.Dimension
import xyz.calcugames.levelz.Level
import xyz.calcugames.levelz.Level2D
import xyz.calcugames.levelz.Level3D
import xyz.calcugames.levelz.LevelObject
import xyz.calcugames.levelz.coord.Coordinate2D
import xyz.calcugames.levelz.coord.Coordinate3D
import xyz.calcugames.levelz.coord.CoordinateMatrix2D
import xyz.calcugames.levelz.coord.CoordinateMatrix3D
import kotlin.collections.iterator
import kotlin.random.Random

// Exceptions

/**
 * Thrown to indicate errors in parsing a LevelZ file.
 */
open class ParseException : RuntimeException {
    /**
     * Creates a new Parse Exception.
     * @param message Message
     */
    constructor(message: String?) : super(message)

    /**
     * Creates a new Parse Exception.
     * @param message Message
     * @param cause Cause
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause)

    companion object {
        private const val serialVersionUID = 3594456258251637068L
    }
}

/**
 * Thrown to indicate a LevelZ Header is missing.
 */
open class MissingHeaderException : ParseException {
    /**
     * Creates a new Missing Header Exception.
     * @param message Message
     */
    constructor(message: String?) : super(message)

    /**
     * Creates a new Missing Header Exception.
     * @param message Message
     * @param cause Cause
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause)

    companion object {
        private const val serialVersionUID = 3594456258251637068L
    }
}


/**
 * Thrown when a point is malformed.
 */
open class MalformedPointException : ParseException {
    /**
     * Creates a new Malformed Point Exception.
     * @param message Message
     */
    constructor(message: String?) : super(message)

    /**
     * Creates a new Malformed Point Exception.
     * @param message Message
     * @param cause Cause
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause)

    companion object {
        private const val serialVersionUID = 3594456258251637068L
    }
}

// Internal Functions

internal fun value(str: String): Any {
    if (str.equals("true", ignoreCase = true)) return true
    if (str.equals("false", ignoreCase = true)) return false

    return try {
        str.toInt()
    } catch (e: NumberFormatException) {
        try {
            str.toDouble()
        } catch (e1: NumberFormatException) {
            str
        }
    }
}

internal fun <T> roll(map: Map<T, Double>, seed: Random): T? {
    val sum = map.values.sum()
    if (sum > 1.0) throw ParseException("LevelZ Block Probabilities exceeded 1.0, found $sum")

    var t: T? = null

    val r: Double = seed.nextDouble()
    var cumulative = 0.0
    for ((key, value) in map) {
        cumulative += value
        if (r <= cumulative) {
            t = key
            break
        }
    }

    return t
}


// Parsing
internal fun split(file: Array<String>): Array<Array<String>> {
    var index = 0
    for (i in file.indices) {
        if (file[i] == HEADER_END) {
            index = i
            break
        }
    }

    val header = arrayOfNulls<String>(index)
    val body = arrayOfNulls<String>(file.size - index - 1)

    file.copyInto(header, endIndex = index)
    file.copyInto(body, startIndex = index + 1)

    return arrayOf(
        header.filterNotNull().toTypedArray(),
        body.filterNotNull().toTypedArray()
    )
}

internal fun readHeaders(headers: Array<String>): MutableMap<String, String> {
    val headers0 = mutableMapOf<String, String>()
    for (l in headers) {
        if (l.isBlank()) continue
        if (!l.startsWith("@")) throw ParseException("Invalid LevelZ Header: $l")
        val parts = l.split("\\s".toRegex(), limit = 2).toTypedArray()

        if (parts.size < 2) throw ParseException("Invalid LevelZ Header: $l")

        headers0[parts[0].substring(1)] = parts[1]
    }

    if (!headers0.containsKey("type")) throw MissingHeaderException("Missing Dimension Type (@type)")
    if (!headers0.containsKey("spawn")) throw MissingHeaderException("Missing Level Spawnpoint (@spawn)")

    return headers0
}

internal fun read2DPoints(input: String): Set<Coordinate2D> {
    val points = mutableSetOf<Coordinate2D>()
    val inputs = input.split("\\*".toRegex()).dropLastWhile { it.isEmpty() }

    for (s0 in inputs) {
        val s = s0.trim()
        if (s.isEmpty()) continue

        if (s.startsWith("(") && s.endsWith("]"))
            points.addAll(CoordinateMatrix2D.fromString(s).coordinates)
        else
            points.add(Coordinate2D.fromString(s))
    }

    return points
}

internal fun read3DPoints(input: String): Set<Coordinate3D> {
    val points = mutableSetOf<Coordinate3D>()
    val inputs = input.split("\\*".toRegex()).dropLastWhile { it.isEmpty() }

    for (s0 in inputs) {
        val s = s0.trim()
        if (s.isEmpty()) continue

        if (s.startsWith("(") && s.endsWith("]"))
            points.addAll(CoordinateMatrix3D.fromString(s).coordinates)
        else
            points.add(Coordinate3D.fromString(s))
    }

    return points
}

internal fun read2DLine(line: String, seed: Random): Pair<Block, Set<Coordinate2D>> {
    val split = line.replace("\\s".toRegex(), "").split(":".toRegex()).dropLastWhile { it.isEmpty() }
    return readBlock(split[0], seed) to read2DPoints(split[1])
}

internal fun read3DLine(line: String, seed: Random): Pair<Block, Set<Coordinate3D>> {
    val split = line.replace("\\s".toRegex(), "").split(":".toRegex()).dropLastWhile { it.isEmpty() }
    return readBlock(split[0], seed) to read3DPoints(split[1])
}

internal fun readBlock(blockLine: String, seed: Random): Block {
    val block: Block
    if (blockLine.startsWith("{") && blockLine.endsWith("}")) {
        val block0 = blockLine.replace("[{}]".toRegex(), "")
        val blocks: Array<String>

        if (blockLine.contains(">,")) {
            blocks = block0.split(">,".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (i in blocks.indices) if (blocks[i].contains("<")) blocks[i] = blocks[i] + '>'
        } else blocks = block0.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        val l = blocks.size

        val blockToChance = mutableMapOf<String, Double>()
        for (b in blocks) {
            val split0 = b.split("=".toRegex(), limit = 2).toTypedArray()

            if (split0.size == 1)
                blockToChance[b] = 1.0 / l
            else {
                try {
                    blockToChance[split0[1]] = split0[0].toDouble()
                } catch (e: NumberFormatException) {
                    blockToChance[b] = 1.0 / l
                }
            }
        }

        block = Block.fromString(roll(blockToChance, seed)!!)
    } else
        block = Block.fromString(blockLine)

    return block
}

internal fun parse(file: Array<String>, seed: Random): Level {
    val split = split(file)
    val headers = readHeaders(split[0])
    val dimension = Dimension.entries[headers["type"]!!.toInt() - 2]
    val is2D = dimension.is2D

    headers["spawn"] = if (headers["spawn"] == "default") dimension.defaultCoordinate.toString() else headers["spawn"]!!
    if (is2D) headers.getOrPut("scroll") { "none" }

    val blocks = mutableSetOf<LevelObject>()
    for (line in split[1]) {
        if (line.isEmpty()) continue
        if (line.equals(END, ignoreCase = true)) break

        val ci = line.indexOf('#')
        val line0 = (if (ci != -1) line.substring(0, ci) else line).trim()

        if (is2D) {
            val blocks2D = read2DLine(line0, seed)
            for (c in blocks2D.second)
                blocks.add(LevelObject(blocks2D.first, c))
        } else {
            val blocks3D = read3DLine(line0, seed)
            for (c in blocks3D.second)
                blocks.add(LevelObject(blocks3D.first, c))
        }
    }

    return if (is2D) Level2D(headers, blocks) else Level3D(headers, blocks)
}

// Parser API

/**
 * Marks the end of the header section.
 */
const val HEADER_END = "---"

/**
 * Marks the end of the file
 */
const val END = "end"

/**
 * Parses a LevelZ file from the given string.
 * @param level Level String
 * @param seed Random Seed
 * @param lineSeparator Line Separator
 * @return Parsed Level
 * @throws ParseException If the LevelZ file is malformed
 */
fun parseLevel(level: String, seed: Random = Random, lineSeparator: String = "\n"): Level {
    return parse(level.split(lineSeparator).toTypedArray(), seed)
}

/**
 * Parses a LevelZ file from the given string.
 * @param level Level Lines
 * @param seed Random Seed
 * @return Parsed Level
 * @throws ParseException If the LevelZ file is malformed
 */
fun parseLevel(level: Iterable<String>, seed: Random = Random): Level {
    return parse(level.toList().toTypedArray(), seed)
}