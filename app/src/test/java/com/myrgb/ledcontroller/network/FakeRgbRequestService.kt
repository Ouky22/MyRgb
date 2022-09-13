package com.myrgb.ledcontroller.network

import com.myrgb.ledcontroller.domain.RgbRequest
import retrofit2.Response
import java.io.IOException

class FakeRgbRequestService : RgbRequestService {
    var ipAddressesRgbSettingsMap: HashMap<String, RgbSettingsResponse> = hashMapOf()

    var loadingSettingsThrowsIoException: Boolean = false

    override suspend fun getCurrentSettings(ipAddress: String): Response<RgbSettingsResponse> =
        if (loadingSettingsThrowsIoException)
            throw IOException()
        else
            Response.success(ipAddressesRgbSettingsMap[ipAddress])

    override suspend fun sendRgbRequest(ipAddress: String, rgbRequest: RgbRequest) {}
}