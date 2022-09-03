package com.myrgb.ledcontroller.domain

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RgbCircleTest {
    private lateinit var rgbCircle: RgbCircle

    @Before
    fun rgbCircle() {
        rgbCircle = RgbCircle()
    }

    @Test
    fun `when angle is 0 degree color should be red`() {
        assertEquals(RgbTriplet(255, 0, 0), rgbCircle.calculateColorAtAngle(0))
    }

    @Test
    fun `when angle is 120 degrees color should be green`() {
        assertEquals(RgbTriplet(0, 255, 0), rgbCircle.calculateColorAtAngle(120))
    }

    @Test
    fun `when angle is 180 degrees color should be (0,255,255)`() {
        assertEquals(RgbTriplet(0, 255, 255), rgbCircle.calculateColorAtAngle(180))
    }

    @Test
    fun `when angle is 240 degrees color should be blue`() {
        assertEquals(RgbTriplet(0, 0, 255), rgbCircle.calculateColorAtAngle(240))
    }

    @Test
    fun `when angle is 360 degrees color should be red`() {
        assertEquals(RgbTriplet(255, 0, 0), rgbCircle.calculateColorAtAngle(360))
    }

    @Test
    fun `when angle is -480 degrees color should be green`() {
        assertEquals(RgbTriplet(0, 0, 255), rgbCircle.calculateColorAtAngle(-480))
    }
}










































