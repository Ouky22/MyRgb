package com.example.ledcontroller.viewmodel

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ControllerViewModelTest {
    private lateinit var viewModel: ControllerViewModel

    @Before
    fun init() {
        viewModel = ControllerViewModel()
    }

    @Test
    fun testGetRgbColors() {
        val rgbCircleCenterX = 10
        val rgbCircleCenterY = 10

        // test color values at 0 degree
        var colors = viewModel.getRgbColors(arrayOf(10, 5), rgbCircleCenterX, rgbCircleCenterY)
        assertArrayEquals(colors, arrayOf(255, 0, 0))

        // test color values at 90 degrees
        colors = viewModel.getRgbColors(arrayOf(15, 10), rgbCircleCenterX, rgbCircleCenterY)
        assertArrayEquals(colors, arrayOf(127, 255, 0))

        // test color values at 180 degrees
        colors = viewModel.getRgbColors(arrayOf(10, 15), rgbCircleCenterX, rgbCircleCenterY)
        assertArrayEquals(colors, arrayOf(0, 255, 255))

        // test color values at 270 degrees
        colors = viewModel.getRgbColors(arrayOf(5, 10), rgbCircleCenterX, rgbCircleCenterY)
        assertArrayEquals(colors, arrayOf(127, 0, 255))

    }

}