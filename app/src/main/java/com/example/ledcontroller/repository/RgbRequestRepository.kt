package com.example.ledcontroller.repository

import com.example.ledcontroller.model.RgbCircle
import com.example.ledcontroller.network.*

class RgbRequestRepository {
    suspend fun setColor(color: RgbCircle.RgbTriplet) {
        val rgbRequest = RgbRequest(color.red, color.green, color.blue)
        RgbRequestServiceDesk.retrofitService.sendRgbRequest(rgbRequest)
        RgbRequestServiceSofaBed.retrofitService.sendRgbRequest(rgbRequest)
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