package com.myrgb.ledcontroller.domain

data class Esp32Microcontroller(
    val ipAddress: String,
    val rgbStrips: List<RgbStrip>
)
