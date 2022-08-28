package com.myrgb.ledcontroller.network

import com.myrgb.ledcontroller.domain.RgbRequest
import com.myrgb.ledcontroller.feature.rgbcontroller.RgbSettingsResponse
import com.myrgb.ledcontroller.feature.rgbcontroller.Strip
import retrofit2.Response

class FakeRgbRequestService(
    private val rgbSettingsResponse: RgbSettingsResponse =
        RgbSettingsResponse(0, 0, 0, 0, 0, listOf(), 0)
) :
    RgbRequestService {

    override suspend fun getCurrentSettings(): Response<RgbSettingsResponse> =
        Response.success(rgbSettingsResponse)

    override suspend fun sendRgbRequest(request: RgbRequest) {}
}