package com.example.ledcontroller.viewmodel

import androidx.lifecycle.ViewModel
import com.example.ledcontroller.model.RgbCircle
import kotlin.math.acos
import kotlin.math.sqrt

class ControllerViewModel : ViewModel() {
    private val rgbCircle = RgbCircle()
    var rgbCircleCenterX = 0
    var rgbCircleCenterY = 0

    fun getRgbColorAtTouchPosition(touchPositionX: Int, touchPositionY: Int): RgbCircle.RgbTriplet {
        val angle = computeAngleBetweenTouchAndRgbCircleCenter(touchPositionX, touchPositionY)
        return rgbCircle.computeColorAtAngle(angle)
    }

    private fun computeAngleBetweenTouchAndRgbCircleCenter(x: Int, y: Int): Int {
        // This method computes the angle (0-359) between a vertical vector (0,-1) starting in the rgb
        // center and a vector between the rgb circle center and the specified position

        // vector from the rgb circle center to the specified position
        val centerPointVectorX = x - rgbCircleCenterX
        val centerPointVectorY = y - rgbCircleCenterY

        // length of the vector from rgb circle center to the specified position
        val centerPointVectorLength =
            sqrt((centerPointVectorX * centerPointVectorX + centerPointVectorY * centerPointVectorY).toDouble())

        // compute the angle between the two vectors
        var angle = acos(-centerPointVectorY / centerPointVectorLength) / Math.PI * 180

        // if the position is left to the center, compute the greater angle
        if (x < rgbCircleCenterX)
            angle = 360 - angle

        return angle.toInt()
    }
}




































