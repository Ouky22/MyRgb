package com.myrgb.ledcontroller.feature.rgbcontroller

data class RgbSettingsResponse(
    val id: Int,
    val redValue: Int,
    val greenValue: Int,
    val blueValue: Int,
    val brightness: Int,
    val strips: List<Strip>,
    val isRgbShowActive: Int
)

data class Strip(
    val name: String,
    val isOn: Int
)