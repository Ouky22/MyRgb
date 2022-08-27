package com.myrgb.ledcontroller.feature.rgbcontroller

import com.myrgb.ledcontroller.domain.RgbTriplet

interface ControllerRepository {
    suspend fun getCurrentSettings(): RgbSettingsResponse

    suspend fun startRgbShow(speed: Int)

    suspend fun stopRgbShow()

    suspend fun setRgbShowSpeed(speed: Int)

    suspend fun setColor(color: RgbTriplet)

    suspend fun setBrightness(brightness: Int)

    suspend fun turnAllLedStripsOff()

    suspend fun turnAllLedStripsOn()

    suspend fun turnSofaLedStripOff()

    suspend fun turnSofaLedStripOn()

    suspend fun turnBedLedStripOff()

    suspend fun turnBedLedStripOn()

    suspend fun turnDeskLedStripOff()

    suspend fun turnDeskLedStripOn()
}