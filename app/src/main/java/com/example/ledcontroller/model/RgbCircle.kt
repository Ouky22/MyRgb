package com.example.ledcontroller.model

/**
 * Represents a rgb circle, which knows what rgb values it has at certain angles.
 * Each color has a area in the rgb circle.
 * Each color area has a certain size. The center of a color area is half the size of the whole
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
     * The following attributes contain the angle of the center of the color area.
     */
    private val redAreaCenterAngle = 0
    private val greenAreaCenterAngle = 120
    private val blueAreaCenterAngle = 240

    /**
     * The following attributes contain the offsets of the color areas.
     * The value of the offset determines by how many degrees a color area needs to be shifted clockwise,
     * so that it starts at 0 degree.
     */
    private val redOffset = 120
    private val greenOffset = 0
    private val blueOffset = 240

    enum class RgbColor {
        RED, GREEN, BLUE
    }

    /**
     * @param rgbColor which rgb color
     * @param a which angle in the rgb circle
     * @return returns the value of the specified rgb color (0-255) at the given angle
     */
    fun getColorValue(rgbColor: RgbColor, a: Int): Int {
        // shift the given angle accordingly to the specified color and simplify it, so that
        // it is between 0 and 359
        val angle = (a + getOffset(rgbColor)).mod(360)

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
     * @return the offset of the specified color
     */
    private fun getOffset(rgbColor: RgbColor): Int {
        return when (rgbColor) {
            RgbColor.RED -> redOffset
            RgbColor.GREEN -> greenOffset
            RgbColor.BLUE -> blueOffset
        }
    }

    /**
     * @return the angle (0-360) of the center of specified rgb color area with the
     * corresponding offset (clockwise)
     */
    private fun getColorAreaCenterAngle(rgbColor: RgbColor): Int {
        return when (rgbColor) {
            RgbColor.RED -> (redAreaCenterAngle + redOffset) % 360
            RgbColor.GREEN -> (greenAreaCenterAngle + greenOffset) % 360
            RgbColor.BLUE -> (blueAreaCenterAngle + blueOffset) % 360
        }
    }

    /**
     * @return the angle (0 - 359) at which the shifted area of the specified color starts (clockwise)
     */
    private fun getAreaStartAngle(rgbColor: RgbColor): Int {
        return when (rgbColor) {
            RgbColor.RED -> (redAreaCenterAngle - colorAreaSize / 2 + redOffset) % 360
            RgbColor.GREEN -> (greenAreaCenterAngle - colorAreaSize / 2 + greenOffset) % 360
            RgbColor.BLUE -> (blueAreaCenterAngle - colorAreaSize / 2 + blueOffset) % 360
        }
    }

    /**
     * @return the angle (0 - 359) at which the shifted area of the specified color ends (clockwise)
     */
    private fun getAreaEndAngle(rgbColor: RgbColor): Int {
        return when (rgbColor) {
            RgbColor.RED -> (redAreaCenterAngle + colorAreaSize / 2 + redOffset) % 360
            RgbColor.GREEN -> (greenAreaCenterAngle + colorAreaSize / 2 + greenOffset) % 360
            RgbColor.BLUE -> (blueAreaCenterAngle + colorAreaSize / 2 + blueOffset) % 360
        }
    }
}



























