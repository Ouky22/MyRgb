package com.myrgb.ledcontroller.network

import com.myrgb.ledcontroller.domain.RgbRequest
import com.myrgb.ledcontroller.domain.settingsCommandIdentifier
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

interface RgbRequestService {

    @GET("http://{ipAddress}/command/?value=$settingsCommandIdentifier.0.0.")
    suspend fun getCurrentSettings(@Path("ipAddress") ipAddress: String): Response<RgbSettingsResponse>

    @POST("http://{ipAddress}/command/")
    suspend fun sendRgbRequest(
        @Path("ipAddress") ipAddress: String,
        @Query("value") rgbRequest: RgbRequest
    )
}