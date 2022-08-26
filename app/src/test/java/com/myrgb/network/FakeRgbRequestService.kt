package com.myrgb.network

import com.myrgb.ledcontroller.domain.RgbRequest
import com.myrgb.ledcontroller.feature.rgbcontroller.RgbSettingsResponse
import com.myrgb.ledcontroller.network.RgbRequestService
import retrofit2.Response

class FakeRgbRequestService(private val rgbSettingsResponse: RgbSettingsResponse) : RgbRequestService {
    override suspend fun getCurrentSettings(): Response<RgbSettingsResponse> =
        Response.success(rgbSettingsResponse)

    override suspend fun sendRgbRequest(request: RgbRequest) {}
}