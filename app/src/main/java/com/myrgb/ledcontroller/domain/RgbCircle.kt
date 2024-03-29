package com.myrgb.ledcontroller.domain


/**
 * Represents a rgb circle, which knows what rgb values it has at certain angles.
 * Each color has a area in the rgb circle, which has a certain size.
 * The center of a color area is half the size of the whole
 * color area and contains max values of the respective color.
 * Around the center are areas where the color values fade away as the angle gets closer to
 * the start/end of the color area.
 */
class RgbCircle {
    /**
     * How many degrees one color area covers in the rgb circle.
     */
    private val colorAreaSize = 240

    /**
     * The following attributes contain the angle where the center of the color area is.
     */
    private val redAreaCenterAngle = 0
    private val greenAreaCenterAngle = 120
    private val blueAreaCenterAngle = 240

    /**
     * Contains the main colors and their offsets.
     * The value of the offset determines by how many degrees a color area needs to be shifted clockwise,
     * so that it starts at 0 degree, which is necessary to compute the color value at a given angle.
     */
    enum class RgbColor(val offset: Int) {
        RED(120), GREEN(0), BLUE(240)
    }

    /**
     * @param angle if angle is not between 0 and 359 degrees (inclusive) then it will get adjusted
     */
    fun calculateColorAtAngle(angle: Int): RgbTriplet {
        return RgbTriplet(
            calculateValueOfColorAt(RgbColor.RED, angle),
            calculateValueOfColorAt(RgbColor.GREEN, angle),
            calculateValueOfColorAt(RgbColor.BLUE, angle)
        )
    }

    /**
     * @param rgbColor which rgb color
     * @param a which angle in the rgb circle (between 0-359 degrees, angles outside this range get adjusted)
     * @return returns the value of the specified rgb color (0-255) at the given angle
     */
    private fun calculateValueOfColorAt(rgbColor: RgbColor, a: Int): Int {
        // shift the given angle accordingly to the specified color and simplify it, so that
        // it is between 0 and 359
        val angle = (a + rgbColor.offset).mod(360)

        // note that each color area is for now on shifted so it starts at 0 degree)

        // if the angle is >= the size of a color area, the color value is 0
        if (angle >= colorAreaSize)
            return 0


        // angle of the center of color area
        val colorAreaCenterAngle = getColorAreaCenterAngle(rgbColor)
        // size of one fade area in degrees
        val fadeAreaSize = colorAreaSize / 4
        // the angle at the end of the fade area at the start of the color area
        val startFadeAreaEndAngle = colorAreaCenterAngle - fadeAreaSize
        // the angle at the start of the fade area at the end of the color area
        val endFadeAreaStartAngle = colorAreaCenterAngle + fadeAreaSize

        // return the max color value (255) if the angle is inside the center area
        if (angle in startFadeAreaEndAngle..endFadeAreaStartAngle)
            return 255

        // angle is inside fade area at start of color area
        if (angle <= startFadeAreaEndAngle)
            return ((angle.toDouble() / startFadeAreaEndAngle) * 255).toInt()

        // angle is inside fade area at end of color area
        return (255 - ((angle - colorAreaSize * 0.75) / fadeAreaSize) * 255).toInt()
    }

    /**
     * @return the angle (0-359) of the center of specified rgb color area with the
     * corresponding offset (clockwise)
     */
    private fun getColorAreaCenterAngle(rgbColor: RgbColor): Int {
        return when (rgbColor) {
            RgbColor.RED -> (redAreaCenterAngle + RgbColor.RED.offset) % 360
            RgbColor.GREEN -> (greenAreaCenterAngle + RgbColor.GREEN.offset) % 360
            RgbColor.BLUE -> (blueAreaCenterAngle + RgbColor.BLUE.offset) % 360
        }
    }
}



























