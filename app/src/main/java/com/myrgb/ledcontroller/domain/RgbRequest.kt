package com.myrgb.ledcontroller.domain

data class RgbRequest(
    val value1: Int,
    val value2: Int = 0,
    val value3: Int = 0
) {
    override fun toString() = "$value1.$value2.$value3."
}
