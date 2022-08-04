package com.example.ledcontroller.repository

import com.example.ledcontroller.model.RgbCircle
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


    suspend fun setColor(color: RgbCircle.RgbTriplet) {
        val rgbRequest = RgbRequest(color.red, color.green, color.blue)
        try {
            RgbRequestServiceDesk.retrofitService.sendRgbRequest(rgbRequest)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: HttpException) {
            e.printStackTrace()
        }
        try {
            RgbRequestServiceSofaBed.retrofitService.sendRgbRequest(rgbRequest)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: HttpException) {
            e.printStackTrace()
        }
    }

    suspend fun setBrightness(brightness: Int) {
        val rgbRequest = RgbRequest(brightnessCommandIdentifier, brightness)
        try {
            RgbRequestServiceDesk.retrofitService.sendRgbRequest(rgbRequest)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: HttpException) {
            e.printStackTrace()
        }
        try {
            RgbRequestServiceSofaBed.retrofitService.sendRgbRequest(rgbRequest)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: HttpException) {
            e.printStackTrace()
        }
    }

    suspend fun turnAllLedStripsOff() {
        turnAllLedStrips(false)
    }

    suspend fun turnAllLedStripsOn() {
        turnAllLedStrips(true)
    }

    suspend fun turnSofaLedStripOff() {
        turnLedStrip(false, StripName.SOFA)
    }

    suspend fun turnSofaLedStripOn() {
        turnLedStrip(true, StripName.SOFA)
    }

    suspend fun turnBedLedStripOff() {
        turnLedStrip(false, StripName.BED)
    }

    suspend fun turnBedLedStripOn() {
        turnLedStrip(true, StripName.BED)
    }

    suspend fun turnDeskLedStripOff() {
        turnLedStrip(false, StripName.DESK)
    }

    suspend fun turnDeskLedStripOn() {
        turnLedStrip(true, StripName.DESK)
    }

    private suspend fun turnLedStrip(on: Boolean, stripName: StripName) {
        val rgbRequest =
            if (on) RgbRequest(onCommandIdentifier, stripName.stripNumber)
            else RgbRequest(offCommandIdentifier, stripName.stripNumber)

        try {
            if (stripName == StripName.SOFA || stripName == StripName.BED)
                RgbRequestServiceSofaBed.retrofitService.sendRgbRequest(rgbRequest)
            else
                RgbRequestServiceDesk.retrofitService.sendRgbRequest(rgbRequest)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: HttpException) {
            e.printStackTrace()
        }
    }

    private suspend fun turnAllLedStrips(on: Boolean) {
        val rgbRequest =
            if (on) RgbRequest(onCommandIdentifier)
            else RgbRequest(offCommandIdentifier)

        try {
            RgbRequestServiceDesk.retrofitService.sendRgbRequest(rgbRequest)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: HttpException) {
            e.printStackTrace()
        }
        try {
            RgbRequestServiceSofaBed.retrofitService.sendRgbRequest(rgbRequest)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: HttpException) {
            e.printStackTrace()
        }
    }
}