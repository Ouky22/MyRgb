package com.myrgb.ledcontroller.network

import com.myrgb.ledcontroller.domain.RgbRequest
import retrofit2.Response
import java.io.IOException

class FakeRgbRequestService(private val rgbSettingsResponse: RgbSettingsResponse) :
    RgbRequestService {

    var loadingSettingsThrowsIoException: Boolean = false

    override suspend fun getCurrentSettings(ipAddress: String): Response<RgbSettingsResponse> =
        if (loadingSettingsThrowsIoException)
            throw IOException()
        else
            Response.success(rgbSettingsResponse)

    override suspend fun sendRgbRequest(ipAddress: String, rgbRequest: RgbRequest) {}
}