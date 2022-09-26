package com.myrgb.ledcontroller.domain.util

import kotlin.math.acos
import kotlin.math.sqrt

/**
 * @returns the angle (0-359 degrees) between a vertical vector (0,-1) starting in the center of
 * the circle and a vector between the specified circle center and the specified position
 */
fun computeAngleInCircle(
    circleCenterX: Int, circleCenterY: Int,
    pointPositionX: Int, pointPositionY: Int
): Int {
    // vector from the rgb circle center to the specified position
    val centerPointVectorX = pointPositionX - circleCenterX
    val centerPointVectorY = pointPositionY - circleCenterY

    // length of the vector from rgb circle center to the specified position
    val centerPointVectorLength =
        sqrt((centerPointVectorX * centerPointVectorX + centerPointVectorY * centerPointVectorY).toDouble())

    // compute the angle between the two vectors
    var angle = acos(-centerPointVectorY / centerPointVectorLength) / Math.PI * 180

    // if the position is left to the center, compute the greater angle
    if (pointPositionX < circleCenterX)
        angle = 360 - angle

    return angle.toInt()
}