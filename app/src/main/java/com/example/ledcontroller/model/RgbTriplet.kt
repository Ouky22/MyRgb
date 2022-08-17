package com.example.ledcontroller.model

import android.graphics.Color

data class RgbTriplet(
    val red: Int,
    val green: Int,
    val blue: Int,
) {
    fun toRgbInt() = Color.rgb(red, green, blue)
}