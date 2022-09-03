package com.myrgb.ledcontroller.network

import com.myrgb.ledcontroller.domain.RgbRequest
import retrofit2.Response
import java.io.IOException

class FakeRgbRequestService(
    private var ipAddressRgbSettingsMap: HashMap<String, RgbSettingsResponse>
) : RgbRequestService {

    var loadingSettingsThrowsIoException: Boolean = false

    fun setIpAddressRgbSettingsMap(map: HashMap<String, RgbSettingsResponse>) {
        ipAddressRgbSettingsMap = map
    }

    override suspend fun getCurrentSettings(ipAddress: String): Response<RgbSettingsResponse> =
        if (loadingSettingsThrowsIoException)
            throw IOException()
        else
            Response.success(ipAddressRgbSettingsMap[ipAddress])

    override suspend fun sendRgbRequest(ipAddress: String, rgbRequest: RgbRequest) {}
}