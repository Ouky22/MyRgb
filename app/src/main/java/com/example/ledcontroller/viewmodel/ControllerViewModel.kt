package com.example.ledcontroller.viewmodel

import androidx.lifecycle.ViewModel
import com.example.ledcontroller.model.RgbCircle
import kotlin.math.acos
import kotlin.math.sqrt

class ControllerViewModel : ViewModel() {
    private val rgbCircle = RgbCircle()

    /**
     * @param position the position (x,y) to which the color values should be computed
     * @param circleCenterX the horizontal position of the rgb circle view
     * @param circleCenterX the vertical position of the rgb circle view
     * @return the value (0-255) of each rgb color at the given position in following order:
     * [red, green, blue]
     */
    fun getRgbColors(position: Array<Int>, circleCenterX: Int, circleCenterY: Int): Array<Int> {
        if (position.size != 2)
            return arrayOf(0, 0, 0)

        val angle = computeAngle(position[0], position[1], circleCenterX, circleCenterY)

        return arrayOf(
            rgbCircle.getColorValue(RgbCircle.RgbColor.RED, angle),
            rgbCircle.getColorValue(RgbCircle.RgbColor.GREEN, angle),
            rgbCircle.getColorValue(RgbCircle.RgbColor.BLUE, angle)
        )
    }

    /**
     * @param x horizontal position
     * @param y vertical position
     * @param rgbCenterX horizontal position of the rgb circle
     * @param rgbCenterY vertical position of the rgb circle
     */
    private fun computeAngle(x: Int, y: Int, rgbCenterX: Int, rgbCenterY: Int): Int {
        // This method computes the angle (0-359) between a vertical vector (0,-1) starting in the rgb
        // center and a vector between the rgb circle center and the specified position

        // vector between the rgb circle center and the specified position
        val centerPointVectorX = x - rgbCenterX
        val centerPointVectorY = y - rgbCenterY

        // length of the vector between rgb circle center and the specified position
        val centerPointVectorLength =
            sqrt((centerPointVectorX * centerPointVectorX + centerPointVectorY * centerPointVectorY).toDouble())

        // compute the angle between the two vectors
        var angle = acos(-centerPointVectorY / centerPointVectorLength) / Math.PI * 180

        // if the position is left to the center, compute the greater angle
        if (x < rgbCenterX)
            angle = 360 - angle

        return angle.toInt()
    }
}




































