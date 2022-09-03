package com.myrgb.ledcontroller.network

import com.myrgb.ledcontroller.domain.RgbStrip
import com.myrgb.ledcontroller.domain.RgbTriplet

data class RgbSettingsResponse(
    val color: RgbTriplet,
    val brightness: Int,
    val isRgbShowActive: Boolean,
    val strips: List<RgbStrip>
)