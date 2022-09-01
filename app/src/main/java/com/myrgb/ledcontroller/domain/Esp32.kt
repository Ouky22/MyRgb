package com.myrgb.ledcontroller.domain

data class Esp32(
    val ipAddress: String,
    val rgbStrips: List<RgbStrip>
)
