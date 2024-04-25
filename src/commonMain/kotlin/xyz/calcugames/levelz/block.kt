package xyz.calcugames.levelz

import xyz.calcugames.levelz.coord.Coordinate
import xyz.calcugames.levelz.coord.Coordinate2D
import xyz.calcugames.levelz.coord.Coordinate3D

/**
 * Represents a Block in a Level.
 */
class Block(
    /**
     * The name of this block.
     */
    val name: String,
    /**
     * The properties of this block.
     */
    private val properties: Map<String, Any> = emptyMap()
) {

    /**
     * Creates a new Block with the given name and these properties.
     * @param name Block Name
     * @return New Block
     */
    fun withName(name: String): Block {
        return Block(name, properties)
    }

    /**
     * Creates a new Block with this name and the given properties.
     * @param properties Block Properties
     * @return New Block
     */
    fun withProperties(properties: Map<String, Any>): Block {
        return Block(name, properties)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (other !is Block) return false

        return name == other.name && properties == other.properties
    }

    override fun hashCode(): Int {
        return (31 * name.hashCode()) + properties.hashCode()
    }

    override fun toString(): String {
        if (properties.isEmpty()) return name
        return "$name<$properties>"
    }
}

/**
 * Utility Object for representing a Level Block and its Coordinate.
 */
class LevelObject(
    /**
     * The block of this object.
     */
    val block: Block,
    /**
     * The coordinate of this object.
     */
    val coordinate: Coordinate
) : Comparable<LevelObject> {
    /**
     * Creates a new LevelObject with the given block and coordinate.
     * @param block Block
     * @param x X Coordinate
     * @param y Y Coordinate
     */
    constructor(block: Block, x: Int, y: Int) : this(block, Coordinate2D(x, y))

    /**
     * Creates a new LevelObject with the given block and coordinate.
     * @param name Block Name
     * @param x X Coordinate
     * @param y Y Coordinate
     */
    constructor(name: String, x: Int, y: Int) : this(Block(name), Coordinate2D(x, y))

    /**
     * Creates a new LevelObject with the given block and coordinate.
     * @param name Block Name
     * @param properties Block Properties
     * @param x X Coordinate
     * @param y Y Coordinate
     */
    constructor(name: String, properties: Map<String, Any>, x: Int, y: Int) : this(
        Block(name, properties), Coordinate2D(x, y)
    )

    /**
     * Creates a new LevelObject with the given block and coordinate.
     * @param block Block
     * @param x X Coordinate
     * @param y Y Coordinate
     * @param z Z Coordinate
     */
    constructor(block: Block, x: Int, y: Int, z: Int) : this(block, Coordinate3D(x, y, z))

    /**
     * Creates a new LevelObject with the given block and coordinate.
     * @param name Block Name
     * @param x X Coordinate
     * @param y Y Coordinate
     * @param z Z Coordinate
     */
    constructor(name: String, x: Int, y: Int, z: Int) : this(
        Block(name), Coordinate3D(x, y, z)
    )

    /**
     * Creates a new LevelObject with the given block and coordinate.
     * @param name Block Name
     * @param properties Block Properties
     * @param x X Coordinate
     * @param y Y Coordinate
     * @param z Z Coordinate
     */
    constructor(name: String, properties: Map<String, Any>, x: Int, y: Int, z: Int) : this(
        Block(name, properties), Coordinate3D(x, y, z)
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (other !is LevelObject) return false

        return block == other.block && coordinate == other.coordinate
    }

    override fun hashCode(): Int {
        return (31 * block.hashCode()) + coordinate.hashCode()
    }

    override fun toString(): String {
        return "$block: $coordinate"
    }

    override fun compareTo(other: LevelObject): Int {
        return coordinate.compareTo(other.coordinate)
    }
}