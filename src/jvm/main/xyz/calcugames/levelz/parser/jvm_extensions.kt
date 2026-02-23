package xyz.calcugames.levelz.parser

import xyz.calcugames.levelz.Level
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.Reader
import java.util.stream.Collectors
import kotlin.random.Random

internal fun lines(r: Reader): List<String> {
    val reader = BufferedReader(r)
    val lines = reader.lines().collect(Collectors.toList())

    try {
        reader.close()
    } catch (e: IOException) {
        throw ParseException("Failed to Close Reader", e)
    }

    return lines
}

/**
 * Parses a Level from a reader.
 * @param reader Reader to use
 * @param seed Random Seed
 */
fun parseLevel(reader: Reader, seed: Random = Random): Level {
    val lines = lines(reader)
    return parse(lines.toTypedArray(), seed)
}

/**
 * Parses a Level from an input stream.
 * @param stream Input Stream to use
 * @param seed Random Seed
 */
fun parseLevel(stream: InputStream, seed: Random = Random): Level {
    val lines = lines(stream.reader())
    return parse(lines.toTypedArray(), seed)
}