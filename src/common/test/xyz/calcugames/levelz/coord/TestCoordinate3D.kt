package xyz.calcugames.levelz.coord

import kotlin.test.Test
import kotlin.test.assertEquals

class TestCoordinate3D {

    @Test
    fun testFromString() {
        assertEquals(Coordinate3D(0, 0, 0), Coordinate3D.fromString("[0,0,0]"))
        assertEquals(Coordinate3D(0, 0, 0), Coordinate3D.fromString("[0, 0, 0]"))
        assertEquals(Coordinate3D(0, 0, 0), Coordinate3D.fromString("[0,  0,  0]"))

        assertEquals(Coordinate3D.fromString("[0,0,0]"), Coordinate3D.fromString("0,0,0"))
        assertEquals(Coordinate3D.fromString("[0,0,0]"), Coordinate3D.fromString("0, 0, 0"))

        assertEquals(Coordinate3D(3, 4, 2), Coordinate3D.fromString("[3,4,2]"))
        assertEquals(Coordinate3D(-3, 4, -1), Coordinate3D.fromString("[-3, 4, -1]"))
        assertEquals(Coordinate3D(7, -14, 17), Coordinate3D.fromString("[7, -14, 17]"))

        assertEquals(Coordinate3D(2.5, -1.25, 12.5), Coordinate3D.fromString("[2.5, -1.25, 12.5]"))
        assertEquals(Coordinate3D(-4.5, 7.75, -11.0), Coordinate3D.fromString("[-4.5, 7.75, -11]"))
        assertEquals(Coordinate3D(0.5, 1.0, -0.5), Coordinate3D.fromString("[0.5, 1, -0.5]"))
        assertEquals(Coordinate3D(0.5, -1.0, -0.5), Coordinate3D.fromString("[0.5, -1.0, -0.5]"))

        val a = Coordinate3D(0, 0, 0)
        assertEquals(a.toString(), "[0, 0, 0]")
        assertEquals(a, Coordinate3D.fromString(a.toString()))

        val b = Coordinate3D(3.0, 4.0, -2.5)
        assertEquals(b.toString(), "[3, 4, -2.5]")
        assertEquals(b, Coordinate3D.fromString(b.toString()))

        val c = Coordinate3D(2.5, -1.25, 12.5)
        assertEquals(c.toString(), "[2.5, -1.25, 12.5]")
        assertEquals(c, Coordinate3D.fromString(c.toString()))

        val d = Coordinate3D(-11.0, -45.75, 88.0)
        assertEquals(d.toString(), "[-11, -45.75, 88]")
        assertEquals(d, Coordinate3D.fromString(d.toString()))
    }
}