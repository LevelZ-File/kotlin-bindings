package xyz.calcugames.levelz.parser

import xyz.calcugames.levelz.Level2D
import xyz.calcugames.levelz.Level3D
import xyz.calcugames.levelz.Scroll
import xyz.calcugames.levelz.coord.Coordinate2D
import xyz.calcugames.levelz.coord.Coordinate3D
import kotlin.test.Test
import kotlin.test.assertEquals


class TestLevelParser {

    @Test
    fun testParse() {
        val f1 = TestLevelParser::class.java.getResourceAsStream("/examples/2D/grasslands/1.lvlz") ?: throw Exception("File not found")
        val l1 = parseLevel(f1) as Level2D

        assertEquals(l1.scroll, Scroll.NONE)
        assertEquals(l1.spawn, Coordinate2D(0, 0))

        val f2 = TestLevelParser::class.java.getResourceAsStream("/examples/2D/grasslands/3.lvlz") ?: throw Exception("File not found")
        val l2 = parseLevel(f2) as Level2D

        assertEquals(l2.scroll, Scroll.HORIZONTAL_RIGHT)
        assertEquals(l2.spawn, Coordinate2D(0, 10))

        val f3 = TestLevelParser::class.java.getResourceAsStream("/examples/3D/grasslands/1.lvlz") ?: throw Exception("File not found")
        val l3 = parseLevel(f3) as Level3D

        assertEquals(l3.spawn, Coordinate3D(0, 0, 0))

        val f4 = TestLevelParser::class.java.getResourceAsStream("/examples/2D/volcano/4.lvlz") ?: throw Exception("File not found")
        val l4 = parseLevel(f4) as Level2D

        assertEquals(l4.scroll, Scroll.NONE)
        assertEquals(l4.spawn, Coordinate2D(5, 1))

        val f5 = TestLevelParser::class.java.getResourceAsStream("/examples/3D/grasslands/3.lvlz") ?: throw Exception("File not found")
        val l5 = parseLevel(f5) as Level3D

        assertEquals(l5.spawn, Coordinate3D(0, 10, 0))
    }

}