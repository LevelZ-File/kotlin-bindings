package xyz.calcugames.levelz

import xyz.calcugames.levelz.coord.Coordinate
import xyz.calcugames.levelz.coord.Coordinate2D
import xyz.calcugames.levelz.coord.Coordinate3D

/**
 * Represents a Game Dimension.
 */
enum class Dimension {

    /**
     * Represents a 2D Plane.
     */
    TWO,

    /**
     * Represents a 3D Space.
     */
    THREE

    ;

    /**
     * Gets if this dimension is 2D.
     * @return true if 2D, false if 3D
     */
    val is2D: Boolean
        get() = this == TWO

    /**
     * Gets if this dimension is 3D.
     * @return true if 3D, false if 2D
     */
    val is3D: Boolean
        get() = this == THREE

    /**
     * Gets the default coordinate for this dimension.
     * @return Default Coordinate
     */
    val defaultCoordinate: Coordinate
        get() = if (is2D) Coordinate2D(0, 0) else Coordinate3D(0, 0, 0)

    /**
     * Gets the number of dimensions.
     * @return Number of Dimensions
     */
    val asNumber: Int
        get() = if (is2D) 2 else 3

}

/**
 * Represents the scroll direction of a 2D Level.
 */
enum class Scroll {
    /**
     * No Scrolling
     */
    NONE,

    /**
     * Horizontal Scrolling moving left
     */
    HORIZONTAL_LEFT,

    /**
     * Horizontal Scrolling moving right
     */
    HORIZONTAL_RIGHT,

    /**
     * Vertical Scrolling moving up
     */
    VERTICAL_UP,

    /**
     * Vertical Scrolling moving down
     */
    VERTICAL_DOWN
}

/**
 * Represents a LevelZ level.
 */
abstract class Level : Iterable<LevelObject> {

    private val _headers: MutableMap<String, String> = mutableMapOf()

    /**
     * Creates an empty Level.
     */
    protected constructor()

    /**
     * Creates a new Level with the given headers.
     * @param headers Level Headers
     */
    protected constructor(headers: Map<String, String>) {
        this._headers.putAll(headers)
    }

    // Implementation

    /**
     * The spawnpoint for the level.
     */
    abstract val spawn: Coordinate

    /**
     * The dimension of the level.
     */
    abstract val dimension: Dimension

    /**
     * The coordinates in the level.
     */
    abstract val coordinates: Set<Coordinate>

    /**
     * The blocks in the level.
     */
    abstract val blocks: Set<LevelObject>

    // Default

    /**
     * Gets an immutable copy of the raw headers for this level, excluding the dimension.
     */
    val headers: Map<String, String>
        get() = _headers.toMap()

    /**
     * Gets an immutable copy of the raw headers for this level.
     * @return Level Headers
     */
    fun getHeaders(): Map<String, String> {
        return mapOf("type" to dimension.asNumber.toString()) + headers.toMap()
    }

    override fun iterator(): Iterator<LevelObject> {
        return blocks.iterator()
    }
}

/**
 * Represents a 2D level.
 */
class Level2D : Level {
    private val _blocks: MutableSet<LevelObject> = mutableSetOf()

    override val spawn: Coordinate2D
    override val dimension: Dimension = Dimension.TWO

    /**
     * The scroll direction for this 2D Level.
     */
    val scroll: Scroll

    /**
     * Creates an empty 2D Level.
     */
    constructor() {
        this.spawn = Coordinate2D.zero
        this.scroll = Scroll.NONE
    }

    /**
     * Creates a new 2D Level with the given headers.
     * @param headers Level Headers
     */
    constructor(headers: Map<String, String>) : super(headers) {
        this.spawn = Coordinate2D.fromString(headers["spawn"] ?: "[0, 0]")
        this.scroll = Scroll.valueOf(headers["scroll"]?.replace('-', '_')?.uppercase() ?: "NONE")
    }

    /**
     * Creates a new 2D Level with the given headers and blocks.
     * @param headers Level Headers
     * @param blocks Level Blocks
     */
    constructor(headers: Map<String, String>, blocks: Collection<LevelObject>) : this(headers) {
        this._blocks.addAll(blocks)
    }

    override val coordinates: Set<Coordinate2D>
        get() = _blocks.map(LevelObject::coordinate).filterIsInstance<Coordinate2D>().toSet()

    override val blocks: Set<LevelObject>
        get() = _blocks.toSet()

}

/**
 * Represents a 3D level.
 */
class Level3D : Level {
    private val _blocks: MutableSet<LevelObject> = mutableSetOf()

    override val spawn: Coordinate3D
    override val dimension: Dimension = Dimension.THREE

    /**
     * Creates an empty 3D Level.
     */
    constructor() {
        this.spawn = Coordinate3D(0, 0, 0)
    }

    /**
     * Creates a new 3D Level with the given headers.
     * @param headers Level Headers
     */
    constructor(headers: Map<String, String>) : super(headers) {
        this.spawn = Coordinate3D.fromString((headers["spawn"])!!)
    }

    /**
     * Creates a new 3D Level with the given headers and blocks.
     * @param headers Level Headers
     * @param blocks Level Blocks
     */
    constructor(headers: Map<String, String>, blocks: Collection<LevelObject>) : this(headers) {
        this._blocks.addAll(blocks)
    }

    override val coordinates: Set<Coordinate3D>
        get() = _blocks.map(LevelObject::coordinate).filterIsInstance<Coordinate3D>().toSet()

    override val blocks: Set<LevelObject>
        get() = _blocks.toSet()

}

/**
 * Represents a class that exports a LevelZ Level to a file.
 */
class LevelExporter private constructor(private val level: Level) {
    /**
     * Whether to include headers in the export.
     */
    var includeHeaders: Boolean = true

    /**
     * Whether to include data in the export.
     */
    var includeData: Boolean = true

    /**
     * Whether to include a section separator in the export.
     */
    var fileExtension: String = "lvlz"

    /**
     * The line separator to use in the export. Default is `\n`.
     */
    var lineSeparator: String = "\n"

    /**
     * A comparator to sort the headers by. Default is in order.
     */
    var sortHeadersBy: Comparator<Map.Entry<String, String>>? = null

    /**
     * A comparator to sort the blocks by. Default is by [LevelObject.coordinate].
     */
    var sortBlocksBy: Comparator<LevelObject>? = null

    /**
     * Exports the Level to a string.
     * @return Level String
     */
    fun writeToString(): String {
        val builder = StringBuilder()

        if (includeHeaders) {
            val entries = level.getHeaders().entries.toMutableList()
            if (sortHeadersBy != null)
                entries.sortWith(sortHeadersBy!!)

            entries.forEach {
                builder.append('@').append(it.key).append(' ').append(it.value).append(lineSeparator)
            }

            builder.append("---").append(lineSeparator)
        }

        if (includeData) {
            val blockMap: MutableMap<Block, String> = mutableMapOf()
            val blocks = level.blocks.sorted().toMutableList()

            if (sortBlocksBy != null)
                blocks.sortWith(sortBlocksBy!!)

            for (block in blocks)
                if (blockMap.containsKey(block.block))
                    blockMap[block.block] = blockMap[block.block] + "*" + block.coordinate.toString()
                else
                    blockMap[block.block] = block.coordinate.toString()

            blockMap.entries.forEach {
                builder.append(it.key).append(": ").append(it.value).append(lineSeparator)
            }
        }

        builder.append("end")
        return builder.toString()
    }

    companion object {
        /**
         * Exports the Level to a file.
         * @param level Level to Export
         * @return Level Exporter
         */
        fun export(level: Level?): LevelExporter {
            if (level == null) throw IllegalArgumentException("Level cannot be null")

            return LevelExporter(level)
        }
    }
}