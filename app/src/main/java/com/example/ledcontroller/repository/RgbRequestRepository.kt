package com.example.ledcontroller.repository

import com.example.ledcontroller.model.RgbCircle
import com.example.ledcontroller.network.*
import retrofit2.HttpException
import java.io.IOException

class RgbRequestRepository {
    enum class StripName(val stripNumber: Int) { SOFA(1), BED(2), DESK(1) }

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

    suspend fun getCurrentSettings(): CurrentSettingsResponse {
        val responseDesk = RgbRequestServiceDesk.retrofitService.getCurrentSettings().body()
        val responseSofaBed = RgbRequestServiceSofaBed.retrofitService.getCurrentSettings().body()

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
}