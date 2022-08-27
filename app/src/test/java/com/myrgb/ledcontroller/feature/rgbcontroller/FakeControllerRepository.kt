package com.myrgb.ledcontroller.feature.rgbcontroller

import com.myrgb.ledcontroller.domain.RgbTriplet
import com.myrgb.ledcontroller.network.RgbRequestService

class FakeControllerRepository(private val rgbRequestService: RgbRequestService) :
    ControllerRepository {

    override suspend fun getCurrentSettings(): RgbSettingsResponse {
        return rgbRequestService.getCurrentSettings().body() ?: RgbSettingsResponse(
            0, 0, 0, 0, 0, listOf(), 0
        )
    }

    override suspend fun startRgbShow(speed: Int) {}

    override suspend fun stopRgbShow() {}

    override suspend fun setRgbShowSpeed(speed: Int) {}

    override suspend fun setColor(color: RgbTriplet) {}

    override suspend fun setBrightness(brightness: Int) {}

    override suspend fun turnAllLedStripsOff() {}

    override suspend fun turnAllLedStripsOn() {}

    override suspend fun turnSofaLedStripOff() {}

    override suspend fun turnSofaLedStripOn() {}

    override suspend fun turnBedLedStripOff() {}

    override suspend fun turnBedLedStripOn() {}

    override suspend fun turnDeskLedStripOff() {}

    override suspend fun turnDeskLedStripOn() {}
}