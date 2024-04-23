package xyz.calcugames.levelz

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
        get() = this == TWO;

    /**
     * Gets if this dimension is 3D.
     * @return true if 3D, false if 2D
     */
    val is3D: Boolean
        get() = this == THREE;

}