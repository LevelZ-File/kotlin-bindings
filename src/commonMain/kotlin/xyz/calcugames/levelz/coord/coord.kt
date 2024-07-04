package xyz.calcugames.levelz.coord

import xyz.calcugames.levelz.Dimension
import kotlin.math.sqrt

/**
 * Represents a Game Coordinate.
 */
interface Coordinate : Comparable<Coordinate> {
    /**
     * The magnitude of the Coordinate.
     */
    val magnitude: Double

    /**
     * The dimension of the Coordinate.
     */
    val dimension: Dimension

    override fun toString(): String

    override fun hashCode(): Int

    override fun compareTo(other: Coordinate): Int {
        return magnitude.compareTo(other.magnitude)
    }

    companion object {
        /**
         * Creates a Coordinate from an array of integers.
         * @param coords Coordinate Array
         * @return A [2D][Coordinate2D] or [3D][Coordinate3D] Coordinate, depending on array size
         */
        fun fromArray(coords: IntArray): Coordinate {
            if (coords.size == 2) return Coordinate2D(coords[0], coords[1])
            if (coords.size == 3) return Coordinate3D(coords[0], coords[1], coords[2])

            throw IllegalArgumentException("Invalid Coordinate Length")
        }
    }
}

/**
 * Represents a 2-Dimensional Coordinate.
 */
class Coordinate2D(
    /**
     * The X Value of the Coordinate.
     */
    var x: Double,
    /**
     * The Y Value of the Coordinate.
     * @return Y Value
     */
    var y: Double
) : Coordinate {

    /**
     * Constructs a 2D Coordinate.
     * @param x X Value
     * @param y Y Value
     */
    constructor(x: Int, y: Int) : this(x.toDouble(), y.toDouble())

    /**
     * Constructs a 2D Coordinate.
     * @param xy Array of X and Y Values
     */
    constructor(xy: IntArray) : this(xy[0], xy[1])

    /**
     * Constructs a 2D Coordinate.
     * @param xy Array of X and Y Values
     */
    constructor(xy: DoubleArray) : this(xy[0], xy[1])

    override val magnitude: Double
        get() = sqrt((x * x) + (y * y))

    override val dimension: Dimension
        get() = Dimension.TWO

    operator fun component1(): Double = x
    operator fun component2(): Double = y

    operator fun plus(other: Coordinate2D): Coordinate2D = Coordinate2D(x + other.x, y + other.y)
    operator fun plus(scalar: Double): Coordinate2D = Coordinate2D(x + scalar, y + scalar)
    operator fun plusAssign(other: Coordinate2D) {
        x += other.x
        y += other.y
    }

    operator fun minus(other: Coordinate2D): Coordinate2D = Coordinate2D(x - other.x, y - other.y)
    operator fun minus(scalar: Double): Coordinate2D = Coordinate2D(x - scalar, y - scalar)
    operator fun minusAssign(other: Coordinate2D) {
        x -= other.x
        y -= other.y
    }

    operator fun times(other: Coordinate2D): Coordinate2D = Coordinate2D(x * other.x, y * other.y)
    operator fun times(scalar: Double): Coordinate2D = Coordinate2D(x * scalar, y * scalar)
    operator fun timesAssign(other: Coordinate2D) {
        x *= other.x
        y *= other.y
    }

    operator fun div(other: Coordinate2D): Coordinate2D = Coordinate2D(x / other.x, y / other.y)
    operator fun div(scalar: Double): Coordinate2D = Coordinate2D(x / scalar, y / scalar)
    operator fun divAssign(other: Coordinate2D) {
        x /= other.x
        y /= other.y
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (other !is Coordinate2D) return false

        return x == other.x && y == other.y
    }

    override fun hashCode(): Int {
        return (31 * x.hashCode()) + y.hashCode()
    }

    override fun toString(): String {
        val xInt = x.toInt(); val yInt = y.toInt()
        val xs = if (x == xInt.toDouble()) xInt.toString() else x.toString()
        val ys = if (y == yInt.toDouble()) yInt.toString() else y.toString()

        return "[$xs, $ys]"
    }

    companion object {
        /**
         * A 2D Coordinate at the origin.
         */
        val zero = Coordinate2D(0, 0)

        /**
         * Parses a 2D Coordinate from a string.
         * @param point String
         * @return 2D Coordinate
         */
        fun fromString(point: String): Coordinate2D {
            val point0 = point.trim { it <= ' ' }.replace("[\\[\\]\\s]".toRegex(), "")
            val parts = point0.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            val x = parts[0].toDouble(); val y = parts[1].toDouble()
            return Coordinate2D(x, y)
        }
    }
}

/**
 * Represents a 3-Dimensional Coordinate.
 */
class Coordinate3D(
    /**
     * The X Value of the Coordinate.
     */
    var x: Double,
    /**
     * The Y Value of the Coordinate.
     * @return Y Value
     */
    var y: Double,
    /**
     * The Z Value of the Coordinate.
     * @return Z Value
     */
    var z: Double
) : Coordinate {

    /**
     * Constructs a 3D Coordinate.
     * @param x X Value
     * @param y Y Value
     * @param z Z Value
     */
    constructor(x: Int, y: Int, z: Int) : this(x.toDouble(), y.toDouble(), z.toDouble())

    /**
     * Constructs a 3D Coordinate.
     * @param xyz Array of X, Y, and Z Values
     */
    constructor(xyz: IntArray) : this(xyz[0], xyz[1], xyz[2])

    /**
     * Constructs a 3D Coordinate.
     * @param xyz Array of X, Y, and Z Values
     */
    constructor(xyz: DoubleArray) : this(xyz[0], xyz[1], xyz[2])

    override val magnitude: Double
        get() = sqrt((x * x) + (y * y) + (z * z))

    override val dimension: Dimension
        get() = Dimension.THREE

    operator fun component1(): Double = x
    operator fun component2(): Double = y
    operator fun component3(): Double = z

    operator fun plus(other: Coordinate3D): Coordinate3D = Coordinate3D(x + other.x, y + other.y, z + other.z)
    operator fun plus(scalar: Double): Coordinate3D = Coordinate3D(x + scalar, y + scalar, z + scalar)
    operator fun plusAssign(other: Coordinate3D) {
        x += other.x
        y += other.y
        z += other.z
    }

    operator fun minus(other: Coordinate3D): Coordinate3D = Coordinate3D(x - other.x, y - other.y, z - other.z)
    operator fun minus(scalar: Double): Coordinate3D = Coordinate3D(x - scalar, y - scalar, z - scalar)
    operator fun minusAssign(other: Coordinate3D) {
        x -= other.x
        y -= other.y
        z -= other.z
    }

    operator fun times(other: Coordinate3D): Coordinate3D = Coordinate3D(x * other.x, y * other.y, z * other.z)
    operator fun times(scalar: Double): Coordinate3D = Coordinate3D(x * scalar, y * scalar, z * scalar)
    operator fun timesAssign(other: Coordinate3D) {
        x *= other.x
        y *= other.y
        z *= other.z
    }

    operator fun div(other: Coordinate3D): Coordinate3D = Coordinate3D(x / other.x, y / other.y, z / other.z)
    operator fun div(scalar: Double): Coordinate3D = Coordinate3D(x / scalar, y / scalar, z / scalar)
    operator fun divAssign(other: Coordinate3D) {
        x /= other.x
        y /= other.y
        z /= other.z
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (other !is Coordinate3D) return false

        return x == other.x && y == other.y && z == other.z
    }

    override fun hashCode(): Int {
        return (31 * x.hashCode()) + (31 * y.hashCode()) + z.hashCode()
    }

    override fun toString(): String {
        val xInt = x.toInt(); val yInt = y.toInt(); val zInt = z.toInt()
        val xs = if (x == xInt.toDouble()) xInt.toString() else x.toString()
        val ys = if (y == yInt.toDouble()) yInt.toString() else y.toString()
        val zs = if (z == zInt.toDouble()) zInt.toString() else z.toString()

        return "[$xs, $ys, $zs]"
    }

    companion object {
        /**
         * A 3D Coordinate at the origin.
         */
        val zero = Coordinate3D(0, 0, 0)

        /**
         * Parses a 3D Coordinate from a string.
         * @param point String
         * @return 3D Coordinate
         */
        fun fromString(point: String): Coordinate3D {
            val point0 = point.trim { it <= ' ' }.replace("[\\[\\]\\s]".toRegex(), "")
            val parts = point0.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            val x = parts[0].toDouble(); val y = parts[1].toDouble(); val z = parts[2].toDouble()
            return Coordinate3D(x, y, z)
        }
    }
}