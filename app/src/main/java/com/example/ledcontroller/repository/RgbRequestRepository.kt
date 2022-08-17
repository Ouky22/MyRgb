package com.example.ledcontroller.repository

import com.example.ledcontroller.model.RgbTriplet
import com.example.ledcontroller.network.*
import retrofit2.HttpException
import java.io.IOException

class RgbRequestRepository {

    enum class StripName(val stripNumber: Int) {
        SOFA(1),
        BED(2),
        DESK(1)
    }

    suspend fun getCurrentSettings(): CurrentSettingsResponse {
        val responseDesk: CurrentSettingsResponse? = try {
            RgbRequestServiceDesk.retrofitService.getCurrentSettings().body()
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } catch (e: HttpException) {
            e.printStackTrace()
            null
        }

        val responseSofaBed: CurrentSettingsResponse? = try {
            RgbRequestServiceSofaBed.retrofitService.getCurrentSettings().body()
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

        return CurrentSettingsResponse(
            0,
            responseDesk?.redValue ?: responseSofaBed?.redValue ?: 0,
            responseDesk?.greenValue ?: responseSofaBed?.greenValue ?: 0,
            responseDesk?.blueValue ?: responseSofaBed?.blueValue ?: 0,
            responseDesk?.brightness ?: responseSofaBed?.brightness ?: 0,
            strips,
            responseDesk?.isRgbShowActive ?: responseSofaBed?.isRgbShowActive ?: 0
        )
    }

    suspend fun startRgbShow(speed: Int) {
        val rgbRequest = RgbRequest(rgbShowCommandIdentifier, speed)
        sendRgbRequestToDesk(rgbRequest)
        sendRgbRequestToBedSofa(rgbRequest)
    }

    suspend fun stopRgbShow() {
        // TODO no command provided to stop the rgb show directly, instead a color has to be send
    }

    suspend fun setRgbShowSpeed(speed: Int) {
        val rgbRequest = RgbRequest(rgbShowCommandIdentifier, speed)
        sendRgbRequestToDesk(rgbRequest)
        sendRgbRequestToBedSofa(rgbRequest)
    }

    suspend fun setColor(color: RgbTriplet) {
        val rgbRequest = RgbRequest(color.red, color.green, color.blue)

        sendRgbRequestToDesk(rgbRequest)
        sendRgbRequestToBedSofa(rgbRequest)
    }

    suspend fun setBrightness(brightness: Int) {
        val rgbRequest = RgbRequest(brightnessCommandIdentifier, brightness)
        sendRgbRequestToDesk(rgbRequest)
        sendRgbRequestToBedSofa(rgbRequest)
    }

    suspend fun turnAllLedStripsOff() {
        sendRgbRequestToDesk(RgbRequest(offCommandIdentifier))
        sendRgbRequestToBedSofa(RgbRequest(offCommandIdentifier))
    }

    suspend fun turnAllLedStripsOn() {
        sendRgbRequestToDesk(RgbRequest(onCommandIdentifier))
        sendRgbRequestToBedSofa(RgbRequest(onCommandIdentifier))
    }

    suspend fun turnSofaLedStripOff() {
        sendRgbRequestToBedSofa(RgbRequest(offCommandIdentifier, StripName.SOFA.stripNumber))
    }

    suspend fun turnSofaLedStripOn() {
        sendRgbRequestToBedSofa(RgbRequest(onCommandIdentifier, StripName.SOFA.stripNumber))
    }

    suspend fun turnBedLedStripOff() {
        sendRgbRequestToBedSofa(RgbRequest(offCommandIdentifier, StripName.BED.stripNumber))
    }

    suspend fun turnBedLedStripOn() {
        sendRgbRequestToBedSofa(RgbRequest(onCommandIdentifier, StripName.BED.stripNumber))
    }

    suspend fun turnDeskLedStripOff() {
        sendRgbRequestToDesk(RgbRequest(offCommandIdentifier, StripName.DESK.stripNumber))
    }

    suspend fun turnDeskLedStripOn() {
        sendRgbRequestToDesk(RgbRequest(onCommandIdentifier, StripName.DESK.stripNumber))
    }

    private suspend fun sendRgbRequestToDesk(rgbRequest: RgbRequest) {
        try {
            RgbRequestServiceDesk.retrofitService.sendRgbRequest(rgbRequest)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: HttpException) {
            e.printStackTrace()
        }
    }

    private suspend fun sendRgbRequestToBedSofa(rgbRequest: RgbRequest) {
        try {
            RgbRequestServiceSofaBed.retrofitService.sendRgbRequest(rgbRequest)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: HttpException) {
            e.printStackTrace()
        }
    }
}