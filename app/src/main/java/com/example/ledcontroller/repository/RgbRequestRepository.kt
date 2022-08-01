package com.example.ledcontroller.repository

import com.example.ledcontroller.network.*

class RgbRequestRepository {
    suspend fun sendRgbRequest(request: RgbRequest) {
        RgbRequestServiceSofaBed.retrofitService.sendRgbRequest(request)
        RgbRequestServiceDesk.retrofitService.sendRgbRequest(request)
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