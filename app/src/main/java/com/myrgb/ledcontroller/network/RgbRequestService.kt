package com.myrgb.ledcontroller.network

import com.myrgb.ledcontroller.domain.settingsCommandIdentifier
import com.myrgb.ledcontroller.feature.rgbcontroller.CurrentSettingsResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

private const val esp32DeskBaseUrl = "http://192.168.1.249/"
private const val esp32SofaBedBaseUrl = "http://192.168.1.250/"

private val moshi = Moshi.Builder()
    .addLast(KotlinJsonAdapterFactory())
    .build()

object RgbRequestServiceDesk {
    val retrofitService: RgbRequestService by lazy {
        Retrofit.Builder()
            .baseUrl(esp32DeskBaseUrl)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(RgbRequestService::class.java)
    }
}

object RgbRequestServiceSofaBed {
    val retrofitService: RgbRequestService by lazy {
        Retrofit.Builder()
            .baseUrl(esp32SofaBedBaseUrl)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(RgbRequestService::class.java)
    }
}

interface RgbRequestService {
    @GET("command/?value=$settingsCommandIdentifier.0.0.")
    suspend fun getCurrentSettings(): Response<CurrentSettingsResponse>

    @POST("command/")
    suspend fun sendRgbRequest(@Query("value") request: RgbRequest)
}

data class RgbRequest(
    val value1: Int,
    val value2: Int = 0,
    val value3: Int = 0
) {
    override fun toString() = "$value1.$value2.$value3."
}
