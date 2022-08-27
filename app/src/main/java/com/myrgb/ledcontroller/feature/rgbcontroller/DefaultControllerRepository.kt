package com.myrgb.ledcontroller.feature.rgbcontroller

import com.myrgb.ledcontroller.domain.*
import com.myrgb.ledcontroller.network.*
import retrofit2.HttpException
import java.io.IOException

class DefaultControllerRepository(
    private val rgbRequestServiceDesk: RgbRequestService,
    private val rgbRequestServiceSofaBed: RgbRequestService
) : ControllerRepository {

    enum class StripName(val stripNumber: Int) {
        SOFA(1),
        BED(2),
        DESK(1)
    }

    override suspend fun getCurrentSettings(): RgbSettingsResponse {
        val responseDesk: RgbSettingsResponse? = try {
            rgbRequestServiceDesk.getCurrentSettings().body()
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } catch (e: HttpException) {
            e.printStackTrace()
            null
        }

        val responseSofaBed: RgbSettingsResponse? = try {
            rgbRequestServiceSofaBed.getCurrentSettings().body()
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } catch (e: HttpException) {
            e.printStackTrace()
            null
        }

        val strips = mutableListOf<Strip>()
        responseDesk?.strips?.let { strips.addAll(it) }
        responseSofaBed?.strips?.let { strips.addAll(it) }

        return RgbSettingsResponse(
            0,
            responseDesk?.redValue ?: responseSofaBed?.redValue ?: 0,
            responseDesk?.greenValue ?: responseSofaBed?.greenValue ?: 0,
            responseDesk?.blueValue ?: responseSofaBed?.blueValue ?: 0,
            responseDesk?.brightness ?: responseSofaBed?.brightness ?: 0,
            strips,
            responseDesk?.isRgbShowActive ?: responseSofaBed?.isRgbShowActive ?: 0
        )
    }

    override suspend fun startRgbShow(speed: Int) {
        val rgbRequest = RgbRequest(rgbShowCommandIdentifier, speed)
        sendRgbRequestToDesk(rgbRequest)
        sendRgbRequestToBedSofa(rgbRequest)
    }

    override suspend fun stopRgbShow() {
        // TODO no command provided to stop the rgb show directly, instead a color has to be send
    }

    override suspend fun setRgbShowSpeed(speed: Int) {
        val rgbRequest = RgbRequest(rgbShowCommandIdentifier, speed)
        sendRgbRequestToDesk(rgbRequest)
        sendRgbRequestToBedSofa(rgbRequest)
    }

    override suspend fun setColor(color: RgbTriplet) {
        val rgbRequest = RgbRequest(color.red, color.green, color.blue)

        sendRgbRequestToDesk(rgbRequest)
        sendRgbRequestToBedSofa(rgbRequest)
    }

    override suspend fun setBrightness(brightness: Int) {
        val rgbRequest = RgbRequest(brightnessCommandIdentifier, brightness)
        sendRgbRequestToDesk(rgbRequest)
        sendRgbRequestToBedSofa(rgbRequest)
    }

    override suspend fun turnAllLedStripsOff() {
        sendRgbRequestToDesk(RgbRequest(offCommandIdentifier))
        sendRgbRequestToBedSofa(RgbRequest(offCommandIdentifier))
    }

    override suspend fun turnAllLedStripsOn() {
        sendRgbRequestToDesk(RgbRequest(onCommandIdentifier))
        sendRgbRequestToBedSofa(RgbRequest(onCommandIdentifier))
    }

    override suspend fun turnSofaLedStripOff() {
        sendRgbRequestToBedSofa(RgbRequest(offCommandIdentifier, StripName.SOFA.stripNumber))
    }

    override suspend fun turnSofaLedStripOn() {
        sendRgbRequestToBedSofa(RgbRequest(onCommandIdentifier, StripName.SOFA.stripNumber))
    }

    override suspend fun turnBedLedStripOff() {
        sendRgbRequestToBedSofa(RgbRequest(offCommandIdentifier, StripName.BED.stripNumber))
    }

    override suspend fun turnBedLedStripOn() {
        sendRgbRequestToBedSofa(RgbRequest(onCommandIdentifier, StripName.BED.stripNumber))
    }

    override suspend fun turnDeskLedStripOff() {
        sendRgbRequestToDesk(RgbRequest(offCommandIdentifier, StripName.DESK.stripNumber))
    }

    override suspend fun turnDeskLedStripOn() {
        sendRgbRequestToDesk(RgbRequest(onCommandIdentifier, StripName.DESK.stripNumber))
    }

    private suspend fun sendRgbRequestToDesk(rgbRequest: RgbRequest) {
        try {
            rgbRequestServiceDesk.sendRgbRequest(rgbRequest)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: HttpException) {
            e.printStackTrace()
        }
    }

    private suspend fun sendRgbRequestToBedSofa(rgbRequest: RgbRequest) {
        try {
            rgbRequestServiceSofaBed.sendRgbRequest(rgbRequest)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: HttpException) {
            e.printStackTrace()
        }
    }
}