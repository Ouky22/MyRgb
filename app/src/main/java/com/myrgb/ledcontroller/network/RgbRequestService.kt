package com.myrgb.ledcontroller.network

import com.myrgb.ledcontroller.domain.RgbRequest
import com.myrgb.ledcontroller.domain.settingsCommandIdentifier
import com.myrgb.ledcontroller.feature.rgbcontroller.RgbSettingsResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface RgbRequestService {
    @GET("command/?value=$settingsCommandIdentifier.0.0.")
    suspend fun getCurrentSettings(): Response<RgbSettingsResponse>

    @POST("command/")
    suspend fun sendRgbRequest(@Query("value") request: RgbRequest)


    companion object {
        fun create(baseUrl: String): RgbRequestService {
            val moshi = Moshi.Builder()
                .addLast(KotlinJsonAdapterFactory())
                .build()

            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(RgbRequestService::class.java)
        }
    }
}