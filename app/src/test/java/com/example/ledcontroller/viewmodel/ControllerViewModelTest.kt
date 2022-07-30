package com.example.ledcontroller.viewmodel

import com.example.ledcontroller.model.RgbCircle
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
        viewModel.rgbCircleCenterX = 10
        viewModel.rgbCircleCenterY = 10

        // test color values at 0 degree
        var color = viewModel.getRgbColorAtTouchPosition(10, 5)
        assertEquals(color, RgbCircle.RgbTriplet(255, 0, 0))

        // test color values at 90 degrees
        color = viewModel.getRgbColorAtTouchPosition(15, 10)
        assertEquals(color, RgbCircle.RgbTriplet(127, 255, 0))

        // test color values at 180 degrees
        color = viewModel.getRgbColorAtTouchPosition(10, 15)
        assertEquals(color, RgbCircle.RgbTriplet(0, 255, 255))

        // test color values at 270 degrees
        color = viewModel.getRgbColorAtTouchPosition(5, 10)
        assertEquals(color, RgbCircle.RgbTriplet(127, 0, 255))
    }
}