package xyz.calcugames.levelz.builder

import xyz.calcugames.levelz.*
import xyz.calcugames.levelz.coord.Coordinate
import xyz.calcugames.levelz.coord.Coordinate2D
import xyz.calcugames.levelz.coord.Coordinate3D

/**
 * Represents a builder for creating LevelZ Levels.
 */
class LevelBuilder private constructor(private val dimension: Dimension) {

    private val headers = mutableMapOf<String, String>()
    private val blocks = mutableSetOf<LevelObject>()

    /**
     * Loads a level into the builder.
     * @param level Level
     * @return this class, for chaining
     */
    fun loadLevel(level: Level): LevelBuilder {
        headers.putAll(level.headers)
        blocks.addAll(level.blocks)
        return this
    }

    /**
     * Sets the spawn point for the level.
     * @param spawn Spawn Point
     * @return this class, for chaining
     */
    fun spawn(spawn: Coordinate): LevelBuilder {
        if (dimension.is2D) if (spawn !is Coordinate2D) throw IllegalArgumentException("Spawn must be 2D")
        if (dimension.is3D) if (spawn !is Coordinate3D) throw IllegalArgumentException("Spawn must be 3D")

        headers["spawn"] = spawn.toString()
        return this
    }

    /**
     * Sets the spawn point for the level.
     * @param coords Spawn Point
     * @return this class, for chaining
     */
    fun spawn(coords: IntArray): LevelBuilder {
        return spawn(Coordinate.fromArray(coords))
    }

    /**
     * Sets the scroll direction for the level.
     * @param scroll Scroll Direction
     * @return this class, for chaining
     * @throws IllegalArgumentException If the level is not 2D
     */
    fun scroll(scroll: Scroll?): LevelBuilder {
        if (!dimension.is2D) throw IllegalArgumentException("Scroll is only supported for 2D levels")

        if (scroll == null) headers.remove("scroll")
        else headers["scroll"] = scroll.name.lowercase()

        return this
    }

    /**
     * Sets a header value.
     * @param key Header Key
     * @param value Header Value, can be null
     * @return this class, for chaining
     * @throws IllegalArgumentException If the key is null
     */
    fun header(key: String, value: String?): LevelBuilder {
        if (value == null) headers.remove(key)
        else headers[key] = value
        
        return this
    }

    /**
     * Sets multiple header values.
     * @param headers Header Map
     * @return this class, for chaining
     */
    fun headers(headers: Map<String, String>): LevelBuilder {
        this.headers.putAll(headers)
        return this
    }

    /**
     * Adds a block to the level.
     * @param block Level Block
     * @return this class, for chaining
     * @throws IllegalArgumentException If the block coordinate is not the same dimension as the level
     */
    fun block(block: LevelObject): LevelBuilder {
        if (dimension.is2D && block.coordinate !is Coordinate2D) throw IllegalArgumentException("Block Coordinate must be 2D")
        if (dimension.is3D && block.coordinate !is Coordinate3D) throw IllegalArgumentException("Block Coordinate must be 3D")

        blocks.add(block)
        return this
    }

    /**
     * Adds multiple blocks to the level.
     * @param blocks Level Blocks
     * @return this class, for chaining
     * @throws IllegalArgumentException If any of the block coordinates are not the same dimension as the level
     */
    fun blocks(vararg blocks: LevelObject): LevelBuilder {
        for (block in blocks) block(block)
        return this
    }

    /**
     * Adds multiple blocks to the level.
     * @param blocks Level Blocks
     * @return this class, for chaining
     * @throws IllegalArgumentException If any of the block coordinates are not the same dimension as the level
     */
    fun blocks(blocks: Iterable<LevelObject>): LevelBuilder {
        for (block in blocks) block(block)
        return this
    }

    /**
     * Adds a block to the level.
     * @param block Block
     * @param coordinate Coordinate
     * @return this class, for chaining
     */
    fun block(block: Block, coordinate: Coordinate): LevelBuilder {
        return block(LevelObject(block, coordinate))
    }

    /**
     * Adds a block to the level.
     * @param block Block
     * @param x X Coordinate
     * @param y Y Coordinate
     * @return this class, for chaining
     * @throws IllegalArgumentException If the dimension is not 2D
     */
    fun block(block: Block, x: Int, y: Int): LevelBuilder {
        if (!dimension.is2D) throw IllegalArgumentException("Block Coordinate cannot be 2D")

        return block(block, Coordinate2D(x, y))
    }

    /**
     * Adds a block to the level.
     * @param block Block
     * @param x X Coordinate
     * @param y Y Coordinate
     * @param z Z Coordinate
     * @return this class, for chaining
     * @throws IllegalArgumentException If the dimension is not 3D
     */
    fun block(block: Block, x: Int, y: Int, z: Int): LevelBuilder {
        if (!dimension.is3D) throw IllegalArgumentException("Block Coordinate cannot be 3D")
        return block(block, Coordinate3D(x, y, z))
    }

    /**
     * Adds a block to the level.
     * @param name Block Name
     * @param properties Block Properties
     * @param coordinate Block Coordinate
     * @return this class, for chaining
     */
    fun block(name: String, properties: Map<String, Any>, coordinate: Coordinate): LevelBuilder {
        return block(LevelObject(Block(name, properties), coordinate))
    }

    /**
     * Adds a block to the level.
     * @param name Block Name
     * @param properties Block Properties
     * @param coordinates Array of Block Coordinates
     * @return this class, for chaining
     */
    fun block(name: String, properties: Map<String, Any>, vararg coordinates: Coordinate): LevelBuilder {
        for (c in coordinates) block(name, properties, c)
        return this
    }

    /**
     * Adds a block to the level.
     * @param name Block Name
     * @param properties Block Properties
     * @param coordinates Iterable of Block Coordinates
     * @return this class, for chaining
     */
    fun block(name: String, properties: Map<String, Any>, coordinates: Iterable<Coordinate>): LevelBuilder {
        for (c in coordinates) block(name, properties, c)
        return this
    }

    /**
     * Adds a block with no properties to the level.
     * @param name Block Name
     * @param coordinate Block Coordinate
     * @return this class, for chaining
     */
    fun block(name: String, coordinate: Coordinate): LevelBuilder {
        return block(name, mutableMapOf(), coordinate)
    }

    /**
     * Adds a block with no properties to the level.
     * @param name Block Name
     * @param coordinate Block Coordinate
     * @return this class, for chaining
     */
    fun block(name: String, vararg coordinate: Coordinate): LevelBuilder {
        for (c in coordinate) block(name, emptyMap(), c)
        return this
    }

    /**
     * Adds a block with no properties to the level.
     * @param name Block Name
     * @param coordinates Iterable of Block Coordinates
     * @return this class, for chaining
     */
    fun block(name: String, coordinates: Iterable<Coordinate>): LevelBuilder {
        for (c in coordinates) block(name, emptyMap(), c)
        return this
    }

    /**
     * Performs a 2D block matrix operation.
     * @param block Block
     * @param cx Center X
     * @param cy Center Y
     * @param x1 First X Coordinate
     * @param x2 Second X Coordinate
     * @param y1 First Y Coordinate
     * @param y2 Second Y Coordinate
     * @return this class, for chaining
     * @throws IllegalArgumentException If the dimension is not 2D, or first is bigger than second
     */
    fun matrix(block: Block, cx: Int, cy: Int, x1: Int, x2: Int, y1: Int, y2: Int): LevelBuilder {
        if (!dimension.is2D) throw IllegalArgumentException("2D Matrix is only supported for 2D levels")
        if (x1 > x2 || y1 > y2) throw IllegalArgumentException("First coordinate must be smaller than second")

        for (x in x1..x2) for (y in y1..y2) block(block, cx + x, cy + y)

        return this
    }

    /**
     * Performs a 3D block matrix operation.
     * @param block Block
     * @param cx Center X
     * @param cy Center Y
     * @param cz Center Z
     * @param x1 First X Coordinate
     * @param x2 Second X Coordinate
     * @param y1 First Y Coordinate
     * @param y2 Second Y Coordinat
     * @param z1 First Z Coordinate
     * @param z2 Second Z Coordinate
     * @return this class, for chaining
     * @throws IllegalArgumentException If the dimension is not 3D, or first is bigger than second
     */
    fun matrix(block: Block, cx: Int, cy: Int, cz: Int, x1: Int, x2: Int, y1: Int, y2: Int, z1: Int, z2: Int): LevelBuilder {
        if (!dimension.is3D) throw IllegalArgumentException("3D Matrix is only supported for 3D levels")
        if (x1 > x2 || y1 > y2 || z1 > z2) throw IllegalArgumentException("First coordinate must be smaller than second")

        for (x in x1..x2) for (y in y1..y2) for (z in z1..z2) block(block, cx + x, cy + y, cz + z)

        return this
    }

    /**
     * Gets the current dimension of the level.
     * @return Level Dimension
     */
    fun getDimension(): Dimension = dimension

    /**
     * Gets an immutable copy of the current headers of the level.
     * @return Level Headers
     */
    fun getHeaders(): Map<String, String> = headers.toMap()

    /**
     * Gets an immutable copy of the current blocks of the level.
     * @return Level Blocks
     */
    fun getBlocks(): Set<LevelObject> = blocks.toSet()

    /**
     * Builds the level.
     * @return Level
     */
    fun build(): Level {
        if (dimension.is2D) return Level2D(headers, blocks)
        if (dimension.is3D) return Level3D(headers, blocks)

        throw IllegalStateException("Invalid Dimension: $dimension")
    }

    companion object {
        /**
         * Creates a 2D Level Builder.
         * @return 2D Level Builder
         */
        fun create2D(): LevelBuilder = LevelBuilder(Dimension.TWO)

        /**
         * Creates a 3D Level Builder.
         * @return 3D Level Builder
         */
        fun create3D(): LevelBuilder = LevelBuilder(Dimension.THREE)

        /**
         * Creates a Level Builder with the given dimension.
         * @param dimension Level Dimension
         */
        fun create(dimension: Dimension): LevelBuilder = LevelBuilder(dimension)
    }
}