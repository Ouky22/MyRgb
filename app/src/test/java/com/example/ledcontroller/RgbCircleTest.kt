package com.example.ledcontroller

import com.example.ledcontroller.model.RgbCircle
import junit.framework.Assert.assertEquals
import org.junit.Test

class RgbCircleTest {
    @Test
    fun testGetColorValue() {
        val rgbCircle = RgbCircle()

        // test red colors
        assertEquals(255, rgbCircle.getColorValue(RgbCircle.RgbColor.RED, 0))
        assertEquals(127, rgbCircle.getColorValue(RgbCircle.RgbColor.RED, 90))
        assertEquals(0, rgbCircle.getColorValue(RgbCircle.RgbColor.RED, 240))

        // test green colors
        assertEquals(255, rgbCircle.getColorValue(RgbCircle.RgbColor.GREEN, 120))
        assertEquals(127, rgbCircle.getColorValue(RgbCircle.RgbColor.GREEN, 30))
        assertEquals(0, rgbCircle.getColorValue(RgbCircle.RgbColor.GREEN, 240))

        // test blue colors
        assertEquals(255, rgbCircle.getColorValue(RgbCircle.RgbColor.BLUE, 240))
        assertEquals(127, rgbCircle.getColorValue(RgbCircle.RgbColor.BLUE, 330))
        assertEquals(0, rgbCircle.getColorValue(RgbCircle.RgbColor.BLUE, 0))

        // test angles >= 360
        assertEquals(255, rgbCircle.getColorValue(RgbCircle.RgbColor.RED, 360))
        assertEquals(255, rgbCircle.getColorValue(RgbCircle.RgbColor.GREEN, 2 * 360 + 60))
        assertEquals(255, rgbCircle.getColorValue(RgbCircle.RgbColor.BLUE, 360 + 180))

        // test negative angles
        assertEquals(255, rgbCircle.getColorValue(RgbCircle.RgbColor.RED, -360))
        assertEquals(255, rgbCircle.getColorValue(RgbCircle.RgbColor.GREEN, -180 - 360))
        assertEquals(127, rgbCircle.getColorValue(RgbCircle.RgbColor.BLUE, -30))
    }
}