package com.myrgb.ledcontroller.domain

data class LedMicrocontroller(
    val ipAddress: String,
    val rgbStrips: List<RgbStrip>
)
