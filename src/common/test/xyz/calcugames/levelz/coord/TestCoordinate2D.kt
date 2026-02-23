package xyz.calcugames.levelz.coord

import kotlin.test.Test
import kotlin.test.assertEquals

class TestCoordinate2D {

    @Test
    fun testFromString() {
        assertEquals(Coordinate2D(0, 0), Coordinate2D.fromString("[0,0]"))
        assertEquals(Coordinate2D(0, 0), Coordinate2D.fromString("[0, 0]"))
        assertEquals(Coordinate2D(0, 0), Coordinate2D.fromString("[0,  0]"))

        assertEquals(Coordinate2D.fromString("[0,0]"), Coordinate2D.fromString("0,0"))
        assertEquals(Coordinate2D.fromString("[0,0]"), Coordinate2D.fromString("0, 0"))

        assertEquals(Coordinate2D(3, 4), Coordinate2D.fromString("[3,4]"))
        assertEquals(Coordinate2D(-3, 4), Coordinate2D.fromString("[-3, 4]"))
        assertEquals(Coordinate2D(7, -14), Coordinate2D.fromString("[7, -14]"))

        assertEquals(Coordinate2D(2.5, -1.25), Coordinate2D.fromString("[2.5, -1.25]"))
        assertEquals(Coordinate2D(-4.5, 7.75), Coordinate2D.fromString("[-4.5, 7.75]"))
        assertEquals(Coordinate2D(0.5, 1.0), Coordinate2D.fromString("[0.5, 1]"))
        assertEquals(Coordinate2D(0.5, 1.0), Coordinate2D.fromString("[0.5, 1.0]"))

        val a = Coordinate2D(0, 0)
        assertEquals(a.toString(), "[0, 0]")
        assertEquals(a, Coordinate2D.fromString(a.toString()))

        val b = Coordinate2D(3, 4)
        assertEquals(b.toString(), "[3, 4]")
        assertEquals(b, Coordinate2D.fromString(b.toString()))

        val c = Coordinate2D(2.5, -1.25)
        assertEquals(c.toString(), "[2.5, -1.25]")
        assertEquals(c, Coordinate2D.fromString(c.toString()))

        val d = Coordinate2D(-7.0, 9.5)
        assertEquals(d.toString(), "[-7, 9.5]")
        assertEquals(d, Coordinate2D.fromString(d.toString()))
    }
}