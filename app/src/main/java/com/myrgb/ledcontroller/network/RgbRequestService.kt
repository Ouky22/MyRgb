package com.myrgb.ledcontroller.network

import com.myrgb.ledcontroller.domain.RgbRequest
import com.myrgb.ledcontroller.domain.settingsCommandIdentifier
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*


const val esp32DeskIpAddress = "192.168.1.249"
const val esp32SofaBedIpAddress = "192.168.1.250"

interface RgbRequestService {

    @GET("http://{ipAddress}/command/?value=$settingsCommandIdentifier.0.0.")
    suspend fun getCurrentSettings(@Path("ipAddress") ipAddress: String): Response<RgbSettingsResponse>

    @POST("http://{ipAddress}/command/")
    suspend fun sendRgbRequest(
        @Path("ipAddress") ipAddress: String,
        @Query("value") rgbRequest: RgbRequest
    )

    companion object {
        fun create(): RgbRequestService {
            val moshi = Moshi.Builder()
                .addLast(KotlinJsonAdapterFactory())
                .build()

            return Retrofit.Builder()
                .baseUrl("http://localhost/") // localhost is used because retrofit forces baseUrl, but it will be overwritten
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(RgbRequestService::class.java)
        }
    }
}