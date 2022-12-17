package com.myrgb.ledcontroller.domain

import android.graphics.Color
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RgbTriplet(
    val red: Int,
    val green: Int,
    val blue: Int,
) : Parcelable {
    fun toRgbInt() = Color.rgb(red, green, blue)
}