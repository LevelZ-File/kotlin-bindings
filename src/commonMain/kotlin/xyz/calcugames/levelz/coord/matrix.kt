package xyz.calcugames.levelz.coord

import xyz.calcugames.levelz.Dimension
import kotlin.math.max
import kotlin.math.min

/**
 * Represents a matrix of coordinates.
 */
abstract class CoordinateMatrix : Iterable<Coordinate> {

    /**
     * The dimension of the matrix.
     */
    abstract val dimension: Dimension

    /**
     * Gets all the coordinates in this matrix.
     */
    abstract val coordinates: Set<Coordinate>

    /**
     * Gets the starting coordinate of this matrix.
     */
    abstract val start: Coordinate

    final override fun iterator(): Iterator<Coordinate> = coordinates.iterator()

    /**
     * Gets the string version of this coordinate matrix.
     */
    abstract override fun toString(): String

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (other !is CoordinateMatrix) return false

        if (dimension != other.dimension) return false
        if (coordinates != other.coordinates) return false
        if (start != other.start) return false

        return true
    }

    override fun hashCode(): Int {
        var result = dimension.hashCode()
        result = 31 * result + coordinates.hashCode()
        result = 31 * result + start.hashCode()
        return result
    }
}

/**
 * Represents a 2D matrix of coordinates.
 */
class CoordinateMatrix2D(
    /**
     * The minimum x value of the matrix.
     */
    val minX: Int = 0,
    /**
     * The maximum x value of the matrix.
     */
    val maxX: Int,
    /**
     * The minimum y value of the matrix.
     */
    val minY: Int = 0,
    /**
     * The maximum y value of the matrix.
     */
    val maxY: Int,
    /**
     * The starting coordinate of the matrix.
     */
    override val start: Coordinate2D,
) : CoordinateMatrix() {

    init {
        require(minX <= maxX) { "minX must be less than or equal to maxX" }
        require(minY <= maxY) { "minY must be less than or equal to maxY" }
    }

    override val dimension: Dimension = Dimension.TWO

    override val coordinates: Set<Coordinate2D>
        get() {
            val set = mutableSetOf<Coordinate2D>()

            for (x in minX..maxX)
                for (y in minY..maxY)
                    set.add(Coordinate2D(x, y))

            return set
        }

    override fun toString(): String = "($minX, $maxX, $minY, $maxY)^$start"

    operator fun component1(): Int = minX
    operator fun component2(): Int = maxX
    operator fun component3(): Int = minY
    operator fun component4(): Int = maxY

    companion object {

        /**
         * Creates a 2D Coordinate Matrix from a string.
         * @param string The string to create the matrix from.
         * @return The created CoordinateMatrix.
         */
        fun fromString(string: String): CoordinateMatrix2D {
            val split = string.split("\\^".toRegex()).dropLastWhile { it.isEmpty() }

            val coords = split[1].replace("[\\[\\]\\s]".toRegex(), "").split(",".toRegex()).dropLastWhile { it.isEmpty() }
            val matrix = split[0].replace("[()\\s]".toRegex(), "").split(",".toRegex()).dropLastWhile { it.isEmpty() }

            val cx = coords[0].toDouble(); val cy = coords[1].toDouble()

            val x1 = matrix[0].toInt(); val x2 = matrix[1].toInt()
            val y1 = matrix[2].toInt(); val y2 = matrix[3].toInt()

            val minX = min(x1, x2)
            val maxX = max(x1, x2)
            val minY = min(y1, y2)
            val maxY = max(y1, y2)

            return CoordinateMatrix2D(minX, maxX, minY, maxY, Coordinate2D(cx, cy))
        }

    }
}

/**
 * Represents a 3D matrix of coordinates.
 */
class CoordinateMatrix3D(
    /**
     * The minimum x value of the matrix.
     */
    val minX: Int = 0,
    /**
     * The maximum x value of the matrix.
     */
    val maxX: Int,
    /**
     * The minimum y value of the matrix.
     */
    val minY: Int = 0,
    /**
     * The maximum y value of the matrix.
     */
    val maxY: Int,
    /**
     * The minimum z value of the matrix.
     */
    val minZ: Int = 0,
    /**
     * The maximum z value of the matrix.
     */
    val maxZ: Int,
    /**
     * The starting coordinate of the matrix.
     */
    override val start: Coordinate3D,
) : CoordinateMatrix() {

    init {
        require(minX <= maxX) { "minX must be less than or equal to maxX" }
        require(minY <= maxY) { "minY must be less than or equal to maxY" }
        require(minZ <= maxZ) { "minZ must be less than or equal to maxZ" }
    }

    override val dimension: Dimension = Dimension.THREE

    override val coordinates: Set<Coordinate3D>
        get() {
            val set = mutableSetOf<Coordinate3D>()

            for (x in minX..maxX)
                for (y in minY..maxY)
                    for (z in minZ..maxZ)
                        set.add(Coordinate3D(x, y, z))

            return set
        }

    override fun toString(): String = "($minX, $maxX, $minY, $maxY, $minZ, $maxZ)^$start"

    operator fun component1(): Int = minX
    operator fun component2(): Int = maxX
    operator fun component3(): Int = minY
    operator fun component4(): Int = maxY
    operator fun component5(): Int = minZ
    operator fun component6(): Int = maxZ

    companion object {

        /**
         * Creates a 3D Coordinate Matrix from a string.
         * @param string The string to create the matrix from.
         * @return The created CoordinateMatrix.
         */
        fun fromString(string: String): CoordinateMatrix3D {
            val split = string.split("\\^".toRegex()).dropLastWhile { it.isEmpty() }

            val coords = split[1].replace("[\\[\\]\\s]".toRegex(), "").split(",".toRegex()).dropLastWhile { it.isEmpty() }
            val matrix = split[0].replace("[()\\s]".toRegex(), "").split(",".toRegex()).dropLastWhile { it.isEmpty() }

            val cx = coords[0].toDouble(); val cy = coords[1].toDouble(); val cz = coords[2].toDouble()

            val x1 = matrix[0].toInt(); val x2 = matrix[1].toInt()
            val y1 = matrix[2].toInt(); val y2 = matrix[3].toInt()
            val z1 = matrix[4].toInt(); val z2 = matrix[5].toInt()

            val minX = min(x1, x2); val maxX = max(x1, x2)
            val minY = min(y1, y2); val maxY = max(y1, y2)
            val minZ = min(z1, z2); val maxZ = max(z1, z2)

            return CoordinateMatrix3D(minX, maxX, minY, maxY, minZ, maxZ, Coordinate3D(cx, cy, cz))
        }

    }
}