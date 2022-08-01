package com.example.ledcontroller.network

data class CurrentSettingsResponse(
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