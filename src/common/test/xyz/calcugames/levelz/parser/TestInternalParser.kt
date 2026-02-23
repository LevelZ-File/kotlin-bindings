package xyz.calcugames.levelz.parser

import xyz.calcugames.levelz.Block
import xyz.calcugames.levelz.coord.Coordinate2D
import xyz.calcugames.levelz.coord.Coordinate3D
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

class TestInternalParser {
    
    @Test
    fun testValue() {
        assertEquals(true, value("true"))
        assertEquals(false, value("false"))

        assertEquals(5, value("5"))
        assertEquals(5.0, value("5.0"))

        assertEquals("Hello, World!", value("Hello, World!"))
    }

    @Test
    fun testRead2DPoints() {
        assertEquals(setOf(Coordinate2D(0, 0)), read2DPoints("[0,0]"))
        assertEquals(setOf(Coordinate2D(5, 5)), read2DPoints("[5, 5]"))
        assertEquals(setOf(Coordinate2D(2.5, 2.5)), read2DPoints("[2.5, 2.5]"))

        assertEquals(setOf(Coordinate2D(0, 0), Coordinate2D(5, 5)), read2DPoints("[0,0]*[5, 5]"))
        assertEquals(
            setOf(Coordinate2D(0, 0), Coordinate2D(5, 5), Coordinate2D(2.5, 2.5)),
            read2DPoints("[0,0]*[5, 5]*[2.5, 2.5]")
        )

        assertEquals(
            setOf(
                Coordinate2D(0, 0), Coordinate2D(0, 1), Coordinate2D(0, 2),
                Coordinate2D(1, 0), Coordinate2D(1, 1), Coordinate2D(1, 2),
                Coordinate2D(2, 0), Coordinate2D(2, 1), Coordinate2D(2, 2)
            ), read2DPoints("(0, 2, 0, 2)^[0, 0]")
        )
    }

    @Test
    fun testRead3DPoints() {
        assertEquals(setOf(Coordinate3D(0, 0, 0)), read3DPoints("[0,0,0]"))
        assertEquals(setOf(Coordinate3D(5, 5, 5)), read3DPoints("[5, 5, 5]"))
        assertEquals(setOf(Coordinate3D(2.5, 2.5, 2.5)), read3DPoints("[2.5, 2.5, 2.5]"))

        assertEquals(
            setOf(Coordinate3D(0, 0, 0), Coordinate3D(5, 5, 5)),
            read3DPoints("[0,0,0]*[5, 5, 5]")
        )
        assertEquals(
            setOf(
                Coordinate3D(0, 0, 0),
                Coordinate3D(5, 5, 5),
                Coordinate3D(2.5, 2.5, 2.5)
            ), read3DPoints("[0,0,0]*[5, 5, 5]*[2.5, 2.5, 2.5]")
        )

        assertEquals(
            setOf(
                Coordinate3D(0, 0, 0), Coordinate3D(0, 1, 0), Coordinate3D(0, 2, 0),
                Coordinate3D(1, 0, 0), Coordinate3D(1, 1, 0), Coordinate3D(1, 2, 0),
                Coordinate3D(2, 0, 0), Coordinate3D(2, 1, 0), Coordinate3D(2, 2, 0),
                Coordinate3D(0, 0, 1), Coordinate3D(0, 1, 1), Coordinate3D(0, 2, 1),
                Coordinate3D(1, 0, 1), Coordinate3D(1, 1, 1), Coordinate3D(1, 2, 1),
                Coordinate3D(2, 0, 1), Coordinate3D(2, 1, 1), Coordinate3D(2, 2, 1)
            ), read3DPoints("(0, 2, 0, 2, 0, 1)^[0, 0, 0]")
        )
    }

    @Test
    fun testReadBlock() {
        assertEquals(
            Block("test", mapOf("test" to true)),
            readBlock("test<test=true>", Random)
        )
        assertEquals(
            Block("test", mapOf("test" to 5)),
            readBlock("test<test=5>", Random)
        )
        assertEquals(
            Block("test", mapOf("test" to 5.0)),
            readBlock("test<test=5.0>", Random)
        )

        assertEquals(
            Block("test", mapOf("test" to false)),
            readBlock("{1.0=test<test=false>}", Random)
        )
        assertEquals(
            Block("grass", mapOf("value" to 7)),
            readBlock("{grass<value=7>}", Random)
        )

        assertNotNull(readBlock("{stone,grass,water<value=5>}", Random))
        assertNotNull(readBlock("{wood,grass<value=2,other=4>,water<value=5>}", Random))
        assertNotNull(
            readBlock(
                "{cobblestone,grass<value=2,other=4>,water<value=5>,magma<temp=0.3,type=lava,wet=false>,air}",
                Random
            )
        )
    }

    @Test
    fun testReadRawBlock() {
        assertEquals(Block("test", mapOf("test" to true)), Block.fromString("test<test=true>"))
        assertEquals(Block("ball", mapOf("bounce" to 5)), Block.fromString("ball<bounce=5>"))
        assertEquals(Block("stone", mapOf("weight" to 5.0)), Block.fromString("stone<weight=5.0>"))
    }

    @Test
    fun testReadHeaders() {
        val h1a = arrayOf(
            "@type 2",
            "@spawn default"
        )
        val h1 = readHeaders(h1a)
        assertEquals(2, h1.size)
        assertEquals("2", h1["type"])
        assertEquals("default", h1["spawn"])

        val h2a = arrayOf(
            "@type 3",
            "@spawn [0, 0, 0]"
        )
        val h2 = readHeaders(h2a)
        assertEquals(2, h2.size)
        assertEquals("3", h2["type"])
        assertEquals("[0, 0, 0]", h2["spawn"])

        val h3a = arrayOf(
            "@type 2",
            "@spawn [15, 0]",
            "@scroll horizontal-left"
        )
        val h3 = readHeaders(h3a)
        assertEquals(3, h3.size)
        assertEquals("2", h3["type"])
        assertEquals("[15, 0]", h3["spawn"])
        assertEquals("horizontal-left", h3["scroll"])

        val h4a = arrayOf(
            "@type 3",
        )
        assertFailsWith(MissingHeaderException::class) { readHeaders(h4a) }

        val h5a = arrayOf(
            "@spawn [15, 0]",
        )
        assertFailsWith(MissingHeaderException::class) { readHeaders(h5a) }
    }
}